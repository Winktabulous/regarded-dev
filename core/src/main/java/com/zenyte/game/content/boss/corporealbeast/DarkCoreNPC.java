package com.zenyte.game.content.boss.corporealbeast;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * @author Kris | 9. veebr 2018 : 22:17.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DarkCoreNPC extends NPC {
	public DarkCoreNPC(final Location tile, final CorporealBeastNPC corporealBeast) {
		super(NpcId.DARK_ENERGY_CORE, tile, Direction.SOUTH, 5);
		this.corporealBeast = corporealBeast;
		this.spawned = true;
		this.canRespawn = true;
		this.canBeStunned = true;
	}

	private boolean canRespawn;
	private boolean stunned;
	private boolean canBeStunned;
	private final CorporealBeastNPC corporealBeast;
	private final MutableInt jumpCounter = new MutableInt();
	private final MutableInt siphonCounter = new MutableInt();
	private final MutableInt flightCounter = new MutableInt();
	private final Set<Player> targets = new ObjectOpenHashSet<>();

	@Override
	public void processNPC() {
		if (isDead()) {
			return;
		}
		if (canBeStunned && !stunned) {
			if (toxins.isPoisoned() || toxins.isVenomed()) {
				stunned = true;
			}
		}
		if (flightCounter.getValue() > 0) {
			if (flightCounter.decrementAndGet() <= 0) {
				setLocation(new Location(getX(), getY(), 2));
			}
			return;
		}
		targets.clear();
		targets.addAll(corporealBeast.getCavern().getPlayers());
		if (targets.isEmpty() || corporealBeast.isDead()) {
			sendDeath();
			return;
		}
		siphon(targets);
		if (containsSiphonableTarget(targets)) {
			return;
		}
		if (targets.size() == 0) {
			sendDeath();
			return;
		}
		if ((jumpCounter.getAndIncrement() & 1) == 0) {
			return;
		}
		while (!targets.isEmpty()) {
			final Player player = Utils.getRandomCollectionElement(targets);
			if (player == null || this.isProjectileClipped(player, false)) {
				targets.remove(player);
				continue;
			}
			final Location destination = new Location(player.getLocation());
			final int distance = this.getLocation().getTileDistance(destination);
			//Every three tiles adds an extra tick to the flighttime of the projectile.
			final Projectile projectile = new Projectile(319, 0, 0, 0, 15, 30 + ((int) Math.ceil(distance / 3.0F) * 30), 0, 0);
			final int duration = World.sendProjectile(this, destination, projectile);
			this.flightCounter.setValue(duration + 1);
			setLocation(new Location(player.getX(), player.getY(), 3));
			if (stunned) {
				stunned = false;
				canBeStunned = false;
			}
			return;
		}
		sendDeath();
	}

	@Override
	public NPC spawn() {
		stunned = false;
		return super.spawn();
	}

	private void siphon(@NotNull final Collection<Player> collection) {
		if (stunned) {
			return;
		}
		if (siphonCounter.getAndIncrement() < 2) {
			return;
		}
		siphonCounter.setValue(0);
		if (collection.isEmpty()) {
			return;
		}
		for (final Player player : collection) {
			if (player.getLocation().withinDistance(getLocation(), 1)) {
				final int amount = Utils.random(1, 13);
				player.applyHit(new Hit(this, amount, HitType.REGULAR));
				corporealBeast.heal(amount);
				corporealBeast.setCoreDamaged();
				player.sendFilteredMessage("The dark core creature steals some life from you for its master.");
			}
		}
	}

	private final boolean containsSiphonableTarget(@NotNull final Collection<Player> collection) {
		for (final Player t : collection) {
			if (t.getLocation().withinDistance(getLocation(), 1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		return false;
	}

	@Override
	public void sendDeath() {
		targets.clear();
		final Player source = getMostDamagePlayerCheckIronman();
		WorldTasksManager.schedule(() -> onFinish(source));
		if (source != null) {
			corporealBeast.setCoreDamaged();
		}
	}

	@Override
	public void processHit(final Hit hit) {
		final int health = getHitpoints();
		super.processHit(hit);
		//If it had health before this hit got processed, but is now defined as dead, it means this hit killed it.
		if (health > 0 && isDead()) {
			//If it is "mid-air"
			if (getPlane() == 3) {
				final Object weapon = hit.getWeapon();
				//And if the weapon that killed it is dwarf multicannon.
				if (weapon != null && Objects.equals(weapon.toString(), "Dwarf Multicannon")) {
					//The core can no longer respawn for the rest of this kill.
					canRespawn = false;
				}
			}
		}
	}

	public boolean isCanRespawn() {
		return canRespawn;
	}
}
