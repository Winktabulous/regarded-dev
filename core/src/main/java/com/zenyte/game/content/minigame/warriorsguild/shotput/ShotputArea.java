package com.zenyte.game.content.minigame.warriorsguild.shotput;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.WarriorsGuildArea;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;

/**
 * @author Kris | 16. dets 2017 : 21:45.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShotputArea extends WarriorsGuildArea implements PartialMovementPlugin {

	public static final Location SHOT_18LB = new Location(2861, 3553, 1);
	public static final Location SHOT_18LB_FACE = new Location(2864, 3553, 1);
	public static final Location SHOT_22LB = new Location(2861, 3547, 1);
	public static final Location SHOT_22LB_FACE = new Location(2864, 3547, 1);
	
	public static final Item SHOT_18LB_ITEM = new Item(8858);
	public static final Item SHOT_22LB_ITEM = new Item(8859);
	
	private static final String[] MESSAGES = new String[] {
			"You throw the shot as hard as you can.",
			 "You take a step and throw the shot as hard as you can.",
			 "You spin around and release the shot."
	};
	
	private static final Animation[] THROW_ANIMATIONS = new Animation[] {
			new Animation(4181), new Animation(4182), new Animation(4183)
	};
	
	private static final Projectile PROJ = new Projectile(690, 30, 5, 50, 15, 58, 0, 5);
	private static final Projectile DELAYED_PROJ = new Projectile(690, 30, 5, 100, 15, 58, 0, 5);
	
	private static final String[] LAND_MESSAGES = new String[] {
			"The shot is perfectly thrown and gently drops to the floor.",
			"The shot drops to the floor.",
			"The shot falls from the air like a brick, landing with a sickening thud."
	};
	
	public static final void throwShot(final Player player, final int style) {
		player.lock();
		player.setFaceLocation(player.getLocation().getPositionHash() == SHOT_18LB.getPositionHash() ? SHOT_18LB_FACE : SHOT_22LB_FACE);
		player.sendMessage("You take a deep breath and prepare yourself.");
		player.getTemporaryAttributes().remove("shotput");
		WorldTasksManager.schedule(new WorldTask() {
			private int stage;
			private final int distance = getRandomDistance(player);
			private final Location landTile = new Location(player.getX() + distance, player.getY(), 1);
			private final int time = PROJ.getTime(player.getLocation(), landTile);
			@Override
			public void run() {
				if (stage == 0) {
					player.sendMessage(MESSAGES[style]);
					player.setAnimation(THROW_ANIMATIONS[style]);
				} else if (stage == 1) {
					if (player.getVariables().getRunEnergy() / 100 < Utils.randomDouble()) {
						player.sendMessage("You fumble and drop the shot onto your toe. Ow!");
						final double runEnergy = player.getVariables().getRunEnergy();
						player.getVariables().setRunEnergy((runEnergy - 50) < 0 ? 0 : (runEnergy - 50));
						player.applyHit(new Hit(player, 1, HitType.REGULAR));
						player.setAnimation(Animation.STOP);
						player.unlock();
						stop();
						return;
					}
					World.sendProjectile(player, landTile, style == 1 ? PROJ : DELAYED_PROJ);
				} if (stage == (time + (style == 1 ? 2 : 4))) {
					final int random = Utils.random(15);
					final Object grip = player.getTemporaryAttributes().get("shotputGrip");
					if (grip != null) {
						final int gripBoost = grip instanceof Integer ? (Integer) grip - 1 : 0;
						if (gripBoost > 0) {
							player.getTemporaryAttributes().put("shotputGrip", gripBoost);
						} else {
							player.getTemporaryAttributes().remove("shotputGrip");
						}
					}
					World.whenFindNPC(
							player.getLocation().getPositionHash() == SHOT_18LB.getPositionHash() ? 6073 : 6074,
									player, ref -> ref.setFaceLocation(new Location(player.getLocation())));
					player.getDialogueManager().start(new ShotThrowD(player, 6073, distance));
					final double runEnergy = player.getVariables().getRunEnergy();
					player.getVariables().setRunEnergy((runEnergy - 10) < 0 ? 0 : (runEnergy - 10));
					player.getPacketDispatcher().sendRunEnergy();
					if (random < 3) {
						player.sendMessage(LAND_MESSAGES[random]);
					}
					final int base = random == 0 ? distance * 5 : random == 1 ? distance * 3 : distance;
					final int tokens = player.getNumericAttribute("warriorsGuildTokens").intValue();
					player.getAttributes().put("warriorsGuildTokens", tokens + base + (player.getLocation().getPositionHash() == SHOT_18LB.getPositionHash() ? 1 : 3));
					World.spawnFloorItem(player.getLocation().getPositionHash() == SHOT_18LB.getPositionHash() ? SHOT_18LB_ITEM : SHOT_22LB_ITEM, landTile, player, 50, 0);
					player.unlock();
					player.getSkills().addXp(SkillConstants.STRENGTH, 11.8d * distance);
					stop();
					return;
				}
				stage++;
			}
		}, 1, 0);
	}
	
	private static final int getRandomDistance(final Player player) {
		final int energy = (int) player.getVariables().getRunEnergy();
		final Object grip = player.getTemporaryAttributes().get("shotputGrip");
		final int baseDistance = ((!(grip instanceof Integer)) ? 0 : (Integer) grip);
		final int strengthDistance = player.getSkills().getLevel(SkillConstants.STRENGTH) / 10;
		final int random = Utils.random((int) (energy * 0.7d), energy);
		final int value = (int) ((1 + strengthDistance * (random / 100d)) + baseDistance);
		return value > 14 ? 14 : value;
	}

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2858, 3557 },
                        { 2858, 3544 },
                        { 2877, 3544 },
                        { 2877, 3551 },
                        { 2878, 3552 },
                        { 2878, 3553 },
                        { 2877, 3554 },
                        { 2877, 3557 },
                        { 2874, 3557 },
                        { 2873, 3558 },
                        { 2872, 3558 },
                        { 2871, 3557 },
                        { 2868, 3557 },
                        { 2867, 3558 },
                        { 2866, 3558 },
                        { 2865, 3557 },
                        { 2862, 3557 },
                        { 2861, 3558 },
                        { 2860, 3558 },
                        { 2859, 3557 }
                }, 1)
        };
    }

    @Override
    public boolean processMovement(Player player, int x, int y) {
        player.getTemporaryAttributes().remove("shotput");
        return false;
    }

    @Override
    public String name() {
        return "Warriors' Guild Shotput Area";
    }
}
