package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.VarManager;
import mgi.types.config.enums.Enums;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.util.AccessMask.*;

/**
 * @author Kris | 24/04/2019 01:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ExperienceTrackerInterface extends Interface {
    private static final int POSITION_VARBIT = 4692;
    private static final int SIZE_VARBIT = 4693;
    private static final int SPEED_VARBIT = 4722;
    private static final int SKILL_VARBIT = 4697;
    private static final int COLOR_VARBIT = 4695;
    private static final int PROGRESS_BAR_VARBIT = 4698;
    private static final int DURATION_VARBIT = 4694;
    private static final int GROUP_VARBIT = 4696;
    private static final int TRACKER_VARBIT = 4703;
    private static final int[] varbits = new int[] {POSITION_VARBIT, SIZE_VARBIT, SPEED_VARBIT, SKILL_VARBIT, COLOR_VARBIT, PROGRESS_BAR_VARBIT, DURATION_VARBIT, GROUP_VARBIT, TRACKER_VARBIT};
    private static final int[] SKILLS = new int[] {SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.RANGED, SkillConstants.MAGIC, SkillConstants.DEFENCE, SkillConstants.HITPOINTS, SkillConstants.PRAYER, SkillConstants.AGILITY, SkillConstants.HERBLORE, SkillConstants.THIEVING, SkillConstants.CRAFTING, SkillConstants.RUNECRAFTING, SkillConstants.MINING, SkillConstants.SMITHING, SkillConstants.FISHING, SkillConstants.COOKING, SkillConstants.FIREMAKING, SkillConstants.WOODCUTTING, SkillConstants.FLETCHING, SkillConstants.SLAYER, SkillConstants.FARMING, SkillConstants.CONSTRUCTION, SkillConstants.HUNTER};

    static {
        for (final int varbit : varbits) {
            VarManager.appendPersistentVarbit(varbit);
        }
        for (int i = 0; i < SKILLS.length; i++) {
            VarManager.appendPersistentVarp(1229 + i);
            VarManager.appendPersistentVarp(1253 + i);
        }
    }

    @Override
    protected void attach() {
        put(17, "Configure skill");
        put(51, "Set position");
        put(52, "Set size");
        put(53, "Set duration");
        put(54, "Set counter");
        put(56, "Set colour");
        put(57, "Set group");
        put(55, "Set progress bar");
        put(59, "Set fake drops");
        put(58, "Set speed");
        put(21, "Select no tracker or goal");
        put(25, "Select tracker");
        put(33, "Select goal");
        put(38, "Set goal start point");
        put(42, "Set goal end point");
        put(30, "Set tracker start point");
        put(46, "Save goal");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentSettings(getInterface(), getComponent("Configure skill"), 0, SkillConstants.SKILLS.length, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set position"), 1, Enums.EXPERIENCE_TRACKER_POSITION.getSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set size"), 1, Enums.EXPERIENCE_TRACKER_SIZE.getSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set duration"), 1, Enums.EXPERIENCE_TRACKER_DURATION.getSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set counter"), 1, 32, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set progress bar"), 1, 32, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set colour"), 1, Enums.EXPERIENCE_TRACKER_COLOURS.getSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP6, CLICK_OP7, CLICK_OP8);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set group"), 1, Enums.EXPERIENCE_TRACKER_GROUP.getSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Set speed"), 1, Enums.EXPERIENCE_TRACKER_SPEED.getSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3);
    }

    @Override
    protected void build() {
        bind("Configure skill", (player, slotId, itemId, option) -> {
            if (slotId < 0 || slotId >= SkillConstants.SKILLS.length) {
                throw new IllegalStateException("Skill id out of boundaries: " + slotId);
            }
            player.getTemporaryAttributes().put("SelectedSkillTracker", slotId);
        });
        bind("Select no tracker or goal", (player, slotId, itemId, option) -> selectTrackerSetting(player, 0));
        bind("Select tracker", (player, slotId, itemId, option) -> selectTrackerSetting(player, 1));
        bind("Select goal", (player, slotId, itemId, option) -> selectTrackerSetting(player, 2));
        bind("Set goal start point", (player, slotId, itemId, option) -> setTrackerPoint(player, option, true));
        bind("Set tracker start point", (player, slotId, itemId, option) -> setTrackerPoint(player, option, true));
        bind("Set goal end point", (player, slotId, itemId, option) -> setTrackerPoint(player, option, false));
        bind("Save goal", (player, slotId, itemId, option) -> {
            final int skill = player.getNumericTemporaryAttribute("SelectedSkillTracker").intValue();
            final int type = player.getNumericTemporaryAttribute("SelectedSkillTrackerType").intValue();
            final int startpoint = player.getVarManager().getValue(261);
            final int endpoint = player.getVarManager().getValue(262);
            if (type != 0 && startpoint > player.getSkills().getExperience(SKILLS[skill])) {
                player.sendMessage("You can't set the start point of your tracker higher than your current XP in the skill.");
                return;
            }
            if (type == 2 && startpoint != 0 && startpoint >= endpoint) {
                player.sendMessage("Goals must have their end point higher than their start point.");
                return;
            }
            player.getVarManager().sendBit(TRACKER_VARBIT, 0);
            player.getVarManager().sendVar(1253 + skill, type == 0 ? 0 : type == 1 ? -1 : player.getVarManager().getValue(262));
            player.getVarManager().sendVar(1229 + skill, startpoint);
        });
        bind("Set position", (player, slotId, itemId, option) -> player.getVarManager().sendBit(POSITION_VARBIT, slotId - 1));
        bind("Set size", (player, slotId, itemId, option) -> player.getVarManager().sendBit(SIZE_VARBIT, slotId - 1));
        bind("Set duration", (player, slotId, itemId, option) -> player.getVarManager().sendBit(DURATION_VARBIT, slotId - 1));
        bind("Set counter", (player, slotId, itemId, option) -> player.getVarManager().sendBit(SKILL_VARBIT, slotId - 1));
        bind("Set progress bar", (player, slotId, itemId, option) -> player.getVarManager().sendBit(PROGRESS_BAR_VARBIT, slotId - 1));
        bind("Set colour", (player, slotId, itemId, option) -> player.getVarManager().sendBit(COLOR_VARBIT, slotId - 1));
        bind("Set group", (player, slotId, itemId, option) -> player.getVarManager().sendBit(GROUP_VARBIT, slotId - 1));
        bind("Set speed", (player, slotId, itemId, option) -> player.getVarManager().sendBit(SPEED_VARBIT, slotId - 1));
    }

    private final void selectTrackerSetting(@NotNull final Player player, final int type) {
        final int skill = player.getNumericTemporaryAttribute("SelectedSkillTracker").intValue();
        player.getTemporaryAttributes().put("SelectedSkillTrackerType", type);
        player.getVarManager().sendVar(1253 + skill, 0);
        player.getVarManager().sendVar(1229 + skill, 0);
        player.getVarManager().sendVar(261, 0);
        player.getVarManager().sendVar(262, 0);
    }

    private void setTrackerPoint(@NotNull final Player player, final int option, final boolean start) {
        final int skill = player.getNumericTemporaryAttribute("SelectedSkillTracker").intValue();
        final int config = start ? 261 : 262;
        if (option == 6 || option == 7) {
            player.sendInputInt("Set tracker start point: " + (option == 6 ? "(skill level)" : "(XP value)"), amount -> {
                if (option == 6 && amount > 99) {
                    player.sendMessage(amount + " is not a valid skill level.");
                    return;
                }
                final int value = option == 6 ? Skills.getXPForLevel(amount) : amount;
                player.getVarManager().sendVar(config, value);
            });
        } else {
            final int value = skill == 23 ? player.getSkills().getTotalXp() : (int) player.getSkills().getExperience(SKILLS[skill]);
            player.getVarManager().sendVar(config, value);
        }
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EXPERIENCE_TRACKER;
    }
}
