package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;

import java.util.Map;

/**
 * @author Kris | 16/04/2019 16:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EquipmentTabInterface extends Interface {
    public static final String REMOVE = "Remove";
    public static final String EXAMINE = "Examine";
    public static final String CHECK = "Check";

    @Override
    protected void attach() {
        put(1, "View equipment stats");
        put(3, "Price checker");
        put(5, "View items kept on death");
        put(7, "Call follower");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
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
            final String opt = getOption(item, optionId);
            final ItemPlugin plugin = ItemPlugin.getPlugin(item.getId());
            final ItemPlugin.OptionHandler handler = plugin.getHandler(opt);
            if (handler != null) {
                handler.handle(player, item, player.getEquipment().getContainer(), slotId);
                return;
            }
            if (opt.equals(EXAMINE)) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            player.sendMessage("Nothing interesting happens.");
        };
    }

    public static final String getOption(final Item item, final int optionId) {
        if (optionId == 1) {
            return REMOVE;
        }
        if (optionId == 10) {
            return EXAMINE;
        }
        final Map<Integer, Object> params = item.getDefinitions().getParameters();
        if (params == null) {
            return "null";
        }
        final Object option = params.get(449 + optionId);
        if (!(option instanceof String)) {
            return "null";
        }
        return (String) option;
    }

    @Override
    protected void build() {
        bind("View equipment stats", player -> {
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.stopAll();
            player.getEquipment().sendEquipmentStatsInterface();
        });
        bind("Price checker", player -> {
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.stopAll();
            player.getPriceChecker().openPriceChecker();
        });
        bind("View items kept on death", player -> {
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.stopAll();
            GameInterface.ITEMS_KEPT_ON_DEATH.open(player);
        });
        bind("Call follower", player -> {
            if (player.isLocked()) {
                return;
            }
            if (player.getFollower() != null) {
                if (player.getFollower().getLocation().withinDistance(player, 5)) {
                    player.sendMessage("Your follower is already close enough.");
                    return;
                }
                player.getFollower().call();
            } else {
                player.sendMessage("You do not have a follower.");
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EQUIPMENT_TAB;
    }
}
