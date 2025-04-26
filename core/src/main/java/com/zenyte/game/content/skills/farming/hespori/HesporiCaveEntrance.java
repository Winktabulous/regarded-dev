package com.zenyte.game.content.skills.farming.hespori;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/02/2019 20:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HesporiCaveEntrance implements ObjectAction {

    private static final Animation animation = new Animation(2796);

    private static final SoundEffect sound = new SoundEffect(2454, 0, 4);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Location destination = object.getId() == 34435 ? new Location(1230, 3729, 0) : new Location(1243, 10081, 0);
        final FadeScreen screen = new FadeScreen(player, () -> player.setLocation(destination));
        screen.fade();
        player.setAnimation(animation);
        player.sendSound(sound);
        WorldTasksManager.schedule(screen::unfade, 2);
    }

    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_34499, ObjectId.CAVE_34435 };
    }
}
