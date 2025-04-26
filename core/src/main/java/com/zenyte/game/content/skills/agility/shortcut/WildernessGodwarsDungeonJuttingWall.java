package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 23:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessGodwarsDungeonJuttingWall implements Shortcut {

    @Override
    public int getLevel(final WorldObject object) {
        return 60;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                26768
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.setAnimation(player.getY() >= 10149 ? new Animation(3276) : new Animation(3277));
        player.autoForceMovement(player.getY() >= 10149 ? new Location(3066, 10147, 3) : new Location(3066, 10149, 3), 0, 120);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
