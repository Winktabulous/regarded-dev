package com.zenyte.game.content.skills.construction.objects;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.RoomType;
import com.zenyte.game.content.skills.construction.dialogue.ClimbEmptyStaircaseD;
import com.zenyte.game.content.skills.construction.dialogue.ClimbStaircaseD;
import com.zenyte.game.content.skills.construction.dialogue.UpperRoomRemovalD;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 22. nov 2017 : 23:45.08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Staircase implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRCASE_13497, ObjectId.STAIRCASE_13498, ObjectId.STAIRCASE_13499, ObjectId.STAIRCASE_13500, ObjectId.STAIRCASE_13501, ObjectId.STAIRCASE_13502, ObjectId.STAIRCASE_13503, ObjectId.STAIRCASE_13505 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (optionId == 1) {
            player.getDialogueManager().start(new ClimbStaircaseD(player, object));
            return;
        } else if (optionId == 2) {
            if (player.getPlane() == 2) {
                player.getDialogueManager().start(new PlainChat(player, "You cannot climb any higher than this."));
                return;
            }
            final Location tile = new Location(player.getX(), player.getY(), player.getPlane() + 1);
            final RoomReference ref = player.getConstruction().getReference(tile);
            if (ref == null) {
                boolean upstairs = player.getPlane() == 1 || reference.getRoom() == RoomType.SKILL_HALL || reference.getRoom() == RoomType.QUEST_HALL;
                player.getDialogueManager().start(new ClimbEmptyStaircaseD(player, reference, upstairs, object));
            } else
                player.setLocation(tile);
        } else if (optionId == 3) {
            if (player.getPlane() == 0) {
                player.getDialogueManager().start(new PlainChat(player, "You cannot climb any lower than this."));
                return;
            }
            final Location tile = new Location(player.getX(), player.getY(), player.getPlane() - 1);
            final RoomReference ref = player.getConstruction().getReference(tile);
            if (ref == null)
                player.getDialogueManager().start(new ClimbEmptyStaircaseD(player, reference, reference.getRoom() == RoomType.SKILL_HALL || reference.getRoom() == RoomType.QUEST_HALL, object));
            else
                player.setLocation(tile);
        } else if (optionId == 4) {
            if (player.getPlane() == 2) {
                player.getDialogueManager().start(new PlainChat(player, "You cannot remove a room supporting another room."));
                return;
            }
            final RoomReference ref = player.getConstruction().getReference(new Location(player.getX(), player.getY(), player.getPlane() + 1));
            if (ref == null) {
                player.getDialogueManager().start(new PlainChat(player, "There's no room above to remove."));
                return;
            }
            player.getDialogueManager().start(new UpperRoomRemovalD(player, ref));
            return;
        }
    }
}
