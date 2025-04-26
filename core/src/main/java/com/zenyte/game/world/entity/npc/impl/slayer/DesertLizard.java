package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.achievementdiary.diaries.DesertDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.plugins.itemonnpc.IceCoolerOnLizardAction.ANIM;
import static com.zenyte.plugins.itemonnpc.IceCoolerOnLizardAction.GFX;

/**
 * @author Tommeh | 7 dec. 2017 : 19:01:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DesertLizard extends NPC implements Spawnable {
	public static final Item ICE_COOLER = new Item(6696);

	public DesertLizard(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
	}

	@Override
	public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		if (source == null) {
			super.sendDeath();
			return;
		}
		if (getHitpoints() > 0) {
			return;
		}
		final boolean unlocked = source.getSlayer().isUnlocked("Reptile freezer") && source.getInventory().containsItem(ItemId.ICE_COOLER);
		if (unlocked) {
			setGraphics(GFX);
			source.setAnimation(ANIM);
			source.getInventory().deleteItem(ItemId.ICE_COOLER, 1);
			kill(source);
		} else {
			heal(1);
		}
	}

	public void kill(final Player player) {
		player.getAchievementDiaries().update(DesertDiary.SLAY_DESERT_LIZARD);
		player.sendMessage("The lizard shudders and collapses from the freezing water.");
		super.sendDeath();
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id >= 458 && id <= 463;
	}
}
