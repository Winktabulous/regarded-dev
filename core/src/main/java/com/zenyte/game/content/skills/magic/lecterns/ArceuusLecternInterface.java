package com.zenyte.game.content.skills.magic.lecterns;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/09/2019 08:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArceuusLecternInterface extends Interface {
    private static final int QUANTITY_VARP = 2224;
    private static final int SELECTED_TABLET_TYPE = 10600;

    @Override
    protected void attach() {
        put(4, "Make one");
        put(5, "Make five");
        put(6, "Make ten");
        put(7, "Make x");
        put(8, "Make all");

        put(16, "Battlefront");
        put(15, "Draynor manor");
        put(17, "Mind altar");
        put(18, "Salve graveyard");
        put(19, "Fenkenstrains castle");
        put(20, "West ardougne");
        put(21, "Harmony island");
        put(22, "Wilderness cemetery");
        put(23, "Barrows");
        put(24, "Ape atoll");
        put(14, "Arceuus Library");

        put(13, "Create");
    }

    @Override
    public void open(final Player player) {
        player.getVarManager().sendBit(10599, 3);
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

        bind("Arceuus Library", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.ARCEUUS_LIBRARY));
        bind("Draynor manor", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.DRAYNOR_MANOR));
        bind("Battlefront", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.BATTLEFRONT));
        bind("Mind altar", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.MIND_ALTAR));
        bind("Salve graveyard", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.SALVE_GRAVEYARD));
        bind("Fenkenstrains castle", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.FENKENSTRAINS_CASTLE));
        bind("West ardougne", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.WEST_ARDOUGNE));
        bind("Harmony island", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.HARMONY_ISLAND));
        bind("Wilderness cemetery", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.WILDERNESS_CEMETERY));
        bind("Barrows", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.BARROWS));
        bind("Ape atoll", (player, slotId, itemId, option) -> setType(player, ArceuusTablet.APE_ATOLL));

        bind("Create", (player) -> {
            ArceuusTablet type = ArceuusTablet.get(player.getVarManager().getBitValue(SELECTED_TABLET_TYPE));
            if (type == null) return;
            player.getInterfaceHandler().closeInterface(getInterface());
            player.getActionManager().setAction(new TabletCreation(type, player.getVarManager().getValue(QUANTITY_VARP)));
        });
    }

    private void setType(@NotNull final Player player, @NotNull final LecternTablet tablet) {
        player.getVarManager().sendBit(SELECTED_TABLET_TYPE, tablet.type());
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ARCEUUS_LECTERN;
    }
}
