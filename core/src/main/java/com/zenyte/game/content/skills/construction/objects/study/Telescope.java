package com.zenyte.game.content.skills.construction.objects.study;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.RoomType;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 25. veebr 2018 : 1:15.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Telescope implements ObjectInteraction {

    private static final Animation ANIM = new Animation(3649);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TELESCOPE_13658, ObjectId.TELESCOPE_13657, ObjectId.TELESCOPE_13656 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        int x = reference.getX();
        int y = reference.getY();
        if (object.getRotation() == 0)
            y--;
        else if (object.getRotation() == 1)
            x--;
        else if (object.getRotation() == 2)
            y++;
        else
            x++;
        if (reference.getPlane() == 2 || construction.getReference(x, y, 2) != null) {
            player.getDialogueManager().start(new PlainChat(player, "You can't see the stars from here."));
            return;
        }
        final RoomReference ref = construction.getReference(x, y, 1);
        if (ref != null) {
            if (!(ref.getRoom() == RoomType.GARDEN || ref.getRoom() == RoomType.FORMAL_GARDEN || ref.getRoom() == RoomType.SUPERIOR_GARDEN || ref.getRoom() == RoomType.MENAGERIE_OUTDOORS)) {
                player.getDialogueManager().start(new PlainChat(player, "You can't see the stars from here."));
                return;
            }
        }
        player.setAnimation(ANIM);
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 552);
        player.getDialogueManager().start(new PlainChat(player, "You look to the stars.."));
    }
}
