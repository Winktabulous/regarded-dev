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
public enum EasyClueNPC implements ClueNPC {
    SCREAMING_BANSHEE(12.8), HAM_MEMBER("H.A.M. Member", 50), HAM_GUARD("H.A.M. Guard", 55), GOBLIN_GUARD(50), SWAMP_CRAB(96), MINOTAUR(101, npc -> npc.getCombatLevel() == 12 || npc.getCombatLevel() == 27), AFFLICTED(128), AL_KHARID_WARRIOR("Al-kharid warrior", 128), AMMONITE_CRAB(128), ANJA(128), BANSHEE(128), BARBARIAN(128), BREOCA(128), CAVE_BUG(128), CAVE_SLIME(128), CEOLBURG(128), CUFFS(128), DAGANNOTH_SPAWN(128), FARMER(128), FREIDIR(128), GOBLIN(128), GUNTHOR_THE_BRAVE(128), HENGEL(128), HYGD(128), ICEFIEND(128), JEFF(128), LOBSTROSITY(128), MAN(128), MOLANISK(128), MUGGER(128), NARF(128), OCGA(128), ORK(128), PENDA(128), ROCK_CRAB(128), ROGUE(128), RUSTY(128), SAND_CRAB(128), SKELETON(128, npc -> npc.getCombatLevel() >= 21 && npc.getCombatLevel() <= 85 && !(npc.getCombatLevel() == 45 || npc.getCombatLevel() == 72 || npc.getCombatLevel() == 77 || npc.getCombatLevel() == 87)), THIEF(128), THUG(128), WEREWOLF(128), WOMAN(128), BORROKAR(128), CAVE_GOBLIN(128), SKOGRE(128), ZOGRE(128), LANZIG(256);

    EasyClueNPC(final String name, final double rate) {
        this(rate, name.toLowerCase().replace("_", " ").trim(), null);
    }

    EasyClueNPC(final double rate) {
        this(rate, null);
    }

    EasyClueNPC(final double rate, final Predicate<NPCDefinitions> predicate) {
        this(rate, null, predicate);
    }

    EasyClueNPC(final double rate, final String name, final Predicate<NPCDefinitions> predicate) {
        this.rate = rate;
        this.monsterName = name == null ? name().toLowerCase().replace("_", " ").trim() : name;
        this.predicate = predicate;
    }

    public static final Map<String, List<PredicatedClueDrop>> mappedNPCs = new Object2ObjectOpenHashMap<>();

    static {
        for (final EasyClueNPC value : values()) {
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

    EasyClueNPC(String monsterName, double rate, Predicate<NPCDefinitions> predicate) {
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
