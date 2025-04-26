package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 31-1-2019 | 14:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TourmalineCoreOnBandosBootsAction implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final BossDropItem item = BossDropItem.getItemByMaterials(from, to);
        if (item == null || !item.equals(BossDropItem.GUARDIAN_BOOTS)) {
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(item.getMaterials()[0], item.getMaterials()[1], "Do you really want to combine these items together into Guardian boots? You cannot undo this process.");
                options("Really combine these into guardian boots?", "Yes.", "No.").onOptionOne(() -> {
                    player.getInventory().deleteItemsIfContains(item.getMaterials(), () -> player.getInventory().addItem(item.getItem()));
                    setKey(5);
                });
                item(5, item.getItem(), "The second you hold your tourmaline core by the Bandos boots, the core is absorbed rapidly. Perhaps there is some link between the metal in Bandos and tourmaline...");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {11836, 21730};
    }
}
