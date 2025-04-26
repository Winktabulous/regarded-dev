package com.zenyte.game.content.skills.farming.contract;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 4/10/2020
 */
public class GuildmasterJaneDialogue extends Dialogue {
    private static final Item reward = new Item(ItemId.SEED_PACK);
    private static final int ABOUT_YOURSELF_KEY = 5;
    private static final int ASK_CONTRACT_KEY = 10;
    private static final int EASY_KEY = 15;
    private static final int MEDIUM_KEY = 20;
    private static final int HARD_KEY = 25;
    private static final int EASY_ASSIGNED_CONTRACT_KEY = 30;
    private static final int MEDIUM_ASSIGNED_CONTRACT_KEY = 35;
    private static final int HARD_ASSIGNED_CONTRACT_KEY = 40;
    private static final int OPTIONS_AFTER_CONTRACT_KEY = 45;
    private static final int EASIER_CONTRACT_KEY = 50;
    private static final int NO_EASIER_CONTRACT_KEY = 55;
    private static final int ALREADY_HAVE_CONTRACT_KEY = 60;
    private static final int COMPLETED_CONTRACT_KEY = 65;
    private static final int COMPLETED_CONTRACTS_COUNT_KEY = 70;
    private final JaneDialogueType dialogueType;

    public GuildmasterJaneDialogue(Player player, NPC npc, final JaneDialogueType dialogueType) {
        super(player, npc);
        this.dialogueType = dialogueType;
    }

    @Override
    public void buildDialogue() {
        final FarmingContract easyContract = FarmingContract.generateContract(player, FarmingContractDifficulty.EASY);
        final FarmingContract mediumContract = FarmingContract.generateContract(player, FarmingContractDifficulty.MEDIUM);
        final FarmingContract hardContract = FarmingContract.generateContract(player, FarmingContractDifficulty.HARD);
        final boolean contractCompleted = player.getBooleanAttribute(FarmingContract.COMPLETED_ATTR);
        final int askForContractStage = dialogueType == JaneDialogueType.FULL || contractCompleted ? ASK_CONTRACT_KEY : 1;
        final int completedContractStage = dialogueType == JaneDialogueType.FULL || !contractCompleted ? COMPLETED_CONTRACT_KEY : 1;
        final int completedContractAmount = player.getNumericAttribute(FarmingContract.COMPLETED_COUNT_ATTR).intValue();
        if (dialogueType == JaneDialogueType.FULL) {
            npc("Welcome to the farming guild. How may I help you?");
            if (player.getSkills().getLevel(SkillConstants.FARMING) < FarmingContract.MINIMUM_LEVEL) {
                options(TITLE, new DialogueOption("Tell me about yourself.", key(ABOUT_YOURSELF_KEY)), new DialogueOption("I'm just passing by."));
            } else {
                final Dialogue.DialogueOption secondOption = contractCompleted ? new DialogueOption("I've completed " +
                        "my farming contract.", key(COMPLETED_CONTRACT_KEY)) : new DialogueOption("I'd like a farming contract.", key(ASK_CONTRACT_KEY));
                options(TITLE, new DialogueOption("Tell me about yourself.", key(ABOUT_YOURSELF_KEY)), secondOption, completedContractAmount > 0 ? new DialogueOption("How many farming contracts have I completed?", key(COMPLETED_CONTRACTS_COUNT_KEY)) : null, new DialogueOption("I'm just passing by."));
            }
            player(ABOUT_YOURSELF_KEY, "Tell me about yourself.");
            npc("My name is Jane, and I am the founder of the Farming Guild.");
            npc("Is there anything else I can help you with?").executeAction(() -> setKey(2));
        }
        askForContract(askForContractStage, easyContract, mediumContract, hardContract);
        completedContract(completedContractStage);
        completedContractsCount(completedContractAmount);
        anythingEasier(easyContract, mediumContract);
    }

    private void askForContract(final int startStage, final FarmingContract easyContract, final FarmingContract mediumContract, final FarmingContract hardContract) {
        final String askForContractString = dialogueType == JaneDialogueType.NEW_CONTRACT ? "Yes please." : "I'd like" +
                " a farming contract.";
        player(startStage, askForContractString).executeAction(() -> {
            if (player.getAttributes().get(FarmingContract.CONTRACT_ATTR) != null) {
                setKey(ALREADY_HAVE_CONTRACT_KEY);
            }
        });
        if (player.getAttributes().get(FarmingContract.CONTRACT_ATTR) != null) {
            final FarmingContract currentContract = FarmingContract.getByName((String) player.getAttributes().get(FarmingContract.CONTRACT_ATTR));
            final String productName = getFormattedName(currentContract);
            npc(ALREADY_HAVE_CONTRACT_KEY, "You already have a contract of growing " + productName + ".").executeAction(() -> setKey(OPTIONS_AFTER_CONTRACT_KEY));
        }
        npc("What kind of contract would you like?");
        options(TITLE, new DialogueOption("Easy Contract (Requires 45 Farming)", key(EASY_KEY)), new DialogueOption("Medium Contract (Requires 65 Farming)", key(MEDIUM_KEY)), new DialogueOption("Hard Contract (Requires 85 Farming)", key(HARD_KEY)));
        player(EASY_KEY, "Could I have an easy contract please?").executeAction(() -> {
            assign(player, FarmingContractDifficulty.EASY, easyContract);
            setKey(EASY_ASSIGNED_CONTRACT_KEY);
        });
        player(MEDIUM_KEY, "Could I have a medium contract please?").executeAction(() -> {
            assign(player, FarmingContractDifficulty.MEDIUM, mediumContract);
            setKey(MEDIUM_ASSIGNED_CONTRACT_KEY);
        });
        player(HARD_KEY, "Could I have a hard contract please?").executeAction(() -> {
            assign(player, FarmingContractDifficulty.HARD, hardContract);
            setKey(HARD_ASSIGNED_CONTRACT_KEY);
        });
        npc(EASY_ASSIGNED_CONTRACT_KEY, "Please could you grow " + getFormattedName(easyContract) + " for us? I'll " +
                "reward you once you have " + getCompletionType(easyContract)).executeAction(() -> setKey(OPTIONS_AFTER_CONTRACT_KEY));
        npc(MEDIUM_ASSIGNED_CONTRACT_KEY, "Please could you grow " + getFormattedName(mediumContract) + " for us? " +
                "I'll reward you once you have " + getCompletionType(mediumContract)).executeAction(() -> setKey(OPTIONS_AFTER_CONTRACT_KEY));
        npc(HARD_ASSIGNED_CONTRACT_KEY, "Please could you grow " + getFormattedName(hardContract) + " for us? I'll " +
                "reward you once you have " + getCompletionType(hardContract)).executeAction(() -> setKey(OPTIONS_AFTER_CONTRACT_KEY));
    }

    private void completedContract(final int startStage) {
        player(startStage, "I've completed my farming contract.");
        npc("You'll be wanting a reward then.").executeAction(() -> {
            final FarmingContract currentContract = FarmingContract.getByName(player.getAttributes().get(FarmingContract.CONTRACT_ATTR).toString());
            currentContract.reward(player);
        });
        item(reward, "Guildmaster Jane has give you a seed pack.");
        npc("Now, would you like another contract?");
        options(TITLE, new DialogueOption("Yes please.", () -> player.getDialogueManager().start(new GuildmasterJaneDialogue(player, npc, JaneDialogueType.NEW_CONTRACT))), new DialogueOption("No please."));
    }

    private void completedContractsCount(@NonNegative final int completedContractAmount) {
        player(COMPLETED_CONTRACTS_COUNT_KEY, "How many farming contracts have I completed?");
        npc("You've completed " + completedContractAmount + " farming contract" + Utils.plural(completedContractAmount) + ".");
        npc("Is there anything else I can help you with?").executeAction(() -> setKey(2));
    }

    private void anythingEasier(final FarmingContract easyContract, final FarmingContract mediumContract) {
        options(OPTIONS_AFTER_CONTRACT_KEY, TITLE, new DialogueOption("Do you have anything easier?", key(EASIER_CONTRACT_KEY)), new DialogueOption("Thank you."));
        player(EASIER_CONTRACT_KEY, "Do you have anything easier?").executeAction(() -> {
            final FarmingContractDifficulty currentDifficulty = FarmingContractDifficulty.getByName((String) player.getAttributes().get(FarmingContract.CONTRACT_DIFFICULTY_ATTR));
            if (currentDifficulty == FarmingContractDifficulty.EASY) {
                setKey(NO_EASIER_CONTRACT_KEY);
            } else {
                final FarmingContractDifficulty newDifficulty = currentDifficulty.decrease(1);
                if (newDifficulty == FarmingContractDifficulty.MEDIUM) {
                    assign(player, newDifficulty, mediumContract);
                    setKey(MEDIUM_ASSIGNED_CONTRACT_KEY);
                } else {
                    assign(player, newDifficulty, easyContract);
                    setKey(EASY_ASSIGNED_CONTRACT_KEY);
                }
            }
        });
        npc(NO_EASIER_CONTRACT_KEY, "I'm afraid I don't have any easier contracts. Finish your current contract to " +
                "get a new one.");
    }

    private void assign(@NotNull final Player player, @NotNull final FarmingContractDifficulty difficulty, FarmingContract contract) {
        if (contract == null || player.getSkills().getLevel(SkillConstants.FARMING) < difficulty.getLevelRequirement()) {
            player.sendMessage("You're not high enough level to take this type of contract.");
            player.getDialogueManager().finish();
            return;
        }
        FarmingContract.assign(player, difficulty, contract);
        player.getVarManager().sendBit(GuildmasterJane.JANE_VARBIT, 1);
    }

    private String getFormattedName(final FarmingContract contract) {
        if (contract == null) return "";
        return contract.getDisplayName();
    }

    private String getCompletionType(final FarmingContract contract) {
        if (contract == null) {
            return "";
        }
        return contract.getProduct().getCheckHealthXP() > 0 ? "checked their health." : "harvested them.";
    }


    public enum JaneDialogueType {
        FULL, CONTRACT_OPTION, NEW_CONTRACT
    }
}
