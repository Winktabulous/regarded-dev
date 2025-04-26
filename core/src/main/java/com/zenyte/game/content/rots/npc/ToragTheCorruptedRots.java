package com.zenyte.game.content.rots.npc;

import com.zenyte.game.content.rots.RotsInstance;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;

public class ToragTheCorruptedRots extends RotsBrother implements CombatScript {

	private static final Graphics TORAGS_GFX = new Graphics(399);
	private long reflectTicks;
	public ToragTheCorruptedRots(final Location tile, RotsInstance instance) {
		super(16039, tile, instance);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);

		if (reflectTicks >= WorldThread.getCurrentCycle() && hit.getDamage() > 0 && hit.getSource() instanceof Player player) {
			player.scheduleHit(this, hit, 0);
		}
	}

	@Override
	public int attack(final Entity target) {
		if (Utils.randomBoolean(11)) {
			reflectTicks = WorldThread.getCurrentCycle() + 10;
			setGraphics(new Graphics(1517, 0, 300));
		}

		this.executeMeleeHit(target, combatDefinitions.getMaxHit()).onLand(hit -> {
			if (hit.getDamage() > 0 && Utils.random(3) == 0) {
				target.setGraphics(TORAGS_GFX);
				if (target instanceof Player) {
					final PlayerVariables variables = ((Player) target).getVariables();
					double energy = variables.getRunEnergy();
					energy -= (energy * 0.2F);
					variables.setRunEnergy(energy);
				}
			}
		});
		animate();
		return combatDefinitions.getAttackSpeed();
	}

}
