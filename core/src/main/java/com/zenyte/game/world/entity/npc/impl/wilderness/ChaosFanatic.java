package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
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

import java.util.ArrayList;

/**
 * @author Tommeh | 3 feb. 2018 : 21:26:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChaosFanatic extends NPC implements CombatScript, Spawnable {
	private static final Projectile SPECIAL_ATTACK_PROJ = new Projectile(551, 42, 30, 55, 55, 60, 32, 5);
	private static final Graphics FIRE_WAVE_GFX = new Graphics(157);
	private static final Graphics SPECIAL_ATTACK_GFX = new Graphics(552);
	private static final Projectile AUTO_ATTACK_PROJ = new Projectile(554, 42, 30, 55, 15, 29, 32, 5);
	private static final Graphics AUTO_ATTACK_GFX = new Graphics(305);

	private static final String[] FORCECHAT = new String[] {"WEUGH!", "Devilish Oxen Roll!", "All your wilderness are belong to them!", "AhehHeheuhHhahueHuUEehEahAH", "I shall call him squidgy and he shall be my squidgy!"};
	private static final byte[][][] OFFSETS = new byte[][][] {new byte[][] {new byte[] {1, 0}, new byte[] {0, 1}}, new byte[][] {new byte[] {0, -1}, new byte[] {1, 0}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, -1}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, 1}}};
	public static final int CA_TASK_PRAYING_TO_THE_GODS_STARTED = 1;
	private String chat;
	private boolean hitAnyone;

	public ChaosFanatic(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int getRespawnDelay() {
		return 50;
	}

	@Override
	public int attack(Entity target) {
		if (!(target instanceof Player)) {
			return 0;
		}
		final Player player = (Player) target;

		int attack;
		int chance = Utils.random(100);
		if (chance < 75) {
			attack = 1;
		} else if (chance < 85) {
			attack = 2;
		} else {
			attack = 3;
		}

		switch (attack) {
			case 1:
				attackNormal(player);
				break;
			case 2:
				splashAttack(player);
				break;
			case 3:
				attackRemoveItems(player);
				break;
		}


		setForceTalk(chat);
		setAnimation(getCombatDefinitions().getAttackAnim());
		return getCombatDefinitions().getAttackSpeed();
	}

	private void attackNormal(Player player) {
		delayHit(this, World.sendProjectile(this, player, AUTO_ATTACK_PROJ), player, new Hit(this, 0, HitType.MAGIC)
				.onLand((h) -> player.setGraphics(AUTO_ATTACK_GFX)));
		randomChat();
	}

	private void splashAttack(Player target) {
		final ArrayList<Location> tiles = new ArrayList<Location>();
		final Location location = new Location(target.getLocation());
		final int r = Utils.random(OFFSETS.length - 1);
		for (int i = 0; i <= 1; i++) {
			tiles.add(new Location(location.getX() + OFFSETS[r][0][i], location.getY() + OFFSETS[r][1][i]));
		}
		tiles.add(location);
		getCombatDefinitions().setAttackStyle("Magic");
		for (final Location tile : tiles) {
			World.sendProjectile(this, tile, SPECIAL_ATTACK_PROJ);
		}
		WorldTasksManager.schedule(() -> {
			for (final Location tile : tiles) {
				World.sendGraphics(tile.equals(tiles.get(2)) ? SPECIAL_ATTACK_GFX : FIRE_WAVE_GFX, tile);
				if (target.getX() == tile.getX() && target.getY() == tile.getY()) {
					delayHit(this, 0, target, new Hit(this, tile.equals(tiles.get(2)) ? getCombatDefinitions().getMaxHit() : Utils.random(12, 22), HitType.REGULAR));
					hitAnyone = true;
				}
			}
		}, SPECIAL_ATTACK_PROJ.getTime(this, location));
		setChat("BURN!");
	}

	private void attackRemoveItems(Player target){
		delayHit(this, World.sendProjectile(this, target, SPECIAL_ATTACK_PROJ), target, new Hit(this, 0, HitType.MAGIC).onLand(hit -> {
			if (target.getEquipment().unequipItem(EquipmentSlot.WEAPON.getSlot())) {
				target.getActionManager().forceStop();
				target.sendMessage("The Chaos Fanatic disarms you!");
			}
		}));
		randomChat();
	}

	public void randomChat() {
		setChat(FORCECHAT[Utils.random(FORCECHAT.length - 1)]);
	}

	public void setChat(String chat) {
		this.chat = chat;
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof final Player player) {
			player.getAchievementDiaries().update(WildernessDiary.KILL_CRAZY_ARCHEAOLOGIST, 2);
		}
	}

	@Override
	protected void onFinish(Entity source) {
		super.onFinish(source);

		if (source instanceof final Player player) {
			player.getCombatAchievements().checkKcTask("chaos fanatic", 10, CAType.CHAOS_FANATIC_CHAMPION);
			player.getCombatAchievements().checkKcTask("chaos fanatic", 25, CAType.CHAOS_FANATIC_ADEPT);
			if (!hitAnyone) {
				player.getCombatAchievements().complete(CAType.SORRY_WHAT_WAS_THAT);
			}
		}
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("chaos fanatic");
	}
}
