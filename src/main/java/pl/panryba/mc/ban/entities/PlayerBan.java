package pl.panryba.mc.ban.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "banned_players")
public class PlayerBan extends Ban {
    @Column(name = "player", nullable = false, unique = true)
    public String player;

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return this.player;
    }

    public boolean isValid(Date now) {
        return getValidity() == null || getValidity().getTime() >= now.getTime();
    }
}
