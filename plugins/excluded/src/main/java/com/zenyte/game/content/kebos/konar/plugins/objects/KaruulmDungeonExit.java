package com.zenyte.game.content.kebos.konar.plugins.objects;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 13/10/2019 | 22:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class KaruulmDungeonExit implements ObjectAction {

    private static final Animation crawlAnim = new Animation(2796);

    private static final Location elevatorDestination = new Location(1311, 3807, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Exit")) {
            player.lock();
            player.setAnimation(crawlAnim);
            new FadeScreen(player, () -> player.setLocation(elevatorDestination)).fade(3);
        }
    }

    @Override
    public int getDelay() {
        return 2;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_EXIT_34514 };
    }
}
