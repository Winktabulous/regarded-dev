package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;


public final class BloodStatuePlugin implements ObjectAction {

    public static final Animation PRAY_ANIM = new Animation(645);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Pray-at") || option.equals("Pray")) {
            if (player.getPrayerManager().getPrayerPoints() >= player.getSkills().getLevelForXp(SkillConstants.PRAYER)) {
                player.sendMessage("You already have full prayer points.");
                return;
            }
            player.lock();
            player.setAnimation(PRAY_ANIM);
            player.sendSound(2674);
            WorldTasksManager.schedule(() -> {
                player.getPrayerManager().restorePrayerPoints(99);
                player.sendMessage("The statue takes a blood sacrifice from you as you recharge your Prayer points.");
                player.applyHit(new Hit(Utils.random(1, 5), HitType.DEFAULT));
                player.unlock();
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STATUE_39234};
    }
}
