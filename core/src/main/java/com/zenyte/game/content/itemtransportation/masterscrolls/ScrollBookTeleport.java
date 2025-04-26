package com.zenyte.game.content.itemtransportation.masterscrolls;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.Ordinal;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * @author Kris | 19/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@Ordinal
public enum ScrollBookTeleport {
    NARDAH(TeleportScroll.NARDAH, 5672), DIGSITE(TeleportScroll.DIGSITE, 5673), FELDIP_HILLS(TeleportScroll.FELLDIP_HILLS, 5674), LUNAR_ISLE(TeleportScroll.LUNAR_ISLE, 5675), MORTTON(TeleportScroll.MORT_TON, 5676) {
        @Override
        public String toString() {
            return "Mort'ton";
        }
    },
    PEST_CONTROL(TeleportScroll.PEST_CONTROL, 5677), PISCATORIS(TeleportScroll.PISCATORIS, 5678), TAI_BWO_WANNAI(TeleportScroll.TAI_BWO_WANNAI, 5679), ELF_CAMP(TeleportScroll.ELF_CAMP, 5680), MOS_LE_HARMLESS(TeleportScroll.MOS_LE_HARMLESS, 5681) {
        @Override
        public String toString() {
            return "Mos Le'Harmless";
        }
    },
    LUMBERYARD(TeleportScroll.LUMBERYARD, 5682), ZUL_ANDRA(TeleportScroll.ZUL_ANDRA, 5683) {
        @Override
        public String toString() {
            return "Zul-andra";
        }
    },
    KEY_MASTER(TeleportScroll.KEY_MASTER, 5684), REVENANT_CAVES(TeleportScroll.REVENANT_CAVE, 6056), 
    //Special case with varbits: 8252 << 8 + 8253
    WATSON(TeleportScroll.WATSON, -1);
    private final TeleportScroll scroll;
    private final int varbit;
    private static final EnumMap<TeleportScroll, ScrollBookTeleport> map = new EnumMap<>(TeleportScroll.class);
    static final List<ScrollBookTeleport> values = Arrays.asList(values());

    static {
        for (final ScrollBookTeleport value : values) {
            map.put(value.scroll, value);
        }
    }

    final int getScrollId() {
        final Item[] items = scroll.getItems();
        assert items != null && items.length == 1;
        return items[0].getId();
    }

    public static final void decrementCountInBook(@NotNull final Player player, @NotNull final TeleportScroll scroll) {
        final Item[] items = scroll.getItems();
        assert items != null && items.length == 1;
        final Object scrollBook = player.getTemporaryAttributes().get("Master scroll book item attr");
        if (!(scrollBook instanceof Item)) {
            return;
        }
        final Item book = (Item) scrollBook;
        final int id = items[0].getId();
        MasterScrollBookInterface.setCount(book, id, Math.max(0, Math.min(MasterScrollBookInterface.MAXIMUM_TELEPORTS_COUNT, (MasterScrollBookInterface.getCount(book, id) - 1))));
    }

    ScrollBookTeleport(TeleportScroll scroll, int varbit) {
        this.scroll = scroll;
        this.varbit = varbit;
    }

    public TeleportScroll getScroll() {
        return scroll;
    }

    public int getVarbit() {
        return varbit;
    }
}
