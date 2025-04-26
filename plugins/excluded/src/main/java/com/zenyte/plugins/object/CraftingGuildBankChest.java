package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 29. mai 2018 : 17:33:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CraftingGuildBankChest implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
	    if (option.equalsIgnoreCase("Use")) {
            if (!DiaryUtil.eligibleFor(DiaryReward.FALADOR_SHIELD3, player) && !SkillcapePerk.CRAFTING.isEffective(player)) {
                player.sendMessage("You need to complete the falador hard diaries in order to use this bank chest");
                return;
            }
            GameInterface.BANK.open(player);
        } else if (option.equalsIgnoreCase("Collect")) {
	        GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player);
        }
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.BANK_CHEST_14886};
	}
}
