package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

/**
 * @author Kris | 06/07/2019 04:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VasaNistirioMagicalFire implements ObjectAction {

    private static final SoundEffect sound = new SoundEffect(509, 10, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (player.getNumericTemporaryAttribute("tendril_cox_delay").longValue() > System.currentTimeMillis()) {
                return;
            }
            player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(15));
            player.lock();
            player.setRunSilent(true);
            final Hit hit = new Hit((int) ((player.getHitpoints() * 0.15F) + (player.getHitpoints() * 0.075F)), HitType.REGULAR);
            hit.setExecuteIfLocked();
            player.applyHit(hit);
            World.sendSoundEffect(player.getLocation(), sound);
            switch(object.getRotation()) {
                case 0:
                case 2:
                    if (player.getY() < object.getY()) {
                        player.addWalkSteps(player.getX(), object.getY() + 2, 3, false);
                    } else {
                        player.addWalkSteps(player.getX(), object.getY() - 1, 3, false);
                    }
                    break;
                default:
                    if (player.getX() < object.getX()) {
                        player.addWalkSteps(object.getX() + 2, player.getY(), 3, false);
                    } else {
                        player.addWalkSteps(object.getX() - 1, player.getY(), 3, false);
                    }
                    break;
            }
            WorldTasksManager.schedule(() -> {
                player.setRunSilent(false);
                player.unlock();
                player.sendMessage("You get burnt by the magical fire.");
                player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(2));
            }, 2);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGICAL_FIRE };
    }
}
