package pl.panryba.mc.ban.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.panryba.mc.ban.PluginApi;
import pl.panryba.mc.ban.entities.PlayerBan;

/**
 * @author PanRyba.pl
 */
public class UnbanCommand implements CommandExecutor {

    private final PluginApi api;

    public UnbanCommand(PluginApi api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            return false;
        }

        String name = args[0];

        PlayerBan ban = api.unbanPlayer(name);
        if(ban == null) {
            sender.sendMessage(ChatColor.GRAY + name + " nie jest zbanowany");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + ban.getPlayer() + " zostal odbanowany");
        return true;
    }
}
