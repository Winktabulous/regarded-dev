package com.zenyte.game.content.event.easter2020.plugin.object;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.EasterConstants.EasterItem;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.content.event.easter2020.plugin.npc.Incubator;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Corey
 * @since 08/04/2020
 */
@SkipPluginScan
public class CoalSupplyObject implements ObjectAction {
    
    public static boolean isFixed(final Player player) {
        return player.getVarManager().getBitValue(EasterConstants.COAL_SUPPLY_VARBIT) == 1;
    }
    
    public static void fix(final Player player) {
        player.getVarManager().sendBit(EasterConstants.COAL_SUPPLY_VARBIT, 1);
    }
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new PlainChat(player, "This looks like the supply of coal which feeds the incubator."));
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{EasterConstants.COAL_SUPPLY};
    }
    
    public static class ItemOnCoalSupply implements ItemOnObjectAction {
        
        @Override
        public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
            if (isFixed(player)) {
                player.getDialogueManager().start(new PlainChat(player, "This machine is already repaired."));
                return;
            }
    
            if (!SplittingHeirs.progressedAtLeast(player, Stage.SPOKEN_WITH_INCUBATOR_WORKER)) {
                player.getDialogueManager().start(new PlayerChat(player, "I should speak with the Impling working this machine to see what they want me to do."));
                return;
            }
    
            if (Incubator.State.getCurrentState(player) != Incubator.State.WITH_CHIMNEY) {
                player.getDialogueManager().start(new PlainChat(player, "The main incubator needs to be repaired first."));
                return;
            }
    
            if (item.getId() != EasterItem.SOOTY_PIPE.getItemId()) {
                player.getDialogueManager().start(new PlainChat(player, "Doesn't look like that will fit anywhere on this section. I wonder if it goes on the main incubator, or one of the other units."));
                return;
            }
            
            player.getInventory().deleteItem(item);
            player.sendMessage("You connect the coal burner to the incubator with the sooty pipe.");
            fix(player);
            Incubator.fixIncubator(player);
        }
        
        @Override
        public Object[] getItems() {
            return new Object[]{
                    EasterItem.COG.getItemId(),
                    EasterItem.PISTONS.getItemId(),
                    EasterItem.CHIMNEY.getItemId(),
                    EasterItem.CLEAN_PIPE.getItemId(),
                    EasterItem.SOOTY_PIPE.getItemId(),
                    EasterItem.WET_PIPE.getItemId()
            };
        }
        
        @Override
        public Object[] getObjects() {
            return new Object[]{EasterConstants.COAL_SUPPLY};
        }
    }
    
}
