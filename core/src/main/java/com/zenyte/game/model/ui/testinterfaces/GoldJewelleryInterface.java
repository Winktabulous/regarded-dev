package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.JewelleryData;
import com.zenyte.game.content.skills.crafting.actions.JewelleryCrafting;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;

import java.util.Optional;

/**
 * @author Kris | 26/05/2019 22:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GoldJewelleryInterface extends Interface {
    private static final int QUANTITY_VARP = 2224;

    @Override
    protected void attach() {
        put(60, "Make one");
        put(61, "Make five");
        put(62, "Make ten");
        put(63, "Make x");
        put(64, "Make all");
        for (JewelleryData data : JewelleryData.VALUES) {
            if (isExcluded(data)) {
                continue;
            }
            put(data.getComponentId(), data.name());
        }
    }

    static {
        VarManager.appendPersistentVarp(QUANTITY_VARP);
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        if (player.getVarManager().getValue(QUANTITY_VARP) < 1) {
            player.getVarManager().sendVar(QUANTITY_VARP, 1);
        }
    }

    public static void restoreInputFromCrafting(Player player) {
        player.getPacketDispatcher().sendClientScript(6125);
    }

    @Override
    public void close(Player player, Optional<GameInterface> replacement) {
        restoreInputFromCrafting(player);
    }

    @Override
    protected void build() {
        bind("Make one", player -> player.getVarManager().sendVar(QUANTITY_VARP, 1));
        bind("Make five", player -> player.getVarManager().sendVar(QUANTITY_VARP, 5));
        bind("Make ten", player -> player.getVarManager().sendVar(QUANTITY_VARP, 10));
        bind("Make x", player -> {
            restoreInputFromCrafting(player);
            player.sendInputInt("How many would you like to make?", value -> player.getVarManager().sendVar(QUANTITY_VARP, Math.max(0, Math.min(28, value))));
        });
        bind("Make all", player -> player.getVarManager().sendVar(QUANTITY_VARP, 28));
        for (JewelleryData data : JewelleryData.VALUES) {
            if (isExcluded(data)) {
                continue;
            }
            bind(data.name(), player -> {
                if (JewelleryData.SLAYER_RING.equals(data) && !player.getSlayer().isUnlocked("Ring bling")) {
                    player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
                    player.sendMessage("You have not unlocked the ability to make slayer rings.");
                    return;
                }
                final int amount = Math.max(0, Math.min(28, player.getVarManager().getValue(QUANTITY_VARP)));
                player.getActionManager().setAction(new JewelleryCrafting(data, amount));
            });
        }
    }

    private boolean isExcluded(final JewelleryData data) {
        return data == JewelleryData.ETERNAL_SLAYER_RING || data.getSlotId() != -1;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GOLD_JEWELLERY_INTERFACE;
    }
}
