package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 6. dets 2017 : 1:43.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class BarrowsWightNPC extends BarrowsNPC {

	private boolean receivedNonSpecialDamage;
	public BarrowsWightNPC(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (!pvmArenaVersion && hit.getSource() instanceof final Player player) {
			if (!HitType.MAGIC.equals(hit.getHitType()) && !HitType.DEFAULT.equals(hit.getHitType())) {
				player.getCombatAchievements().removeCurrentTaskFlag(CAType.DEFENCE_WHAT_DEFENCE, Barrows.CA_TASK_MAGIC_DAMAGE_ONLY);
			}
			if (id == BarrowsWight.KARIL.getNpcId() && !hit.isSpecial() && !HitType.POISON.equals(hit.getHitType())) {
				receivedNonSpecialDamage = true;
			}
		}
	}

	@NotNull
	BarrowsWight getWight() {
		final BarrowsWight wight = CollectionUtils.findMatching(BarrowsWight.values, npc -> npc.getNpcId() == getId());
		if (wight == null) {
			return null;
		}
		return wight;
	}

	@Override
	public boolean isAcceptableTarget(final Entity entity) {
		if (pvmArenaVersion)
			return super.isAcceptableTarget(entity);
		if (owner == null || !(entity instanceof Player)) {
			return false;
		}
		final Player owner = this.owner.get();
		if (owner == null) {
			return false;
		}
		return owner.getUsername().equals(((Player) entity).getUsername());
	}

	@Override
	public void handleOutgoingHit(Entity target, Hit hit) {
		super.handleOutgoingHit(target, hit);
		if (!pvmArenaVersion && hit.getDamage() > 0 && target instanceof final Player player) {
			player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAY_FOR_SUCCESS, Barrows.CA_TASK_NO_DAMAGE);
		}
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (pvmArenaVersion)
			return;
		if (this.owner == null) {
			return;
		}
		final Player owner = this.owner.get();
		if (owner == null) {
			return;
		}
		owner.getBarrows().removeTarget();
	}

	@Override
	public void onFinish(final Entity source) {
		super.onFinish(source);
		if (pvmArenaVersion)
			return;
		if (source instanceof Player) {
			if (this.owner == null) {
				return;
			}
			final Player owner = this.owner.get();
			if (owner == null) {
				return;
			}
			owner.getBarrows().onDeath(this);
			if (owner.getCombatAchievements().hasCurrentTaskFlags(CAType.DEFENCE_WHAT_DEFENCE, Barrows.CA_TASK_MAGIC_DAMAGE_ONLY)) {
				owner.getCombatAchievements().complete(CAType.DEFENCE_WHAT_DEFENCE);
			}
			if (!receivedNonSpecialDamage && id == BarrowsWight.KARIL.getNpcId()) {
				owner.getCombatAchievements().complete(CAType.JUST_LIKE_THAT);
			}
		}
	}
}
