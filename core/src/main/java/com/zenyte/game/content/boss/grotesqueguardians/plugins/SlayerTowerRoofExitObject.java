package com.zenyte.game.content.boss.grotesqueguardians.plugins;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 21/07/2019 | 21:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class SlayerTowerRoofExitObject implements ObjectAction {

    private static final Location OUTSIDE_LOCATION = new Location(3428, 3541, 2);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Escape")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Do you wish to leave the slayer tower roof?", "Yes.", "No.").onOptionOne(() -> escape(player));
                }
            });
        } else {
            escape(player);
        }
    }

    private void escape(final Player player) {
        player.getDialogueManager().start(new PlainChat(player, "You escape down from the slayer tower roof...", false));
        new FadeScreen(player, () -> {
            player.getInterfaceHandler().closeInterfaces();
            player.setLocation(OUTSIDE_LOCATION);
        }).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROOF_EXIT };
    }
}
