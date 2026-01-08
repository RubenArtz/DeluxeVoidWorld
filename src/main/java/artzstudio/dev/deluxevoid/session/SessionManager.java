package artzstudio.dev.deluxevoid.session;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private final Map<UUID, SessionMode> playerModes = new ConcurrentHashMap<>();

    private final Set<UUID> damageImmunity = ConcurrentHashMap.newKeySet();

    public void setMode(UUID uuid, SessionMode mode) {
        if (mode == SessionMode.NONE) {
            playerModes.remove(uuid);
        } else {
            playerModes.put(uuid, mode);
        }
    }

    public SessionMode getMode(UUID uuid) {
        return playerModes.getOrDefault(uuid, SessionMode.NONE);
    }

    public boolean isInMode(UUID uuid, SessionMode mode) {
        return getMode(uuid) == mode;
    }

    public void clearMode(UUID uuid) {
        playerModes.remove(uuid);
    }

    public void addDamageImmunity(UUID uuid) {
        damageImmunity.add(uuid);
    }

    public void removeDamageImmunity(UUID uuid) {
        damageImmunity.remove(uuid);
    }

    public boolean isDamageImmune(UUID uuid) {
        return damageImmunity.contains(uuid);
    }

    public void clearAll(UUID uuid) {
        playerModes.remove(uuid);
        damageImmunity.remove(uuid);
    }

    public enum SessionMode {
        NONE,
        ADD_WORLD_CHAT,
        EDIT_Y_CHAT,
        CREATE_WORLD_CHAT
    }
}