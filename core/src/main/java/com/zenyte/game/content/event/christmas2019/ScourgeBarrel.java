package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 17/12/2019
 */
public class ScourgeBarrel implements ObjectAction {
    
    private static final int BUCKET_BARREL = 46112;
    private static final int BUCKET_BARREL_2 = 46113;
    private static final int EMPTY_BARREL = 46224;
    private static final int CHAIN_BARREL = 46099;
    
    private static final Item chains = new Item(ChristmasConstants.CHAINS_ID);
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_SCOURGE)) {
            player.sendMessage("You probably don't want to do that right now with Scourge wandering around.");
            return;
        }
        if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SANTA_FREED)) {
            player.sendMessage("The barrel is empty.");
            return;
        }
        
        switch (object.getId()) {
            case EMPTY_BARREL:
                player.sendMessage("The barrel is empty.");
                break;
            case BUCKET_BARREL:
            case BUCKET_BARREL_2:
                if (player.getInventory().containsItem(ItemId.BUCKET, 6)) {
                    player.sendMessage("The barrel is empty.");
                } else {
                    player.sendMessage("You find an old bucket.");
                    player.getInventory().addOrDrop(new Item(ItemId.BUCKET));
                }
                break;
            case CHAIN_BARREL:
                if (player.getInventory().containsItem(chains) || AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.HAS_GHOST_COSTUME)) {
                    player.sendMessage("The barrel is empty.");
                } else {
                    player.sendMessage("You find a rusty coil of chains.");
                    if (player.getInventory().addItem(chains).isFailure()) {
                        player.sendMessage("But your inventory is too full.");
                    }
                }
                break;
        }
        
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{BUCKET_BARREL, BUCKET_BARREL_2, EMPTY_BARREL, CHAIN_BARREL};
    }
}
