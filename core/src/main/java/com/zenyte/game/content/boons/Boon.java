package com.zenyte.game.content.boons;

import com.zenyte.GameToggles;
import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * This represents an abstract player perk that can be purchased
 * with exchange points.
 *
 * @author John J. Woloszyk / Kryeus
 */
public abstract class Boon {

    public abstract String name();

    public abstract int price();

    public abstract String description();

    public boolean isModeEligible(GameMode gameMode) {
        return true;
    }

    public boolean isMemberEligible(MemberRank rank) {
        return true;
    }

    public boolean isPlayerEligible(Player player) {
        return true;
    }

    public boolean isStaffEligible(PlayerPrivilege rights) {
        return true;
    }

    public boolean isHidden() {
        return false;
    }

    public boolean isActive(Player player) {
        return !deactivated() && isPlayerEligible(player);
    }

    public boolean deactivated() {
        return false;
    }

    public boolean isAlwaysUnlocked(Player player) {
        return player.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR) && GameConstants.WORLD_PROFILE.isBeta() && GameToggles.ADMINS_HAVE_ALL_PERKS;
    }

    public boolean purchasable(Player p) {
        return isPlayerEligible(p) && isMemberEligible(p.getMemberRank()) && isModeEligible(p.getGameMode()) && isStaffEligible(p.getPrivilege());
    }

    public abstract int item();

    public String getAlternateName() {
        return "empty";
    }
}
