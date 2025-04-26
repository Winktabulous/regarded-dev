package com.zenyte.game.content.skills.magic.lecterns;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/09/2019 07:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RegularLecternInterface extends Interface {

    private static final int QUANTITY_VARP = 2224;

    @Override
    protected void attach() {
        put(5, "Make one");
        put(6, "Make five");
        put(7, "Make ten");
        put(8, "Make x");
        put(9, "Make all");

        put(11, "Enchant onyx");
        put(13, "Enchant diamond");
        put(12, "Lumbridge teleport");
        put(14, "Watchtower teleport");
        put(15, "House teleport");

        put(16, "Enchant emerald");
        put(17, "Enchant sapphire");
        put(18, "Falador teleport");
        put(19, "Ardougne teleport");
        put(20, "Bones to bananas");

        put(21, "Enchant dragonstone");
        put(22, "Enchant ruby");
        put(23, "Varrock teleport");
        put(24, "Camelot teleport");
        put(25, "Bones to peaches");
    }

    @Override
    public void open(final Player player) {
        for (int i = 0; i < 2; i++) {
            player.getVarManager().sendVar(261 + i, 3);
        }
        player.getVarManager().sendBit(10599, 0);
        if (player.getVarManager().getValue(QUANTITY_VARP) < 1) {
            player.getVarManager().sendVar(QUANTITY_VARP, 1);
        }
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Make one", player -> player.getVarManager().sendVar(QUANTITY_VARP, 1));
        bind("Make five", player -> player.getVarManager().sendVar(QUANTITY_VARP, 5));
        bind("Make ten", player -> player.getVarManager().sendVar(QUANTITY_VARP, 10));
        bind("Make x", player -> player.sendInputInt("How many would you like to make?", value -> player.getVarManager().sendVar(QUANTITY_VARP, Math.max(0, Math.min(28, value)))));
        bind("Make all", player -> player.getVarManager().sendVar(QUANTITY_VARP, 28));

        bind("Enchant onyx", (player, slotId, itemId, option) -> create(player, RegularTablet.ONYX_ENCHANTMENT, option));
        bind("Enchant emerald", (player, slotId, itemId, option) -> create(player, RegularTablet.EMERALD_ENCHANTMENT, option));
        bind("Enchant dragonstone", (player, slotId, itemId, option) -> create(player, RegularTablet.DRAGONSTONE_ENCHANTMENT, option));
        bind("Enchant sapphire", (player, slotId, itemId, option) -> create(player, RegularTablet.SAPPHIRE_ENCHANTMENT, option));
        bind("Enchant ruby", (player, slotId, itemId, option) -> create(player, RegularTablet.RUBY_ENCHANTMENT, option));
        bind("Enchant diamond", (player, slotId, itemId, option) -> create(player, RegularTablet.DIAMOND_ENCHANTMENT, option));

        bind("Lumbridge teleport", (player, slotId, itemId, option) -> create(player, RegularTablet.LUMBRIDGE_TELEPORT, option));
        bind("Falador teleport", (player, slotId, itemId, option) -> create(player, RegularTablet.FALADOR_TELEPORT, option));
        bind("Varrock teleport", (player, slotId, itemId, option) -> create(player, RegularTablet.VARROCK_TELEPORT, option));
        bind("Watchtower teleport", (player, slotId, itemId, option) -> create(player, RegularTablet.WATCHTOWER_TELEPORT, option));
        bind("Ardougne teleport", (player, slotId, itemId, option) -> create(player, RegularTablet.ARDOUGNE_TELEPORT, option));
        bind("Camelot teleport", (player, slotId, itemId, option) -> create(player, RegularTablet.CAMELOT_TELEPORT, option));
        bind("House teleport", (player, slotId, itemId, option) -> create(player, RegularTablet.TELEPORT_TO_HOUSE, option));
        bind("Bones to bananas", (player, slotId, itemId, option) -> create(player, RegularTablet.BONES_TO_BANANAS, option));
        bind("Bones to peaches", (player, slotId, itemId, option) -> create(player, RegularTablet.BONES_TO_PEACHES, option));
    }

    private final void create(@NotNull final Player player, @NotNull final LecternTablet tablet, final int option) {
        player.getInterfaceHandler().closeInterface(getInterface());
        player.getActionManager().setAction(new TabletCreation(tablet, player.getVarManager().getValue(QUANTITY_VARP)));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.REGULAR_LECTERN;
    }

}
