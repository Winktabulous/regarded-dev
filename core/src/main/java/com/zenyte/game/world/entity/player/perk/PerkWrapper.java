package com.zenyte.game.world.entity.player.perk;

import com.zenyte.game.world.entity.player.perk.impl.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 11-1-2019 | 19:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum PerkWrapper {
    MASTER_MINER(MasterMinerPerk.class, 30003),
    ATHLETIC_RUNNER(AthleticRunnerPerk.class, 30004),
    MASTER_FISHERMAN(MasterFisherManPerk.class, 30005),
    //ITS_A_BLESSING(ItsABlessingPerk.class),
    MASTER_SLAYER(MasterSlayerPerk.class, 30006), SLEIGHT_OF_HAND(SleightOfHandPerk.class, 30007), FILL_THE_BANK(FillTheBankPerk.class, 30008), LUMBERJACK(LumberjackPerk.class, 30009), LEPRECHAUNS_FRIEND(LeprechaunsFriendPerk.class, 30010), AUBURYS_APPRENTICE(AuburysApprenticePerk.class, 30011), WRATH_OF_ZAROS(WrathOfZaros.class, 30012), RIDDLE_IN_THE_TUNNELS(RiddleInTunnels.class, 30013), FERTILIZER(Fertilizer.class, 30014), BACKFIRE(Backfire.class, 30015), PYROMANCER(Pyromancer.class, 30016);
    private final Class<? extends Perk> perk;
    private final int id;
    private static final PerkWrapper[] VALUES = values();
    private static final Map<Class<? extends Perk>, PerkWrapper> PERKS_BY_CLASS = new HashMap<>();
    private static final Map<String, PerkWrapper> PERKS_BY_NAME = new HashMap<>();
    public static final Int2ObjectOpenHashMap<PerkWrapper> PERKS_BY_ID = new Int2ObjectOpenHashMap<>();

    static {
        for (final PerkWrapper value : VALUES) {
            PERKS_BY_CLASS.put(value.getPerk(), value);
            PERKS_BY_NAME.put(value.name(), value);
            PERKS_BY_ID.put(value.getId(), value);
        }
    }

    public static PerkWrapper get(final Class<? extends Perk> perk) {
        return PERKS_BY_CLASS.get(perk);
    }

    public static PerkWrapper get(final String perk) {
        return PERKS_BY_NAME.get(perk);
    }

    public static PerkWrapper get(final int id) {
        return PERKS_BY_ID.get(id);
    }

    PerkWrapper(Class<? extends Perk> perk, int id) {
        this.perk = perk;
        this.id = id;
    }

    public Class<? extends Perk> getPerk() {
        return perk;
    }

    public int getId() {
        return id;
    }
}
