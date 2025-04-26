package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.room.LargeScavengerRoom;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Kris | 06/09/2019 10:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScavengerBoulder implements ObjectAction {
    private static final Animation pushAnimation = new Animation(6131);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (option.equalsIgnoreCase("Push")) {
                if (!(object instanceof LargeScavengerRoom.BlockingObject)) {
                    return;
                }
                final LargeScavengerRoom.BlockingObject blockingObject = (LargeScavengerRoom.BlockingObject) object;
                final Direction direction = player.getX() < object.getX() ? Direction.EAST : player.getX() > object.getX() ? Direction.WEST : player.getY() > object.getY() ? Direction.SOUTH : Direction.NORTH;
                if (blockingObject.getLevelRequired() == -1) {
                    final int dir = DirectionUtil.getMoveDirection(direction.getOffsetX(), direction.getOffsetY());
                    if (World.checkWalkStep(object.getPlane(), object.getX(), object.getY(), dir, 1, false, false)) {
                        player.sendMessage("There\'s no need to push the boulder anymore.");
                        return;
                    }
                    player.sendMessage("It\'s not going to go anywhere.");
                    return;
                }
                player.getActionManager().setAction(new Action() {
                    @Override
                    public boolean start() {
                        if (player.getSkills().getLevel(SkillConstants.STRENGTH) < blockingObject.getLevelRequired()) {
                            player.sendMessage("You need a Strength level of " + blockingObject.getLevelRequired() + " to have any effect here.");
                            return false;
                        }
                        player.setAnimation(pushAnimation);
                        blockingObject.getInteractingPlayers().add(player);
                        delay(Utils.random(Math.max(5, Math.min(20, raid.getOriginalPlayers().size() / 3))));
                        return true;
                    }
                    @Override
                    public boolean process() {
                        return World.exists(object);
                    }
                    @Override
                    public int processWithDelay() {
                        if (!World.exists(object)) {
                            return -1;
                        }
                        for (final Player player : blockingObject.getInteractingPlayers()) {
                            raid.addPoints(player, 495);
                        }
                        player.setAnimation(Animation.STOP);
                        blockingObject.getInteractingPlayers().clear();
                        World.removeObject(object);
                        blockingObject.setLevelRequired(-1);
                        final ScavengerBoulder.BoulderNPC boulder = new BoulderNPC(object, direction, destination -> {
                            object.setLocation(destination);
                            World.spawnObject(object);
                            CharacterLoop.forEach(destination, 0, Player.class, target -> {
                                target.applyHit(new Hit(Utils.random(4, 6), HitType.DEFAULT));
                                target.lock(1);
                                target.setRouteEvent(new ObjectEvent(target, new ObjectStrategy(object), null));
                            });
                        });
                        boulder.spawn();
                        return -1;
                    }
                    @Override
                    public void stop() {
                        blockingObject.getInteractingPlayers().remove(player);
                    }
                    @Override
                    public boolean interruptedByCombat() {
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {LargeScavengerRoom.BOULDER};
    }


    private static final class BoulderNPC extends NPC {
        private BoulderNPC(@NotNull final WorldObject object, @NotNull final Direction direction, @NotNull final Consumer<Location> onDestination) {
            super(7489, object, direction, 0);
            this.onDestination = onDestination;
        }

        private final Consumer<Location> onDestination;

        @Override
        public void processNPC() {
            final boolean moved = addWalkSteps(getX() + spawnDirection.getOffsetX(), getY() + spawnDirection.getOffsetY(), 1, true);
            if (!moved || getLocation().getDistance(getRespawnTile()) >= 3) {
                onDestination.accept(new Location(getLocation()));
                finish();
            }
        }

        @Override
        public boolean isEntityClipped() {
            return false;
        }
    }
}
