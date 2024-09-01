package ruben_artz.world.events.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VOString {

    private String player;
    private String name;

    public VOString(String player, String name) {
        this.player = player;
        this.name = name;
    }
}
