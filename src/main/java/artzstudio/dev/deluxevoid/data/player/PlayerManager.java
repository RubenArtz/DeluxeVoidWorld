package artzstudio.dev.deluxevoid.data.player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private final Map<UUID, PlayerData> players = new ConcurrentHashMap<>();

    public PlayerData getPlayer(UUID uuid) {
        return players.computeIfAbsent(uuid, PlayerData::new);
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }
}