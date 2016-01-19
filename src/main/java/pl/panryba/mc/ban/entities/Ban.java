package pl.panryba.mc.ban.entities;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public abstract class Ban {
    @Id
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "banned_by")
    public String bannedBy;

    @Column(name = "reason")
    public String reason;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "validity", nullable = true)
    private Date validity;

    @Version
    private Timestamp lastUpdate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public void setBannedBy(String bannedBy) {
        this.bannedBy = bannedBy;
    }

    public String getBannedBy() {
        return this.bannedBy;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
