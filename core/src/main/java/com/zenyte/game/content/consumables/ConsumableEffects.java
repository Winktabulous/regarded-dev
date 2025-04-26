package com.zenyte.game.content.consumables;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.entity.player.variables.TickVariable;

import static com.zenyte.game.world.entity.player.SkillConstants.*;

/**
 * Utility class for handling miscellaneous consumable effects.
 */
public final class ConsumableEffects {

    public static final Animation SHOCK_ANIM = new Animation(3170);
    public static final Graphics SHOCK_GFX = new Graphics(560);
    public static final int OVERLOAD_REFRESHES_REMAINING = 5418;
    public static final int ANTIFIRE_REFRESHES_REMAINING = 3981;
    public static final int SUPER_ANTIFIRE_REFRESHES_REMAINING = 6101;

    public static void damagePlayer(Player player, final int repeat) {
        WorldTasksManager.schedule(new WorldTask() {
            private int count;
            @Override
            public void run() {
                if (player.isDead() || player.isFinished() || count++ == repeat) {
                    stop();
                    return;
                }
                player.setAnimation(SHOCK_ANIM);
                player.setGraphics(SHOCK_GFX);
                player.applyHit(new Hit(10, HitType.REGULAR));
            }
        }, 0, 1);
    }

    private static final int[] OVERLOAD_SKILLS = new int[] {ATTACK, STRENGTH, DEFENCE, MAGIC, RANGED};

    public static void applySalt(final Player player) {
        applyOverload(player, .16F, 11);
    }

    public static void applyHeart(final Player player) {
        applyOverload(player, 0, 20);
    }

    public static void applyOverload(final Player player) {
        final int type = player.getVariables().getOverloadType();
        applyOverload(player, type == 1 ? 0.1F : type == 2 ? 0.13F : 0.16F, type == 1 ? 4 : type == 2 ? 5 : 6);
    }

    public static void applySaturatedHeart(final Player player) {
        final Skills skills = player.getSkills();
        int currentLevel;
        int realLevel;
        int skill = MAGIC;
        final int boost = (int) (player.getSkills().getLevelForXp(SkillConstants.MAGIC) * 0.1 + 4);
        currentLevel = skills.getLevel(skill);
        realLevel = skills.getLevelForXp(skill);
        skills.setLevel(skill, Math.min(currentLevel, realLevel) + boost);
    }

    public static void applyOverload(final Player player, float modifier, int boost) {
        final int type = player.getVariables().getOverloadType();
        final int currentRefresh = player.getVarManager().getBitValue(OVERLOAD_REFRESHES_REMAINING) - 1;
        player.getVarManager().sendBit(OVERLOAD_REFRESHES_REMAINING, Math.max(0, currentRefresh));

        final Skills skills = player.getSkills();
        int currentLevel;
        int realLevel;
        for (final int skill : OVERLOAD_SKILLS) {
            currentLevel = skills.getLevel(skill);
            realLevel = skills.getLevelForXp(skill);
            skills.setLevel(skill, (int) (Math.min(currentLevel, realLevel) + boost + (realLevel * modifier)));
        }
    }

    public static void resetOverload(final Player player) {
        player.getVarManager().sendBit(OVERLOAD_REFRESHES_REMAINING, 0);
        if (resetSkills(player, "overload", OVERLOAD_SKILLS)) {
            player.heal(50);
        }
    }

    public static void resetSalt(final Player player) {
        player.getVariables().cancel(TickVariable.SALT);
        resetSkills(player, "your smelling salt", OVERLOAD_SKILLS);
    }

    public static boolean resetSkills(final Player player, String potionName, int[] skillIds) {
        if (player.isDead() || player.isFinished() || !player.isInitialized())
            return false;
        final Skills skills = player.getSkills();
        for (final int skill : skillIds)
            skills.setLevel(skill, skills.getLevelForXp(skill));
        player.sendMessage("<col=ff0000>The effects of " + potionName + " have worn off, and you feel normal again.");
        return true;
    }

    public static void applyAntifire(final Player player, final int ticks) {
        player.getVariables().schedule(ticks, TickVariable.ANTIFIRE);
        player.getVarManager().sendBit(ANTIFIRE_REFRESHES_REMAINING, (ticks / 30));
    }

    public static void applySuperAntifire(final Player player, final int ticks) {
        player.getVariables().schedule(ticks, TickVariable.SUPER_ANTIFIRE);
        player.getVarManager().sendBit(SUPER_ANTIFIRE_REFRESHES_REMAINING, (ticks / 20));
    }

    public static void deductVariable(Player player, final int varbit) {
        player.getVarManager().sendBit(varbit, Math.max(0, player.getVarManager().getBitValue(varbit) - 1));
    }

    public static void resetSaturatedHeart(Player player) {
        resetSkills(player, "Saturated Heart", new int[]{MAGIC});
    }
}
