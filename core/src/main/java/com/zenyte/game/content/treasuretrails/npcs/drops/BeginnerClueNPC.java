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
public enum BeginnerClueNPC implements ClueNPC {
    BRYOPHYTA(1), OBOR(1), GUNTHOR_THE_BRAVE(30), DARK_WIZARD(35, npc -> npc.getCombatLevel() >= 20), ICE_GIANT(40, npc -> npc.getCombatLevel() == 53), MOSS_GIANT(45), CYCLOPS(50, npc -> npc.getCombatLevel() == 56), DARK_WIZARD_(50, npc -> npc.getCombatLevel() < 20), HILL_GIANT(50), BARBARIAN(55, npc -> npc.getCombatLevel() >= 15), MINOTAUR(60), GIANT_FROG(64), GOBLIN(64, npc -> npc.getCombatLevel() >= 15), HOBGOBLIN(70), BARBARIAN_(75, npc -> npc.getCombatLevel() < 15), GOBLIN_(80, npc -> npc.getCombatLevel() < 15), MUGGER(80), AFFLICTED(90), AL_KHARID_WARRIOR("Al-kharid warrior", 90), ANJA(90), BEAR_CUB(90), BLACK_BEAR(90), BREOCA(90), CEOLBURG(90), CUFFS(90), FARMER(90), GHOST(90), GRIZZLY_BEAR(90), GRIZZLY_BEAR_CUB(90), HENGEL(90), HYGD(90), JEFF(90), MAN(90), NARF(90), OCGA(90), PENDA(90), ROGUE(90), RUSTY(90), THIEF(90), WOMAN(90), DWARF(100), GRAVE_SCORPION(100), KING_SCORPION(100), PIT_SCORPION(100), POISON_SCORPION(100), SCORPIAS_OFFSPRING("Scorpia's offspring", 100), SCORPION(100), SKELETAL_MINER(100), SKELETON(100), COW(128), COW_CALF(128), DEADLY_RED_SPIDER(128), DUNGEON_RAT(128), GIANT_RAT(128), GIANT_SPIDER(128), ICE_SPIDER(128), SHADOW_SPIDER(128), SPIDER(128), CAVE_GOBLIN(128), DEATH_WING(128), WOLF(128), CHICKEN(300);

    BeginnerClueNPC(final String name, final double rate) {
        this(rate, name.toLowerCase().replace("_", " ").trim(), null);
    }

    BeginnerClueNPC(final double rate) {
        this(rate, null);
    }

    BeginnerClueNPC(final double rate, final Predicate<NPCDefinitions> predicate) {
        this(rate, null, predicate);
    }

    BeginnerClueNPC(final double rate, final String name, final Predicate<NPCDefinitions> predicate) {
        this.rate = rate;
        this.monsterName = name == null ? name().toLowerCase().replace("_", " ").trim() : name;
        this.predicate = predicate;
    }

    public static final Map<String, List<PredicatedClueDrop>> mappedNPCs = new Object2ObjectOpenHashMap<>();

    static {
        for (final BeginnerClueNPC value : values()) {
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

    BeginnerClueNPC(String monsterName, double rate, Predicate<NPCDefinitions> predicate) {
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
