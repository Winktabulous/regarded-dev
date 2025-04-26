package com.zenyte.plugins.object;

import com.near_reality.api.service.user.UserPlayerHandler;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.renewednpc.ZenyteGuide;
import kotlin.Unit;

/**
 * Represents an {@link ObjectAction} that handles the portal at the tutorial island.
 */
@SuppressWarnings("unused")
public class TutorialIslandPortal implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getTemporaryAttributes().containsKey("ironman_setup")) {
            player.getDialogueManager().start(new Dialogue(player, ZenyteGuide.NPC_ID) {
                @Override
                public void buildDialogue() {
                    npc("You should talk to me before leaving.");
                }
            });
            return;
        }

        final GameMode selectedGameMode = PlayerAttributesKt.getSelectedGameMode(player);
        if (selectedGameMode.isGroupIronman()) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("This portal will take you to The Node. Here you will be able to create " +
                            "a group with other Group Iron people. You will not be able to return " +
                            "to Tutorial Island.");
                    options("Travel to The Node",
                            new DialogueOption("Yes.", () ->
                                    new FadeScreen(player, () -> UserPlayerHandler.INSTANCE.updateGameMode(player, selectedGameMode, (success) -> {
                                        if (!success) {
                                            player.log(LogLevel.ERROR, "Failed to update game-mode " + selectedGameMode + " in API, setting anyways.");
                                            player.setGameMode(selectedGameMode);
                                        }
                                        player.setLocation(new Location(3105, 3028));
                                        player.getAppearance().setInvisible(false);
                                        player.putBooleanAttribute("registered", true);
                                        return Unit.INSTANCE;
                                    })
                                    ).fade(3, true)),
                            new DialogueOption("No."));
                }
            });
        } else {
            new FadeScreen(player, () -> {
                player.setLocation(ZenyteGuide.SPAWN_LOCATION);
                ZenyteGuide.finishAppearance(player);
            }).fade(3, false);
        }
    }


    @Override
    public Object[] getObjects() {
        return new Object[]{55070};
    }
}
