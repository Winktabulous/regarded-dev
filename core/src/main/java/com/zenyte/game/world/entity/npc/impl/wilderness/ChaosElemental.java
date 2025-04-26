package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import kotlin.collections.CollectionsKt;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Tommeh | 2 feb. 2018 : 20:56:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChaosElemental extends NPC implements CombatScript, Spawnable {

	private static final Graphics NPC_TELEPORT_GFX = new Graphics(553, 0, 280);
	private static final Graphics NPC_DISARMING_GFX = new Graphics(550, 0, 280);
	private static final Graphics NPC_PRIMARY_GFX = new Graphics(557, 0, 280);
	private static final Graphics PLAYER_TELEPORT_GFX = new Graphics(555, 0, 92);
	private static final Graphics PLAYER_DISARMING_GFX = new Graphics(552, 0, 92);
	private static final Graphics PLAYER_PRIMARY_GFX = new Graphics(558, 0, 92);
	private static final Projectile TELEPORT_PROJ = new Projectile(554, 70, 30, 0, 15, 29, 32, 5);
	private static final Projectile DISARMING_PROJ = new Projectile(551, 70, 30, 0, 15, 29, 32, 5);
	private static final Projectile PRIMARY_PROJ = new Projectile(557, 70, 30, 0, 15, 29, 32, 5);
	private static final ArrayList<String> unequippedPlayers = new ArrayList<String>();
	private static final ArrayList<String> damagedPlayers = new ArrayList<String>();
	private boolean canTeleportPlayers = true;
	private boolean canAttackMultipleTarget = false;

	public ChaosElemental(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int getRespawnDelay() {
		return BossRespawnTimer.CHAOS_ELEMENTAL.getTimer().intValue();
	}

	@Override public void handleOutgoingHit(Entity target, Hit hit) {
		super.handleOutgoingHit(target, hit);
		if (hit.getDamage() > 0 && hit.getSource() instanceof final Player player && !damagedPlayers.contains(player.getUsername())) {
			damagedPlayers.add(player.getUsername());
		}
	}

	@Override
	public int attack(Entity target) {
		final int style = Utils.random(3);
		final int attack = canTeleportPlayers
				? Utils.random(4)
				: 1 + Utils.random(4);
		setAnimation(getCombatDefinitions().getAttackAnim());
		final Set<Player> targets = canAttackMultipleTarget
				? CollectionsKt.toSet(CollectionsKt.filterIsInstance(getPossibleTargets(EntityType.PLAYER), Player.class))
				: Set.of((Player) target);
		switch (attack) {
		case 0:
			setGraphics(NPC_TELEPORT_GFX);
			targets.forEach(player -> {
				WorldTasksManager.schedule(() -> {
					if (!player.getLocation().withinDistance(getLocation(), 15)) {
						return;
					}
					Location tile;
					int count = 50;
					while (true) {
						if (--count == 0) {
							tile = new Location(player.getX() + Utils.random(12), player.getY() + Utils.random(12), player.getPlane());
							break;
						}
						tile = new Location(getX() + Utils.random(12), getY() + Utils.random(12), getPlane());
						if (World.isTileFree(tile, 1)) {
							break;
						}
					}
					if (World.isTileFree(tile, 1)) {
						player.setGraphics(PLAYER_TELEPORT_GFX);
						player.setLocation(tile);
					} else
						player.sendDeveloperMessage("Failed to teleport player to a free tile.");
				}, World.sendProjectile(this, player, TELEPORT_PROJ) + 1);
			});
			break;
		case 1:
			setGraphics(NPC_DISARMING_GFX);
			targets.forEach(player -> {
				WorldTasksManager.schedule(() -> {
					if (!player.getLocation().withinDistance(getLocation(), 15)) {
						return;
					}
					if (player.getEquipment().unequipItem(EquipmentSlot.WEAPON.getSlot())) {
						player.getActionManager().forceStop();
						player.sendMessage("The Chaos Elemental disarms you!");
						player.setGraphics(PLAYER_DISARMING_GFX);
						if (!unequippedPlayers.contains(player.getUsername())) {
							unequippedPlayers.add(player.getUsername());
						}
					}
				}, World.sendProjectile(this, player, DISARMING_PROJ));
			});

			break;
		default:
			setGraphics(NPC_PRIMARY_GFX);
			targets.forEach(player -> delayHit(this, World.sendProjectile(this, player, PRIMARY_PROJ), player, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), style == 0 ? MELEE : style == 1 ? RANGED : MAGIC, player), style == 0 ? HitType.MELEE : style == 1 ? HitType.RANGED : HitType.MAGIC).onLand(hit -> player.setGraphics(PLAYER_PRIMARY_GFX))));
			break;
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player player) {
			player.getAchievementDiaries().update(WildernessDiary.KILL_THE_CHAOS_ELEMENTAL);
		}
	}

	@Override
	protected void onFinish(Entity source) {
		super.onFinish(source);

		if (source instanceof Player player) {
			player.getCombatAchievements().checkKcTask("chaos elemental", 10, CAType.CHAOS_ELEMENTAL_ADEPT);
			player.getCombatAchievements().checkKcTask("chaos elemental", 25, CAType.CHAOS_ELEMENTAL_VETERAN);
			if (!unequippedPlayers.contains(player.getUsername())) {
				player.getCombatAchievements().complete(CAType.HOARDER);
			}
			if (!damagedPlayers.contains(player.getUsername())) {
				player.getCombatAchievements().complete(CAType.THE_FLINCHER);
			}
		}
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("chaos elemental");
	}

	public void setCanTeleportPlayers(boolean canTeleportPlayers) {
		this.canTeleportPlayers = canTeleportPlayers;
	}

	public void setCanAttackMultipleTarget(boolean canAttackMultipleTarget) {
		this.canAttackMultipleTarget = canAttackMultipleTarget;
	}
}
