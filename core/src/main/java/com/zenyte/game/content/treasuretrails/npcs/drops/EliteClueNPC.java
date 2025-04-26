package com.zenyte.game.content.treasuretrails.npcs.drops;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Kris | 22/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum EliteClueNPC implements ClueNPC {
    SKOTIZO(5), SARACHNIS(60), VORKATH(65), NUCLEAR_SMOKE_DEVIL(75), ZULRAH(75), CALLISTO(100), CERBERUS(100), KALPHITE_QUEEN(100), VENENATIS(100), VETION("Vet'ion reborn", 100), GREATER_ABYSSAL_DEMON(120), NIGHT_BEAST(120), ABYSSAL_SIRE(180), CHAOS_ELEMENTAL(200), CORPOREAL_BEAST(200), DERANGED_ARCHAEOLOGIST(200), DUSK(230), BRUTAL_BLACK_DRAGON(250), COMMANDER_ZILYANA(250), GENERAL_GRAARDOR(250), KRIL_TSUTSAROTH("K'ril Tsutsaroth", 250), KREE_ARRA("Kree'arra", 250), LAVA_DRAGON(250), ALCHEMICAL_HYDRA(256), RUNE_DRAGON(300), ADAMANT_DRAGON(320), ANCIENT_WYVERN(350), MITHRIL_DRAGON(350), SKELETAL_WYVERN(350), KING_BLACK_DRAGON(450), BLACK_DRAGON(500), BRUTAL_RED_DRAGON(500), DEMONIC_GORILLA(500), GIANT_MOLE(500), KRAKEN(500), SALARIN_THE_TWISTED(500), STEEL_DRAGON(500), THERMONUCLEAR_SMOKE_DEVIL(500), HYDRA(512), BRUTAL_BLUE_DRAGON(750), DAGANNOTH_PRIME(750), DAGANNOTH_REX(750), DAGANNOTH_SUPREME(750), SMOKE_DEVIL(750), ABYSSAL_DEMON(1200), CAVE_KRAKEN(1200), DARK_BEAST(1200), LIZARDMAN_SHAMAN(1200), TORTURED_GORILLA(1500);

    EliteClueNPC(final String name, final double rate) {
        this(rate, name.toLowerCase().replace("_", " ").trim());
    }

    EliteClueNPC(final double rate) {
        this(rate, null);
    }

    EliteClueNPC(final double rate, final String name) {
        this.rate = rate;
        this.monsterName = name == null ? name().toLowerCase().replace("_", " ").trim() : name;
    }

    public static final Map<String, List<PredicatedClueDrop>> mappedNPCs = new Object2ObjectOpenHashMap<>();

    static {
        for (final EliteClueNPC value : values()) {
            final List<PredicatedClueDrop> list = mappedNPCs.computeIfAbsent(value.monsterName, __ -> new LinkedList<>());
            list.add(new PredicatedClueDrop(value.rate, null));
        }
    }

    private final String monsterName;
    private final double rate;

    @Override
    public boolean accept(@NotNull NPCDefinitions definitions) {
        return true;
    }

    public String monsterName() {
        return monsterName;
    }

    public double rate() {
        return rate;
    }
}
