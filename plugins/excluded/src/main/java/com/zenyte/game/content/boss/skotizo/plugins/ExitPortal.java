package com.zenyte.game.content.boss.skotizo.plugins;

import com.zenyte.game.content.boss.skotizo.instance.SkotizoInstance;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

/**
 * @author Tommeh | 11/03/2020 | 22:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ExitPortal implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.lock();
        player.setAnimation(DarkAltar.teleportAnimation);
        player.setGraphics(DarkAltar.teleportGraphics);
        new FadeScreen(player, () -> {
            player.blockIncomingHits(4);
            player.unlock();
            player.setLocation(SkotizoInstance.outsideLocation);
            player.setAnimation(Animation.STOP);
        }).fade(4);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.PORTAL_28925 };
    }
}
