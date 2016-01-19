package pl.panryba.mc.ban.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.panryba.mc.ban.PluginApi;
import pl.panryba.mc.ban.StringHelper;

import java.util.Date;

/**
 * @author PanRyba.pl
 */
public class TempBanCommand implements CommandExecutor {
    private final PluginApi api;

    public TempBanCommand(PluginApi api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 2) {
            return false;
        }

        Date validity = parseFutureDate(args[1]);
        String reason = StringHelper.join(" ", args, 2);

        api.banPlayer(args[0], sender.getName(), reason, validity);
        sender.sendMessage(ChatColor.YELLOW + "Zbanowales " + args[0] + " na " + args[1]);

        return true;
    }

    private Date parseFutureDate(String value) {
        if(value.endsWith("d")) {
            int days = Integer.parseInt(value.substring(0, value.length() - 1));
            Date date = new Date(new Date().getTime() + 24 * 60 * 60 * 1000 * (long)days);

            return date;
        }

        throw new IllegalArgumentException("invalid value: " + value);
    }
}
