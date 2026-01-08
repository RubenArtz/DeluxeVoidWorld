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
