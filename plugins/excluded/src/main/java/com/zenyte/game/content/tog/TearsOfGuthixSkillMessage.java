package com.zenyte.game.content.tog;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.zenyte.game.world.entity.player.SkillConstants;

import java.util.EnumSet;

/**
 * @author Chris
 * @since September 07 2020
 */
public enum TearsOfGuthixSkillMessage {
    ATTACK(SkillConstants.ATTACK, "You feel a brief surge of aggression."), STRENGTH(SkillConstants.STRENGTH, "Your muscles bulge."), DEFENCE(SkillConstants.DEFENCE, "You feel more able to defend yourself."), RANGED(SkillConstants.RANGED, "Your aim improves."), PRAYER(SkillConstants.PRAYER, "You suddenly feel very close to the gods."), MAGIC(SkillConstants.MAGIC, "You feel the power of the runes surging through you."), HITPOINTS(SkillConstants.HITPOINTS, "You feel very healthy."), AGILITY(SkillConstants.AGILITY, "You feel very nimble."), HERBLORE(SkillConstants.HERBLORE, "You gain a deep understanding of all kinds of strange plants."), THIEVING(SkillConstants.THIEVING, "You feel your respect for others\' property slipping away."), CRAFTING(SkillConstants.CRAFTING, "Your fingers feel nimble and suited to delicate work."), FLETCHING(SkillConstants.FLETCHING, "You gain a deep understanding of wooden sticks."), MINING(SkillConstants.MINING, "You gain a deep understanding of the stones of the earth."), SMITHING(SkillConstants.SMITHING, "You gain a deep understanding of all types of metal."), FISHING(SkillConstants.FISHING, "You gain a deep understanding of the creatures of the sea."), COOKING(SkillConstants.COOKING, "You have a brief urge to cook some food."), FIREMAKING(SkillConstants.FIREMAKING, "You have a brief urge to set light to something."), WOODCUTTING(SkillConstants.WOODCUTTING, "You gain a deep understanding of the trees in the wood."), RUNECRAFT(SkillConstants.RUNECRAFTING, "You gain a deep understanding of runes."), SLAYER(SkillConstants.SLAYER, "You gain a deep understanding of many strange creatures."), FARMING(SkillConstants.FARMING, "You gain a deep understanding of the cycles of nature."), CONSTRUCTION(SkillConstants.CONSTRUCTION, "You feel homesick."), HUNTER(SkillConstants.HUNTER, "You briefly experience the joy of the hunt.");
    private static final ImmutableSet<TearsOfGuthixSkillMessage> MESSAGES = Sets.immutableEnumSet(EnumSet.allOf(TearsOfGuthixSkillMessage.class));
    private final int skillId;
    private final String message;

    public static TearsOfGuthixSkillMessage of(final int skillId) {
        for (final TearsOfGuthixSkillMessage message : MESSAGES) {
            if (message.skillId == skillId) {
                return message;
            }
        }
        throw new IllegalArgumentException("Could not find TOG skill message for skill id: " + skillId);
    }

    TearsOfGuthixSkillMessage(int skillId, String message) {
        this.skillId = skillId;
        this.message = message;
    }

    public int getSkillId() {
        return skillId;
    }

    public String getMessage() {
        return message;
    }
}
