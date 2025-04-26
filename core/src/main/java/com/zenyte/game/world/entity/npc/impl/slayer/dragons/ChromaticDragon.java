package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;

/**
 * @author Tommeh | 15 jan. 2018 : 18:45:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class ChromaticDragon extends NPC implements CombatScript, Spawnable {
	private static final Animation ANIMATION = new Animation(84);
	private static final Graphics GRAPHICS = new Graphics(1, 0, 92);
	private static final Animation MELEE_ANIMATION = new Animation(91);
	private static final Animation SECOND_MELEE_ANIMATION = new Animation(25);
	private static final SoundEffect fireSound = new SoundEffect(3750, 10, 0);

	public ChromaticDragon(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		if (Utils.random(2) == 0 && target instanceof Player) {
			setAnimation(ANIMATION);
			setGraphics(GRAPHICS);
			final Player player = (Player) target;
			final boolean perk = player.getPerkManager().isValid(PerkWrapper.BACKFIRE);
			final double modifier = !perk ? 1 : Math.max(0, Utils.randomDouble() - 0.25F);
			final Dragonfire dragonfire = new Dragonfire(DragonfireType.CHROMATIC_DRAGONFIRE, 50, DragonfireProtection.getProtection(this, player, true));
			final int deflected = !perk ? 0 : ((int) Math.floor(dragonfire.getMaximumDamage() * modifier));
			PlayerCombat.appendDragonfireShieldCharges(player);
			World.sendSoundEffect(getMiddleLocation(), fireSound);
			player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's fiery breath"));
			delayHit(0, target, new Hit(this, Utils.random(Math.max(0, dragonfire.getDamage() - deflected)), HitType.REGULAR));
			if (perk) {
				dragonfire.backfire(this, player, 1, deflected);
			}
		} else {
			attackSound();
			setAnimation(Utils.random(2) == 0 ? MELEE_ANIMATION : SECOND_MELEE_ANIMATION);
			delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
		}
		return 6;
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			if (getName(player).equals("Green dragon")) {
				player.getAchievementDiaries().update(WildernessDiary.KILL_A_GREEN_DRAGON);
			} else if (getName(player).equals("Lava dragon")) {
				player.getAchievementDiaries().update(WildernessDiary.KILL_A_LAVA_DRAGON, 1);
			} else if (getName(player).equalsIgnoreCase("black dragon")) {
				player.getCombatAchievements().complete(CAType.BIG_BLACK_AND_FIERY);
			}
		}
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("green dragon") || name.equals("blue dragon") || name.equals("red dragon") || name.equals("black dragon") || name.equals("lava dragon") || name.equals("reanimated dragon");
	}
}
