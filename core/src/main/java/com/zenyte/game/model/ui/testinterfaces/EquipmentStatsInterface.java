package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;

/**
 * @author Tommeh | 19/05/2019 | 15:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class EquipmentStatsInterface extends Interface {
    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
    }

    @Override
    protected DefaultClickHandler getDefaultHandler() {
        return (player, componentId, slotId, itemId, optionId) -> {
            if (player.isLocked()) {
                return;
            }
            slotId = Equipment.getIndexByButton(getInterface().getId(), componentId);
            final Item item = player.getEquipment().getItem(slotId);
            if (item == null) {
                return;
            }
            final String opt = EquipmentTabInterface.getOption(item, optionId);
            final ItemPlugin plugin = ItemPlugin.getPlugin(item.getId());
            final ItemPlugin.OptionHandler handler = plugin.getHandler(opt);
            if (handler != null) {
                handler.handle(player, item, player.getEquipment().getContainer(), slotId);
                return;
            }
            if (opt.equals(EquipmentTabInterface.EXAMINE)) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            player.sendMessage("Nothing interesting happens.");
        };
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EQUIPMENT_STATS;
    }
}
