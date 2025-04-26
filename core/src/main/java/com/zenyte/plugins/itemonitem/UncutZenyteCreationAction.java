package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 23-4-2019 | 23:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class UncutZenyteCreationAction implements ItemOnItemAction {
    private static final Item UNCUT_ZENYTE = new Item(19496);
    private static final Item CHISEL = new Item(1755);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item onyx = from.getId() == 6573 ? from : to;
        final Item shard = from.getId() == 19529 ? from : to;
        if (!player.getInventory().containsItem(CHISEL)) {
            player.sendMessage("You need a chisel to for the infusion process.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(shard, onyx, "Are you sure you want to infuse the Onyx with the contents of the Zenyte shard to create an Uncut zenyte? This can not be reversed.");
                options(TITLE, "Proceed with the infusion.", "Cancel").onOptionOne(() -> {
                    player.getInventory().deleteItem(onyx);
                    player.getInventory().deleteItem(shard);
                    player.getInventory().addItem(UNCUT_ZENYTE);
                    player.setAnimation(CraftingDefinitions.GemCuttingData.ZENYTE.getAnimation());
                    player.getSkills().addXp(SkillConstants.CRAFTING, 15);
                    player.sendMessage("You successfully infuse the Onyx and Zenyte shard to create an Uncut zenyte.");
                });
                item(5, UNCUT_ZENYTE, "You successfully infuse the Onyx and Zenyte shard to create an Uncut zenyte.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {19529, 6573};
    }
}
