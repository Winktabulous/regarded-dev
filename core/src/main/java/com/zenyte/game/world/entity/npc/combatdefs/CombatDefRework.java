package com.zenyte.game.world.entity.npc.combatdefs;

import com.zenyte.game.parser.impl.NPCCombatDefinitionsLoader;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.entity.AnimationMap;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.OldNPCCombatDefinitions;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.Indice;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Kris | 05/11/2018 18:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CombatDefRework {
    private static final Logger log = LoggerFactory.getLogger(CombatDefRework.class);
    private static final TreeMap<Integer, MonsterCombatDefinition> updatedMap = new TreeMap<>();
    /**
     * Everything below is used for OSRS definitions.
     */
    private static final Map<String, MonsterExamineDefinition> receivedMap = new HashMap<>();
    private static final Map<String, Int2ObjectOpenHashMap<OSRSDef>> map = new HashMap<>();
    private static Map<String, Double> healthDefinitions;

    public static void launch() {
        load();
        populate();
        NPCCombatDefinitionsLoader.loadCombatDefinitions();
        NPCCombatDefinitionsLoader.DEFINITIONS.forEach((id, def) -> {
            final OldNPCCombatDefinitions d = new OldNPCCombatDefinitions(def);
            d.setBonuses();
            final MonsterCombatDefinition defs = new MonsterCombatDefinition();
            defs.construct(d);
            updatedMap.put(id, defs);
        });
        map.forEach((name, cbmap) -> {
            cbmap.forEach((level, osrsdef) -> {
                for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
                    final NPCDefinitions npcDefinitions = NPCDefinitions.get(i);
                    if (npcDefinitions == null || !npcDefinitions.getName().equals(name) || level != npcDefinitions.getCombatLevel()) continue;
                    MonsterCombatDefinition monsterDef = updatedMap.get(i);
                    if (monsterDef == null) {
                        monsterDef = new MonsterCombatDefinition();
                        updatedMap.put(i, monsterDef);
                    }
                    monsterDef.append(osrsdef);
                }
            });
        });
        healthDefinitions.forEach((name_cb, hp) -> {
            final String[] split = name_cb.split("_");
            final String name = split[0];
            final int level = Integer.parseInt(split[1]);
            for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
                final NPCDefinitions npcDefinitions = NPCDefinitions.get(i);
                if (npcDefinitions == null || !npcDefinitions.getName().equals(name) || level != npcDefinitions.getCombatLevel()) continue;
                final MonsterCombatDefinition defs = updatedMap.get(i);
                if (defs == null) continue;
                defs.setHitpoints(hp.intValue());
            }
        });
        //Filter the unnecessary details.
        updatedMap.forEach((id, def) -> {
            //TODO: Add a way to reset animations if they do not suit the skeleton of the npc.
            if (def.getAttackDefinitions() != null) {
                if (def.getAttackDefinitions().getAnimation() != null && def.getAttackDefinitions().getAnimation().getId() == -1) {
                    def.getAttackDefinitions().setAnimation(null);
                }
                if (def.getAttackDefinitions().getAnimation() != null) {
                    if (!AnimationMap.isValidAnimation(id, def.getAttackDefinitions().getAnimation().getId())) {
                        def.getAttackDefinitions().setAnimation(null);
                    }
                }
            }
            if (def.getBlockDefinitions() != null) {
                if (def.getBlockDefinitions().getAnimation() != null && def.getBlockDefinitions().getAnimation().getId() == -1) {
                    def.getBlockDefinitions().setAnimation(null);
                }
                if (def.getBlockDefinitions().getAnimation() != null) {
                    if (!AnimationMap.isValidAnimation(id, def.getBlockDefinitions().getAnimation().getId())) {
                        def.getBlockDefinitions().setAnimation(null);
                    }
                }
            }
            if (def.getSpawnDefinitions() == null) {
                final MonsterCombatDefinition.SpawnDefinitions spawnDefs = new MonsterCombatDefinition.SpawnDefinitions();
                spawnDefs.setRespawnDelay(25);
                spawnDefs.setDeathAnimation(Animation.STOP);
                def.setSpawnDefinitions(spawnDefs);
            }
            if (def.getSpawnDefinitions().getDeathAnimation() != null && def.getSpawnDefinitions().getDeathAnimation().getId() == -1) {
                def.getSpawnDefinitions().setDeathAnimation(null);
            }
            if (def.getSpawnDefinitions().getDeathAnimation() != null) {
                if (!AnimationMap.isValidAnimation(id, def.getSpawnDefinitions().getDeathAnimation().getId())) {
                    def.getSpawnDefinitions().setDeathAnimation(null);
                }
            }
            if (def.getSpawnDefinitions().getSpawnAnimation() != null && def.getSpawnDefinitions().getSpawnAnimation().getId() == -1) {
                def.getSpawnDefinitions().setSpawnAnimation(null);
            }
            if (def.getSpawnDefinitions().getSpawnAnimation() != null) {
                if (!AnimationMap.isValidAnimation(id, def.getSpawnDefinitions().getSpawnAnimation().getId())) {
                    def.getSpawnDefinitions().setSpawnAnimation(null);
                }
            }
        });
        final String toJson = DefaultGson.getGson().toJson(updatedMap.values());
        try {
            final PrintWriter pw = new PrintWriter("out.json", StandardCharsets.UTF_8);
            pw.println(toJson);
            pw.close();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("receivedDefinitions.json"));
            MonsterExamineDefinition[] definitions = DefaultGson.getGson().fromJson(reader, MonsterExamineDefinition[].class);
            for (final MonsterExamineDefinition def : definitions) {
                receivedMap.put(def.getName(), def);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        try {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("npc_health.json"));
                healthDefinitions = DefaultGson.getGson().fromJson(reader, Map.class);
            } catch (Exception e) {
                log.error("", e);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void populate() {
        receivedMap.forEach((name, def) -> {
            def.getDefinitions().forEach((level, statistics) -> {
                final CombatDefRework.OSRSDef defs = new OSRSDef();
                defs.set(statistics);
                Int2ObjectOpenHashMap<CombatDefRework.OSRSDef> defMap = map.get(name);
                if (defMap == null) {
                    defMap = new Int2ObjectOpenHashMap<>();
                    map.put(name, defMap);
                }
                defMap.put((int) level, defs);
            });
        });
    }

    public void fix() {
        load();
        populate();
        map.forEach((name, cbmap) -> {
            cbmap.forEach((level, osrsdef) -> {
                for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
                    final NPCDefinitions npcDefinitions = NPCDefinitions.get(i);
                    if (npcDefinitions == null || !npcDefinitions.getName().equals(name) || level != npcDefinitions.getCombatLevel()) continue;
                    final NPCCombatDefinitions d = NPCCDLoader.get(i);
                    if (d != null) {
                        final int ranged = d.getStatDefinitions().get(StatType.MAGIC);
                        final int magic = d.getStatDefinitions().get(StatType.RANGED);
                        d.getStatDefinitions().set(StatType.RANGED, ranged);
                        d.getStatDefinitions().set(StatType.MAGIC, magic);
                    }
                }
            });
        });
        NPCCDLoader.save();
    }


    static final class OSRSDef {
        private int combatLevel;
        private int hitpoints;
        private MonsterCombatDefinition.StatDefinitions statDefinitions;
        private int maxHit;
        private int attackSpeed;
        private MonsterCombatDefinition.AttackType style;
        private EnumSet<MonsterCombatDefinition.ImmunityType> immunityTypes;
        private EnumSet<MonsterCombatDefinition.WeaknessType> weaknessTypes;

        private void set(final String statistics) {
            final String escapedString = statistics.replaceAll("<col=ffb000>", "").replaceAll("</col>", "").replaceAll(" : ", ": ");
            final String[] tabs = escapedString.split("[|]");
            this.statDefinitions = new MonsterCombatDefinition.StatDefinitions();
            setStats(tabs[0]);
            setAggressiveStats(tabs[1]);
            setDefensiveStats(tabs[2]);
            setOtherAttributes(tabs[3]);
        }

        private void setOtherAttributes(final String attributes) {
            final String[] splitStats = attributes.split("<br>");
            for (final String split : splitStats) {
                if (split.equals("Other Attributes")) continue;
                switch (split) {
                case "Immune to venom.": 
                    if (immunityTypes == null) {
                        immunityTypes = EnumSet.noneOf(MonsterCombatDefinition.ImmunityType.class);
                    }
                    immunityTypes.add(MonsterCombatDefinition.ImmunityType.VENOM);
                    break;
                case "Immune to poison.": 
                    if (immunityTypes == null) {
                        immunityTypes = EnumSet.noneOf(MonsterCombatDefinition.ImmunityType.class);
                    }
                    immunityTypes.add(MonsterCombatDefinition.ImmunityType.POISON);
                    break;
                case "Is vulnerable to demonbane weapons.": 
                    if (weaknessTypes == null) {
                        weaknessTypes = EnumSet.noneOf(MonsterCombatDefinition.WeaknessType.class);
                    }
                    weaknessTypes.add(MonsterCombatDefinition.WeaknessType.DEMONBANE_WEAPONS);
                    break;
                case "Is vulnerable to dragonbane weapons.": 
                    if (weaknessTypes == null) {
                        weaknessTypes = EnumSet.noneOf(MonsterCombatDefinition.WeaknessType.class);
                    }
                    weaknessTypes.add(MonsterCombatDefinition.WeaknessType.DRAGONBANE_WEAPONS);
                    break;
                }
            }
        }

        private void setDefensiveStats(final String stats) {
            final String[] splitStats = stats.split("<br>");
            for (final String split : splitStats) {
                if (split.equals("Defensive Stats")) continue;
                if (split.startsWith("Stab: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.DEFENCE_STAB, Integer.valueOf(split.replace("Stab: ", "")));
                } else if (split.startsWith("Slash: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.DEFENCE_SLASH, Integer.valueOf(split.replace("Slash: ", "")));
                } else if (split.startsWith("Crush: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.DEFENCE_CRUSH, Integer.valueOf(split.replace("Crush: ", "")));
                } else if (split.startsWith("Magic: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.DEFENCE_MAGIC, Integer.valueOf(split.replace("Magic: ", "")));
                } else if (split.startsWith("Ranged: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.DEFENCE_RANGED, Integer.valueOf(split.replace("Ranged: ", "")));
                } else {
                    throw new RuntimeException("Unhandled defensive stat type: " + split);
                }
            }
        }

        private void setAggressiveStats(final String stats) {
            final String[] splitStats = stats.split("<br>");
            for (final String split : splitStats) {
                if (split.equals("Aggressive Stats")) continue;
                if (split.startsWith("Attack speed: ")) {
                    this.attackSpeed = Integer.valueOf(split.replace("Attack speed: ", ""));
                } else if (split.startsWith("Attack bonus: ")) {
                    final int bonus = Integer.parseInt(split.replace("Attack bonus: ", ""));
                    switch (style) {
                    case STAB: 
                        statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_STAB, bonus);
                        break;
                    case SLASH: 
                        statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_SLASH, bonus);
                        break;
                    case CRUSH: 
                        statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_CRUSH, bonus);
                        break;
                    default: 
                        throw new RuntimeException("Unhandled attack bonus type: " + split);
                    }
                } else if (split.startsWith("Stab bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_STAB, Integer.valueOf(split.replace("Stab bonus: ", "")));
                } else if (split.startsWith("Slash bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_SLASH, Integer.valueOf(split.replace("Slash bonus: ", "")));
                } else if (split.startsWith("Crush bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_CRUSH, Integer.valueOf(split.replace("Crush bonus: ", "")));
                } else if (split.startsWith("Magic bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_MAGIC, Integer.valueOf(split.replace("Magic bonus: ", "")));
                } else if (split.startsWith("Ranged bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK_RANGED, Integer.valueOf(split.replace("Ranged bonus: ", "")));
                } else if (split.startsWith("Strength bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.MELEE_STRENGTH_BONUS, Integer.valueOf(split.replace("Strength bonus: ", "")));
                } else if (split.startsWith("Range str bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.RANGED_STRENGTH_BONUS, Integer.valueOf(split.replace("Range str bonus: ", "")));
                } else if (split.startsWith("Magic str bonus: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.MAGIC_STRENGTH_BONUS, Integer.valueOf(split.replace("Magic str bonus: ", "")));
                } else {
                    throw new RuntimeException("Unhandled aggressive type: " + split);
                }
            }
        }

        private void setStats(final String stats) {
            final String[] splitStats = stats.split("<br>");
            for (final String split : splitStats) {
                if (split.equals("Stats")) continue;
                if (split.startsWith("Combat level: ")) {
                    this.combatLevel = Integer.valueOf(split.replace("Combat level: ", ""));
                } else if (split.startsWith("Hitpoints: ")) {
                    this.hitpoints = Integer.valueOf(split.replace("Hitpoints: ", ""));
                } else if (split.startsWith("Attack: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.ATTACK, Integer.valueOf(split.replace("Attack: ", "")));
                } else if (split.startsWith("Defence: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.DEFENCE, Integer.valueOf(split.replace("Defence: ", "")));
                } else if (split.startsWith("Strength: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.STRENGTH, Integer.valueOf(split.replace("Strength: ", "")));
                } else if (split.startsWith("Magic: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.MAGIC, Integer.valueOf(split.replace("Magic: ", "")));
                } else if (split.startsWith("Ranged: ")) {
                    statDefinitions.set(MonsterCombatDefinition.StatType.RANGED, Integer.valueOf(split.replace("Ranged: ", "")));
                } else if (split.startsWith("Max standard hit: ")) {
                    this.maxHit = Integer.valueOf(split.replace("Max standard hit: ", ""));
                } else if (split.startsWith("Main attack style: ")) {
                    final String value = split.replace("Main attack style: ", "");
                    switch (value) {
                    case "Crush": 
                        style = MonsterCombatDefinition.AttackType.CRUSH;
                        break;
                    case "Stab": 
                        style = MonsterCombatDefinition.AttackType.STAB;
                        break;
                    case "Slash": 
                        style = MonsterCombatDefinition.AttackType.SLASH;
                        break;
                    case "Magic": 
                        style = MonsterCombatDefinition.AttackType.MAGIC;
                        break;
                    case "Range": 
                        style = MonsterCombatDefinition.AttackType.RANGED;
                        break;
                    default: 
                        throw new RuntimeException("Unknown style: " + value);
                    }
                } else {
                    throw new RuntimeException("Unhandled value: " + split);
                }
            }
        }

        public int getCombatLevel() {
            return combatLevel;
        }

        public int getHitpoints() {
            return hitpoints;
        }

        public MonsterCombatDefinition.StatDefinitions getStatDefinitions() {
            return statDefinitions;
        }

        public int getMaxHit() {
            return maxHit;
        }

        public int getAttackSpeed() {
            return attackSpeed;
        }

        public MonsterCombatDefinition.AttackType getStyle() {
            return style;
        }

        public EnumSet<MonsterCombatDefinition.ImmunityType> getImmunityTypes() {
            return immunityTypes;
        }

        public EnumSet<MonsterCombatDefinition.WeaknessType> getWeaknessTypes() {
            return weaknessTypes;
        }
    }
}
