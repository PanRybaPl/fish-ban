package pl.panryba.mc.ban;

import java.util.Date;

public class CheckBanResult {
    private CheckBanReason reason;
    private String details;
    private Date validity;

    protected CheckBanResult(CheckBanReason reason, String details, Date validity) {
        this.reason = reason;
        this.details = details;
        this.validity = validity;
    }

    public static CheckBanResult Banned(String details, Date validity) {
        return new CheckBanResult(CheckBanReason.BANNED, details, validity);
    }

    public static CheckBanResult NotBanned() {
        return new CheckBanResult(CheckBanReason.NOT_BANNED, null, null);
    }

    public CheckBanReason getReason() {
        return reason;
    }
    public String getDetails() {
        return details;
    }
    public Date getValidity() { return validity; }
}
