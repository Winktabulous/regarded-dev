package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.calog.CAType;

import java.util.ArrayList;

/**
 * @author Tommeh | 4 aug. 2018 | 18:40:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CrazyArchaeologist extends NPC implements CombatScript, Spawnable {

	private static final Graphics SPECIAL_GFX = new Graphics(157, 0, 124);
	private static final ForceTalk[] MESSAGES = {new ForceTalk("I'm Bellock - respect me!"), new ForceTalk("Get off my site!"), new ForceTalk("No-one messes with Bellock's dig!"), new ForceTalk("These ruins are mine!"), new ForceTalk("Taste my knowledge!"), new ForceTalk("You belong in a museum!")};
	private static final ForceTalk DEATH_MESSAGE = new ForceTalk("Ow!");
	private static final ForceTalk SPECIAL_MESSAGE = new ForceTalk("Rain of knowledge!");
	private static final Animation RANGED_ANIM = new Animation(3353);
	private static final Graphics EXPLOSIVE_GFX = new Graphics(305, 0, 92);
	private static final Projectile RANGED_PROJ = new Projectile(1259, 42, 30, 40, 10, 25, 32, 5);
	private static final Projectile SPECIAL_PROJ = new Projectile(1260, 42, 0, 40, 40, 50, 32, 5);
	private static final byte[][][] OFFSETS = new byte[][][] {new byte[][] {new byte[] {1, 0}, new byte[] {0, 1}}, new byte[][] {new byte[] {0, -1}, new byte[] {1, 0}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, -1}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, 1}}};
	private boolean hitAnyone;
	private ArrayList<String> caTaskUsernames = new ArrayList<String>();

	public CrazyArchaeologist(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
		this.attackDistance = 10;
		this.radius = 20;
		this.maxDistance = 20;
	}

	@Override public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (!HitType.MAGIC.equals(hit.getHitType()) && hit.getSource() instanceof final Player player && !caTaskUsernames.contains(player.getUsername())) {
			caTaskUsernames.add(player.getUsername());
		}
	}

	@Override
	public int getRespawnDelay() {
		return 50;
	}

	@Override
	public int attack(final Entity target) {
		if (!(target instanceof Player)) {
			return 0;
		}
		if (isWithinMeleeDistance(this, target)) {
			melee(target);
		} else {
			if (Utils.random(6) == 0) {
				special(target);
			} else {
				ranged(target);
			}
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	private void melee(final Entity target) {
		setForceTalk(MESSAGES[Utils.random(MESSAGES.length - 1)]);
		setAnimation(getCombatDefinitions().getAttackAnim());
		final Hit hit = new Hit(this, getRandomMaxHit(this, 15, MELEE, target), HitType.REGULAR);
		delayHit(this, 0, target, hit);
		getCombatDefinitions().setAttackStyle("Ranged");
	}

	private void ranged(final Entity target) {
		setForceTalk(MESSAGES[Utils.random(MESSAGES.length - 1)]);
		setAnimation(RANGED_ANIM);
		delayHit(this, World.sendProjectile(this, target, RANGED_PROJ), target, new Hit(this, getRandomMaxHit(this, 15, RANGED, target), HitType.RANGED).onLand(hit -> {
			if (Utils.random(2) == 0) {
				World.sendGraphics(EXPLOSIVE_GFX, target.getLocation());
			}
		}));
	}

	private void special(final Entity target) {
		setAnimation(RANGED_ANIM);
		final ArrayList<Location> locations = new ArrayList<Location>();
		final Location base = new Location(target.getLocation());
		final int r = Utils.random(OFFSETS.length - 1);
		for (int i = 0; i < 2; i++) {
			locations.add(new Location(base.getX() + OFFSETS[r][0][i], base.getY() + OFFSETS[r][1][i]));
		}
		locations.add(base);
		for (final Location l : locations) {
			World.sendProjectile(this, l, SPECIAL_PROJ);
		}
		WorldTasksManager.schedule(() -> {
			for (final Location l : locations) {
				World.sendGraphics(SPECIAL_GFX, l);
				if (CollisionUtil.collides(target.getPosition(), target.getSize(), l, 3, 0)) {
					delayHit(this, 0, target, new Hit(this, Utils.random(12, 22), HitType.REGULAR));
					hitAnyone = true;
				}
			}
		}, SPECIAL_PROJ.getTime(this, base));
		setForceTalk(SPECIAL_MESSAGE);
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		setForceTalk(DEATH_MESSAGE);
		if (source instanceof final Player player) {
			player.getAchievementDiaries().update(WildernessDiary.KILL_CRAZY_ARCHEAOLOGIST, 1);
		}
	}

	@Override
	protected void onFinish(Entity source) {
		super.onFinish(source);

		if (source instanceof final Player player) {
			player.getCombatAchievements().checkKcTask("crazy archaeologist", 10, CAType.CRAZY_ARCHAEOLOGIST_CHAMPION);
			player.getCombatAchievements().checkKcTask("crazy archaeologist", 25, CAType.CRAZY_ARCHAEOLOGIST_ADEPT);
			if (!hitAnyone) {
				player.getCombatAchievements().complete(CAType.ID_RATHER_NOT_LEARN);
			}
			if (!caTaskUsernames.contains(player.getUsername())) {
				player.getCombatAchievements().complete(CAType.MAGE_OF_THE_RUINS);
			}
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 6618;
	}
}
