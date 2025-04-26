package com.zenyte.plugins.item;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Kris | 26/04/2019 17:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BasaltTeleportItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Weiss", (player, item, container, slotId) -> {
            player.getInventory().deleteItem(item.getId(), 1);
            TeleportCollection.ICY_BASALT_WEISS.teleport(player);
        });
        bind("Troll Stronghold", (player, item, container, slotId) -> {
            player.getInventory().deleteItem(item.getId(), 1);
            if (player.getSkills().getLevel(SkillConstants.AGILITY) >= 73 && DiaryUtil.eligibleFor(DiaryReward.FREMENNIK_SEA_BOOTS3, player)) {
                TeleportCollection.STONY_BASALT_TROLL_STRONGHOLD_TOP.teleport(player);
            } else {
                TeleportCollection.STONY_BASALT_TROLL_STRONGHOLD.teleport(player);
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                22599, 22601
        };
    }
}
