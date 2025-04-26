package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.greatolm.FireWallNPC;
import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 17. jaan 2018 : 21:28.24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class FireWall implements OlmCombatScript {
	private static final Projectile projectile = new Projectile(1347, 65, 0, 30, 15, 30, 0, 0);
	private static final Projectile[] projectiles = new Projectile[] {new Projectile(1348, 10, 10, 0, 27, 30, 0, 0), new Projectile(1348, 10, 10, 0, 24, 30, 0, 0), new Projectile(1348, 10, 10, 0, 21, 30, 0, 0), new Projectile(1348, 10, 10, 0, 18, 30, 0, 0), new Projectile(1348, 10, 10, 0, 15, 30, 0, 0), new Projectile(1348, 10, 10, 0, 12, 30, 0, 0), new Projectile(1348, 10, 10, 0, 9, 30, 0, 0), new Projectile(1348, 10, 10, 0, 6, 30, 0, 0), new Projectile(1348, 10, 10, 0, 3, 30, 0, 0), new Projectile(1348, 10, 10, 0, 0, 30, 0, 0)};
	private final List<NPC> firewalls = new ArrayList<>(20);
	private final List<Location> tiles = new ArrayList<>(20);
	private static final SoundEffect sound = new SoundEffect(3750, 15, 0);

	@Override
	public void handle(final GreatOlm olm) {
		final Player player = olm.random(olm.getDirection());
		if (player == null) {
			return;
		}
		final OlmRoom room = olm.getRoom();
		final int side = room.getSide();
		final int fromX = side == OlmRoom.RIGHT ? room.getFireWallSouthWesternLoc().getX() : room.getFireWallNorthEasternLoc().getX();
		olm.performAttack();
		if (!(player.getY() > room.getFireWallSouthWesternLoc().getY() + 1 && player.getY() < room.getFireWallNorthEasternLoc().getY() - 1)) {
			return;
		}
		olm.getScripts().add(this.getClass());
		final Location head = olm.getFaceCoordinates();
		final Location fromNorthernWall = new Location(fromX, player.getY() + 1, player.getPlane());
		final Location fromSouthernWall = new Location(fromX, player.getY() - 1, player.getPlane());
		final int y = player.getY();
		World.sendProjectile(head, fromNorthernWall, projectile);
		World.sendProjectile(head, fromSouthernWall, projectile);
		World.sendSoundEffect(fromNorthernWall, sound);
		World.sendSoundEffect(fromSouthernWall, sound);
		WorldTasksManager.schedule(new WorldTask() {
			private int count = 0;
			private int ticks = 1;
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed()) {
					stop();
					return;
				}
				if (ticks == 1) {
					for (final Projectile proj : projectiles) {
						final Location north = new Location(fromNorthernWall.getX() + (side == OlmRoom.LEFT ? -count : count), fromNorthernWall.getY(), fromNorthernWall.getPlane());
						final Location south = new Location(fromSouthernWall.getX() + (side == OlmRoom.LEFT ? -count++ : count++), fromSouthernWall.getY(), fromSouthernWall.getPlane());
						tiles.add(north);
						tiles.add(south);
						World.sendProjectile(fromNorthernWall, north, proj);
						World.sendProjectile(fromSouthernWall, south, proj);
					}
					for (final Location tile : tiles) {
						World.spawnObject(new WorldObject(0, 10, 0, tile));
					}
				} else if (ticks == 0) {
					final List<Player> everyone = olm.everyone(GreatOlm.ENTIRE_CHAMBER);
					checkPlayers(fromNorthernWall.getPositionHash(), true, everyone);
					checkPlayers(fromSouthernWall.getPositionHash(), false, everyone);
					count = 0;
					for (int i = 0; i < projectiles.length + 5; i++) {
						if (count >= tiles.size()) {
							break;
						}
						final Location north = tiles.get(count++);
						final Location south = tiles.get(count++);
						firewalls.add(new FireWallNPC(north).spawn());
						firewalls.add(new FireWallNPC(south).spawn());
						checkPlayers(north.getPositionHash(), true, everyone);
						checkPlayers(south.getPositionHash(), false, everyone);
					}
				} else if (ticks == -8) {
					for (final NPC firewall : firewalls) {
						firewall.finish();
					}
					for (final Player player : olm.everyone(GreatOlm.ENTIRE_CHAMBER)) {
						if (player.getY() == y) {
							player.applyHit(new Hit(olm, Utils.random(40, 75), HitType.REGULAR));
						}
					}
					olm.getScripts().remove(FireWall.this.getClass());
					stop();
				}
				ticks--;
			}
		}, 1, 0);
	}

	private static final Animation animation = new Animation(1114);

	private void checkPlayers(final int hash, final boolean north, final List<Player> everyone) {
		for (final Player player : everyone) {
			if (player.getLocation().getPositionHash() == hash) {
				player.stop(Player.StopType.ROUTE_EVENT, Player.StopType.ACTIONS, Player.StopType.WALK, Player.StopType.INTERFACES);
				player.lock(1);
				player.applyHit(new Hit(Utils.random(3, 6), HitType.REGULAR).setExecuteIfLocked());
				player.sendMessage("You leap away from the flame, getting slightly scorched in the process.");
				player.setAnimation(animation);
				final Location tile = player.getLocation().transform(north ? Direction.NORTH : Direction.SOUTH, 1);
				player.setForceMovement(new ForceMovement(player.getLocation(), 0, tile, 30, OlmRoom.getMovementDirection(player, tile)));
				WorldTasksManager.schedule(() -> player.setLocation(tile));
			}
		}
	}
}
