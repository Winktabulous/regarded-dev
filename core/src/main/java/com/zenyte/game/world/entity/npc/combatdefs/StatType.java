package com.zenyte.game.world.entity.npc.combatdefs;

/**
 * @author Kris | 18/11/2018 02:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StatType {
    ATTACK, STRENGTH, DEFENCE, MAGIC, RANGED,
    ATTACK_STAB, ATTACK_SLASH, ATTACK_CRUSH, ATTACK_MAGIC, ATTACK_RANGED,
    DEFENCE_STAB, DEFENCE_SLASH, DEFENCE_CRUSH, DEFENCE_MAGIC, DEFENCE_RANGED,
    MELEE_STRENGTH_BONUS, RANGED_STRENGTH_BONUS, MAGIC_STRENGTH_BONUS;

    /** Returns index in the respective stat definition array. Order of the enum must remain the same. */
    public int index() {
        return ordinal() % 5;
    }

    public static StatType getAttackType(final AttackType type) {
        return type == AttackType.STAB ? ATTACK_STAB : type == AttackType.SLASH ? ATTACK_SLASH :
                type == AttackType.CRUSH ? ATTACK_CRUSH : type == AttackType.MAGIC ? ATTACK_MAGIC : ATTACK_RANGED;
    }

    public static StatType getDefenceType(final AttackType type) {
        return type == AttackType.STAB ? DEFENCE_STAB : type == AttackType.SLASH ? DEFENCE_SLASH :
                type == AttackType.CRUSH ? DEFENCE_CRUSH : type == AttackType.MAGIC ? DEFENCE_MAGIC : DEFENCE_RANGED;
    }

    public static final StatType[] levelTypes = new StatType[] {
            ATTACK, STRENGTH, DEFENCE, MAGIC, RANGED
    };

}
