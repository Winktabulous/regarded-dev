package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.npcs.mimic.MimicInstance;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 04/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MimicKeyhole implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Use")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Really leave the instance?", new DialogueOption("Yes.", () -> exit(player)), new DialogueOption("No."));
                }
            });
        } else if (option.equalsIgnoreCase("Exit")) {
            exit(player);
        }
    }

    private final void exit(@NotNull final Player player) {
        new FadeScreen(player, () -> {
            player.blockIncomingHits();
            player.setLocation(MimicInstance.strangeCasketLocation);
        }).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.KEYHOLE_34727 };
    }
}
