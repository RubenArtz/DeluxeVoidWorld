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

package artzstudio.dev.deluxevoid.license;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;

import java.util.HashMap;
import java.util.Map;

public class LicenseManager {

    private static final Map<String, String> ERROR_MESSAGES = new HashMap<>();

    static {
        ERROR_MESSAGES.put("invalid", "License key not found in our database.");
        ERROR_MESSAGES.put("suspended", "This license has been suspended.");
        ERROR_MESSAGES.put("expired", "The license has expired.");
        ERROR_MESSAGES.put("ip_limit", "IP Address limit reached for this license.");
        ERROR_MESSAGES.put("error", "An internal server error occurred.");
        ERROR_MESSAGES.put("connection_error", "Could not connect to authentication server.");
        ERROR_MESSAGES.put("parse_error", "Could not parse server response.");
    }

    private final DeluxeVoidWorld plugin;
    private final LicenseService service;

    public LicenseManager(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
        this.service = new LicenseService();
    }

    public void verifyLicense(String licenseKey) {
        plugin.sendConsole(plugin.getPrefix() + "&eVerifying license...");

        service.validate(licenseKey)
                .thenAccept(result -> UtilityFunctions.runTask(() -> processResult(result)));
    }

    private void processResult(LicenseService.LicenseResult result) {
        if (result.isValid()) {
            Launcher.getInstance().initGameplay("SECURE_ACCESS_" + System.currentTimeMillis());
            plugin.sendConsole(plugin.getPrefix() + "&aLicense verified successfully!");
        } else {
            handleFailure(result.statusCode(), result.serverMessage());
        }
    }

    private void handleFailure(String statusCode, String serverMessage) {
        String finalMessage = serverMessage;

        if (finalMessage == null || finalMessage.equalsIgnoreCase("No message provided")) {
            finalMessage = ERROR_MESSAGES.getOrDefault(statusCode, "Error: " + statusCode);
        }

        plugin.sendConsole("");
        plugin.sendConsole(plugin.getPrefix() + "&c############## WARNING ##############");
        plugin.sendConsole(plugin.getPrefix());
        plugin.sendConsole(plugin.getPrefix() + "&cFailed to validate license:");
        plugin.sendConsole(plugin.getPrefix() + "&cReason: " + finalMessage);

        if (statusCode.equalsIgnoreCase("invalid") || statusCode.equalsIgnoreCase("suspended")) {
            plugin.sendConsole(plugin.getPrefix() + "&cYour license is not valid for this product.");
        }

        plugin.sendConsole(plugin.getPrefix() + "&cTo get a license please visit https://store.rubenmatias.com/");
        plugin.sendConsole(plugin.getPrefix() + "&c-> Our discord: https://artzstudio.dev/discord");
        plugin.sendConsole(plugin.getPrefix() + "&c-> Dashboard: https://dash.artzstudio.dev/dashboard");
        plugin.sendConsole(plugin.getPrefix());
        plugin.sendConsole(plugin.getPrefix() + "&c############## WARNING ##############");
        plugin.sendConsole("");
    }
}