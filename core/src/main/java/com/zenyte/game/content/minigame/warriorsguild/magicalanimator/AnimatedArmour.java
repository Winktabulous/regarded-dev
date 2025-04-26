package com.zenyte.game.content.minigame.warriorsguild.magicalanimator;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16. dets 2017 : 2:45.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AnimatedArmour extends NPC {

	private static final ForceTalk FORCETALK = new ForceTalk("I'm ALIVE!");
	private static final Animation RISE = new Animation(4166);

    AnimatedArmour(final int id, final Location tile, final Player player) {
		super(id, tile, Direction.NORTH, 0);
		setSpawned(true);
		this.player = player;
		setForceTalk(FORCETALK);
		setAnimation(RISE);
		this.supplyCache = false;
	}

	private int ticks;

	private final Player player;

    @Override
	public boolean isAcceptableTarget(final Entity entity) {
        return entity == player;
    }

	@Override
	public void processNPC() {
		super.processNPC();
		if (!isUnderCombat()) {
		    if (++ticks > 100) {
		        getReceivedDamage().clear();
		        sendDeath();
            }
        } else {
		    ticks = 0;
        }
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getHitType() == HitType.MAGIC || hit.getHitType() == HitType.RANGED) {
			hit.setDamage(0);
		}
	}

    @Override
    public float getXpModifier(final Hit hit) {
        return hit.getHitType() == HitType.MAGIC || hit.getHitType() == HitType.RANGED ? 0 : 1;
    }

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public boolean canAttack(final Player source) {
		return source == player;
	}

	@Override
	public void onFinish(final Entity source) {
	    super.onFinish(source);
		player.getTemporaryAttributes().remove("animatedArmour");
		player.getPacketDispatcher().resetHintArrow();
	}

}
