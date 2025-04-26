package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 15. veebr 2018 : 22:30.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Geomancy implements DefaultSpell {

	//private static final List<FarmingPatch> LIST = Arrays.asList(FarmingPatch.VALUES);
	private static final Animation ANIM = new Animation(7118);
	private static final Graphics GFX = new Graphics(1285, 30, 0);
	private static final SoundEffect SOUND = new SoundEffect(2891, 3, 50);
	
	@Override
	public int getDelay() {
		return 5000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		player.lock();
        World.sendSoundEffect(player, SOUND);
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		this.addXp(player, 60);
		WorldTasksManager.schedule(() -> {
			player.unlock();
			//sendInterface(player);
		}, 3);
		return true;
	}
	
/*	public static final void sendInterface(final Player player) {
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 179);
		final EnumList enumeration = EnumList.get(1236);
		final List<FarmingPatch> patches = new ArrayList<FarmingPatch>(LIST);
		sendFarmingPatches(player, enumeration, patches);
		sendCompostBins(player, enumeration, patches);
		sendEmptyPatches(player, enumeration, patches);
	}
	
	private static final void sendFarmingPatches(final Player player, final EnumList enumeration, final List<FarmingPatch> patches) {
		final List<FarmingSpot> spots = player.getFarming().getSpots();
		final int size = spots.size();
		for (int i = 0; i < size; i++) {
			final FarmingSpot spot = spots.get(i);
			if (spot == null) {
				continue;
			}
			final FarmingPatch patch = spot.getPatch();
			patches.remove(patch);
			final int state = getState(spot);
			final int settings = getSettings(spot);
			final int product = getProduct(spot);
			final int componentId = enumeration.getIntValue(patch.getIndex()) & 0x3FFF;
			if (product != 1925) {
				player.getPacketDispatcher().sendComponentItem(179, componentId, product, 0);
			}
			player.getPacketDispatcher().sendClientScript(1119, patch.getIndex(), product, state, settings);
		}
	}
	
	private static final void sendCompostBins(final Player player, final EnumList enumeration, final List<FarmingPatch> patches) {
		final List<CompostBinType> bins = player.getFarming().getBins();
		final int binCount = bins.size();
		for (int i = 0; i < binCount; i++) {
			final CompostBinType spot = bins.get(i);
			if (spot == null) {
				continue;
			}
			final FarmingPatch patch = spot.getPatch();
			patches.remove(patch);
			final int state = getCompostState(spot);
			final int product = CompostBinClearingAction.PRODUCE[spot.getType() - 1];
			final int componentId = enumeration.getIntValue(patch.getIndex()) & 0x3FFF;
			if (product != 1925) {
				player.getPacketDispatcher().sendComponentItem(179, componentId, product, 0);
			}
			player.getPacketDispatcher().sendClientScript(1119, patch.getIndex(), product, state, 0);
		}
	}
	
	private static final void sendEmptyPatches(final Player player, final EnumList enumeration, final List<FarmingPatch> patches) {
		final int remaining = patches.size();
		for (int i = 0; i < remaining; i++) {
			final FarmingPatch patch = patches.get(i);
			if (patch == null) {
				continue;
			}
			final int componentId = enumeration.getIntValue(patch.getIndex()) & 0x3FFF;
			if (patch.getType() != PatchType.GRAPEVINE_PATCH) {
				player.getPacketDispatcher().sendComponentItem(179, componentId, 6055, 0);
			}
			player.getPacketDispatcher().sendClientScript(1119, patch.getIndex(), 6055, 0, 0);
		}
	}
	
	private static final int getState(final FarmingSpot spot) {
		int state = 0;
		if (spot.isWatered()) {
			state |= 1 << 28;
		}
		if (spot.getProduct() == null) {
			return state;
		}
		final PatchDefinition def = spot.getProduct().getDefinition();
		if (spot.hasRenewableProduce() && spot.getLives() != -1) {
			state |= spot.getLives() << 20;
		}
		final int maxState = def.getStagesAmount();
		final int currentState = spot.getStage() - def.getBaseStage();
		state |= maxState;
		state |= currentState << 14;
		return state;
	}
	
	private static final int getCompostState(final CompostBinType bin) {
		int state = 0;
		if (bin.isFinished()) {
			state |= bin.getSize() << 20;
		}
		return state;
	}
	
	private static final int getSettings(final FarmingSpot spot) {
		if (spot.getProduct() == null) {
			return 0;
		}
		int settings = 0;
		if (spot.isDead()) {
			settings |= 1 << 1;
		} else if (spot.isDiseased()) {
			settings |= 1 << 0;
		}
		settings |= 1 << (4 + spot.getCompostType());
		if (spot.isWatchedOver()) {
			settings |= 1 << 2;
		} else if (spot instanceof AllotmentSpot && spot.isProtected()) {
			settings |= 1 << 3;
		}
		return settings;
	}
	
	private static final int getProduct(final FarmingSpot spot) {
		if (spot.getPatch().getType() == PatchType.SPIRIT_TREE_PATCH) {
			return 20635;
		} else if (spot.getPatch().getType() == PatchType.GRAPEVINE_PATCH) {
			return 1987;
		}
		int product = 1925;
		if (spot.getStage() < 3 && spot.getPatch().getType() != PatchType.GRAPEVINE_PATCH) {
			product = 6055;
		} else if (spot.getProduct() != null && spot.getProduct().getProduct() != null) {
			product = spot.getProduct().getProduct().getId();
		}
		return product;
	}
	*/
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}
