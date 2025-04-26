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
public enum MediumClueNPC implements ClueNPC {
    COCKATHRICE(12.8), FLAMING_PYRELORD(12.8), CATABLEPON(101), ANCIENT_ZYGOMITE(128), ANGRY_BARBARIAN_SPIRIT(128), BERSERK_BARBARIAN_SPIRIT(128), BLACK_GUARD(128), BLACK_GUARD_BERSERKER(128), BLACK_HEATHER(128), BRINE_RAT(128), COCKATRICE(128), DAGANNOTH(128), DONNY_THE_LAD(128), ENRAGED_BARBARIAN_SPIRIT(128), FERAL_VAMPYRE(128), FORTRESS_GUARD(128), GIANT_ROCK_CRAB(128), GIANT_SEA_SNAKE(128), GIANT_SKELETON(128), GUARD(128, npc -> npc.getCombatLevel() >= 19 && npc.getCombatLevel() <= 22), HARPIE_BUG_SWARM(128), ICE_WARRIOR(128), JUNGLE_HORROR(128), KOUREND_GUARD(128), KOUREND_HEAD_GUARD(128), MAMMOTH(128), PALADIN(128), PYREFIEND(128), ROCK_LOBSTER(128), SEA_SNAKE_HATCHLING(128), SEA_SNAKE_YOUNG(128), SIR_CARL(128), SIR_HARRY(128), SIR_JERRO(128), SKELETAL_MINER(128), SKELETON(128, npc -> npc.getCombatLevel() == 45 || npc.getCombatLevel() == 72 || npc.getCombatLevel() == 77 || npc.getCombatLevel() == 87), SKELETON_MAGE(128), SKELETON_BRUTE(128), SKELETON_HEAVY(128), SKELETON_HERO(128), SKELETON_THUG(128), SKELETON_WARLORD(128), SPEEDY_KEITH(128), SULPHUR_LIZARD(128), TOWN_GUARD(128, npc -> npc.getCombatLevel() == 19 || npc.getCombatLevel() == 22), WALLASALKI(128), JOGRE(129), TRIBESMAN(138), ABYSSAL_GUARDIAN(256), ABYSSAL_LEECH(256), ABYSSAL_WALKER(256), MAGIC_AXE(256), MARKET_GUARD(256), POSSESSED_PICKAXE(256), VAMPYRE_JUVINATE(128), WALL_BEAST(128), WARRIOR(128, npc -> npc.getCombatLevel() == 48), WEREWOLF(512), MUMMY(513), OGRE(513), KING_SAND_CRAB(1024);

    MediumClueNPC(final String name, final double rate) {
        this(rate, name.toLowerCase().replace("_", " ").trim(), null);
    }

    MediumClueNPC(final double rate) {
        this(rate, null);
    }

    MediumClueNPC(final double rate, final Predicate<NPCDefinitions> predicate) {
        this(rate, null, predicate);
    }

    MediumClueNPC(final double rate, final String name, final Predicate<NPCDefinitions> predicate) {
        this.rate = rate;
        this.monsterName = name == null ? name().toLowerCase().replace("_", " ").trim() : name;
        this.predicate = predicate;
    }

    public static final Map<String, List<PredicatedClueDrop>> mappedNPCs = new Object2ObjectOpenHashMap<>();

    static {
        for (final MediumClueNPC value : values()) {
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

    MediumClueNPC(String monsterName, double rate, Predicate<NPCDefinitions> predicate) {
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
