package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.wheeloffortune.WheelOfFortune;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;

/**
 * @author Tommeh | 11/02/2020 | 15:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class WheelOfFortuneInterface extends Interface {
    @Override
    protected void attach() {
        put(7, "Examine item");
        put(16, "Spin");
        put(22, "Claim");
        put(25, "Deposit to bank");
        put(28, "Claim later");
        put(31, "Discard");
        put(53, "Yes (Discard)");
        put(56, "No (Discard)");
    }

    @Override
    public void open(Player player) {
        final WheelOfFortune wheelOfFortune = player.getWheelOfFortune();
        int type = wheelOfFortune.isPendingReward() ? 1 : 0;
        if (!wheelOfFortune.isPendingReward()) {
            wheelOfFortune.roll();
        }
        final Container container = wheelOfFortune.getContainer();
        final Item reward = wheelOfFortune.getPrize();
        player.getPacketDispatcher().sendUpdateItemContainer(container);
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendClientScript(10034, 15, reward.getDefinitions().getExamine(), type);
        player.getPacketDispatcher().sendClientScript(10042, 0);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Examine item"), -1, container.getSize(), AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Claim"), -1, 0, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Deposit to bank"), -1, 0, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Claim later"), -1, 0, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Discard"), -1, 0, AccessMask.CLICK_OP1);
        wheelOfFortune.refreshSpins();
    }

    @Override
    protected void build() {
        bind("Examine item", (player, slotId, itemId, option) -> ItemUtil.sendItemExamine(player, itemId));
        bind("Spin", player -> player.getWheelOfFortune().spin());
        bind("Claim", player -> player.getWheelOfFortune().claim(false));
        bind("Deposit to bank", player -> player.getWheelOfFortune().claim(true));
        bind("Claim later", player -> close(player));
        bind("Discard", player -> {
            player.getPacketDispatcher().sendClientScript(10042, 1);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Claim"), -1, 0, AccessMask.NONE);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Deposit to bank"), -1, 0, AccessMask.NONE);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Claim later"), -1, 0, AccessMask.NONE);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Discard"), -1, 0, AccessMask.NONE);
        });
        bind("Yes (Discard)", player -> {
            final WheelOfFortune wheelOfFortune = player.getWheelOfFortune();
            wheelOfFortune.setPendingReward(false);
            open(player);
        });
        bind("No (Discard)", player -> {
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Claim"), -1, 0, AccessMask.CLICK_OP1);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Deposit to bank"), -1, 0, AccessMask.CLICK_OP1);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Claim later"), -1, 0, AccessMask.CLICK_OP1);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Discard"), -1, 0, AccessMask.CLICK_OP1);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WHEEL_OF_FORTUNE;
    }
}
