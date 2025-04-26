package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.npcs.mimic.MimicInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.packet.out.IfSetAngle;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.NPCMessage;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 29/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StrangeCasket implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Search")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options(TITLE, new DialogueOption("Search.", () -> search(player)), new DialogueOption(TreasureTrail.isMimicEnabled(player) ? "Stop receiving the Mimic." : "Start receiving the Mimic.", () -> {
                        TreasureTrail.setMimicStatus(player, !TreasureTrail.isMimicEnabled(player));
                        setKey(TreasureTrail.isMimicEnabled(player) ? 20 : 10);
                    }));
                    plain(10, "You will no longer find the Mimic inside treasure caskets.");
                    plain(20, "You will now find the Mimic inside treasure caskets.");
                }
            });
        }
    }

    private static final void search(@NotNull final Player player) {
        player.getDialogueManager().finish();
        if (player.getRetrievalService().is(ItemRetrievalService.RetrievalServiceType.MIMIC)) {
            GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
            return;
        }
        if (!player.getInventory().containsItem(ItemId.MIMIC, 1)) {
            player.getDialogueManager().start(new PlainChat(player, "You find nothing in the strange casket."));
            return;
        }
        final Item item = CollectionUtils.findMatching(player.getInventory().getContainer().getItems().values(), it -> it.getId() == ItemId.MIMIC && it.getNumericAttribute("The Mimic slain").intValue() == 0);
        if (item == null) {
            player.getDialogueManager().start(new PlainChat(player, "You find nothing in the strange casket."));
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                final NPCMessage message = npc(1, "The Mimic has... found you!<br>Will you fight now?", key);
                message.setOnDisplay(() -> {
                    // Very unsafe but I cannot see a way to do it in any other way as the casket isn't an NPC.
                    player.getPacketDispatcher().sendComponentItem(231, 1, 405, 100);
                    player.send(new IfSetAngle(231, 1, 150, 1580, 650));
                    player.getPacketDispatcher().sendComponentText(231, 2, Colour.BLUE.wrap("Strange casket"));
                });
                options(TITLE, new DialogueOption("Fight the Mimic", () -> {
                    final Item item = CollectionUtils.findMatching(player.getInventory().getContainer().getItems().values(), it -> it.getId() == ItemId.MIMIC && it.getNumericAttribute("The Mimic slain").intValue() == 0);
                    if (item != null) {
                        item.setAttribute("The Mimic initialized", 1);
                    }
                    MimicInstance.build(player);
                }), new DialogueOption("Come back later"));
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STRANGE_CASKET };
    }
}
