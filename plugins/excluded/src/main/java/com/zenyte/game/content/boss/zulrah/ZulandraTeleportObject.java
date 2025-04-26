package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 16. juuli 2018 : 22:55:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ZulandraTeleportObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Leave Zulrah's shrine?", "Yes.", "No.").onOptionOne(() -> TeleportCollection.ZULANDRA_OBJECT_TELEPORT.teleport(player));
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ZULANDRA_TELEPORT };
    }
}
