package com.zenyte.game.content.skills.magic.spells.regular;

import com.zenyte.game.content.partyroom.FaladorPartyRoom;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.FloorItemSpell;
import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.AreaManager;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.region.Chunk;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.plugins.flooritem.FloorItemPlugin;
import com.zenyte.plugins.flooritem.FloorItemPluginLoader;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 8. juuli 2018 : 13:29:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TelekineticGrab implements FloorItemSpell, NPCSpell {
	@Override
	public int getDelay() {
		return 2000;
	}

	private final List<Integer> blackListItems = List.of(SMOULDERING_HEART, SMOULDERING_GLAND, SMOULDERING_PILE_OF_FLESH);

	@Override
	public boolean spellEffect(final Player player, final FloorItem item) {
		if (blackListItems.contains(item.getId())) {
			player.sendMessage("You cannot grab this item.");
			return false;
		}
		player.getActionManager().setAction(new TelekineticGrabFollowAction(item, this));
		return false;
	}

	@Override
	public boolean spellEffect(final Player player, final NPC npc) {
		player.sendMessage("You can't use Telekinetic Grab on them.");
		return false;
	}

	@Override
	public boolean canUse(Player player) {
		if (player.inArea(FaladorPartyRoom.class)) {
			player.sendMessage("You can't use Telekinetic Grab here.");
			return false;
		}
		return FloorItemSpell.super.canUse(player);
	}

	private static final class TelekineticGrabFollowAction extends Action {
		private static final Animation CAST = new Animation(723);
		private static final Graphics CAST_GFX = new Graphics(142, 0, 92);
		private static final Graphics CATCH = new Graphics(144);
		private static final Projectile PROJECTILE = new Projectile(143, 45, 5, 50, 15, 10, 64, 5);
		private static final SoundEffect synth = new SoundEffect(3006);
		private static final SoundEffect area = new SoundEffect(3007, 10, 17);

		public TelekineticGrabFollowAction(final FloorItem item, final MagicSpell spell) {
			this.item = item;
			this.spell = spell;
		}

		public final FloorItem item;
		private final MagicSpell spell;

		@Override
		public boolean start() {
			return true;
		}

		@Override
		public boolean process() {
			return true;
		}

		@Override
		public int processWithDelay() {
			final Location location = item.getLocation();
			final double distance = player.getLocation().getDistance(item.getLocation());
			if (distance >= 11 || player.isProjectileClipped(location, false)) {
				if (!player.hasWalkSteps()) {
					player.calcFollow(item.getLocation(), -1, true, true, false);
					if (!player.hasWalkSteps()) {
						player.sendMessage("I can't reach that.");
						return -1;
					}
				}
				return 0;
			}
			if (player.isIronman() && item.hasOwner() && !item.isOwner(player)) {
				player.sendMessage("You're an Iron Man, so you can't take items that other players have dropped.");
				return -1;
			}
			player.resetWalkSteps();
			player.setFaceLocation(location);
			player.setAnimation(CAST);
			player.setGraphics(CAST_GFX);
			final SpellDefinitions definitions = SpellDefinitions.SPELLS.get(spell.getSpellName());
			if (definitions == null) {
				return -1;
			}
			final int level = definitions.getLevel();
			final Item[] runes = definitions.getRunes();
			final long spellDelay = player.getNumericTemporaryAttribute("spellDelay").longValue();
			if (spellDelay > Utils.currentTimeMillis()) {
				return -1;
			}
			if (player.isLocked()) {
				return -1;
			}
			final SpellState state = new SpellState(player, level, runes);
			if (!state.check()) {
				return -1;
			}
			player.lock();
			player.setLunarDelay(spell.getDelay());
			state.remove();
			SpellbookSwap.checkSpellbook(player);
			spell.addXp(player, 43);
			player.sendSound(synth);
			final int delay = World.sendProjectile(player, location, PROJECTILE);
			WorldTasksManager.schedule(() -> World.sendGraphics(CATCH, location), delay - 1);
			WorldTasksManager.schedule(() -> {
				final Location tile = item.getLocation();
				final int chunkId = Chunk.getChunkHash(tile.getX() >> 3, tile.getY() >> 3, tile.getPlane());
				final Chunk chunk = World.getChunk(chunkId);
				World.sendSoundEffect(tile, area);
				if (!chunk.getFloorItems().contains(item)) {
					player.sendMessage("Too late - It's gone!");
					return;
				}
				final FloorItemPlugin plugin = FloorItemPluginLoader.plugins.get(item.getId());
				if (plugin != null) {
					plugin.telegrab(player, item);
				} else {
					player.log(LogLevel.INFO, "Telegrabbed item '" + item + "'.");
					World.destroyFloorItem(item);
					player.getInventory().addItem(item).onFailure(it -> World.spawnFloorItem(it, player));
				}
			}, delay);
			return -1;
		}

		@Override
		public void stop() {
			player.unlock();
		}
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}
}
