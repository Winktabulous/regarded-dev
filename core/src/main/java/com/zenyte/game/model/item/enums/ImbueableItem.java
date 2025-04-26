package com.zenyte.game.model.item.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 17-2-2019 | 22:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum ImbueableItem {
    ARCHERS_RING(6733, 11771),
    WARRIOR_RING(6735, 11772),
    SEERS_RING(6731, 11770),
    BERSERKER_RING(6737, 11773),
    RING_OF_SUFFERING(19550, 19710),
    RING_OF_SUFFERING_RI(20655, 20657),
    TREASONOUS_RING(12605, 12692),
    TYRRANICAL_RING(12603, 12691),
    RING_OF_THE_GODS(12601, 13202),
    GRANITE_RING(21739, 21752),
    SALVE_AMULET(4081, 12017),
    SALVE_AMULET_E(10588, 12018),
    BLACK_MASK(8921, 11784),
    BLACK_MASK1(8919, 11783),
    BLACK_MASK2(8917, 11782),
    BLACK_MASK3(8915, 11781),
    BLACK_MASK4(8913, 11780),
    BLACK_MASK5(8911, 11779),
    BLACK_MASK6(8909, 11778),
    BLACK_MASK7(8907, 11777),
    BLACK_MASK8(8905, 11776),
    BLACK_MASK9(8903, 11775),
    BLACK_MASK10(8901, 11774),
    SLAYER_HELM(11864, 11865),
    BLACK_SLAYER_HELMET(19639, 19641),
    GREEN_SLAYER_HELMET(19643, 19645),
    RED_SLAYER_HELMET(19647, 19649),
    PURPLE_SLAYER_HELMET(21264, 21266),
    TURQUOISE_SLAYER_HELMET(21888, 21890),
    HYDRA_SLAYER_HELMET(23073, 23075),
    CRYSTAL_BOW(4212, 11748),
    CRYSTAL_HALBERD(13091, 13080),
    CRYSTAL_SHIELD(4224, 11759);
    private final int normal;
    private final int imbued;
    public static final ImbueableItem[] values = values();
    public static final Map<Integer, ImbueableItem> IMBUEABLES = new HashMap<>(values.length);

    static {
        for (final ImbueableItem item : values) {
            IMBUEABLES.put(item.normal, item);
            IMBUEABLES.put(item.imbued, item);
        }
    }

    public static ImbueableItem get(final int id) {
        return IMBUEABLES.get(id);
    }

    ImbueableItem(int normal, int imbued) {
        this.normal = normal;
        this.imbued = imbued;
    }

    public int getNormal() {
        return normal;
    }

    public int getImbued() {
        return imbued;
    }
}
