package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.JewelleryData;
import com.zenyte.game.content.skills.crafting.actions.JewelleryCrafting;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Kris | 26/05/2019 22:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SilverJewelleryInterface extends Interface {
    private static final int SMITHING_AMOUNT_VARP = 2224;
    @Override
    protected void attach() {
        put(32, "Make one");
        put(33, "Make five");
        put(34, "Make ten");
        put(35, "Make x");
        put(36, "Make all");
        for (JewelleryData data : JewelleryData.VALUES) {
            if (isExcluded(data)) {
                continue;
            }
            put(data.getComponentId(), data.name());
        }
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        if (player.getVarManager().getValue(SMITHING_AMOUNT_VARP) <= 0) {
            player.getVarManager().sendVar(SMITHING_AMOUNT_VARP, 1);
        }
    }

    @Override
    public void close(Player player, Optional<GameInterface> replacement) {
        GoldJewelleryInterface.restoreInputFromCrafting(player);
    }

    @Override
    protected void build() {
        bind("Make one", (player) -> player.getVarManager().sendVar(SMITHING_AMOUNT_VARP, 1));
        bind("Make five", (player) -> player.getVarManager().sendVar(SMITHING_AMOUNT_VARP, 5));
        bind("Make ten", (player) -> player.getVarManager().sendVar(SMITHING_AMOUNT_VARP, 10));
        bind("Make x", (player) -> {
            GoldJewelleryInterface.restoreInputFromCrafting(player);
            player.sendInputInt("Enter amount:", value -> player.getVarManager().sendVar(SMITHING_AMOUNT_VARP, Math.max(1, value)));
        });
        bind("Make all", (player) -> player.getVarManager().sendVar(SMITHING_AMOUNT_VARP, 28));
        for (JewelleryData data : JewelleryData.VALUES) {
            if (isExcluded(data)) {
                continue;
            }
            bind(data.name(), (player, slotId, itemId, option) -> {
                final int amount = player.getVarManager().getValue(SMITHING_AMOUNT_VARP);
                player.getActionManager().setAction(new JewelleryCrafting(data, amount));
            });
        }
    }

    private boolean isExcluded(final JewelleryData data) {
        return data.getSlotId() == -1;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SILVER_JEWELLERY_INTERFACE;
    }
}
