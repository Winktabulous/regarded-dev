package com.zenyte.game.world.entity.npc.combatdefs;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;
import org.apache.commons.lang3.ArrayUtils;

import java.util.EnumSet;

/**
 * @author Kris | 05/11/2018 01:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NPCCombatDefinitions {
    private int id;
    private int hitpoints = 1;
    private int attackSpeed;
    private int slayerLevel;
    private int attackDistance;
    private int aggressionDistance;
    private int maximumDistance;
    private Entity.EntityType targetType = Entity.EntityType.PLAYER;
    private EnumSet<ImmunityType> immunityTypes;
    private AggressionType aggressionType;
    private MonsterType monsterType;
    private EnumSet<WeaknessType> weaknesses;
    private ToxinDefinitions toxinDefinitions;
    private StatDefinitions statDefinitions;
    private AttackDefinitions attackDefinitions;
    private BlockDefinitions blockDefinitions;
    private SpawnDefinitions spawnDefinitions;

    public static NPCCombatDefinitions clone(final int id, final NPCCombatDefinitions other) {
        final NPCCombatDefinitions defs = new NPCCombatDefinitions();
        if (other != null) {
            defs.id = other.id;
            defs.hitpoints = other.hitpoints;
            defs.attackSpeed = other.attackSpeed;
            defs.slayerLevel = other.slayerLevel;
            defs.attackDistance = other.attackDistance;
            defs.aggressionDistance = other.aggressionDistance;
            defs.maximumDistance = other.maximumDistance;
            defs.targetType = other.targetType;
            defs.immunityTypes = other.immunityTypes == null ? null : EnumSet.copyOf(other.immunityTypes);
            defs.aggressionType = other.aggressionType;
            defs.monsterType = other.monsterType;
            defs.weaknesses = other.weaknesses == null ? null : EnumSet.copyOf(other.weaknesses);
            defs.toxinDefinitions = other.toxinDefinitions == null ? null : other.toxinDefinitions.clone();
            defs.statDefinitions = other.statDefinitions == null ? new StatDefinitions() : other.statDefinitions.clone();
            updateBaseDefinitions(defs, other);
        } else {
            defs.id = id;
            defs.statDefinitions = new StatDefinitions();
            updateBaseDefinitions(defs, null);
        }
        return defs;
    }

    public static final void updateBaseDefinitions(final NPCCombatDefinitions defs, final NPCCombatDefinitions other) {
        defs.attackDefinitions = AttackDefinitions.construct(other == null ? null : other.attackDefinitions);
        defs.blockDefinitions = BlockDefinitions.construct(other == null ? null : other.blockDefinitions);
        defs.spawnDefinitions = SpawnDefinitions.construct(other == null ? null : other.spawnDefinitions);
    }

    public boolean isMelee() {
        return attackDefinitions.getType().isMelee();
    }

    public boolean isMagic() {
        return attackDefinitions.getType().isMagic();
    }

    public boolean isRanged() {
        return attackDefinitions.getType().isRanged();
    }

    public boolean isAggressive() {
        return aggressionType == AggressionType.AGGRESSIVE || aggressionType == AggressionType.ALWAYS_AGGRESSIVE;
    }

    public boolean isAlwaysAggressive() {
        return aggressionType == AggressionType.ALWAYS_AGGRESSIVE;
    }

    public int getMaxHit() {
        return attackDefinitions.getMaxHit();
    }

    public boolean isVenomImmune() {
        return immunityTypes != null && immunityTypes.contains(ImmunityType.VENOM);
    }

    public boolean isPoisonImmune() {
        return immunityTypes != null && immunityTypes.contains(ImmunityType.POISON);
    }

    public AttackType getAttackType() {
        return attackDefinitions.getType();
    }

    public boolean isUndead() {
        return MonsterType.UNDEAD.equals(monsterType);
    }

    public boolean containsWeakness(final WeaknessType type) {
        return weaknesses != null && weaknesses.contains(type);
    }

    public void setAttackStyle(final String type) {
        switch (type) {
        case "Melee": 
            attackDefinitions.setType(attackDefinitions.getDefaultMeleeType());
            return;
        case "Ranged": 
            attackDefinitions.setType(AttackType.RANGED);
            return;
        case "Magic": 
            attackDefinitions.setType(AttackType.MAGIC);
            return;
        case "Stab": 
            attackDefinitions.setType(AttackType.STAB);
            return;
        case "Slash": 
            attackDefinitions.setType(AttackType.SLASH);
            return;
        case "Crush": 
            attackDefinitions.setType(AttackType.CRUSH);
            return;
        default: 
            throw new RuntimeException("Unable to find type for value: " + type);
        }
    }

    public StatType getAttackStatType() {
        switch (attackDefinitions.getType()) {
        case STAB: 
            return StatType.ATTACK_STAB;
        case SLASH: 
            return StatType.ATTACK_SLASH;
        case CRUSH:
        case MELEE:
            return StatType.ATTACK_CRUSH;
        case RANGED: 
            return StatType.ATTACK_RANGED;
        case MAGIC: 
            return StatType.ATTACK_MAGIC;
        }
        throw new IllegalArgumentException();
    }

    public void setAttackStyle(AttackType type) {
        if (type == AttackType.MELEE) {
            final NPCCombatDefinitions defaultDefinitions = NPCCDLoader.get(getId());
            if (defaultDefinitions != null && defaultDefinitions.getAttackStyle().isMelee()) {
                type = defaultDefinitions.getAttackStyle();
            } else type = AttackType.CRUSH;
        }
        if (attackDefinitions == null)
            attackDefinitions = new AttackDefinitions();
        attackDefinitions.setType(type);
    }

    public Animation getAttackAnim() {
        return attackDefinitions.getAnimation();
    }

    public Animation getDeathAnim() {
        return spawnDefinitions.getDeathAnimation();
    }

    public Animation getBlockAnim() {
        return blockDefinitions.getAnimation();
    }

    public AttackType getAttackStyle() {
        return attackDefinitions.getType();
    }

    /**
     * Drains the specified skill for a given percentage and returns the amount it drained for.
     * @param skill the skill to drain.
     * @param percentage the percentage to drain the skill for.
     * @return the amount of levels that was successfully drained.
     */
    public final int drainSkill(final int skill, final double percentage, final int minimumDrain) {
        final int index = getIndex(skill);
        if (index == -1) return 0;
        final int[] stats = statDefinitions.getCombatStats();
        final int currentLevel = stats[index];
        final int amt = Math.max((int) (currentLevel * (percentage / 100.0F)), minimumDrain);
        final int newLevel = currentLevel - amt;
        stats[index] = newLevel;
        return amt;
    }

    /**
     * Drains the specified skill for a given amount and returns the amount drained; cannot go below zero.
     * @param skill the skill to drain.
     * @param amount the amount to drain the skill for.
     * @return the amount of levels that was successfully drained.
     */
    public final int drainSkill(final int skill, final int amount) {
        final int index = getIndex(skill);
        if (index == -1) return 0;
        final int[] stats = statDefinitions.getCombatStats();
        final int currentLevel = stats[index];
        final int amt = (currentLevel - amount) < 0 ? currentLevel : amount;
        final int newLevel = currentLevel - amt;
        stats[index] = newLevel;
        return amt;
    }

    private static final int[] statArray = new int[] {SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.DEFENCE, SkillConstants.RANGED, SkillConstants.MAGIC};

    private int getIndex(final int skill) {
        return ArrayUtils.indexOf(statArray, skill);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getSlayerLevel() {
        return slayerLevel;
    }

    public void setSlayerLevel(int slayerLevel) {
        this.slayerLevel = slayerLevel;
    }

    public int getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(int attackDistance) {
        this.attackDistance = attackDistance;
    }

    public int getAggressionDistance() {
        return aggressionDistance;
    }

    public void setAggressionDistance(int aggressionDistance) {
        this.aggressionDistance = aggressionDistance;
    }

    public int getMaximumDistance() {
        return maximumDistance;
    }

    public void setMaximumDistance(int maximumDistance) {
        this.maximumDistance = maximumDistance;
    }

    public Entity.EntityType getTargetType() {
        return targetType;
    }

    public void setTargetType(Entity.EntityType targetType) {
        this.targetType = targetType;
    }

    public EnumSet<ImmunityType> getImmunityTypes() {
        return immunityTypes;
    }

    public void setImmunityTypes(EnumSet<ImmunityType> immunityTypes) {
        this.immunityTypes = immunityTypes;
    }

    public AggressionType getAggressionType() {
        return aggressionType;
    }

    public void setAggressionType(AggressionType aggressionType) {
        this.aggressionType = aggressionType;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    public EnumSet<WeaknessType> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(EnumSet<WeaknessType> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public ToxinDefinitions getToxinDefinitions() {
        return toxinDefinitions;
    }

    public void setToxinDefinitions(ToxinDefinitions toxinDefinitions) {
        this.toxinDefinitions = toxinDefinitions;
    }

    public StatDefinitions getStatDefinitions() {
        return statDefinitions;
    }

    public void setStatDefinitions(StatDefinitions statDefinitions) {
        this.statDefinitions = statDefinitions;
    }

    public AttackDefinitions getAttackDefinitions() {
        return attackDefinitions;
    }

    public void setAttackDefinitions(AttackDefinitions attackDefinitions) {
        this.attackDefinitions = attackDefinitions;
    }

    public BlockDefinitions getBlockDefinitions() {
        return blockDefinitions;
    }

    public void setBlockDefinitions(BlockDefinitions blockDefinitions) {
        this.blockDefinitions = blockDefinitions;
    }

    public SpawnDefinitions getSpawnDefinitions() {
        return spawnDefinitions;
    }

    public void setSpawnDefinitions(SpawnDefinitions spawnDefinitions) {
        this.spawnDefinitions = spawnDefinitions;
    }

    public void resetStats() {
        final NPCCombatDefinitions cachedDefs = NPCCDLoader.get(id);
        if (cachedDefs != null) {
            statDefinitions = cachedDefs.statDefinitions.clone();
        }
    }

}
