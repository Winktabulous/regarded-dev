package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 7 dec. 2017 : 20:58:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class HarpieBugSwarm extends NPC implements Spawnable {
	public HarpieBugSwarm(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
	}

	@Override
	public float getXpModifier(Hit hit) {
		final Entity source = hit.getSource();
		if (source instanceof Player) {
			if (!SlayerEquipment.LIT_BUG_LANTERN.isWielding(((Player) source))) {
				return 0.0F;
			}
		}
		return super.getXpModifier(hit);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getSource() instanceof Player) {
			if (!SlayerEquipment.LIT_BUG_LANTERN.isWielding(((Player) hit.getSource()))) {
				hit.setDamage(0);
			}
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 464;
	}
}
