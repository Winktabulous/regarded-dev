package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * @author Kris | 26/04/2019 16:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkAltarRockClimb implements Shortcut {

    private static final Location PREVENTED_OBSTACLE = new Location(1751, 3854, 0);
    private static final Location LONG_SHORTCUT = new Location(1743, 3854, 0);
    private static final Location SHORT_SHORTCUT_SOUTH = new Location(1761, 3872, 0);
    private static final Location SHORT_SHORTCUT_NORTH = new Location(1761, 3873, 0);


    private static final Animation CRAWL = new Animation(1148);
    private static final Location CRAWL_FACE = new Location(1741, 3854, 0);
    private static final Location BOTTOM = new Location(1752, 3854, 0);
    private static final Animation CLIMB = new Animation(839);
    private static final Location UP = new Location(1761, 3874, 0);
    private static final Location DOWN = new Location(1761, 3871, 0);

    @Override
    public int getLevel(final WorldObject object) {
        return object.matches(LONG_SHORTCUT) ? 73 : 69;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{
                27984, 27985
        };
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return object.matches(LONG_SHORTCUT) ? 8 : 3;
    }

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (object.getId() == 27985 && object.matches(PREVENTED_OBSTACLE)) {
            player.getDialogueManager().start(new PlainChat(player, "You can't use that from here!"));
            return false;
        }
        return true;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (object.matches(LONG_SHORTCUT)) {
            player.setTeleportForceMovement(BOTTOM, OptionalInt.of(ForceMovement.WEST), 1, 7, Optional.of(tile -> {
                player.setFaceLocation(CRAWL_FACE);
                player.setAnimation(CRAWL);
                player.getAppearance().setRenderAnimation(new RenderAnimation(4435, 4435, 4435));
            }), Optional.empty(), Optional.of(tile -> player.getAppearance().resetRenderAnimation()));
        } else if (object.matches(SHORT_SHORTCUT_SOUTH)) {
            player.setAnimation(CLIMB);
            player.autoForceMovement(UP, 15, 60);
        } else if (object.matches(SHORT_SHORTCUT_NORTH)) {
            player.setAnimation(CLIMB);
            player.autoForceMovement(DOWN, 15, 60);
        }
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
