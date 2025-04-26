package com.zenyte.game.content.rottenpotato;

/**
 * @author Christopher
 * @since 3/27/2020
 */
public enum RottenPotatoItemOption {

    NONE("None", "None"),
    UTILITY("Utility", "Utility"),
    PUNISHMENT("Punishment", "Punishment");

    private final String itemOption;
    private final String dialogueTitle;
    
    RottenPotatoItemOption(String itemOption, String dialogueTitle) {
        this.itemOption = itemOption;
        this.dialogueTitle = dialogueTitle;
    }
    
    public String getItemOption() {
        return itemOption;
    }
    
    public String getDialogueTitle() {
        return dialogueTitle;
    }
}
