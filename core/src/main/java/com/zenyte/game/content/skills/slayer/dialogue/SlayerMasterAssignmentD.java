package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.content.skills.slayer.*;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.entity.player.dialogue.NPCMessage;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.Keldagrim;

/**
 * @author Tommeh | 4 jun. 2018 | 15:57:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SlayerMasterAssignmentD extends Dialogue {
    public SlayerMasterAssignmentD(final Player player, final NPC npc) {
        super(player, npc);
    }

    private Assignment task;

    @Override
    public void buildDialogue() {
        final SlayerMaster master = SlayerMaster.mappedMasters.get(npc.getId());
        if (master == null) {
            return;
        }
        final Slayer slayer = player.getSlayer();
        final Assignment currentTask = slayer.getAssignment();
        final int zukKc = player.getNotificationSettings().getKillcount("tzkal-zuk");
        if (currentTask != null) {
            final Class<? extends RegionArea> clazz = currentTask.getArea();
            if (master.equals(SlayerMaster.KONAR_QUO_MATEN) && clazz != null) {
                final RegionArea area = GlobalAreaManager.getArea(clazz);
                npc("You're still bringing balance to " + currentTask.getTask().toString().toLowerCase() + " in the " + area.name() + ", with " + currentTask.getAmount() + " to go. Come back when you're finished.");
            } else {
                npc("You're still hunting " + currentTask.getTask().toString().toLowerCase() + "; you have " + currentTask.getAmount() + " to go. Come back when you've finished your task.");
            }
        } else {
            final Skills skills = player.getSkills();
            if (skills.getLevel(SkillConstants.SLAYER) < master.getSlayerRequirement() || skills.getCombatLevel() < master.getCombatRequirement()) {
                npc("Sorry, but you're not skilled enough to be taught by<br><br>me. Your best trainer would be " + slayer.getAdvisedMaster() + ".");
                return;
            }
            final Player partner = slayer.getPartner();
            if (partner != null && (partner.isLocked() || partner.isUnderCombat() || partner.isFinished() || partner.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL) || partner.getInterfaceHandler().containsInterface(InterfacePosition.DIALOGUE))) {
                plain("Your Slayer Partner is currently busy.");
                return;
            }
            task = slayer.generateTask(master);
            if (task.getTask() instanceof BossTask) {
                npc("You're currently assigned to kill " + task.getTask().toString() + ". How many would you like to slay?").executeAction(() -> {
                    finish();
                    player.sendInputInt("How many would you like to slay? (3 - 35)", value -> {
                        final int num = Math.max(Math.min(value, 35), 3);
                        task.setAmount(num);
                        task.setInitialAmount(num);
                        slayer.setAssignment(task);
                        player.getDialogueManager().start(new BossAssignmentExtension(player, npc, task));
                        if (partner != null) {
                            final Slayer partnerSlayer = partner.getSlayer();
                            if (partnerSlayer.getAssignment() == null && partner.getSkills().getLevelForXp(SkillConstants.SLAYER) >= task.getTask().getSlayerRequirement() && partnerSlayer.isAssignable(task.getTask(), master)) {
                                partner.getDialogueManager().start(new PartnerAssignmentExtension(player, task, master, npc));
                            } else {
                                partner.sendMessage("You are not eligible for " + player.getName() + "'s new Slayer " +
                                        "assignment.");
                                player.sendMessage("Your partner is not eligible for this Slayer assignment.");
                            }
                        }
                    });
                });
                return;
            }
            if (partner != null) {
                final Slayer partnerSlayer = partner.getSlayer();
                if (partnerSlayer.getAssignment() == null && partner.getSkills().getLevelForXp(SkillConstants.SLAYER) >= task.getTask().getSlayerRequirement() && partnerSlayer.isAssignable(task.getTask(), master)) {
                    partner.getDialogueManager().start(new PartnerAssignmentExtension(partner, task, master, npc));
                } else {
                    partner.sendMessage("You are not eligible for " + player.getName() + "'s new Slayer assignment.");
                    player.sendMessage("Your partner is not eligible for this Slayer assignment.");
                }
            }
            if (slayer.getMaster() != master) {
                slayer.setMaster(master);
            }
            slayer.setAssignment(task);
            if (master.equals(SlayerMaster.KONAR_QUO_MATEN)) {
                final Class<? extends RegionArea> clazz = task.getArea();
                if (clazz != null) {
                    final RegionArea area = GlobalAreaManager.getArea(clazz);
                    if (area.equals(GlobalAreaManager.getArea(Keldagrim.class))) {
                        npc("You are to bring balance to " + task.getAmount() + " " + task.getTask().toString() + " in " + area.name() + ".");
                    } else {
                        npc("You are to bring balance to " + task.getAmount() + " " + task.getTask().toString() + " in the " + area.name() + ".");
                    }
                }
            } else {
                npc("Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".").executeAction(() -> {
                    if (task.getTask() == RegularTask.TZHAAR && zukKc <= 0) {
                        setKey(10000);
                    }
                });
                if (task.getTask() == RegularTask.TZHAAR) {
                    // options for players with inferno completed, else key is set to 10000
                    options("Replace your assignment?", new DialogueOption("TzTok-Jad.", key(10000)), new DialogueOption("TzKal-Zuk.", key(20000)), new DialogueOption("Cancel."));
                    //TzKal-Zuk
                    npc(20000, "Ah... Tzhaar... as you are undertaking this task alone, for an extra reward you could choose to slay TzKal-Zuk instead of the TzHaar.");
                    options("Switch task to TzKal-Zuk?", new DialogueOption("Yeah, I can beat TzKal-Zuk.", key(1000)), new DialogueOption("No thanks, I'll stick with TzHaar!", key(3000)));
                    player(1000, "Yeah, I can beat TzKal-Zuk.").executeAction(() -> {
                        task = player.getSlayer().getTzKalZukAssignment(master);
                        slayer.setAssignment(task);
                        /* Replace existing string in the dialogue with new task. */
                        dialogue.put(1001, new NPCMessage(npc.getId(), Expression.DEFAULT, "Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + "."));
                        dialogue.put(5, new NPCMessage(npc.getId(), Expression.DEFAULT, task.getTask().getTip()));
                        key(1001);
                    });
                    npc(1001, "Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
                    options(1002, TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(5));
                    // TzTok-Jad
                    npc(10000, "Ah... Tzhaar... as you are undertaking this task alone, for an extra reward you could choose to slay TzTok-Jad instead of the TzHaar.");
                    options("Switch task to TzTok-Jad?", new DialogueOption("Yeah, I can beat TzTok-Jad.", key(2000)), new DialogueOption("No thanks, I'll stick with TzHaar!", key(3000)));
                    player(2000, "Yeah, I can beat TzTok-Jad.").executeAction(() -> {
                        task = player.getSlayer().getTzTokJadAssignment(master);
                        slayer.setAssignment(task);
                        /* Replace existing string in the dialogue with new task. */
                        dialogue.put(2001, new NPCMessage(npc.getId(), Expression.DEFAULT, "Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + "."));
                        dialogue.put(5, new NPCMessage(npc.getId(), Expression.DEFAULT, task.getTask().getTip()));
                        key(2001);
                    });
                    npc(2001, "Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
                    options(2002, TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(5));
                    // Cancel
                    player(3000, "No thanks, I'll stick with TzHaar!");
                }
            }
            options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(5));
            npc(5, task.getTask().getTip());
        }
    }


    public static final class BossAssignmentExtension extends Dialogue {
        public BossAssignmentExtension(final Player player, final NPC npc, final Assignment assignment) {
            super(player, npc);
            this.assignment = assignment;
        }

        private final Assignment assignment;

        @Override
        public void buildDialogue() {
            player.getSlayer().setAssignment(assignment);
            player.getSlayer().setMaster(assignment.getMaster());
            npc("Your new task is to kill " + assignment.getAmount() + " " + assignment.getTask().toString() + ".");
            options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(5));
            npc(5, assignment.getTask().getTip());
        }
    }


    public static final class PartnerAssignmentExtension extends Dialogue {
        public PartnerAssignmentExtension(final Player player, final Assignment task, final SlayerMaster master, final NPC npc) {
            super(player, npc);
            if (task.getTask() instanceof BossTask) {
                this.task = new Assignment(player, player.getSlayer(), task.getTask(), task.getTask().getEnumName(), 0, 0, null);
            } else {
                this.task = player.getSlayer().getAssignment((RegularTask) task.getTask(), master);
            }
            this.master = master;
        }

        private final Assignment task;
        private final SlayerMaster master;

        @Override
        public void buildDialogue() {
            if (task.getTask() instanceof BossTask) {
                npc("You're currently assigned to kill " + task.getTask().toString() + ". How many would you like to slay?").executeAction(() -> {
                    finish();
                    final boolean barrowsTask = task.getTask().equals(BossTask.BARROWS);
                    int maxAmount = barrowsTask ? 36 : 35;
                    for (int indx = 0; indx < CATierType.values.length; indx++) {
                        if (!player.getCombatAchievements().hasTierCompleted(CATierType.values[indx])) {
                            break;
                        }
                        maxAmount += barrowsTask ? 6 : 5;
                    }
                    final int maxAmountFinal = maxAmount;
                    player.sendInputInt("How many would you like to slay? (3 - " + maxAmount + ")", value -> {
                        final int num = Math.max(Math.min(value, maxAmountFinal), 3);
                        task.setAmount(num);
                        task.setInitialAmount(num);
                        player.getDialogueManager().start(new BossAssignmentExtension(player, npc, task));
                    });
                });
            } else {
                player.getSlayer().setAssignment(task);
                npc("Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
                if (task.getTask() == RegularTask.TZHAAR) {
                    npc("Ah... Tzhaar... as you are undertaking this task alone, for an extra reward you could choose to slay TzTok-Jad instead of the TzHaar.");
                    options("Switch task to TzTok-Jad?", new DialogueOption("Yeah, I can beat jad.", key(1000)), new DialogueOption("No thanks, I'll stick with TzHaar!", key(2000)));
                    player(1000, "Yeah, I can beat jad.").executeAction(() -> {
                        final Assignment task = player.getSlayer().getTzTokJadAssignment(master);
                        player.getSlayer().setAssignment(task);
                        /** Replace existing string in the dialogue with new task. */
                        dialogue.put(1001, new NPCMessage(npc.getId(), Expression.DEFAULT, "Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + "."));
                        dialogue.put(5, new NPCMessage(npc.getId(), Expression.DEFAULT, task.getTask().getTip()));
                        key(1001);
                    });
                    npc(1001, "Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
                    options(1002, TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(5));
                    player(2000, "No thanks, I'll stick with TzHaar!");
                }
                options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(5));
                npc(5, task.getTask().getTip());
            }
        }
    }
}
