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

package artzstudio.dev.deluxevoid.data.world;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WorldManager {

    private String player;
    private String name;

    public WorldManager(String player, String name) {
        this.player = player;
        this.name = name;
    }
}
