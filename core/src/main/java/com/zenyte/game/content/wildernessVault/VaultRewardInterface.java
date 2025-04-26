package com.zenyte.game.content.wildernessVault;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.wildernessVault.reward.WildernessVaultRewardHandler;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;

import java.util.Optional;

@SuppressWarnings("unused")
public class VaultRewardInterface extends Interface {

    @Override
    protected void attach() {
        put(10, "Take item");
    }

    @Override
    public void open(Player player) {
        super.open(player);

        WildernessVaultRewardHandler wr = (WildernessVaultRewardHandler) player.getTemporaryAttributes().getOrDefault("WILDY_CHEST_LOOT", null);
        if(wr == null) {
            return;
        }

        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Take item"), 0, Container.getSize(ContainerType.THEATRE_OF_BLOOD), AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendClientScript(150, getId() << 16 | getComponent("Take item"), ContainerType.THEATRE_OF_BLOOD.getId(), 2, 3, 0, -1, "", "", "", "", "", "", "", "", "");
    }

    @Override
    public void close(Player player, Optional<GameInterface> replacement) {
        WildernessVaultRewardHandler wr = (WildernessVaultRewardHandler) player.getTemporaryAttributes().getOrDefault("WILDY_CHEST_LOOT", null);
        if(wr == null) {
            return;
        }

        wr.addLoot();
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WILDERNESS_VAULT_REWARDS;
    }

}
