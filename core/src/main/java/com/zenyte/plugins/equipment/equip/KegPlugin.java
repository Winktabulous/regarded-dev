package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.content.minigame.warriorsguild.kegbalance.KegBalanceArea;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;

/**
 * @author Kris | 29/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KegPlugin implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        GlobalAreaManager.getArea(KegBalanceArea.class).loseBalance(player);
        return false;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.ONE_BARREL, ItemId.TWO_BARRELS, ItemId.THREE_BARRELS, ItemId.FOUR_BARRELS, ItemId.FIVE_BARRELS
        };
    }
}
