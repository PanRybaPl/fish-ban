package pl.panryba.mc.ban;

import com.avaje.ebean.EbeanServer;
import org.bukkit.ChatColor;
import pl.panryba.mc.ban.entities.PlayerBan;
import pl.panryba.mc.pl.LanguageHelper;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author PanRyba.pl
 */
public class PluginApi {
    private final EbeanServer db;
    private Map<String, PlayerBan> bannedPlayers;
    private final GameServer server;

    public PluginApi(GameServer server, EbeanServer db) {
        this.server = server;
        this.db = db;

        this.reload();
    }

    public void reload() {
        this.bannedPlayers = new ConcurrentHashMap<>();

        Set<PlayerBan> playerBans = this.db.find(PlayerBan.class).findSet();
        for(PlayerBan ban : playerBans) {
            this.bannedPlayers.put(ban.getPlayer().toLowerCase(), ban);
        }

        for(GamePlayer player : this.server.getOnlinePlayers()) {
            CheckBanResult result = this.checkBan(player.getName());

            if(result.getReason() == CheckBanReason.BANNED) {
                String msg = this.formatBanMessage(result);
                player.kickPlayer(msg);
            }
        }
    }

    public String formatBanMessage(CheckBanResult result) {
        String msg = ChatColor.YELLOW + "Zostales zbanowany!";

        if(result.getDetails() != null) {
            msg += "\n\n" + ChatColor.GRAY + "Powod\n " + ChatColor.YELLOW + result.getDetails();
        }

        Date validity = result.getValidity();
        if(validity != null) {
            Date now = new Date();
            long diff = validity.getTime() - now.getTime();

            if(diff > 1000) {
                msg += "\n\n" + ChatColor.GRAY + "Mozesz wrocic na serwer za\n" + ChatColor.YELLOW + LanguageHelper.formatDHMS(diff / 1000);
            }
        }

        return msg;
    }

    public CheckBanResult checkBan(String playerName) {
        PlayerBan ban = this.bannedPlayers.get(playerName.toLowerCase());
        if(ban == null) {
            return CheckBanResult.NotBanned();
        }

        Date now = new Date();
        if(!ban.isValid(now)) {
            return CheckBanResult.NotBanned();
        }

        return CheckBanResult.Banned(ban.getReason(), ban.getValidity());
    }

    public void banPlayer(String playerName, String bannedBy, String reason) {
        this.banPlayer(playerName, bannedBy, reason, null);
    }

    public void banPlayer(String playerName, String bannedBy, String reason, Date validity) {
        String lowerName = playerName.toLowerCase();
        PlayerBan ban = this.bannedPlayers.get(lowerName);

        if(ban == null) {
            ban = new PlayerBan();
            ban.setPlayer(playerName);
            this.bannedPlayers.put(lowerName, ban);
        }

        ban.setBannedBy(bannedBy);

        if(reason != null && reason.isEmpty()) {
            reason = null;
        }
        ban.setReason(reason);

        ban.setValidity(validity);
        this.db.save(ban);

        CheckBanResult result = this.checkBan(lowerName);
        String kickMsg = this.formatBanMessage(result);

        for(GamePlayer player : this.server.getOnlinePlayers()) {
            if(player.getName().toLowerCase().equals(lowerName)) {
                player.kickPlayer(kickMsg);
                break;
            }
        }

        String broadcastMsg = ChatColor.RED + playerName + ChatColor.YELLOW + " zostal zbanowany przez " + ChatColor.GREEN + bannedBy;
        if(reason != null && !reason.isEmpty()) {
            broadcastMsg += ChatColor.YELLOW + " - " + reason;
        }

        this.sendToAll(broadcastMsg);
    }

    private void sendToAll(String message) {
        for(GamePlayer player : this.server.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public PlayerBan unbanPlayer(String name) {
        PlayerBan ban = this.bannedPlayers.remove(name.toLowerCase());
        if(ban == null) {
            return null;
        }

        this.db.delete(ban);
        return ban;
    }
}
