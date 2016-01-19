package pl.panryba.mc.ban;

/**
 * @author PanRyba.pl
 */
public interface GamePlayer {
    public String getName();
    public void kickPlayer(String message);
    void sendMessage(String message);
}
