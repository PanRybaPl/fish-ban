package pl.panryba.mc.ban;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * @author PanRyba.pl
 */
public class PlayerListener implements Listener {

    private final PluginApi api;

    public PlayerListener(PluginApi api) {
        this.api = api;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoining(AsyncPlayerPreLoginEvent event) {
        CheckBanResult result = api.checkBan(event.getName());

        switch(result.getReason()) {
            case NOT_BANNED:
                return;

            case BANNED:
                String msg = api.formatBanMessage(result);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, msg);
                break;
        }
    }
}
