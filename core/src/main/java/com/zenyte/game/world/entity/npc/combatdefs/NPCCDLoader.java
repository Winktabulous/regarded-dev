package com.zenyte.game.world.entity.npc.combatdefs;

import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.entity.masks.Animation;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.AnimationDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kris | 18/11/2018 02:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NPCCDLoader {
    private static final Logger log = LoggerFactory.getLogger(NPCCDLoader.class);
    public static Int2ObjectMap<NPCCombatDefinitions> definitions = new Int2ObjectOpenHashMap<>();

    public static void parse() {
        try {
            Path path = Path.of("cache", "data", "npcs", "combat");
            try (Stream<Path> stream = Files.walk(path, 1)) {
                List<NPCCombatDefinitions> defs = stream.filter(file -> !Files.isDirectory(file))
                        .parallel()
                        .map(file -> {
                            NPCCombatDefinitions def;
                            try (BufferedReader br = Files.newBufferedReader(file)) {
                                def = DefaultGson.getGson().fromJson(br, NPCCombatDefinitions.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return def;
                        })
                        .collect(Collectors.toList());
                defs.forEach(def -> insert(def.getId(), def));
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void save() {
        final ArrayList<NPCCombatDefinitions> defs = new ArrayList<>(definitions.values());
        defs.sort(Comparator.comparingInt(NPCCombatDefinitions::getId));
        final String toJson = DefaultGson.getGson().toJson(defs);
        try {
            final PrintWriter pw = new PrintWriter("cache/data/npcs/combatDefs.json", StandardCharsets.UTF_8);
            pw.println(toJson);
            pw.close();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static NPCCombatDefinitions get(final int id) {
        return definitions.get(id);
    }

    public static final Int2ObjectMap<AttackDefinitions> skeletonToAttackDefs = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectMap<BlockDefinitions> skeletonToBlockDefs = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectMap<SpawnDefinitions> skeletonToSpawnDefs = new Int2ObjectOpenHashMap<>();

    public static void insert(int id, NPCCombatDefinitions def) {
        definitions.put(id, def);

        int skeleton = -1;
        AttackDefinitions attackDef = def.getAttackDefinitions();
        if (attackDef != null) {
            Animation animation = attackDef.getAnimation();
            if (animation != null && animation != Animation.STOP) {
                skeleton = AnimationDefinitions.getSkeletonId(animation.getId());
                if (skeleton >= 0) skeletonToAttackDefs.putIfAbsent(skeleton, AttackDefinitions.construct(attackDef));
            }
        }

        BlockDefinitions blockDef = def.getBlockDefinitions();
        if (blockDef != null) {
            Animation animation = blockDef.getAnimation();
            if (animation != null && animation != Animation.STOP) {
                if (skeleton < 0) skeleton = AnimationDefinitions.getSkeletonId(animation.getId());
                if (skeleton >= 0) skeletonToBlockDefs.putIfAbsent(skeleton, BlockDefinitions.construct(blockDef));
            }
        }

        SpawnDefinitions spawnDef = def.getSpawnDefinitions();
        if (spawnDef != null) {
            Animation deathAnimation = spawnDef.getDeathAnimation();
            if (deathAnimation != null && deathAnimation != Animation.STOP) {
                if (skeleton < 0) skeleton = AnimationDefinitions.getSkeletonId(deathAnimation.getId());
                if (skeleton >= 0) {
                    skeletonToSpawnDefs.putIfAbsent(skeleton, SpawnDefinitions.construct(spawnDef));
                    return;
                }
            }

            Animation spawnAnimation = spawnDef.getSpawnAnimation();
            if (spawnAnimation != null && spawnAnimation != Animation.STOP) {
                if (skeleton < 0) skeleton = AnimationDefinitions.getSkeletonId(spawnAnimation.getId());
                if (skeleton >= 0) {
                    skeletonToSpawnDefs.putIfAbsent(skeleton, SpawnDefinitions.construct(spawnDef));
                }
            }
        }
    }

}
