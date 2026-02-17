/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.launcher;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.commands.RegisterCommand;
import artzstudio.dev.deluxevoid.data.player.PlayerManager;
import artzstudio.dev.deluxevoid.database.Cache;
import artzstudio.dev.deluxevoid.events.chat.playerInteractEvent;
import artzstudio.dev.deluxevoid.events.inventory.InventoryClose;
import artzstudio.dev.deluxevoid.events.inventory.click.InventoryClickCreate;
import artzstudio.dev.deluxevoid.events.inventory.click.InventoryClickPlayer;
import artzstudio.dev.deluxevoid.events.updateEvent;
import artzstudio.dev.deluxevoid.events.world.*;
import artzstudio.dev.deluxevoid.firework.FireworkDamage;
import artzstudio.dev.deluxevoid.firework.FireworkExplode;
import artzstudio.dev.deluxevoid.license.LicenseManager;
import artzstudio.dev.deluxevoid.menu.home.HomeListener;
import artzstudio.dev.deluxevoid.menu.icon.IconListener;
import artzstudio.dev.deluxevoid.session.SessionManager;
import artzstudio.dev.deluxevoid.utils.Slime;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.WorldSettingsHandler;
import artzstudio.dev.deluxevoid.utils.commands.PlayerCommand.MainCommand;
import artzstudio.dev.deluxevoid.utils.task.NotificationManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Launcher implements Launch {

    private static final boolean SPIGOT_BUILD = false;
    public static int numberWorlds;

    @Getter
    public static Cache cache;
    private static Launcher launcher;
    public DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    public BukkitAudiences audiences;
    @Getter
    private HomeListener homeListener;

    public static Launcher getInstance() {
        return launcher;
    }

    public static int getNumberWorlds() {
        return Bukkit.getWorlds().size();
    }

    @Override
    public void launch(DeluxeVoidWorld plugin) {
        launcher = this;
        audiences = BukkitAudiences.create(plugin);

        plugin.initiate();

        if (SPIGOT_BUILD) {
            initGameplay("SECURE_ACCESS_SPIGOT_BYPASS");
        } else {
            String licenseKey = plugin.getConfigYaml().getString("ADMIN-CONFIG.RATE_LIMIT", "ENTER THE LICENSE IF PURCHASED FROM POLYMART OR BUILTBYBIT.");
            new LicenseManager(plugin).verifyLicense(licenseKey);
        }
    }

    @Override
    public void shutdown() {
        Section worldsSection = plugin.getGenerated().getSection("WORLDS");

        if (worldsSection != null) {
            for (Object key : worldsSection.getKeys()) {
                String keyStr = key.toString();

                String spawnData = worldsSection.getString(keyStr + ".SPAWN");

                if (spawnData != null && spawnData.contains(",")) {
                    String[] parts = spawnData.split(",");
                    String worldName = parts[0];

                    UtilityFunctions.saveWorlds(worldName);
                    plugin.getLogger().info("[DeluxeVoidWorld] Saving world " + worldName + "...");
                }
            }
        }

        if (getCache() != null) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> getCache().getMethod().shutdown());

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    plugin.getLogger().warning("Cache took too long to shut down. Skipping it.");
                }
            } catch (InterruptedException ignored) {
            }
        }

        NotificationManager.shutdown();
    }

    public void initGameplay(String securityToken) {
        if (securityToken == null || !securityToken.startsWith("SECURE_ACCESS")) {
            return;
        }

        plugin.sendConsole(plugin.prefix + "&aLicense verified successfully. Initializing core...");

        this.registerWorlds();
        this.getCommands();
        this.registerEvents();
        this.setConnection();

        this.getNumbers();
        this.getMetrics();

        this.welcome();
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void registerEvents() {
        PluginManager event = plugin.getServer().getPluginManager();

        this.homeListener = new HomeListener(plugin);

        Arrays.asList(new updateEvent(),
                        new playerMoveEvent(),
                        new playerInteractEvent(),
                        new FireworkDamage(),
                        new FireworkExplode(),
                        new playerJoin(),
                        new InventoryClose(),
                        new playerQuitEvent(),
                        new WorldInitEvent(),
                        new EntityDamageEvent(),
                        new InventoryClickCreate(),
                        new InventoryClickPlayer(),
                        new IconListener(plugin),
                        homeListener)
                .forEach(listener -> event.registerEvents(listener, plugin));
        if (UtilityFunctions.isPluginEnabled("SlimeWorldManager")) {
            new Slime();
        }

        NotificationManager.launch();

        plugin.sessionManager = new SessionManager();
        plugin.playerManager = new PlayerManager();
    }

    private void getCommands() {
        Objects.requireNonNull(plugin.getCommand("deluxevoidworld")).setExecutor(new RegisterCommand());
        Objects.requireNonNull(plugin.getCommand("empty")).setExecutor(new MainCommand());
    }

    private void getMetrics() {
        UtilityFunctions.runTaskLater(60, () -> {
            Metrics metrics = new Metrics(plugin, 9736);
            YamlDocument worlds = plugin.getWorlds();
            Section worldsSection = worlds.getSection("WORLDS");

            if (worldsSection != null) {
                for (Object key : worldsSection.getKeys()) {
                    String keyStr = key.toString();
                    metrics.addCustomChart(new SimplePie("active_teleports", () ->
                            worldsSection.getString(keyStr + ".TP-WHEN-FALLING", "true")));
                }
            }

            metrics.addCustomChart(new SimplePie("language", () ->
                    plugin.getConfigYaml().getString("ADMIN-CONFIG.LANGUAGE", "en")));

            metrics.addCustomChart(new SingleLineChart("number_of_world_servers", () -> numberWorlds));
            metrics.addCustomChart(new SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
        });
    }

    private void setConnection() {
        cache = new Cache();
    }

    private void getNumbers() {
        UtilityFunctions.runTaskLater(15, () -> {
            dev.dejvokep.boostedyaml.block.implementation.Section worldsSection = plugin.getWorlds().getSection("WORLDS");
            if (worldsSection != null) {
                numberWorlds = worldsSection.getKeys().size();
            } else {
                numberWorlds = 0;
            }
        });
    }

    private void registerWorlds() {
        WorldSettingsHandler.loadWorld();
        WorldSettingsHandler.setWorlds();
        WorldSettingsHandler.ifConfigWorld();

        WorldSettingsHandler.setTime();
    }

    public void welcome() {
        UtilityFunctions.runTaskLater(16L, () -> {
            String linea1 = "___  ____ _    _  _ _  _ ____    _  _ ____ _ ___     _ _ _ ____ ____ _    ___  ";
            String linea2 = "|  \\ |___ |    |  |  \\/  |___    |  | |  | | |  \\    | | | |  | |__/ |    |  \\ ";
            String linea3 = "|__/ |___ |___ |__| _/\\_ |___     \\/  |__| | |__/    |_|_| |__| |  \\ |___ |__/ ";

            String color1 = "#54daf4";
            String color2 = "#545eb6";

            plugin.sendConsole("<gradient:" + color1 + ":" + color2 + ">" + linea1 + "</gradient><reset>");
            plugin.sendConsole("<gradient:" + color1 + ":" + color2 + ">" + linea2 + "</gradient><reset>");
            plugin.sendConsole("<gradient:" + color1 + ":" + color2 + ">" + linea3 + "</gradient><reset>");

            plugin.sendConsole("&f");
            plugin.sendConsole(plugin.getPrefix() + "&aSuccessfully enabled &cv" + plugin.version);
            plugin.sendConsole("&8--------------------------------------------------------------------------------------");
            plugin.sendConsole("&7         Developed by &c" + plugin.authors);
            plugin.sendConsole(plugin.getPrefix() + "&aVersion: &c" + plugin.version + " &ais loading... &8(&6Current&8)");
            plugin.sendConsole(plugin.getPrefix() + "&aServer: &c" + Bukkit.getVersion());
            plugin.sendConsole(plugin.getPrefix() + "&aLoading necessary files...");
            plugin.sendConsole("&f");
            plugin.sendConsole("&9[Loader] &fMaps loaded correctly: &f'&a" + Launcher.getNumberWorlds() + "&f'");
            plugin.sendConsole("&f");
            plugin.sendConsole("&fDeluxeVoidWorld &aStarting plugin...");
            plugin.sendConsole("&f");
            plugin.sendConsole("&8--------------------------------------------------------------------------------------");
        });
    }
}