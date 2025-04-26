package com.zenyte.game.content.skills.mining.actions;

import com.zenyte.game.content.skills.mining.MiningDefinitions.OreDefinitions;
import com.zenyte.game.content.stars.ShootingStar;
import com.zenyte.game.content.stars.ShootingStarLevel;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Noele | Nov 9, 2017 : 12:22:34 AM
 *
 * @see https://noeles.life || noele@zenyte.com
 */

public class Prospect extends Action {

	private final WorldObject rock;
	private final OreDefinitions ore;

	public Prospect(WorldObject rock, OreDefinitions ore) {
		this.rock = rock;
		this.ore = ore;
	}

	public Prospect(WorldObject rock) {
		this(rock, OreDefinitions.getDef(rock.getId()));
	}

	public Prospect(final OreDefinitions ore) {
		this(null, ore);
	}

	@Override
	public boolean start() {
		if(!check()) {
			return false;
		}
		if (!ore.isShootingStar()) {
			player.sendMessage("You examine the rock for ores...");
		}
		player.lock();
		delay(3);
		return true;
	}

	@Override
	public boolean process() {
		return checkObject();
	}

	@Override
	public int processWithDelay() {
		if (ore == OreDefinitions.GRANITE) {
			player.sendMessage("This rock contains granite.");
		} else if (ore == OreDefinitions.SANDSTONE) {
			player.sendMessage("This rock contains sandstone.");
		} else if (ore == OreDefinitions.GEM) {
			player.sendMessage("This rock contains gems.");
		} else if (ore.isShootingStar()) {
			if (!(rock instanceof ShootingStar star)) {
				player.sendMessage("You may only prospect shooting stars that have recently crashed.");
				return -1;
			}

			ShootingStarLevel level = star.getLevel();
			player.getDialogueManager().start(new PlainChat(player, "This is a size-" + level.getNumeric() + " star. A Mining level of at least " + level.getLevelRequirement() + " is required to mine this layer. It has been mined " + star.percentMined() + "% of the way to the core."));
		} else {
			player.sendMessage("This rock contains "+ore.getName()+".");
		}
		player.unlock();
		return -1;
	}

	@Override
	public void stop() {}

	public boolean check() {
		if(ore == null) {
			player.sendMessage("There is currently no ore available in this rock.");
		}
		return (ore != null && checkObject());
	}

	private boolean checkObject() {
		if (ore.equals(OreDefinitions.RUNITE_GOLEM_ROCKS)) {
			return true;
		}
		return rock != null && World.getRegion(rock.getRegionId()).containsObject(rock.getId(), rock.getType(), rock);
	}

}
