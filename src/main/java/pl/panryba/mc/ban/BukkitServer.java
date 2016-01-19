package pl.panryba.mc.ban;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PanRyba.pl
 */
public class BukkitServer implements GameServer {
    @Override
    public List<GamePlayer> getOnlinePlayers() {
        Player[] players = Bukkit.getOnlinePlayers();

        List<GamePlayer> result = new ArrayList<>(players.length);
        for(Player player : players) {
            result.add(new BukkitPlayer(player));
        }

        return result;
    }
}
