package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 22-4-2018 | 00:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class YanilleUndergroundEntranceObject implements ObjectAction {

    private static final Location INSIDE_TILE = new Location(2404, 9415, 0);

    private static final Location OUTSIDE_TILE = new Location(2412, 3060, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.SMOKY_CAVE) {
            if (option.equals("Smoke")) {
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        final boolean on = player.getBooleanAttribute("SmokeOverlay");
                        options("Do you want to turn the smoke overlay back " + (on ? "off?" : "on?"), on ? "Yes, hide the smoke overlay." : "Yes, turn the smoke overlay on", "No, leave it " + (on ? "on." : "off")).onOptionOne(() -> player.toggleBooleanAttribute("SmokeOverlay"));
                    }
                });
                return;
            }
            player.setLocation(INSIDE_TILE);
        } else {
            player.setLocation(OUTSIDE_TILE);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SMOKY_CAVE, ObjectId.CREVICE };
    }
}
