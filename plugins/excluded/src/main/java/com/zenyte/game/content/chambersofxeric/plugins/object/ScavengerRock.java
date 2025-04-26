package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.room.LargeScavengerRoom;
import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Kris | 06/09/2019 09:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScavengerRock implements ObjectAction {
    private static final SoundEffect successfulHit = new SoundEffect(3600);
    private static final SoundEffect rockDepletion = new SoundEffect(360, 4, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (option.equalsIgnoreCase("Mine")) {
                if (!(object instanceof LargeScavengerRoom.BlockingObject)) {
                    throw new IllegalStateException("Object not instanceof blocking object.");
                }
                final LargeScavengerRoom.BlockingObject blockingObject = (LargeScavengerRoom.BlockingObject) object;
                player.getActionManager().setAction(new Action() {
                    private MiningDefinitions.PickaxeDefinitions.PickaxeResult tool;
                    private int ticks;
                    @Override
                    public boolean start() {
                        final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> optionalAxe = MiningDefinitions.PickaxeDefinitions.get(player, true);
                        if (!optionalAxe.isPresent()) {
                            player.sendMessage("You do not have a pickaxe which you have the Mining level to use.");
                            return false;
                        }
                        this.tool = optionalAxe.get();
                        if (blockingObject.getLevelRequired() > player.getSkills().getLevel(SkillConstants.MINING)) {
                            player.sendMessage("You need a Mining level of at least " + blockingObject.getLevelRequired() + " to mine this rock.");
                            return false;
                        }
                        blockingObject.getInteractingPlayers().add(player);
                        player.sendFilteredMessage("You swing your pickaxe at the rocks...");
                        delay(tool.getDefinition().getMineTime());
                        return true;
                    }
                    @Override
                    public boolean process() {
                        if (ticks++ % 4 == 0) player.setAnimation(tool.getDefinition().getAnim());
                        return World.exists(blockingObject);
                    }
                    @Override
                    public int processWithDelay() {
                        player.sendSound(successfulHit);
                        player.getSkills().addXp(SkillConstants.MINING, 4.5);
                        raid.addPoints(player, 44);
                        if (Utils.random(Math.max(5, Math.min(20, raid.getOriginalPlayers().size() / 3))) == 0) {
                            final WorldObject remnants = new WorldObject(blockingObject);
                            remnants.setId(LargeScavengerRoom.RUBBLE);
                            World.spawnObject(remnants);
                            World.sendSoundEffect(object, rockDepletion);
                            ScavengerBlockingObstacleRemnants.cross(player, object);
                            blockingObject.getInteractingPlayers().clear();
                            return -1;
                        }
                        return tool.getDefinition().getMineTime();
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
        return new Object[] {LargeScavengerRoom.ROCK};
    }
}
