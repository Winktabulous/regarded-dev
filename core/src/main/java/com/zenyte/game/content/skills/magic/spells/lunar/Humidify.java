package com.zenyte.game.content.skills.magic.spells.lunar;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.achievementdiary.diaries.DesertDiary;
import com.zenyte.game.content.chambersofxeric.greatolm.FireWallNPC;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 16. veebr 2018 : 16:50.25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Humidify implements DefaultSpell, NPCSpell {
	private static final Animation ANIM = new Animation(6294);
	private static final Graphics GFX = new Graphics(1061, 0, 72);
	private static final SoundEffect sound = new SoundEffect(3614, 5, 86);
	private static final ImmutableMap<Integer, Integer> VESSELS = ImmutableMap.<Integer, Integer>builder().put(1925, 1929).put(6667, 6668).put(1935, 1937).put(229, 227).put(20800, 20801).put(5331, 5340).put(5333, 5340).put(5334, 5340).put(5335, 5340).put(5336, 5340).put(5337, 5340).put(5338, 5340).put(5339, 5340).put(1831, 1823).put(1829, 1823).put(1827, 1823).put(1825, 1823).put(1923, 1921).put(434, 1761).put(5358, 5370).put(5359, 5371).put(5360, 5372).put(5361, 5373).put(5362, 5374).put(5363, 5375).put(5480, 5496).put(5481, 5497).put(5482, 5498).put(5483, 5499).put(5484, 5500).put(5485, 5501).put(5486, 5502).put(5487, 5503).put(21469, 21477).put(21471, 21480).put(22848, 22856).put(22850, 22859).put(22862, 22866).build();

	@Override
	public int getDelay() {
		return 4500;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		return spellEffect(player);
	}

	private final boolean spellEffect(final Player player) {
		if (!containsVessels(player)) {
			player.sendMessage("You have no items capable of being filled in your inventory.");
			return false;
		}
		World.sendSoundEffect(player, sound);
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		final Inventory inventory = player.getInventory();
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItem(i);
			if (item == null) {
				continue;
			}
			final Integer response = VESSELS.get(item.getId());
			if (response == null) {
				continue;
			}
			final Item it = new Item(response);
			item.setId(it.getId());
			if (item.getName().contains("Waterskin")) {
				player.getAchievementDiaries().update(DesertDiary.REFILL_WATERSKINS_WITH_HUMIDIFY);
			}
		}
		player.getInventory().refreshAll();
		this.addXp(player, 65);
		player.sendMessage("Moisture fills your inventory.");
		return true;
	}

	private final boolean containsVessels(final Player player) {
		final Inventory inventory = player.getInventory();
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItem(i);
			if (item == null) {
				continue;
			}
			if (VESSELS.get(item.getId()) != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

	@Override
	public boolean spellEffect(final Player player, final NPC npc) {
		if (!(npc instanceof FireWallNPC)) {
			player.sendMessage("You can only use humidify to douse fire walls within the Chambers of Xeric.");
			return false;
		}
		if (!hasDefenceRequirement(player)) {
			return false;
		}
		final Projectile projectile = new Projectile(1669, 0, 0, 0, 0, 60, 0, 0);
		World.sendProjectile(player, npc, projectile);
		World.sendSoundEffect(player, sound);
		player.setAnimation(ANIM);
		player.faceEntity(npc);
		WorldTasksManager.schedule(() -> {
			player.sendMessage("You douse the flame.");
			npc.finish();
		});
		return true;
	}
}
