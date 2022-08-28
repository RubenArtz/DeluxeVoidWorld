package ruben_artz.world.events.chat;

public class VOString {

    private String player;
    private String name;

    public VOString(String player, String name) {
        this.player = player;
        this.name = name;
    }

    public String getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setName(String name) {
        this.name = name;
    }
}
