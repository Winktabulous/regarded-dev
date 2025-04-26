package com.zenyte.game.content.minigame.pestcontrol;

import com.zenyte.game.content.minigame.pestcontrol.npc.*;

/**
 * @author Kris | 30. juuni 2018 : 14:27:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum PestNPCTable {

    TORCHER(TorcherNPC.class, new int[]{1714, 1715, 1716, 1717}, new int[]{1716, 1717, 1718, 1719, 1720, 1721}, new int[]{1720, 1721, 1722, 1723}),
    DEFILER(DefilerNPC.class, new int[]{1724, 1725, 1726, 1727}, new int[]{1726, 1727, 1728, 1729, 1730, 1731}, new int[]{1730, 1731, 1732, 1733}),
    SHIFTER(ShifterNPC.class, new int[]{1694, 1695, 1696, 1697}, new int[]{1696, 1697, 1698, 1699, 1700, 1701}, new int[]{1700, 1701, 1702, 1703}),
    SPLATTER(SplatterNPC.class, new int[]{1689, 1690, 1691}, new int[]{1690, 1691, 1692}, new int[]{1691, 1692, 1693}),
    RAVAGER(RavagerNPC.class, new int[]{1704, 1705, 1706}, new int[]{1705, 1706, 1707}, new int[]{1706, 1707, 1708}),
    SPINNER(SpinnerNPC.class, new int[]{1709, 1710, 1711}, new int[]{1710, 1711, 1712, 1713}, new int[]{1711, 1712, 1713}),
    BRAWLER(BrawlerNPC.class, new int[]{1734, 1735, 1737}, new int[]{1735, 1737, 1738}, new int[]{1736, 1737, 1738});

    /**
     * Manual weighted table of the types, since the types with more ids in them have a higher chance of spawning.
     */
    public static final PestNPCTable[] VALUES = new PestNPCTable[]{
            TORCHER, TORCHER, DEFILER, DEFILER, SHIFTER, SHIFTER, SPLATTER, RAVAGER, SPINNER, BRAWLER
    };
    private final Class<? extends PestNPC> npcClass;
    private final int[] noviceIds, intermediateIds, veteranIds;

    PestNPCTable(final Class<? extends PestNPC> npcClass, final int[] noviceIds, final int[] intermediateIds, final int[] veteranIds) {
        this.npcClass = npcClass;
        this.noviceIds = noviceIds;
        this.intermediateIds = intermediateIds;
        this.veteranIds = veteranIds;
    }

    public Class<? extends PestNPC> getNpcClass() {
        return npcClass;
    }

    public int[] getNoviceIds() {
        return noviceIds;
    }

    public int[] getIntermediateIds() {
        return intermediateIds;
    }

    public int[] getVeteranIds() {
        return veteranIds;
    }

}
