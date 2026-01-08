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