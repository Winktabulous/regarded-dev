package com.near_reality.game.content.gauntlet.plugins;

import com.near_reality.game.content.gauntlet.GauntletPlayerAttributesKt;
import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.rewards.GauntletRewardType;
import com.near_reality.game.content.gauntlet.map.GauntletMap;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Andys1814.
 * @since 2/7/2022.
 */
public final class GauntletTeleportPlatform implements ObjectAction {

    private static final int TELEPORT_PLATFORM_IN_GAME = 36062;

    private static final int TELEPORT_PLATFORM_IN_GAME_CORRUPTED = 35965;

    private static final int TELEPORT_PLATFORM_LOBBY = 36082;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == TELEPORT_PLATFORM_IN_GAME || object.getId() == TELEPORT_PLATFORM_IN_GAME_CORRUPTED) {
            if (!(player.getArea() instanceof GauntletMap)) {
                return;
            }

            boolean quickExit = optionId == 2;

            if (quickExit) {
                exit(player);
            } else {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("Are you sure you wish to exit the Gauntlet? All of your progress will be lost and you will start again upon re-entering.");
                        options("Exit the Gauntlet?", new DialogueOption("Yes, let me out.", () -> exit(player)), new DialogueOption("No."));
                    }
                });
            }
        } else if (object.getId() == TELEPORT_PLATFORM_LOBBY) {
            player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 641);
            player.getPacketDispatcher().sendClientScript(2921);

            WorldTasksManager.schedule(() -> {
                player.setLocation(new Location(3111 + Utils.random(0, 1), 3489, 0));
                player.getPacketDispatcher().sendClientScript(2922);
            }, 1);
        }
    }

    private void exit(Player player) {
        final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
        if (gauntlet == null) {
            player.sendMessage("You are not currently in a Gauntlet dungeon!");
            return;
        }
        gauntlet.end(GauntletRewardType.NONE, true, false);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { TELEPORT_PLATFORM_IN_GAME, TELEPORT_PLATFORM_IN_GAME_CORRUPTED, TELEPORT_PLATFORM_LOBBY };
    }

}
