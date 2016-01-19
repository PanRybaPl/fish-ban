package pl.panryba.mc.ban.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.panryba.mc.ban.PluginApi;
import pl.panryba.mc.ban.StringHelper;

/**
 * @author PanRyba.pl
 */
public class BanCommand implements CommandExecutor {

    private final PluginApi api;

    public BanCommand(PluginApi api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            return false;
        }

        String reason = StringHelper.join(" ", args, 1);
        api.banPlayer(args[0], sender.getName(), reason);
        sender.sendMessage(ChatColor.YELLOW + "Zbanowales " + args[0]);

        return true;
    }
}
