package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;
import mgi.types.config.ObjectDefinitions;

/**
 * @author Kris | 19. veebr 2018 : 20:07.39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class SpiralStaircaseObject implements ObjectAction {

    private static final Location homeException = new Location(3118, 3484, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Climb")) {
            player.getDialogueManager().start(new OptionDialogue(player, "Which way do you want to go?", new String[] { "Climb up", "Climb down", "Cancel." }, new Runnable[] { () -> {
                final int[] offsets = getUpOffsets(object.getRotation());
                player.setLocation(new Location(object.getX() + offsets[0], object.getY() + offsets[1], object.getPlane() + 1));
            }, () -> {
                if (player.getPlane() >= 2 || object.getDefinitions().containsOption("climb")) {
                    player.setLocation(new Location(player.getX(), player.getY(), object.getPlane() - 1));
                } else {
                    final int[] offsets = getDownOffsets(object.getRotation());
                    player.setLocation(new Location(object.getX() + offsets[0], object.getY() + offsets[1], object.getPlane() - 1));
                }
            }, null }));
        } else if (option.equals("Climb-up")) {
            if (object.matches(homeException)) {
                player.setLocation(new Location(3119, 3482, 1));
                return;
            }
            final int[] offsets = getUpOffsets(object.getRotation());
            player.setLocation(new Location(object.getX() + offsets[0], object.getY() + offsets[1], object.getPlane() + 1));
        } else if (option.equals("Climb-down")) {
            final ObjectDefinitions defs = object.getDefinitions();
            if (defs.getSizeX() != 1 || defs.getSizeY() != 1) {
                final int[] offsets = getDownOffsets(object.getRotation());
                player.setLocation(new Location(object.getX() + offsets[0], object.getY() + offsets[1], object.getPlane() - 1));
            } else {
                player.setLocation(new Location(player.getX(), player.getY(), object.getPlane() - 1));
            }
        }
    }

    private final int[] getUpOffsets(final int rotation) {
        switch(rotation) {
            case 0:
                return new int[] { 2, 0 };
            case 1:
                return new int[] { 0, -1 };
            case 2:
                return new int[] { -1, 1 };
            default:
                return new int[] { 1, 2 };
        }
    }

    private final int[] getDownOffsets(final int rotation) {
        switch(rotation) {
            case 0:
                return new int[] { 1, -1 };
            case 1:
                return new int[] { -1, 0 };
            case 2:
                return new int[] { 2, 0 };
            default:
                return new int[] { 2, 1 };
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRCASE_2608, ObjectId.STAIRCASE_2609, ObjectId.STAIRCASE_2610, 3414, ObjectId.STAIRCASE_3416, ObjectId.STAIRCASE_4568, ObjectId.STAIRCASE_4569, ObjectId.STAIRCASE_4570, ObjectId.STAIRCASE_9582, ObjectId.STAIRCASE_9584, ObjectId.STAIRCASE_11358, ObjectId.STAIRCASE_11511, ObjectId.STAIRCASE_11789, ObjectId.STAIRCASE_11790, ObjectId.STAIRCASE_11791, ObjectId.STAIRCASE_11792, ObjectId.STAIRCASE_11793, ObjectId.STAIRCASE_11888, ObjectId.STAIRCASE_11889, ObjectId.STAIRCASE_11890, ObjectId.STAIRCASE_12536, ObjectId.STAIRCASE_12537, ObjectId.STAIRCASE_12538, ObjectId.STAIRCASE_14735, ObjectId.STAIRCASE_14736, ObjectId.STAIRCASE_14737, ObjectId.STAIRCASE_16671, ObjectId.STAIRCASE_16672, ObjectId.STAIRCASE_16673, ObjectId.STAIRCASE_16674, ObjectId.STAIRCASE_16675, ObjectId.STAIRCASE_16676, ObjectId.STAIRCASE_16677, ObjectId.STAIRCASE_16678, ObjectId.STAIRCASE_17143, ObjectId.STAIRCASE_17155, ObjectId.STAIRCASE_24072, ObjectId.STAIRCASE_24074, ObjectId.STAIRCASE_24075, ObjectId.STAIRCASE_24076, ObjectId.STAIRCASE_24250, ObjectId.STAIRCASE_24251, ObjectId.STAIRCASE_24252, ObjectId.STAIRCASE_24253, ObjectId.STAIRCASE_24254, ObjectId.STAIRCASE_24255, ObjectId.STAIRCASE_24303, ObjectId.STAIRCASE_24358, ObjectId.STAIRCASE_24359, ObjectId.STAIRCASE_25682, ObjectId.STAIRCASE_25683, ObjectId.STAIRCASE_25801, ObjectId.STAIRCASE_25935 };
    }
}
