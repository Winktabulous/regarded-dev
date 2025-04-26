package com.zenyte.game.content.treasuretrails.npcs.drops;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Kris | 22/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum HardClueNPC implements ClueNPC {
    SKOTIZO(1), VITREOUS_WARPED_JELLY(6.4), CAVE_ABOMINATION(12.8), GREATER_ABYSSAL_DEMON(12.8), INSATIABLE_MUTATED_BLOODVELD(12.8), KING_KURASK(12.8), MARBLE_GARGOYLE(12.8), NECHRYARCH(12.8), NIGHT_BEAST(12.8), NUCLEAR_SMOKE_DEVIL(12.8), REPUGNANT_SPECTRE(12.8), SCREAMING_TWISTED_BANSHEE(12.8), VITREOUS_JELLY(12.8), INSATIABLE_BLOODVELD(25.6), SARACHNIS(40), DAGANNOTH_PRIME(42), DAGANNOTH_REX(42), DAGANNOTH_SUPREME(42), BASILISK_SENTINEL(40), HELLHOUND(64), STEEL_DRAGON(64), WARPED_JELLY(64), GANG_BOSS(65), GANGSTER(65), THERMONUCLEAR_SMOKE_DEVIL(96), ALCHEMICAL_HYDRA(100), CAVE_KRAKEN(100), DEMONIC_GORILLA(100), SCORPIA(100), SKELETON(100, npc -> npc.getCombatLevel() == 132), UNDEAD_DRUID(100), ZOMBIE(100, npc -> npc.getCombatLevel() == 132), LONG_TAILED_WYVERN("Long-tailed wyvern", 118), SPITTING_WYVERN(118), TALONED_WYVERN(118), ABERRANT_SPECTRE(128), ABYSSAL_DEMON(128), AVIANSIE(128), BALFRUG_KREEYATH(128), BANDIT(128, npc -> npc.getCombatLevel() == 130), BLACK_DEMON(128), BLACK_DRAGON(128), BLUE_DRAGON(128), BREE(128), BRONZE_DRAGON(128), BRUTAL_BLACK_DRAGON(128), BRUTAL_BLUE_DRAGON(128), BRUTAL_GREEN_DRAGON(128), BRUTAL_RED_DRAGON(128), CAVE_HORROR(128), CHAOS_FANATIC(128), CRAZY_ARCHAEOLOGIST(128), DARK_BEAST(128), DARK_WARRIOR(128, npc -> npc.getCombatLevel() == 145), DEVIANT_SPECTRE(128), DRAKE(128), ELDER_CHAOS_DRUID(128), ELF_ARCHER(128), ELF_WARRIOR(128), EXPERIMENT_NO_TWO("Experiment no.2", 128), FLIGHT_KILISA(128), FLOCKLEADER_GEERIN(128), GARGOYLE(128), GORAK(128), GREATER_NECHRYAEL(128), GREATER_DEMON(128), GREEN_DRAGON(128), GROWLER(128), GUARD(128, npc -> npc.getCombatLevel() == 108), HYDRA(128), ICE_GIANT(128, npc -> npc.getCombatLevel() == 67), IORWERTH_ARCHER(128), IORWERTH_WARRIOR(128), IRON_DRAGON(128), JELLY(128), KNIGHT_OF_SARADOMIN(128), KURASK(128), MUTATED_BLOODVELD(128), NECHRYAEL(128), ORK(128), RED_DRAGON(128), SARADOMIN_PRIEST(128), SERGEANT_GRIMSPIKE(128), SERGEANT_STEELWILL(128), SERGEANT_STRONGSTACK(128), SMOKE_DEVIL(128), SPIRITUAL_MAGE(128), SPIRITUAL_RANGER(128), SPIRITUAL_WARRIOR(128), STARLIGHT(128), TERROR_DOG(128), TSTANON_KARLAK(128), TUROTH(128), TWISTED_BANSHEE(128), VYREWATCH(128), WATERFIEND(128), WINGMAN_SKREE(128), ZAKL_N_GRITCH("Zakl'n Gritch", 128), SUQAH(129), LIZARDMAN_SHAMAN(200), MOSS_GIANT(200, npc -> npc.getCombatLevel() == 84), TEMPLE_SPIDER(200), ANKOU(256, npc -> npc.getCombatLevel() == 98), BASILISK_KNIGHT(256), BLOODVELD(256), CYCLOPS(256, npc -> npc.getCombatLevel() == 106), TYRAS_GUARD(256), WYRM(256), YURI(256), TORTURED_GORILLA(300), ANKOU_(512, npc -> npc.getCombatLevel() >= 75 && npc.getCombatLevel() <= 98), CYCLOPS_(512, npc -> npc.getCombatLevel() == 56 || npc.getCombatLevel() == 76 || npc.getCombatLevel() == 81);

    HardClueNPC(final String name, final double rate) {
        this(rate, name.toLowerCase().replace("_", " ").trim(), null);
    }

    HardClueNPC(final double rate) {
        this(rate, null);
    }

    HardClueNPC(final double rate, final Predicate<NPCDefinitions> predicate) {
        this(rate, null, predicate);
    }

    HardClueNPC(final double rate, final String name, final Predicate<NPCDefinitions> predicate) {
        this.rate = rate;
        this.monsterName = name == null ? name().toLowerCase().replace("_", " ").trim() : name;
        this.predicate = predicate;
    }

    public static final Map<String, List<PredicatedClueDrop>> mappedNPCs = new Object2ObjectOpenHashMap<>();

    static {
        for (final HardClueNPC value : values()) {
            final List<PredicatedClueDrop> list = mappedNPCs.computeIfAbsent(value.monsterName, __ -> new LinkedList<>());
            list.add(new PredicatedClueDrop(value.rate, value.predicate));
        }
    }

    private final String monsterName;
    private final double rate;
    private final Predicate<NPCDefinitions> predicate;

    @Override
    public boolean accept(@NotNull NPCDefinitions definitions) {
        return predicate == null || predicate.test(definitions);
    }

    HardClueNPC(String monsterName, double rate, Predicate<NPCDefinitions> predicate) {
        this.monsterName = monsterName;
        this.rate = rate;
        this.predicate = predicate;
    }

    public String monsterName() {
        return monsterName;
    }

    public double rate() {
        return rate;
    }

    public Predicate<NPCDefinitions> predicate() {
        return predicate;
    }
}
