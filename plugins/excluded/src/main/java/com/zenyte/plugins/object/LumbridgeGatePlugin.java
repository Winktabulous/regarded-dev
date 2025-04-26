package com.zenyte.plugins.object;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.*;

/**
 * @author Kris | 5. dets 2017 : 0:22.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LumbridgeGatePlugin implements ObjectAction {

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }, getDelay()));
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Open")) {
            startDialogue(player, object);
        } else if (option.equals("Pay-toll(10gp)")) {
            if (player.getInventory().containsItem(995, 10)) {
                player.getInventory().deleteItem(995, 10);
                player.sendMessage("You quickly pay the toll 10 gold pieces and go through the gates.");
                TemporaryDoubleDoor.handleDoubleDoor(player, object);
            } else {
                startDialogue(player, object);
            }
        }
    }

    private void startDialogue(final Player player, final WorldObject object) {
        final NPC npc = World.findNPC(player.getX() >= 3268 ? 4288 : 4287, player.getLocation()).orElseThrow(RuntimeException::new);
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                player("Can I come through this gate?").executeAction(() -> player.faceEntity(npc));
                npc("You must pay a toll of 10 gold coins to pass.");
                options(TITLE, new DialogueOption("No thank you, I\'ll walk around.", () -> setKey(10)), new DialogueOption("Who does my money go to?", () -> setKey(20)), new DialogueOption("Yes, ok.", () -> setKey(30)));
                player(10, "No thank you, I\'ll walk around.");
                npc("Ok suit yourself.");
                player(20, "Who does my money go to?");
                npc("The money goes to the city of Al-Kharid.");
                player(30, "Yes, ok.").executeAction(() -> {
                    if (player.getInventory().containsItem(995, 10)) {
                        player.getInventory().deleteItem(995, 10);
                        player.sendMessage("You pay the guard.");
                        TemporaryDoubleDoor.handleDoubleDoor(player, object);
                        finish();
                    }
                });
                player("Oh dear I don\'t actually seem to have enough money.");
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_44052, ObjectId.GATE_44053 };
    }
}
