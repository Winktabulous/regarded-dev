package com.zenyte.game.world.entity.player.loyalty;

import java.util.Date;

public class LoyaltySession {
    private final Date login;
    private final Date logout;

    public LoyaltySession(Date login, Date logout) {
        this.login = login;
        this.logout = logout;
    }

    public Date getLogin() {
        return login;
    }

    public Date getLogout() {
        return logout;
    }

    @Override
    public String toString() {
        return "LoyaltySession(login=" + this.getLogin() + ", logout=" + this.getLogout() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoyaltySession)) return false;
        final LoyaltySession other = (LoyaltySession) o;
        if (!other.canEqual(this)) return false;
        final Object this$login = this.getLogin();
        final Object other$login = other.getLogin();
        if (this$login == null ? other$login != null : !this$login.equals(other$login)) return false;
        final Object this$logout = this.getLogout();
        final Object other$logout = other.getLogout();
        return this$logout == null ? other$logout == null : this$logout.equals(other$logout);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoyaltySession;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $login = this.getLogin();
        result = result * PRIME + ($login == null ? 43 : $login.hashCode());
        final Object $logout = this.getLogout();
        result = result * PRIME + ($logout == null ? 43 : $logout.hashCode());
        return result;
    }
}
