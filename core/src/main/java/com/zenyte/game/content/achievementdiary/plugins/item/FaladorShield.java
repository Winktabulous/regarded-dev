package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 10-11-2018 | 20:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FaladorShield extends DiaryItem {
    @Override
    public void handle() {
        bind("Recharge-prayer", (player, item, slotId) -> rechargePrayer(player, item));
        bind("Recharge Prayer", (player, item, slotId) -> rechargePrayer(player, item));
        bind("Check", (player, item, slotId) -> {
            final int limit = item.getId() == 13120 ? 2 : 1;
            final int chargesRemaining = limit - player.getVariables().getFaladorPrayerRecharges();
            if (chargesRemaining <= 0) {
                player.sendMessage(limit == 1 ? "You have used your charge for today." : "You have used both your charges for today.");
                return;
            }
            player.sendMessage("You have " + StringFormatUtil.asWords(chargesRemaining) + " remaining charge" + Utils.plural(chargesRemaining) + " for today.");
        });
    }

    private void rechargePrayer(final Player player, final Item item) {
        final int limit = item.getId() == 13120 ? 2 : 1;
        final double percentage = item.getId() == 13117 ? 0.25 : item.getId() == 13118 ? 0.5 : 1.0;
        final int recharges = player.getVariables().getFaladorPrayerRecharges();
        if (recharges >= limit) {
            player.sendMessage("You have already used all available recharges today. Try again tomorrow when the shield has recharged.");
            return;
        }
        if (player.getPrayerManager().getPrayerPoints() == player.getSkills().getLevelForXp(SkillConstants.PRAYER)) {
            player.sendMessage("Your prayer is already full!");
            return;
        }
        int points = player.getSkills().getLevelForXp(SkillConstants.PRAYER);
        points *= percentage;
        player.getPrayerManager().restorePrayerPoints(points);
        player.getVariables().setFaladorPrayerRecharges(player.getVariables().getFaladorPrayerRecharges() + 1);
        player.sendMessage("The shield restores some of your prayer points.");
    }

    @Override
    public int[] getItems() {
        return new int[] {13117, 13118, 13119, 13120};
    }
}
