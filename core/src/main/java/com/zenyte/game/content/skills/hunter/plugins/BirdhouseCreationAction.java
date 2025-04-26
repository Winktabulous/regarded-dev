package com.zenyte.game.content.skills.hunter.plugins;

import com.zenyte.game.content.skills.hunter.node.BirdHouseType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.DoubleItemChat;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 24/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BirdhouseCreationAction extends Action {
    private static final Animation animation = new Animation(7057);
    @NotNull
    private final BirdHouseType house;
    private int amount;

    @Override
    public boolean start() {
        final Skills skills = player.getSkills();
        if (skills.getLevel(SkillConstants.CRAFTING) < house.getCraftingRequirement()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of at least " + house.getCraftingRequirement() + " to create this bird house."));
            return false;
        }
        return containsMaterials(player.getInventory());
    }

    private boolean containsMaterials(Inventory inventory) {
        if (!inventory.containsItem(ItemId.HAMMER) || !inventory.containsItem(ItemId.CHISEL)) {
            player.getDialogueManager().start(new DoubleItemChat(player, new Item(ItemId.HAMMER), new Item(ItemId.CHISEL), "You need a hammer and chisel to make a birdhouse trap."));
            return false;
        }
        if (!inventory.containsItem(house.getLogsId()) || !inventory.containsItem(ItemId.CLOCKWORK)) {
            player.getDialogueManager().start(new DoubleItemChat(player, new Item(house.getLogsId()), new Item(ItemId.CLOCKWORK), "You don't have the right ingredients:<br>1 x " + ItemDefinitions.getOrThrow(house.getLogsId()).getName() + "<br>1 x Clockwork"));
            return false;
        }
        return true;
    }

    @Override
    public boolean process() {
        if (amount <= 0) {
            return false;
        }
        return containsMaterials(player.getInventory());
    }

    @Override
    public int processWithDelay() {
        final Inventory inventory = player.getInventory();
        if (!containsMaterials(inventory)) {
            return -1;
        }
        player.setAnimation(animation);
        player.sendSound(2605);
        inventory.deleteItem(house.getLogsId(), 1);
        inventory.deleteItem(ItemId.CLOCKWORK, 1);
        inventory.addItem(new Item(house.getBirdhouseId()));
        player.getSkills().addXp(SkillConstants.CRAFTING, house.getCraftingExperience());
        amount--;
        return 1;
    }

    public BirdhouseCreationAction(BirdHouseType house, int amount) {
        this.house = house;
        this.amount = amount;
    }
}
