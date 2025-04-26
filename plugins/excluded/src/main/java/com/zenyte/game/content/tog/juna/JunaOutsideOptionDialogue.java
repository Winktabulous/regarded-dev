package com.zenyte.game.content.tog.juna;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since September 07 2020
 */
public class JunaOutsideOptionDialogue extends Dialogue {
    public static final String TOG_REMINDER_ENABLED_ATTR = "tog reminder enabled";
    private static final int REMINDER_TOGGLE_KEY = 5;

    public JunaOutsideOptionDialogue(@NotNull final Player player, final int npcId) {
        super(player, npcId);
    }

    @Override
    public void buildDialogue() {
        final boolean reminderEnabled = player.getBooleanAttribute(TOG_REMINDER_ENABLED_ATTR);
        final String reminderToggleOption = reminderEnabled ? "I don\'t want any messages reminding me to return here." : "I\'d like to receive messages prompting me to return here.";
        options(TITLE, new DialogueOption("Can I enter the cave?", () -> player.getDialogueManager().start(new JunaEnterDialogue(player, npcId))), new DialogueOption(reminderToggleOption, key(REMINDER_TOGGLE_KEY)), new DialogueOption("Never mind."));
        player(REMINDER_TOGGLE_KEY, reminderToggleOption);
        final String reminderNpcChat = "Very well, " + (reminderEnabled ? "when you are eligible to drink from the Tears, you shall be reminded daily." : "it will be up to you to remember when you can return. It is good that you are willing to take responsibility for yourself in this way.");
        npc(reminderNpcChat).executeAction(() -> player.toggleBooleanAttribute(TOG_REMINDER_ENABLED_ATTR));
    }
}
