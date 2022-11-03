package ruben_artz.world.world;

import lombok.Getter;
import org.bukkit.entity.Player;

public class VOArrays {
    @Getter private final Player player;
    @Getter private final int page;

    public VOArrays(Player player, int page) {
        this.player = player;
        this.page = page;
    }
}
