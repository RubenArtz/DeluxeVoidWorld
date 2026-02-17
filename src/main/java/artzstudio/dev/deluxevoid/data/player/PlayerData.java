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

package artzstudio.dev.deluxevoid.data.player;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {
    private final UUID uuid;

    private boolean teleportEnabled = true;
    private boolean jumpEnabled = false;
    private boolean lightningEnabled = false;
    private boolean particlesEnabled = false;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }
}