package ruben_artz.world.world;

import org.bukkit.entity.Player;

public class VOArrays {
    private Player player;
    private int page;

    public VOArrays(Player player, int page) {
        this.player = player;
        this.page = page;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int world) {
        this.page = world;
    }

    public int getInit(int slot) {
        return 45 * (this.page - 1) + slot;
    }
}
