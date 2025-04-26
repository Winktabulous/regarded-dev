package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 2-1-2019 | 21:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SeedBoxInterface extends Interface {
    @Override
    protected void attach() {
        put(5, "Empty box");
        put(6, "Fill box");
        put(11, "Remove");
    }

    @Override
    public void open(Player player) {
        player.getSeedBox().refresh();
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendClientScript(149, getInterface().getId() << 16 | getComponent("Remove"), 150, 2, 3, 0, -1, "Remove-1<col=ff9040>", "Remove-5<col=ff9040>", "Remove-10<col=ff9040>", "Remove-All<col=ff9040>", "Remove-X<col=ff9040>");
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Remove"), 0, 6, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
    }

    @Override
    protected void build() {
        bind("Empty box", player -> player.getSeedBox().empty());
        bind("Fill box", player -> player.getSeedBox().fill());
        bind("Remove", (player, slotId, itemId, option) -> {
            if (option == 5) {
                player.sendInputInt("How many would you like to remove?", amt -> player.getSeedBox().remove(slotId, amt));
                return;
            } else if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }
            final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : Integer.MAX_VALUE;
            player.getSeedBox().remove(slotId, amount);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SEED_BOX;
    }
}
