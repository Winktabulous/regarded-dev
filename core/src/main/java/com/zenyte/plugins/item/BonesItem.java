package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 25. aug 2018 : 22:10:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public class BonesItem extends ItemPlugin {

	@Override
	public void handle() {
		bind("Scatter", (player, item, slot) -> callBury(player, item, slot, true));
		bind("Bury", (player, item, slot) -> callBury(player, item, slot, false));
	}

	private void callBury(final Player player, final Item item, final int slotId, final boolean ash) {
		final Bones bone = ash ? Bones.getAsh(item.getId()) : Bones.getBone(item.getId());
		if (bone == null) {
			return;
		}
		if (bone == Bones.SUPERIOR_DRAGON_BONES) {
			if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < 70) {
				player.sendMessage("You need a Prayer level of at least 70 to bury the Superior dragon bones");
				return;
			}
		}
		Bones.bury(player, bone, item, slotId);
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final Bones bones : Bones.VALUES) {
			for (final Item bone : bones.getItems()) {
				list.add(bone.getId());
			}
		}
		return list.toArray(new int[list.size()]);
	}
}