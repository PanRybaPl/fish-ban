package pl.panryba.mc.ban;

import org.bukkit.entity.Player;

public class BukkitPlayer implements GamePlayer {

    private final Player player;

    public BukkitPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public void kickPlayer(String message) {
        this.player.kickPlayer(message);
    }

    @Override
    public void sendMessage(String message) {
        this.player.sendMessage(message);
    }
}
