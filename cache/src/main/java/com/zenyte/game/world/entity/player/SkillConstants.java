package com.zenyte.game.world.entity.player;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.util.IntListUtils;
import it.unimi.dsi.fastutil.ints.IntLists;

import java.util.List;

public class SkillConstants {
    public static final int COUNT = 23;
    public static final int MAXIMUM_EXP = 200000000;
    public static final int ATTACK = 0;
    public static final int DEFENCE = 1;
    public static final int STRENGTH = 2;
    public static final int HITPOINTS = 3;
    public static final int RANGED = 4;
    public static final int PRAYER = 5;
    public static final int MAGIC = 6;
    public static final int COOKING = 7;
    public static final int WOODCUTTING = 8;
    public static final int FLETCHING = 9;
    public static final int FISHING = 10;
    public static final int FIREMAKING = 11;
    public static final int CRAFTING = 12;
    public static final int SMITHING = 13;
    public static final int MINING = 14;
    public static final int HERBLORE = 15;
    public static final int AGILITY = 16;
    public static final int THIEVING = 17;
    public static final int SLAYER = 18;
    public static final int FARMING = 19;
    public static final int RUNECRAFTING = 20;
    public static final int HUNTER = 21;
    public static final int CONSTRUCTION = 22;
    public static final int SKILL_GUIDE_INTERFACE = 214;
    public static final String[] SKILLS = {"Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecraft", "Hunter", "Construction"};
    public static final int[] SKILLS_ICONS = {197, 199, 198, 203, 200, 201, 202, 212, 214, 208, 211, 213, 207, 210, 209, 205, 204, 206, 216, 217, 215, 220, 221};
    static final List<Integer> MILESTONES = ImmutableList.of(50, 100, 150, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2000, 2100, 2200);
    public static final int[] VALID_SKILLS = new int[] {
            ATTACK,
            DEFENCE,
            STRENGTH,
            HITPOINTS,
            RANGED,
            PRAYER,
            MAGIC,
            COOKING,
            WOODCUTTING,
            FLETCHING,
            FISHING,
            FIREMAKING,
            CRAFTING,
            SMITHING,
            MINING,
            HERBLORE,
            AGILITY,
            THIEVING,
            SLAYER,
            FARMING,
            RUNECRAFTING,
            HUNTER
    };
}
