package ruben_artz.world.menu.utils;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class playerPageInfo {
    private final Player player;
    private final int page;

    public playerPageInfo(Player player, int page) {
        this.player = player;
        this.page = page;
    }
}
