package pl.panryba.mc.ban;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class IntegrationTest {

    private class TestPlayer implements GamePlayer {
        private final String name;
        private final TestGameServer server;
        private String kickMessage;
        private String message;

        private TestPlayer(TestGameServer server, String name) {
            this.server = server;
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public void kickPlayer(String message) {
            this.kickMessage = message;
            this.server.getOnlinePlayers().remove(this);
        }

        @Override
        public void sendMessage(String message) {
            this.message = message;
        }

        public String getKickMessage() {
            return kickMessage;
        }

        public String getMessage() {
            return message;
        }
    }

    private class TestGameServer implements GameServer {
        private List<GamePlayer> onlinePlayers;

        public TestGameServer() {
            this.onlinePlayers = new ArrayList<>();
        }

        @Override
        public List<GamePlayer> getOnlinePlayers() {
            return this.onlinePlayers;
        }

    }

    @Test
    public void testIntegration() {
        ServerConfig config = new ServerConfig();
        config.setName("h2test");

        DataSourceConfig db = new DataSourceConfig();
        db.setDriver("org.h2.Driver");
        db.setUsername("sa");
        db.setPassword("");
        db.setUrl("jdbc:h2:mem:tests;DB_CLOSE_DELAY=-1;TRACE_LEVEL_SYSTEM_OUT=2");

        config.setDataSourceConfig(db);

        config.setDdlGenerate(true);
        config.setDdlRun(true);
        config.setDefaultServer(false);
        config.setRegister(false);

        List<Class<?>> list = new ArrayList<>();
        Plugin.fillDatabaseClasses(list);
        for(Class<?> c : list) {
            config.addClass(c);
        }

        EbeanServer dbServer = EbeanServerFactory.create(config);
        TestGameServer gameServer = new TestGameServer();

        TestPlayer p1 = new TestPlayer(gameServer, "p1");
        TestPlayer p2 = new TestPlayer(gameServer, "p2");
        TestPlayer p3 = new TestPlayer(gameServer, "p3");

        PluginApi api = new PluginApi(gameServer, dbServer);
        assertEquals(CheckBanReason.NOT_BANNED, api.checkBan(p1.getName()).getReason());

        List<GamePlayer> online = gameServer.getOnlinePlayers();
        online.add(p1);

        api.banPlayer(p1.getName(), "CONSOLE", null);
        assertEquals(CheckBanReason.BANNED, api.checkBan(p1.getName()).getReason());
        assertEquals(0, online.size());

        CheckBanResult result = api.checkBan(p1.getName());
        assertNull(result.getDetails());
        assertNull(result.getValidity());

        api.banPlayer(p1.getName(), "CONSOLE", "test ban");
        assertEquals(CheckBanReason.BANNED, api.checkBan(p1.getName()).getReason());
        result = api.checkBan(p1.getName());
        assertEquals("test ban", result.getDetails());
        assertNull(result.getValidity());

        api.banPlayer(p1.getName(), "CONSOLE", "test ban", new Date(new Date().getTime() + 1000 * 60 * 24));
        assertEquals(CheckBanReason.BANNED, api.checkBan(p1.getName()).getReason());
        result = api.checkBan(p1.getName());
        assertEquals("test ban", result.getDetails());
        assertNotNull(result.getValidity());

        api.banPlayer(p1.getName(), "CONSOLE", "test ban", new Date(new Date().getTime() - 1000));
        assertEquals(CheckBanReason.NOT_BANNED, api.checkBan(p1.getName()).getReason());

        api.unbanPlayer(p1.getName());
        assertEquals(CheckBanReason.NOT_BANNED, api.checkBan(p1.getName()).getReason());

        online.add(p1);
        assertEquals(1, online.size());
        api.reload();
        assertEquals(1, online.size());

        online.add(p2);
        online.add(p3);

        assertEquals(3, online.size());
        api.banPlayer(p1.getName(), "CONSOLE", "");
        assertEquals(2, online.size());
        api.banPlayer(p2.getName(), "CONSOLE", "");
        assertEquals(1, online.size());
        api.banPlayer(p3.getName(), "CONSOLE", "");
        assertEquals(0, online.size());

        api.unbanPlayer(p1.getName());
        assertEquals(CheckBanReason.NOT_BANNED, api.checkBan(p1.getName()).getReason());
        api.unbanPlayer(p2.getName());
        assertEquals(CheckBanReason.NOT_BANNED, api.checkBan(p2.getName()).getReason());
        api.unbanPlayer(p3.getName());
        assertEquals(CheckBanReason.NOT_BANNED, api.checkBan(p3.getName()).getReason());
    }
}
