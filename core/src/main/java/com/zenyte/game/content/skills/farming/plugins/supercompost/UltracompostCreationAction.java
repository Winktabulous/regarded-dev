package com.zenyte.game.content.skills.farming.plugins.supercompost;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Christopher
 * @since 02/24/2020
 */
public class UltracompostCreationAction extends Action {
    private static final Animation creationAnim = new Animation(7699);
    private static final SoundEffect creationSound = new SoundEffect(2608);
    private static final Item ashesPerCharge = new Item(ItemId.VOLCANIC_ASH, 2);
    private static final Item superCompost = new Item(ItemId.SUPERCOMPOST);
    private final int amount;
    private int cycles;

    @Override
    public boolean start() {
        return check();
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        final Inventory inventory = player.getInventory();
        inventory.deleteItem(ashesPerCharge);
        inventory.replaceItem(ItemId.ULTRACOMPOST, 1, inventory.getContainer().getSlotOf(superCompost.getId()));
        player.sendSound(creationSound);
        player.setAnimation(creationAnim);
        player.sendFilteredMessage("You add some volcanic ash to the bucket of supercompost, turning it into ultracompost.");
        cycles++;
        return 2;
    }

    private boolean check() {
        final Inventory inventory = player.getInventory();
        if (!inventory.checkSpace() || cycles >= amount || !inventory.containsItem(superCompost)) {
            return false;
        }
        if (inventory.getAmountOf(ItemId.VOLCANIC_ASH) < ashesPerCharge.getAmount()) {
            player.getDialogueManager().start(new ItemChat(player, ashesPerCharge, "You need at least two volcanic ashes to turn the supercompost into an ultracompost."));
            return false;
        }
        return true;
    }

    public UltracompostCreationAction(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getCycles() {
        return cycles;
    }
}
