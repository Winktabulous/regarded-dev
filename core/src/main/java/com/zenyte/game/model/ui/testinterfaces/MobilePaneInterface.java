package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Corey
 * @since 17:26 - 18/07/2019
 */
public class MobilePaneInterface extends Interface {
    @Override
    protected void attach() {
        put(30, "Function button");
    }

    @Override
    public void open(Player player) {
        throw new IllegalStateException("Panes cannot be opened as interfaces.");
    }

    @Override
    protected void build() {
        bind("Function button", (player, slotId, itemId, option) -> {
            if (option == 2) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        final String currentSettingName = player.getAttributes().getOrDefault("function button setting", FunctionButtonOption.DISABLE.name()).toString();
                        final MobilePaneInterface.FunctionButtonOption currentSetting = FunctionButtonOption.valueOf(currentSettingName);
                        final Dialogue.DialogueOption keyboard = new DialogueOption(FunctionButtonOption.SHOW_KEYBOARD.optionName, () -> {
                            player.getVarManager().sendBit(SettingVariables.SELECT_FUNCTIONMODE_VARBIT_ID, FunctionButtonOption.SHOW_KEYBOARD.varbitValue);
                            player.getSettings().setSetting(Setting.MOBILE_FUNCTION_BUTTON_ENABLED, 1);
                            player.getVarManager().sendBitInstant(SettingVariables.SINGLE_MOUSE_BUTTON_MODE_VARP_ID, 0);
                            player.getVarManager().sendBitInstant(SettingVariables.SHIFT_CLICK_TO_DROP_ITEMS_VARBIT_ID, 0);
                            player.getAttributes().put("function button setting", FunctionButtonOption.SHOW_KEYBOARD.name());
                        });
                        final Dialogue.DialogueOption singleTap = new DialogueOption(FunctionButtonOption.TOGGLE_SINGLE_TAP_MODE.optionName, () -> {
                            player.getVarManager().sendBit(SettingVariables.SELECT_FUNCTIONMODE_VARBIT_ID, FunctionButtonOption.SHOW_KEYBOARD.varbitValue);
                            player.getSettings().setSetting(Setting.MOBILE_FUNCTION_BUTTON_ENABLED, 0);
                            player.getVarManager().sendBitInstant(SettingVariables.SINGLE_MOUSE_BUTTON_MODE_VARP_ID, 0);
                            player.getVarManager().sendBitInstant(SettingVariables.SHIFT_CLICK_TO_DROP_ITEMS_VARBIT_ID, 0);
                            player.getAttributes().put("function button setting", FunctionButtonOption.TOGGLE_SINGLE_TAP_MODE.name());
                        });
                        final Dialogue.DialogueOption tapToDrop = new DialogueOption(FunctionButtonOption.TOGGLE_TAP_TO_DROP_MODE.optionName, () -> {
                            player.getVarManager().sendBit(SettingVariables.SELECT_FUNCTIONMODE_VARBIT_ID, FunctionButtonOption.SHOW_KEYBOARD.varbitValue);
                            player.getSettings().setSetting(Setting.MOBILE_FUNCTION_BUTTON_ENABLED, 0);
                            player.getVarManager().sendBitInstant(SettingVariables.SINGLE_MOUSE_BUTTON_MODE_VARP_ID, 0);
                            player.getVarManager().sendBitInstant(SettingVariables.SHIFT_CLICK_TO_DROP_ITEMS_VARBIT_ID, 0);
                            player.getAttributes().put("function button setting", FunctionButtonOption.TOGGLE_TAP_TO_DROP_MODE.name());
                        });
                        final Dialogue.DialogueOption disable = new DialogueOption(FunctionButtonOption.DISABLE.optionName, () -> {
                            player.getVarManager().sendBit(SettingVariables.SELECT_FUNCTIONMODE_VARBIT_ID, FunctionButtonOption.SHOW_KEYBOARD.varbitValue);
                            player.getSettings().setSetting(Setting.MOBILE_FUNCTION_BUTTON_ENABLED, 0);
                            player.getVarManager().sendBitInstant(SettingVariables.SINGLE_MOUSE_BUTTON_MODE_VARP_ID, 0);
                            player.getVarManager().sendBitInstant(SettingVariables.SHIFT_CLICK_TO_DROP_ITEMS_VARBIT_ID, 0);
                            player.getAttributes().put("function button setting", FunctionButtonOption.DISABLE.name());
                        });
                        final Dialogue.DialogueOption cancel = new DialogueOption("Cancel");
                        Dialogue.DialogueOption[] options = new DialogueOption[] {};
                        switch (currentSetting) {
                        case TOGGLE_SINGLE_TAP_MODE: 
                            options = new DialogueOption[] {keyboard, tapToDrop, disable, cancel};
                            break;
                        case TOGGLE_TAP_TO_DROP_MODE: 
                            options = new DialogueOption[] {keyboard, singleTap, disable, cancel};
                            break;
                        case SHOW_KEYBOARD: 
                            options = new DialogueOption[] {tapToDrop, singleTap, disable, cancel};
                            break;
                        case DISABLE: 
                            options = new DialogueOption[] {keyboard, tapToDrop, singleTap, cancel};
                            break;
                        }
                        final String optionName = currentSetting == FunctionButtonOption.DISABLE ? "disabled" : currentSetting.optionName.toLowerCase();
                        options("Select function-mode (currently " + optionName + ")", options);
                    }
                });
                return;
            }
            FunctionButtonOption.valueOf(player.getAttributes().get("function button setting").toString()).executeFunction(player);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.MOBILE_PANE;
    }


    public enum FunctionButtonOption {
        TOGGLE_SINGLE_TAP_MODE("Single-tap", 2) {
            @Override
            public void executeFunction(final Player player) {
                player.getSettings().toggleSetting(Setting.MOBILE_FUNCTION_BUTTON_ENABLED);
                player.getVarManager().flipBit(SettingVariables.SINGLE_MOUSE_BUTTON_MODE_VARP_ID);
            }
        },
        TOGGLE_TAP_TO_DROP_MODE("Tap-to-drop", 1) {
            @Override
            public void executeFunction(final Player player) {
                player.getSettings().toggleSetting(Setting.MOBILE_FUNCTION_BUTTON_ENABLED);
                player.getVarManager().flipBit(SettingVariables.SHIFT_CLICK_TO_DROP_ITEMS_VARBIT_ID);
            }
        },
        SHOW_KEYBOARD("Keyboard", 3) {
            @Override
            public void executeFunction(final Player player) {
                player.getPacketDispatcher().sendClientScript(1980, 0, 80);
            }
        },
        DISABLE("Disable", 0) {
            @Override
            public void executeFunction(final Player player) {
            }
        };
        private final String optionName;
        private final int varbitValue;

        public abstract void executeFunction(final Player player);

        FunctionButtonOption(String optionName, int varbitValue) {
            this.optionName = optionName;
            this.varbitValue = varbitValue;
        }

        public String getOptionName() {
            return optionName;
        }

        public int getVarbitValue() {
            return varbitValue;
        }
    }
}
