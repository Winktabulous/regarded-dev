package com.zenyte.game.content.chambersofxeric.npc;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.room.LizardmanShamanRoom;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.LizardmanSpawn;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.region.CharacterLoop;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.List;

/**
 * @author Kris | 19. nov 2017 : 1:03.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LizardmanShaman extends RaidNPC<LizardmanShamanRoom> implements CombatScript {
	public LizardmanShaman(final Raid raid, final LizardmanShamanRoom room, final int id, final Location tile) {
		super(raid, room, id, tile);
		this.setForceAggressive(true);
	}

	private static final Animation attack = new Animation(7193);
	private static final Animation jump = new Animation(7152);
	private static final Animation land = new Animation(6946);
	private static final Animation meleeAnim = new Animation(7158);
	private static final Projectile rangedProj = new Projectile(1291, 70, 15, 60, 15, 10, 64, 5);
	private static final Projectile poisonProj = new Projectile(1293, 70, 15, 60, 15, 20, 64, 5);
	private static final Graphics poisonSplash = new Graphics(1294);
	private long lastTime;

	@Override
	public void processNPC() {
		super.processNPC();
		if (getCombat().getTarget() != null && Utils.random(60) == 0 && lastTime < Utils.currentTimeMillis()) {
			final Entity target = getCombat().getTarget();
			if (isProjectileClipped(target, false) || getCombat().outOfRange(target, combatDefinitions.getAttackDistance(), target.getSize(), true)) {
				return;
			}
			final IntArrayList tiles = new IntArrayList();
			int count = 0;
			Location tile = null;
			while (true) {
				if (++count > 50) {
					break;
				}
				tile = new Location(target.getX() + Utils.random(-2, 2), target.getY() + Utils.random(-2, 2), target.getPlane());
				if (World.isFloorFree(tile, 2)) {
					break;
				}
			}
			if (tile == null) {
				tile = target.getLocation();
			}
			Location t;
			while (tiles.size() < 3) {
				if (++count > 100) {
					for (int i = 0; i < 3 - tiles.size(); i++) {
						tiles.add(tile.getPositionHash());
					}
					break;
				}
				t = new Location(tile.getX() + Utils.random(-1, 1), tile.getY() + Utils.random(-1, 1), tile.getPlane());
				if (!tiles.contains(t.getPositionHash()) && World.isFloorFree(t, 1)) {
					tiles.add(t.getPositionHash());
				}
			}
			if (target instanceof Player) {
				lastTime = Utils.currentTimeMillis() + 20000;
				for (final int spawnTile : tiles) {
					new LizardmanSpawn(new Location(spawnTile), ((Player) target), false).spawn();
				}
				final List<Player> players = CharacterLoop.find(getMiddleLocation(), 2, Player.class, player -> !player.isDead());
				for (final Player p : players) {
					if (p.getLocation().withinDistance(getMiddleLocation(), 2)) {
						p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, (byte) 5, (byte) 0, (byte) 0);
						WorldTasksManager.schedule(() -> p.getPacketDispatcher().resetCamera(), 2);
					}
				}
			}
		}
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	private static final SoundEffect attackSound = new SoundEffect(385, 10, 0);
	private static final SoundEffect meleeSound = new SoundEffect(1695, 10, 0);

	@Override public void handleOutgoingHit(Entity target, Hit hit) {
		super.handleOutgoingHit(target, hit);
		if (hit.getDamage() > 0 && target instanceof final Player player && !room.getHitByShamansUsernames().contains(player.getUsername())) {
			room.getHitByShamansUsernames().add(player.getUsername());
		}
	}

	@Override
	public int attack(final Entity target) {
		final LizardmanShaman npc = this;
		int style = Utils.random(isWithinMeleeDistance(npc, target) ? 6 : 5);
		//If jumping, we validate that the monster can actually perform the jump.
		if (style == 5) {
			final Location targetTile = target.getLocation().transform(-1, -1, 0);
			if (World.isFloorFree(targetTile, npc.getSize())) {
				npc.lock();
				npc.setCantInteract(true);
				npc.setAnimation(jump);
				WorldTasksManager.schedule(new WorldTask() {
					private int ticks;
					@Override
					public void run() {
						if (npc.isDead()) {
							npc.unlock();
							npc.setCantInteract(false);
							stop();
							return;
						}
						if (ticks == 0) {
							//Stop everyone that's attacking the lizardman from attacking it as it moves position.
							CharacterLoop.forEach(targetTile, 15, Player.class, p -> {
								final Action action = p.getActionManager().getAction();
								if (action instanceof PlayerCombat) {
									if (((PlayerCombat) action).getTarget() == LizardmanShaman.this) {
										p.getActionManager().forceStop();
									}
								}
							});
							npc.setLocation(targetTile);
							World.sendSoundEffect(targetTile, meleeSound);
							npc.setAnimation(land);
						} else if (ticks == 1) {
							for (final Player p : CharacterLoop.find(targetTile, 1, Player.class, player -> !player.isDead() && (npc.isMultiArea() || player == target))) {
								delayHit(npc, -1, target, new Hit(npc, Utils.random(20, 25), HitType.REGULAR));
								p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, (byte) 10, (byte) 0, (byte) 0);
								WorldTasksManager.schedule(() -> p.getPacketDispatcher().resetCamera(), 2);
							}
						} else if (ticks == 2) {
							npc.unlock();
							npc.setCantInteract(false);
							npc.getCombat().setTarget(target);
							stop();
						}
						ticks++;
					}
				}, 4, 0);
				return 8;
			} else {
				if ((style = Utils.random(isWithinMeleeDistance(npc, target) ? 5 : 4)) == 5) {
					style = 6;
				}
			}
		}
		switch (style) {
		case 6: 
			World.sendSoundEffect(getLocation(), meleeSound);
			npc.setAnimation(meleeAnim);
			delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, (int) (24 * (raid.isChallengeMode() ? 1.5F : 1.0F)), CRUSH, target), HitType.MELEE));
			break;
		case 4: 
			World.sendSoundEffect(getLocation(), attackSound);
			npc.setAnimation(attack);
			final Location tt = new Location(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getPlane());
			World.sendProjectile(npc, tt, poisonProj);
			final int perfectTime = poisonProj.getProjectileDuration(npc.getLocation(), tt);
			World.sendGraphics(new Graphics(poisonSplash.getId(), perfectTime - 10, 0), tt);
			World.sendSoundEffect(tt, new SoundEffect(1070, 10, perfectTime - 10));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (npc.isDead()) {
						stop();
						return;
					}
					for (final Player p : CharacterLoop.find(tt, 2, Player.class, player -> !player.isDead() && (npc.isMultiArea() || player == target))) {
						if (p != target && room.getSafePolygon().contains(p.getLocation())) {
							continue;
						}
						delayHit(-1, p, new Hit(npc, (int) (Utils.random(20, 35) * com.zenyte.game.world.entity.npc.impl.LizardmanShaman.protectionModifier(p)), HitType.POISON).onLand(hit -> {
							if (Utils.random(3) == 0) {
								p.getToxins().applyToxin(Toxins.ToxinType.POISON, 10, LizardmanShaman.this);
							}
						}));
					}
				}
			}, poisonProj.getTime(npc, tt));
			break;
		default: 
			World.sendSoundEffect(getLocation(), attackSound);
			npc.setAnimation(attack);
			World.sendProjectile(npc.getFaceLocation(target), target, rangedProj);
			delayHit(npc, rangedProj.getTime(npc.getLocation(), target.getLocation()), target, new Hit(npc, getRandomMaxHit(npc, (int) (24 * (raid.isChallengeMode() ? 1.5F : 1.0F)), RANGED, target), HitType.RANGED));
			break;
		}
		return 4;
	}

	@Override
	public void sendDeath() {
		super.sendDeath();
		room.finish();
	}
}
