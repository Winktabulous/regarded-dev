package com.zenyte.game.content;

import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.utils.TextUtils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommeh | 24-3-2019 | 17:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum AccomplishmentCape {
    ATTACK(SkillConstants.ATTACK, 9747, 9748, 9749),
    DEFENCE(SkillConstants.DEFENCE, 9753, 9754, 9755),
    STRENGTH(SkillConstants.STRENGTH, 9750, 9751, 9752),
    HITPOINTS(SkillConstants.HITPOINTS, 9768, 9769, 9770),
    RANGED(SkillConstants.RANGED, 9756, 9757, 9758),
    PRAYER(SkillConstants.PRAYER, 9759, 9760, 9761),
    MAGIC(SkillConstants.MAGIC, 9762, 9763, 9764),
    COOKING(SkillConstants.COOKING, 9801, 9802, 9803),
    WOODCUTTING(SkillConstants.WOODCUTTING, 9807, 9808, 9809),
    FLETCHING(SkillConstants.FLETCHING, 9783, 9784, 9785),
    FISHING(SkillConstants.FISHING, 9798, 9799, 9800),
    FIREMAKING(SkillConstants.FIREMAKING, 9804, 9805, 9806),
    CRAFTING(SkillConstants.CRAFTING, 9780, 9781, 9782),
    SMITHING(SkillConstants.SMITHING, 9795, 9796, 9797),
    MINING(SkillConstants.MINING, 9792, 9793, 9794),
    HERBLORE(SkillConstants.HERBLORE, 9774, 9775, 9776),
    AGILITY(SkillConstants.AGILITY, 9771, 9772, 9773),
    THIEVING(SkillConstants.THIEVING, 9777, 9778, 9779),
    SLAYER(SkillConstants.SLAYER, 9786, 9787, 9788),
    FARMING(SkillConstants.FARMING, 9810, 9811, 9812),
    RUNECRAFTING(SkillConstants.RUNECRAFTING, 9765, 9766, 9767),
    CONSTRUCTION(SkillConstants.CONSTRUCTION, 9789, 9790, 9791),
    HUNTER(SkillConstants.HUNTER, 9948, 9949, 9950),
    DIARY(-1, 19476, 13069, 13070);
    private final int skill;
    private final int untrimmed;
    private final int trimmed;
    private final int hood;
    private static final Set<AccomplishmentCape> ALL = EnumSet.allOf(AccomplishmentCape.class);
    private static final Map<Integer, AccomplishmentCape> CAPES = new HashMap<>();
    private static final Map<Integer, AccomplishmentCape> BY_SKILL = new HashMap<>();

    public static AccomplishmentCape get(final int id) {
        return CAPES.get(id);
    }

    public static AccomplishmentCape getBySkill(final int id) {
        return BY_SKILL.get(id);
    }

    static {
        for (final AccomplishmentCape cape : ALL) {
            CAPES.put(cape.getUntrimmed(), cape);
            CAPES.put(cape.getTrimmed(), cape);
            BY_SKILL.put(cape.getSkill(), cape);
        }
    }

    @Override
    public String toString() {
        return TextUtils.capitalizeFirstCharacter(name().toLowerCase());
    }

    AccomplishmentCape(int skill, int untrimmed, int trimmed, int hood) {
        this.skill = skill;
        this.untrimmed = untrimmed;
        this.trimmed = trimmed;
        this.hood = hood;
    }

    public int getSkill() {
        return skill;
    }

    public int getUntrimmed() {
        return untrimmed;
    }

    public int getTrimmed() {
        return trimmed;
    }

    public int getHood() {
        return hood;
    }
}
