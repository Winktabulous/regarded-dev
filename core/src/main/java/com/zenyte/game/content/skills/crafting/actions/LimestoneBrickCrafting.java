package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 04/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LimestoneBrickCrafting implements PairedItemOnItemPlugin {
    private static final int CRAFTING_REQUIREMENT = 12;
    private static final int MAXIMUM_SUCCESS_LEVEL = 40;
    private static final double BASE_SUCCESS_PROBABILITY = 0.75;
    private static final double MAXIMUM_SUCCESS_PROBABILITY = 1.0;
    private static final Animation animation = new Animation(1309);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Inventory inventory = player.getInventory();
        if (!inventory.containsItem(ItemId.LIMESTONE, 1)) {
            player.getDialogueManager().start(new PlainChat(player, "You have ran out of limestone."));
            return;
        }
        final Skills skills = player.getSkills();
        final int crafting = skills.getLevel(SkillConstants.CRAFTING);
        if (crafting < CRAFTING_REQUIREMENT) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of at least " + CRAFTING_REQUIREMENT + " to turn the limestone into a brick."));
            return;
        }
        final double spreadSuccess = MAXIMUM_SUCCESS_PROBABILITY - BASE_SUCCESS_PROBABILITY;
        final double successPerLevel = spreadSuccess / MAXIMUM_SUCCESS_LEVEL;
        final double successProbability = BASE_SUCCESS_PROBABILITY + (crafting * successPerLevel);
        final boolean succeeded = Utils.randomDouble() <= successProbability;
        player.setAnimation(animation);
        inventory.deleteItem(ItemId.LIMESTONE, 1);
        if (succeeded) {
            skills.addXp(SkillConstants.CRAFTING, 6);
            inventory.addOrDrop(new Item(ItemId.LIMESTONE_BRICK));
            player.sendFilteredMessage("You use the chisel on the limestone and carve it into a building block.");
        } else {
            skills.addXp(SkillConstants.CRAFTING, 1.5);
            inventory.addOrDrop(new Item(ItemId.ROCK));
            player.sendFilteredMessage("You use the chisel on the limestone but fail to carve it into a building block.");
        }
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(ItemId.CHISEL, ItemId.LIMESTONE)};
    }
}
