package pl.panryba.mc.ban;

import com.avaje.ebean.EbeanServer;
import pl.panryba.mc.ban.commands.BanCommand;
import pl.panryba.mc.ban.commands.TempBanCommand;
import pl.panryba.mc.ban.commands.UnbanCommand;
import pl.panryba.mc.ban.entities.PlayerBan;
import pl.panryba.mc.db.FishDbPlugin;

import java.util.List;

/**
 * @author PanRyba.pl
 */
public class Plugin extends FishDbPlugin {
    PluginApi api;

    @Override
    public void onEnable() {
        EbeanServer db = getCustomDatabase();
        BukkitServer server = new BukkitServer();

        api = new PluginApi(server, db);

        getServer().getPluginManager().registerEvents(new PlayerListener(api), this);

        getCommand("b").setExecutor(new BanCommand(api));
        getCommand("ub").setExecutor(new UnbanCommand(api));
        getCommand("tb").setExecutor(new TempBanCommand(api));
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = super.getDatabaseClasses();
        Plugin.fillDatabaseClasses(list);
        return list;
    }


    public static void fillDatabaseClasses(List<Class<?>> list) {
        list.add(PlayerBan.class);
    }
}
