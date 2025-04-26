package com.zenyte.game.content.minigame.puropuro;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Corey
 * @since 01/02/2020
 */
public class JarGenerator extends ItemPlugin {
    
    private static final Animation anim = new Animation(6592);
    private static final Graphics graphic = new Graphics(1117);
    private static final SoundEffect sound = new SoundEffect(3726);
    private static final String CHARGES_LEFT_MESSAGE_TEMPLATE = "You have %d charges left in your jar generator.";
    
    private static final int IMPLING_JAR_CHARGE_COST = 3;
    private static final int BUTTERFLY_JAR_CHARGE_COST = 1;
    
    @Override
    public void handle() {
        bind("Impling-jar", (player, item, slotId) -> {
            if (player.getInventory().getFreeSlots() < 1) {
                player.sendMessage("You need some space in your inventory before you can use the jar generator.");
                return;
            }
            
            if (item.getCharges() < IMPLING_JAR_CHARGE_COST) {
                player.sendMessage("You don't have enough charges left on your jar generator to make an impling jar.");
                return;
            }
            useGenerator(player, ItemId.IMPLING_JAR, IMPLING_JAR_CHARGE_COST, item);
        });
        
        bind("Butterfly-jar", (player, item, slotId) -> {
            if (player.getInventory().getFreeSlots() < 1) {
                player.sendMessage("You need some space in your inventory before you can use the jar generator.");
                return;
            }
            useGenerator(player, ItemId.BUTTERFLY_JAR, BUTTERFLY_JAR_CHARGE_COST, item);
        });
        
        bind("Check", (player, item, slotId) -> player.sendMessage(String.format(CHARGES_LEFT_MESSAGE_TEMPLATE, item.getCharges())));
    }
    
    @Override
    public int[] getItems() {
        return new int[]{ItemId.JAR_GENERATOR};
    }
    
    private void useGenerator(final Player player, final int itemId, final int chargesUsed, final Item item) {
        player.setAnimation(anim);
        player.sendSound(sound);
        player.setGraphics(graphic);
        
        player.getInventory().addItem(new Item(itemId));
        item.setCharges(item.getCharges() - chargesUsed);
        player.sendMessage(String.format(CHARGES_LEFT_MESSAGE_TEMPLATE, item.getCharges()));
        
        if (item.getCharges() <= 0) {
            player.getInventory().deleteItem(item);
            player.sendMessage("Your jar generator runs out of charges and disappears.");
        }
    }
    
}
