package com.zenyte.game.world.entity.player.action.combat;

import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;
import java.util.Objects;

import static com.zenyte.game.world.entity.npc.combatdefs.AttackType.*;
import static com.zenyte.game.world.entity.player.action.combat.AttackStyle.AttackExperienceType.*;

public enum AttackStyleDefinition {

    UNARMED(0, "Unarmed", new AttackStyle(CRUSH, ATTACK_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(CRUSH, DEFENCE_XP)),
    AXE(1, "Axe", new AttackStyle(SLASH, ATTACK_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(SLASH, DEFENCE_XP)),
    MAUL(2, "Blunt", new AttackStyle(CRUSH, ATTACK_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(CRUSH, DEFENCE_XP)),
    BOW(3, "Bow", new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_DEFENCE_XP)),
    CLAWS(4, "Claw", new AttackStyle(SLASH, ATTACK_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(STAB, SHARED_XP), new AttackStyle(SLASH, DEFENCE_XP)),
    CROSSBOW(5, "Crossbow", new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_DEFENCE_XP)),
    LIZARD(6, "Salamander", new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(RANGED, RANGED_XP), new AttackStyle(MAGIC, MAGIC_XP)),
    CHINCHOMPA(7, "Chinchompas", new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_DEFENCE_XP)),
    BAZOOKA(8, "Gun", new AttackStyle(RANGED, RANGED_XP), new AttackStyle(CRUSH, STRENGTH_XP)),
    SCIMITAR(9, "Slash Sword", new AttackStyle(SLASH, ATTACK_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(STAB, SHARED_XP), new AttackStyle(SLASH, DEFENCE_XP)),
    TWO_HANDED(10, "2h Sword", new AttackStyle(SLASH, ATTACK_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(SLASH, DEFENCE_XP)),
    PICKAXE(11, "Pickaxe", new AttackStyle(STAB, ATTACK_XP), new AttackStyle(STAB, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(STAB, DEFENCE_XP)),
    HALBERD(12, "Polearm", new AttackStyle(STAB, SHARED_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(STAB, DEFENCE_XP)),
    NON_AUTOCAST_STAFF(13, "Polestaff", new AttackStyle(CRUSH, ATTACK_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(CRUSH, DEFENCE_XP)),
    SCYTHE(14, "Scythe", new AttackStyle(SLASH, ATTACK_XP), new AttackStyle(STAB, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(SLASH, DEFENCE_XP)),
    SPEAR(15, "Spear", new AttackStyle(STAB, SHARED_XP), new AttackStyle(SLASH, SHARED_XP), new AttackStyle(CRUSH, SHARED_XP), new AttackStyle(STAB, DEFENCE_XP)),
    MACE(16, "Spiked", new AttackStyle(CRUSH, ATTACK_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(STAB, SHARED_XP), new AttackStyle(CRUSH, DEFENCE_XP)),
    DAGGER(17, "Stab Sword", new AttackStyle(STAB, ATTACK_XP), new AttackStyle(STAB, STRENGTH_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(STAB, DEFENCE_XP)),
    WAND(18, "Staff", new AttackStyle(CRUSH, ATTACK_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(CRUSH, DEFENCE_XP)),
    THROWN(19, "Thrown", new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_XP), new AttackStyle(RANGED, RANGED_DEFENCE_XP)),
    WHIP(20, "Whip", new AttackStyle(SLASH, ATTACK_XP), new AttackStyle(SLASH, SHARED_XP), new AttackStyle(SLASH, DEFENCE_XP)),
    STAFF(21, "Bladed Staff", new AttackStyle(STAB, ATTACK_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP)),
    UNUSED_SCIMITAR(22, "Unused", new AttackStyle(SLASH, ATTACK_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(SLASH, DEFENCE_XP)),
    THROWN_MAGIC(23, "Powered staff", new AttackStyle(MAGIC, MAGIC_XP), new AttackStyle(MAGIC, MAGIC_XP), new AttackStyle(MAGIC, MAGIC_DEFENCE_XP)),
    UNUSED_SPEAR(24, "Banner", new AttackStyle(STAB, ATTACK_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(CRUSH, SHARED_XP), new AttackStyle(STAB, DEFENCE_XP)),
    UNUSED_HALBERD(25, "Unused", new AttackStyle(STAB, SHARED_XP), new AttackStyle(SLASH, STRENGTH_XP), new AttackStyle(STAB, DEFENCE_XP)),
    UNUSED_MAUL(26, "Bludgeon", new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP)),
    DINHS_BULWARK(27, "Bulwark", new AttackStyle(CRUSH, ATTACK_XP), null),
    UNUSED_THROWN_MAGIC(28, "Powered staff", new AttackStyle(MAGIC, MAGIC_XP), new AttackStyle(MAGIC, MAGIC_XP), new AttackStyle(MAGIC, MAGIC_DEFENCE_XP)),
    PARTISAN(29, "Partisan", new AttackStyle(STAB, ATTACK_XP), new AttackStyle(STAB, STRENGTH_XP), new AttackStyle(CRUSH, STRENGTH_XP), new AttackStyle(STAB, DEFENCE_XP)),

    ;

    public static final AttackStyleDefinition[] values = values();
    public static IntList SLASH_TYPES;
    private final AttackStyle[] styles;
    private final String prettyName;
    private final int varbit;
    AttackStyleDefinition(int varbit, String prettyName, final AttackStyle... styles) {
        this.varbit = varbit;
        this.prettyName = prettyName;
        this.styles = styles;
    }

    public static boolean isSlashVarbit(int varbit) {
        if (SLASH_TYPES == null) {
            SLASH_TYPES = IntList.of(
                Arrays
                    .stream(values)
                    .filter(it -> Arrays
                        .stream(it.styles)
                        .filter(Objects::nonNull)
                        .anyMatch(style -> style.getType() == SLASH)
                    )
                    .mapToInt(it -> it.varbit)
                    .toArray()
            );
        }
        return SLASH_TYPES.contains(varbit);
    }

    public String getPrettyName() {
        return prettyName;
    }

    public int getVarbit() {
        return varbit;
    }

    public AttackStyle[] getStyles() {
        return styles;
    }
}
