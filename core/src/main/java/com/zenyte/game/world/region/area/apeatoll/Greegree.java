package com.zenyte.game.world.region.area.apeatoll;

import com.zenyte.game.GameInterface;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Appearance;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.CharacterLoop;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 28. aug 2018 : 14:09:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Greegree {
	KARAMJAN_MONKEY_GREEGREE(4031, 1469), SMALL_NINJA_MONKEY_GREEGREE(4024, 1462), MEDIUM_NINJA_MONKEY_GREEGREE(4025, 1463), SMALL_ZOMBIE_MONKEY_GREEGREE(4029, 1467), LARGE_ZOMBIE_MONKEY_GREEGREE(4030, 1468), GORILLA_GREEGREE(4026, 1464), BEARDED_GORILLA_GREEGREE(4027, 1465), ANCIENT_GORILLA_GREEGREE(4028, 1466), KRUK_MONKEY_GREEGREE(19525, 5257);
	public static final Graphics GFX = new Graphics(160, 0, 92);
	public static final Greegree[] VALUES = values();
	public static final Int2ObjectOpenHashMap<Greegree> MAPPED_VALUES = new Int2ObjectOpenHashMap<Greegree>(VALUES.length);
	private final int itemId;
	private final int npcId;

	public final void transform(final Player player) {
		final Appearance appearance = player.getAppearance();
		if (appearance.getNpcId() == npcId) {
			return;
		}
		player.setGraphics(Greegree.GFX);
		player.getTemporaryAttributes().put("greegree", true);
		player.getInterfaceHandler().closeInterface(GameInterface.SPELLBOOK);
		appearance.setNpcId(npcId);
		appearance.resetRenderAnimation();
		player.setMaximumTolerance(true);
		player.setFindTargetDelay(Utils.currentTimeMillis() + 5000);
		CharacterLoop.forEach(player.getLocation(), 15, NPC.class, npc -> {
			if (npc.isDead()) {
				return;
			}
			final NPCCombat combat = npc.getCombat();
			if (combat.getTarget() == player) {
				combat.removeTarget();
			}
		});
	}

	public static final void reset(final Player player) {
		final Appearance appearance = player.getAppearance();
		player.setGraphics(Greegree.GFX);
		appearance.setNpcId(-1);
		appearance.resetRenderAnimation();
		player.getTemporaryAttributes().remove("greegree");
		GameInterface.SPELLBOOK.open(player);
		player.setMaximumTolerance(false);
	}

	static {
		for (final Greegree greegree : VALUES) {
			MAPPED_VALUES.put(greegree.itemId, greegree);
		}
	}

	Greegree(int itemId, int npcId) {
		this.itemId = itemId;
		this.npcId = npcId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getNpcId() {
		return npcId;
	}
}
