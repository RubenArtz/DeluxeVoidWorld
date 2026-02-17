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

package artzstudio.dev.deluxevoid.utils.slim;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.app.builder.SpigotApplicationBuilder;
import io.github.slimjar.injector.loader.factory.InjectableFactory;
import io.github.slimjar.logging.ProcessLogger;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class SlimJar {
    private static final boolean DEBUG = Boolean.getBoolean("deluxe-void.debug-slimjar");
    private static final boolean DISABLE_REMAPPER = Boolean.getBoolean("deluxe-void.disable-remapper");

    private static final ReentrantLock lock = new ReentrantLock();
    private static final AtomicBoolean loaded = new AtomicBoolean();

    public static void load(DeluxeVoidWorld plugin) {
        if (loaded.get()) return;
        lock.lock();

        try {
            if (loaded.getAndSet(true)) return;

            final var downloadPath = plugin.getDataFolder().getParentFile().toPath()
                    .resolve("Artz-Libraries")
                    .resolve(plugin.getName());

            ProcessLogger customLogger = new ProcessLogger() {
                @Override
                public void info(@NonNull String message, @Nullable Object... args) {
                    plugin.getLogger().info(message.formatted(args));
                }

                @Override
                public void error(@NonNull String message, @Nullable Object... args) {
                    plugin.getLogger().severe(message.formatted(args));
                }

                @Override
                public void debug(@NonNull String message, @Nullable Object... args) {
                    if (DEBUG) plugin.getLogger().info("[DEBUG] " + message.formatted(args));
                }
            };

            plugin.getLogger().info("Loading libraries...");

            try {
                new SpigotApplicationBuilder(plugin)
                        .logger(customLogger)
                        .downloadDirectoryPath(downloadPath)
                        .debug(DEBUG)
                        .remap(!DISABLE_REMAPPER)
                        .build();
            } catch (Throwable e) {
                try {
                    ApplicationBuilder.appending(plugin.getName())
                            .logger(customLogger) // Usamos el logger que creamos arriba
                            .injectableFactory(InjectableFactory.selecting(InjectableFactory.ERROR, InjectableFactory.INJECTABLE, InjectableFactory.WRAPPED, InjectableFactory.UNSAFE))
                            .downloadDirectoryPath(downloadPath)
                            .build();
                } catch (Throwable fallbackError) {
                    plugin.getLogger().severe("CRITICAL: Failed to download/load libraries via fallback!\n" + fallbackError.getMessage());
                }

                plugin.getLogger().severe(e.getMessage());
            }
            plugin.getLogger().info("Libraries loaded successfully!");
        } finally {
            lock.unlock();
        }
    }
}