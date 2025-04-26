package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.room.LizardmanShamanRoom;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

/**
 * @author Kris | 06/07/2019 04:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpiritTendrils implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            raid.ifInRoom(player, LizardmanShamanRoom.class, room -> {
                if (room.isDead()) {
                    player.sendMessage("The tendril is about to collapse!");
                    return;
                }
                if (player.getNumericTemporaryAttribute("tendril_cox_delay").longValue() > System.currentTimeMillis()) {
                    return;
                }
                player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(15));
                player.lock();
                player.resetWalkSteps();
                player.setRunSilent(true);
                if (object.getX() > player.getX()) {
                    player.addWalkSteps(player.getX() + 2, player.getY(), 2, false);
                } else if (object.getX() < player.getX()) {
                    player.addWalkSteps(player.getX() - 2, player.getY(), 2, false);
                } else if (object.getY() > player.getY()) {
                    player.addWalkSteps(player.getX(), player.getY() + 2, 2, false);
                } else {
                    player.addWalkSteps(player.getX(), player.getY() - 2, 2, false);
                }
                final boolean hasUsed = room.getTendrils().getInt(player.getUsername()) >= (room.getIndex() == 0 ? 2 : 1);
                if (hasUsed) {
                    player.applyHit(new Hit((int) (player.getMaxHitpoints() * 0.15F), HitType.POISON));
                }
                room.getTendrils().put(player.getUsername(), room.getTendrils().getInt(player.getUsername()) + 1);
                WorldTasksManager.schedule(() -> {
                    player.setRunSilent(false);
                    player.unlock();
                    player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(2));
                }, 1);
            });
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SPIRIT_TENDRILS };
    }
}
