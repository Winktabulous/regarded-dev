package com.zenyte.game.content.minigame.pestcontrol;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.StringEnum;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Consumer;

/**
 * @author Kris | 24/03/2019 15:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VoidKnightRewardsInterface extends Interface {

    private enum VoidKnightReward {
        ATTACK_XP_1("Attack XP", SkillConstants.ATTACK, player -> addExperience(player, SkillConstants.ATTACK, 1)),
        ATTACK_XP_10("Attack XP (+1%)", SkillConstants.ATTACK, player -> addExperience(player, SkillConstants.ATTACK, 10)),
        ATTACK_XP_100("Attack XP (+10%)", SkillConstants.ATTACK, player -> addExperience(player, SkillConstants.ATTACK, 100)),
        DEFENCE_XP_1("Defence XP", SkillConstants.DEFENCE, player -> addExperience(player, SkillConstants.DEFENCE, 1)),
        DEFENCE_XP_10("Defence XP (+1%)", SkillConstants.DEFENCE, player -> addExperience(player, SkillConstants.DEFENCE, 10)),
        DEFENCE_XP_100("Defence XP (+10%)", SkillConstants.DEFENCE, player -> addExperience(player, SkillConstants.DEFENCE, 100)),
        MAGIC_XP_1("Magic XP", SkillConstants.MAGIC, player -> addExperience(player, SkillConstants.MAGIC, 1)),
        MAGIC_XP_10("Magic XP (+1%)", SkillConstants.MAGIC, player -> addExperience(player, SkillConstants.MAGIC, 10)),
        MAGIC_XP_100("Magic XP (+10%)", SkillConstants.MAGIC, player -> addExperience(player, SkillConstants.MAGIC, 100)),
        PRAYER_XP_1("Prayer XP", SkillConstants.PRAYER, player -> addExperience(player, SkillConstants.PRAYER, 1)),
        PRAYER_XP_10("Prayer XP (+1%)", SkillConstants.PRAYER, player -> addExperience(player, SkillConstants.PRAYER, 10)),
        PRAYER_XP_100("Prayer XP (+10%)", SkillConstants.PRAYER, player -> addExperience(player, SkillConstants.PRAYER, 100)),
        STRENGTH_XP_1("Strength XP", SkillConstants.STRENGTH, player -> addExperience(player, SkillConstants.STRENGTH, 1)),
        STRENGTH_XP_10("Strength XP (+1%)", SkillConstants.STRENGTH, player -> addExperience(player, SkillConstants.STRENGTH, 10)),
        STRENGTH_XP_100("Strength XP (+10%)", SkillConstants.STRENGTH, player -> addExperience(player, SkillConstants.STRENGTH, 100)),
        RANGED_XP_1("Ranged XP", SkillConstants.RANGED, player -> addExperience(player, SkillConstants.RANGED, 1)),
        RANGED_XP_10("Ranged XP (+1%)", SkillConstants.RANGED, player -> addExperience(player, SkillConstants.RANGED, 10)),
        RANGED_XP_100("Ranged XP (+10%)", SkillConstants.RANGED, player -> addExperience(player, SkillConstants.RANGED, 100)),
        HITPOINTS_XP_1("Hitpoints XP", SkillConstants.HITPOINTS, player -> addExperience(player, SkillConstants.HITPOINTS, 1)),
        HITPOINTS_XP_10("Hitpoints XP (+1%)", SkillConstants.HITPOINTS, player -> addExperience(player, SkillConstants.HITPOINTS, 10)),
        HITPOINTS_XP_100("Hitpoints XP (+10%)", SkillConstants.HITPOINTS, player -> addExperience(player, SkillConstants.HITPOINTS, 100)),
        HERB_PACK("Herb Pack", -1, null),
        SEED_PACK("Seed Pack", -1, null),
        MINERAL_PACK("Mineral Pack", -1, null),
        VOID_MACE("Void Knight Mace", -1, player -> addItem(player, 8841)),
        VOID_TOP("Void Knight Top", -1, player -> addItem(player, 8839)),
        VOID_ROBES("Void Knight Robes", -1, player -> addItem(player, 8840)),
        VOID_GLOVES("Void Knight Gloves", -1, player -> addItem(player, 8842)),
        VOID_MAGE_HELM("Void Mage Helm", -1, player -> addItem(player, 11663)),
        VOID_RANGER_HELM("Void Ranger Helm", -1, player -> addItem(player, 11664)),
        VOID_MELEE_HELM("Void Melee Helm", -1, player -> addItem(player, 11665)),
        VOID_SEAL("Void Knight Seal", -1, player -> addItem(player, 11666));

        private static final int[] COMBAT_SKILLS = new int[] {SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.DEFENCE, SkillConstants.HITPOINTS, SkillConstants.RANGED, SkillConstants.MAGIC};

        private static boolean isEligibleForVoid(final Player player) {
            final Skills skills = player.getSkills();
            for (final int skill : COMBAT_SKILLS) {
                if (skills.getLevelForXp(skill) < 42) {
                    return false;
                }
            }
            return skills.getLevelForXp(SkillConstants.PRAYER) >= 22;
        }

        private static void addExperience(final Player player, final int skill, final int amount) {
            final float xp = getExperience(player, skill) * (skill == SkillConstants.PRAYER ? 0.5F : 1);
            float bulkModifier = amount == 1 ? 1.0F : amount == 10 ? 1.01F : 1.1F;
            if (player.getGameMode().isGroupIronman()) {
                bulkModifier -= 0.5;
            }
            player.getSkills().addXp(skill, xp * amount * bulkModifier);
        }

        private static void addPack(final Player player, final int skill) {
        }

        private static void addItem(final Player player, final int itemId) {
            final Item it = new Item(itemId);
            player.getCollectionLog().add(it);
            player.getInventory().addItem(it).onFailure(item -> {
                World.spawnFloorItem(item, player);
                player.sendMessage("Your " + item.getName() + " was dropped on the floor due to lack of inventory space.");
            });
        }

        private static final Int2ObjectMap<VoidKnightReward> map = new Int2ObjectOpenHashMap<>();

        static {
            final StringEnum e = Enums.PEST_CONTROL_REWARDS_ENUM;
            for (final VoidKnightRewardsInterface.VoidKnightReward value : values()) {
                map.put(e.getKey(value.enumName).orElseThrow(RuntimeException::new), value);
            }
        }

        private final int getSlot() {
            return Enums.PEST_CONTROL_REWARDS_ENUM.getKey(enumName).orElseThrow(RuntimeException::new);
        }

        private int getPoints() {
            return Enums.PEST_CONTROL_POINTS_ENUM.getValue(getSlot()).orElseThrow(RuntimeException::new);
        }

        private final String enumName;
        private final int skillId;
        private final Consumer<Player> function;

        private OptionalInt getSkill() {
            return skillId == -1 ? OptionalInt.empty() : OptionalInt.of(skillId);
        }

        VoidKnightReward(String enumName, final int skillId, final Consumer<Player> function) {
            this.enumName = enumName;
            this.skillId = skillId;
            this.function = function;
        }

        private static final int getExperience(final Player player, final int skillId) {
            final int level = player.getSkills().getLevelForXp(skillId);
            final int modifier = skillId == SkillConstants.PRAYER ? 18 : (skillId == SkillConstants.MAGIC || skillId == SkillConstants.RANGED) ? 32 : 35;
            final double ceil = Math.ceil((level + 25.0F) * (level - 24.0F) / 606.0F);
            return (int) (ceil * modifier);
        }
    }

    @Override
    protected void attach() {
        put(6, "Confirm reward");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Confirm reward"), 0, Enums.PEST_CONTROL_REWARDS_ENUM.getSize(), AccessMask.CLICK_OP1);
        player.getVarManager().sendVar(261, player.getNumericAttribute("pest_control_points").intValue());
    }

    @Override
    protected void build() {
        bind("Confirm reward", (player, slotId, itemId, option) -> {
            final VoidKnightRewardsInterface.VoidKnightReward reward = Objects.requireNonNull(VoidKnightReward.map.get(slotId));
            final boolean hasVoidRequirements = Enums.PEST_CONTROL_REWARDS_VOID_ELEMENTS_ENUM.getValue(slotId).isPresent();
            if (hasVoidRequirements && !VoidKnightReward.isEligibleForVoid(player)) {
                player.sendMessage("You need level 42 in Attack, Strength, Defence, Ranged, Magic and Hitpoints, and level 22 Prayer, to purchase that item.");
                return;
            }
            final OptionalInt skill = Enums.PEST_CONTROL_REWARDS_PACKS_STATS_ENUM.getValue(slotId);
            if (skill.isPresent()) {
                if (reward.function == null) {
                    player.sendMessage("Item packs are currently unavailable.");
                    return;
                }
                final int skillId = skill.getAsInt();
                if (player.getSkills().getLevelForXp(skillId) < 25) {
                    player.sendMessage("You need level " + skillId + " " + Enums.SKILL_NAMES_ENUM.getValue(skillId).orElseThrow(RuntimeException::new) + " to purchase that item.");
                    return;
                }
            }
            final OptionalInt xpSkill = reward.getSkill();
            if (xpSkill.isPresent()) {
                final int skillId = xpSkill.getAsInt();
                if (player.getSkills().getLevelForXp(skillId) < 25) {
                    player.sendMessage("The Void Knights will not offer training in skills for which you have a level under 25.");
                    return;
                }
            }
            final int points = player.getNumericAttribute("pest_control_points").intValue();
            final int requiredPoints = reward.getPoints();
            if (points < requiredPoints) {
                player.sendMessage("You need " + requiredPoints + " " + (requiredPoints == 1 ? "point" : "points") + " to claim that reward.");
                return;
            }
            player.addAttribute("pest_control_points", points - reward.getPoints());
            player.getVarManager().sendVar(261, player.getNumericAttribute("pest_control_points").intValue());
            reward.function.accept(player);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.VOID_KNIGHT_REWARDS;
    }
}
