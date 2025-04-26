package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

import java.util.List;

/**
 * @author Kris | 29. juuni 2018 : 00:20:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SpinnerNPC extends PestNPC {
	private static final Animation SIPHON = new Animation(3909);
	private static final Animation EXPLODING = new Animation(6876);
	private static final Graphics SIPHON_GFX = new Graphics(658);

	public SpinnerNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(instance, portal, id, tile);
	}

	private boolean siphoning;
	private boolean exploding;
	private int ticks;

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		if (siphoning) {
			return false;
		}
		return super.addWalkStep(nextX, nextY, lastX, lastY, check);
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public void processNPC() {
		if (exploding) {
			if (++ticks == 2) {
				finish();
				final List<Entity> targets = getPossibleTargets(EntityType.BOTH);
				final Location location = getLocation();
				for (final Entity target : targets) {
					if (!target.getLocation().withinDistance(location, 2)) {
						continue;
					}
					target.getToxins().applyToxin(ToxinType.POISON, Utils.random(2, 6), this);
				}
			}
			return;
		}
		if (portal.isDead()) {
			siphoning = false;
			exploding = true;
			ticks = 0;
			setAnimation(EXPLODING);
			this.addWalkSteps(Utils.random(-5, 5), Utils.random(-5, 5));
			return;
		}
		if (!siphoning) {
			if (!hasWalkSteps()) {
				calcFollow(portal, -1, true, false, false);
			}
			if (Utils.isOnRange(getX(), getY(), getSize(), portal.getX(), portal.getY(), portal.getSize(), 0)) {
				siphoning = true;
				faceEntity(portal);
			}
		} else {
			if (ticks++ % 4 == 0) {
				setAnimation(SIPHON);
				setGraphics(SIPHON_GFX);
				portal.heal(PestControlUtilities.SPINNER_HEAL);
			}
		}
	}
}
