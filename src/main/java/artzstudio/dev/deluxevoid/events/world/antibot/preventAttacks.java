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

package artzstudio.dev.deluxevoid.events.world.antibot;

import com.lahuca.botsentry.api.BotSentryAPI;
import org.bukkit.Bukkit;

public class preventAttacks {

    public static boolean isAttacking() {
        if (!Bukkit.getPluginManager().isPluginEnabled("BotSentry")) return false;
        BotSentryAPI api = BotSentryAPI.getAPI();
        return api.getAntiBotMode().isAntiBotMode();
    }
}