package com.zenyte.game.content.tog;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Chris
 * @since September 08 2020
 */
public class CollectTearAction extends Action {
    public static final int TEARS_COLLECTED_VARBIT = 455;
    private static final Animation collectAnim = new Animation(2043);
    private static final Animation finishAnim = new Animation(2044);
    private final TearsOfGuthixWall tearsOfGuthixWall;
    private final WorldObject object;

    @Override
    public boolean start() {
        player.resetWalkSteps();
        player.setAnimation(Animation.STOP);
        delay(1);
        player.faceObject(object);
        return canCollect();
    }

    @Override
    public void onInterruption() {
        player.setAnimation(finishAnim);
    }

    @Override
    public void stop() {
        player.setAnimation(finishAnim);
    }

    @Override
    public boolean process() {
        return canCollect();
    }

    private boolean canCollect() {
        return player.getVarManager().getBitValue(TearsOfGuthixCaveArea.TIMER_VARBIT) > 0;
    }

    @Override
    public int processWithDelay() {
        final TearsOfGuthix tears = tearsOfGuthixWall.getCurrentTears();
        if (tears == TearsOfGuthix.BLUE) {
            player.sendSound(1794);
        } else if (tears == TearsOfGuthix.GREEN) {
            player.sendSound(1793);
        }
        final int newAmount = Math.max(0, player.getVarManager().getBitValue(TEARS_COLLECTED_VARBIT) + tears.getPointModifier());
        player.getVarManager().sendBit(TEARS_COLLECTED_VARBIT, newAmount);
        player.setAnimation(collectAnim);
        player.faceObject(object);
        return 0;
    }

    public CollectTearAction(TearsOfGuthixWall tearsOfGuthixWall, WorldObject object) {
        this.tearsOfGuthixWall = tearsOfGuthixWall;
        this.object = object;
    }
}
