package com.zenyte.game.content.skills.thieving;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.entity.player.privilege.MemberRank;

/**
 * @author Kris | 21. okt 2017 : 14:46.17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class Thieving {
	public static boolean success(final Player player, final int requirement) {
		final int level = player.getSkills().getLevel(SkillConstants.THIEVING);
		final double baseChance = 5.0 / 833 * level;
		final double reqChance = 0.49 - (requirement * 0.0032) - 0.02;
		double chance = baseChance + reqChance;
		if (player.getPerkManager().isValid(PerkWrapper.SLEIGHT_OF_HAND)) {
			chance *= 1.15F;
		}
		if (SkillcapePerk.THIEVING.isEffective(player)) {
			chance *= 1.1F;
		}
		if (player.getMemberRank().equalToOrGreaterThan(MemberRank.RESPECTED)) {
			chance *= 1.05F;
		}
		if (DiaryUtil.eligibleFor(DiaryReward.ARDOUGNE_CLOAK3, player)) {
			chance *= 1.1F;
		} else if (player.inArea("Ardougne") && DiaryUtil.eligibleFor(DiaryReward.ARDOUGNE_CLOAK2, player)) {
			chance *= 1.1F;
		}
		if(PlayerAttributesKt.getFlaggedAsBot(player))
			chance *= 0.01;
		return Utils.randomDouble() < chance;
	}
}
