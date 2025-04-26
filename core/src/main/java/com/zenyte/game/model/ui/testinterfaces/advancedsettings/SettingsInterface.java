package com.zenyte.game.model.ui.testinterfaces.advancedsettings;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.StaticInitializer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.zenyte.game.world.entity.player.VarManager.appendPersistentVarbit;
import static com.zenyte.game.world.entity.player.VarManager.appendPersistentVarp;

/**
 * @author Kris | 10/06/2022
 */
@StaticInitializer
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class SettingsInterface extends Interface {

    static final int SHOW_LESS_OR_MORE_INFORMATION_VARBIT = 9665;
    public static final int SETTINGS_SEARCH_LEFT_VARBIT = 9638;
    public static final int SETTINGS_SEARCH_RIGHT_VARBIT = 9639;
    static final int SETTINGS_CURRENT_CATEGORY_VARBIT = 9656;
    public static final int MUSIC_VOLUME_MUTED_VARBIT_ID = 9666;
    public static final int SOUND_EFFECT_VOLUME_MUTED_VARBIT_ID = 9674;
    public static final int AREA_EFFECT_MUTED_VARBIT_ID = 9675;
    public static final int MINIMUM_LOOT_ITEM_VALUE_VARBIT_ID = 5400;
    public static final int MINIMUM_DROP_ITEM_VALUE_VARBIT_ID = 5412;
    public static final int MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID = 6091;
    static final int CURRENT_SETTING_VARBIT_ID = 9657;
    public static final int COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID = 11959;
    public static final int FRIEND_LOGIN_LOGOUT_MESSAGE_TIMEOUT_VARBIT_ID = 1627;
    public static final int SIDE_PANELS_VARBIT_ID = 4607;

    static {
        for (final Map<Integer, Integer> varpMaps : SettingVariables.ALL_VARP_SETTINGS) {
            for (final Integer value : varpMaps.values()) {
                appendPersistentVarp(value);
            }
        }
        for (final Map<Integer, Integer> varbitMap : SettingVariables.ALL_VARBIT_SETTINGS) {
            for (final Integer value : varbitMap.values()) {
                appendPersistentVarbit(value);
            }
        }
        for (SliderSettingVarps value : SliderSettingVarps.values) {
            appendPersistentVarp(value.getVarp(), value.getDefaultValue());
        }
        for (ToggleSettingVarps value : ToggleSettingVarps.values) {
            appendPersistentVarp(value.getVarp(), value.getDefaultValue());
        }
        for (ToggleSettingVarBits value : ToggleSettingVarBits.values) {
            appendPersistentVarbit(value.getVarbit(), value.getDefaultValue());
        }
        for (OtherVarBits value : OtherVarBits.values) {
            appendPersistentVarbit(value.getVarbit(), value.getDefaultValue());
        }
    }

    @Override
    protected void attach() {
        put(4, "Close");
        put(5, "Show less/more information");
        put(10, "Search");
        put(19, "Setting");
        put(21, "Slider");
        put(23, "Category");
        put(25, "Cancel drop-down selection");
        put(28, "Drop-down menu");
    }

    @Override
    protected void build() {
        bind("Close", player -> {
            player.getInterfaceHandler().closeInterface(getInterface());
            /* Also closes modals. */
            player.getInterfaceHandler().closeInterfaces();
        });
        bind("Show less/more information",
                player -> player.getVarManager().flipBit(SHOW_LESS_OR_MORE_INFORMATION_VARBIT));
        bind("Search", player -> {
            Settings.setCurrentSelectedCategory(player, null);
            player.getVarManager().sendBitInstant(SETTINGS_SEARCH_LEFT_VARBIT, 1);
            player.getVarManager().sendBitInstant(SETTINGS_SEARCH_RIGHT_VARBIT, 1);
        });
        bind("Setting", (player, slotId, itemId, option) -> {
            final SettingCategory category = Settings.getCurrentSelectedCategory(player);
            final List<Setting> settings = category != null ? category.getSettings() : Settings.ALL_SETTINGS;
            final Setting setting = settings.get(slotId);
            if (!setting.checkSetting(player)) return;
            switch (setting.getType()) {
                case TOGGLE:
                    toggle(player, setting);
                    break;
                case DROPDOWN:
                    dropDown(player, setting, false);
                    break;
                case KEYBIND:
                    dropDown(player, setting, true);
                    break;
                case INPUT:
                    input(player, setting);
                    break;
                case BUTTON:
                    button(player, setting);
                    break;
                case COLOUR:
                    colour(player, setting);
                    break;
                default:
                    throw new IllegalStateException("Illegal setting type '" + setting.getType() + "'.");
            }
        });
        bind("Slider", (player, slotId, itemId, option) -> {
            final SettingCategory category = Settings.getCurrentSelectedCategory(player);
            final SliderPosition position = Settings.findSliderPosition(category, slotId);
            slider(player, position.getSlider(), slotId - position.getStartingPosition());
        });
        bind("Category", (player, slotId, itemId, option) -> {
            Settings.setCurrentSelectedCategory(player, Settings.getCategory(slotId));
            player.getVarManager().sendBitInstant(SETTINGS_SEARCH_LEFT_VARBIT, 0);
            player.getVarManager().sendBitInstant(SETTINGS_SEARCH_RIGHT_VARBIT, 0);
            player.getVarManager().sendBitInstant(SETTINGS_CURRENT_CATEGORY_VARBIT, slotId);
            player.getPacketDispatcher().sendClientScript(2158);
        });
        bind("Cancel drop-down selection", player -> Settings.setCurrentDropdownSetting(player, null));
        bind("Drop-down menu", (player, slotId, itemId, option) -> {
            final Setting setting = Settings.getCurrentDropdownSetting(player);
            if (setting == null) return;
            if (!setting.checkSetting(player)) return;
            final int selectedEntryIndex = slotId / 3;
            final boolean keybind = Settings.isCurrentDropdownSettingKeybind(player);
            if (setting.getStructId() == SettingStructs.GAME_CLIENT_LAYOUT_STRUCT_ID) {
                if (selectedEntryIndex == 0) {
                    if (player.getInterfaceHandler().getPane() != PaneType.FIXED) {
                        player.getInterfaceHandler().sendPane(player.getInterfaceHandler().getPane(), PaneType.FIXED);
                    }
                } else if (selectedEntryIndex == 1) {
                    if (player.getInterfaceHandler().getPane() != PaneType.RESIZABLE) {
                        player.getInterfaceHandler().sendPane(player.getInterfaceHandler().getPane(),
                                PaneType.RESIZABLE);
                        player.getVarManager().sendBitInstant(SIDE_PANELS_VARBIT_ID, 0);
                    }
                } else if (selectedEntryIndex == 2) {
                    if (player.getInterfaceHandler().getPane() != PaneType.SIDE_PANELS) {
                        player.getInterfaceHandler().sendPane(player.getInterfaceHandler().getPane(),
                                PaneType.SIDE_PANELS);
                        player.getVarManager().sendBitInstant(SIDE_PANELS_VARBIT_ID, 1);
                    }
                }
                return;
            }
            if (keybind) {
                final List<Setting> keybinds = Settings.SETTINGS_BY_TYPE.get(SettingType.KEYBIND);
                for (final Setting otherSetting : keybinds) {
                    if (otherSetting == setting) continue;
                    if (SettingVariables.getVariableValue(otherSetting, player) == selectedEntryIndex) {
                        SettingVariables.setVariableValue(otherSetting, player, 0);
                    }
                }
            }
            SettingVariables.setVariableValue(setting, player, selectedEntryIndex);
        });
    }

    @Override
    public void open(Player player) {
        if (player.isLocked()) {
            return;
        }
        if (player.isUnderCombat()) {
            player.sendMessage("You can't do this while in combat.");
            return;
        }
        player.getVarManager().sendBitInstant(SETTINGS_SEARCH_LEFT_VARBIT, 0);
        player.getVarManager().sendBitInstant(SETTINGS_SEARCH_RIGHT_VARBIT, 0);
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("Setting"), 0,
                Settings.ALL_SETTINGS.size(), AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("Slider"), 0,
                Settings.SLIDER_NOTCH_COUNT_SUM, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("Category"), 0,
                Settings.CATEGORIES.size(), AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("Drop-down menu"), 0,
                Settings.DROPDOWN_ENTRIES_MAX_COUNT * 3, AccessMask.CLICK_OP1);
        final SettingCategory currentCategory = Settings.getCurrentSelectedCategory(player);
        if (currentCategory == null) {
            player.getVarManager().sendBitInstant(SETTINGS_CURRENT_CATEGORY_VARBIT, 0);
        }
    }

    @Override
    public void close(Player player, Optional<GameInterface> replacement) {
        player.getPacketDispatcher().sendClientScript(2158);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ADVANCED_SETTINGS;
    }

    private void toggle(Player player, Setting setting) {
        switch (setting.getStructId()) {
            case SettingStructs.SHOW_DATA_ORBS_STRUCT_ID:
                if (player.getVarManager().getBitValue(SettingVariables.SHOW_DATA_ORBS_VARBIT_ID) == 1) {
                    GameInterface.ORBS.open(player);
                } else {
                    player.getInterfaceHandler().closeInterface(GameInterface.ORBS);
                }
                break;
            case SettingStructs.LOOT_DROP_NOTIFICATIONS_STRUCT_ID:
                if (player.getVarManager().getBitValue(MINIMUM_LOOT_ITEM_VALUE_VARBIT_ID) == 0) {
                    final Setting otherSetting =
                            Settings.findSettingByStructId(SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_LOOT_NOTIFICATION_STRUCT_ID);
                    input(player, otherSetting);
                    return;
                }
                break;
            case SettingStructs.DROP_ITEM_WARNING_STRUCT_ID:
                if (player.getVarManager().getBitValue(MINIMUM_DROP_ITEM_VALUE_VARBIT_ID) == 0) {
                    final Setting otherSetting =
                            Settings.findSettingByStructId(SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_DROP_ITEM_WARNING_STRUCT_ID);
                    input(player, otherSetting);
                    return;
                }
                break;
            case SettingStructs.COLLECTION_LOG_NEW_ADDITION_NOTIFICATION_STRUCT_ID: {
                final int currentValue = player.getVarManager().getBitValue(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID);
                final int newValue = (currentValue + 1) & 0x1;
                player.getVarManager().sendBitInstant(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID,
                        (currentValue & ~0x1) | newValue);
                return;
            }
            case SettingStructs.COLLECTION_LOG_NEW_ADDITION_POPUP_STRUCT_ID: {
                final int currentValue = player.getVarManager().getBitValue(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID);
                final int newValue = (currentValue + 2) & 0x2;
                player.getVarManager().sendBitInstant(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID,
                        (currentValue & ~0x2) | newValue);
                return;
            }
        }
        final int currentValue = SettingVariables.getVariableValue(setting, player);
        SettingVariables.setVariableValue(setting, player, currentValue == 1 ? 0 : 1);
    }

    private void dropDown(Player player, Setting setting, boolean keybind) {
        Settings.setCurrentDropdownSetting(player, setting);
        Settings.setCurrentDropdownSettingKeybind(player, keybind);
    }

    private void slider(Player player, Setting setting, int value) {
        switch (setting.getStructId()) {
            case SettingStructs.MUSIC_VOLUME_STRUCT_ID:
                if (value != 0) {
                    if (SettingVariables.getVariableValue(setting, player) == 0) {
                        player.getMusic().restartCurrent();
                    }
                }
                SettingVariables.setVariableValue(setting, player, value * 5);
                player.getVarManager().sendBitInstant(MUSIC_VOLUME_MUTED_VARBIT_ID, value == 0 ? 1 : 0);
                break;
            case SettingStructs.SOUND_EFFECT_VOLUME_STRUCT_ID:
                SettingVariables.setVariableValue(setting, player, value * 5);
                player.getVarManager().sendBitInstant(SOUND_EFFECT_VOLUME_MUTED_VARBIT_ID, value == 0 ? 1 : 0);
                break;
            case SettingStructs.AREA_SOUND_VOLUME_STRUCT_ID:
                SettingVariables.setVariableValue(setting, player, value * 5);
                player.getVarManager().sendBitInstant(AREA_EFFECT_MUTED_VARBIT_ID, value == 0 ? 1 : 0);
                break;
            case SettingStructs.SCREEN_BRIGHTNESS_STRUCT_ID:
                SettingVariables.setVariableValue(setting, player, value * 5);
                break;
            default:
                SettingVariables.setVariableValue(setting, player, value);
                break;
        }
    }

    private void button(Player player, Setting setting) {
        switch (setting.getStructId()) {
            case SettingStructs.CLEAR_YOUR_HIGHLIGHTED_TILES_STRUCT_ID:
            case SettingStructs.RESET_INTERFACE_SCALING_STRUCT_ID:
            case SettingStructs.RESTORE_MINIMAP_ZOOM_STRUCT_ID:
                throw new IllegalStateException("Enhanced client not supported.");
            case SettingStructs.RESTORE_DEFAULT_KEYBINDS_STRUCT_ID:
                promptDefaultKeybinds(player);
                break;
            case SettingStructs.RESET_OPAQUE_CHAT_COLOURS_STRUCT_ID:
                setAllSettings(player, Settings.OPAQUE_COLOUR_SETTINGS, 0);
                break;
            case SettingStructs.RESET_TRANSPARENT_CHAT_COLOURS_STRUCT_ID:
                setAllSettings(player, Settings.TRANSPARENT_COLOUR_SETTINGS, 0);
                break;
            case SettingStructs.RESET_QUEST_LIST_TEXT_COLOURS_STRUCT_ID:
                setAllSettings(player, Settings.QUEST_COLOUR_SETTINGS, 0);
                break;
            case SettingStructs.RESET_SPLIT_CHAT_COLOURS_STRUCT_ID:
                setAllSettings(player, Settings.SPLIT_COLOUR_SETTINGS, 0);
                break;
            case SettingStructs.ENABLE_TELEPORT_WARNINGS_STRUCT_ID:
                setAllSettings(player, Settings.TELEPORT_WARNING_SETTINGS, 1);
                break;
            case SettingStructs.DISABLE_TELEPORT_WARNINGS_STRUCT_ID:
                setAllSettings(player, Settings.TELEPORT_WARNING_SETTINGS, 0);
                break;
            case SettingStructs.ENABLE_TABLET_WARNINGS_STRUCT_ID:
                setAllSettings(player, Settings.TABLET_WARNING_SETTINGS, 0);
                break;
            case SettingStructs.DISABLE_TABLET_WARNINGS_STRUCT_ID:
                setAllSettings(player, Settings.TABLET_WARNING_SETTINGS, 1);
                break;
        }
    }

    private void setAllSettings(Player player, List<Setting> settings, int value) {
        for (final Setting setting : settings) {
            SettingVariables.setVariableValue(setting, player, value);
        }
    }

    private void input(Player player, Setting setting) {
        switch (setting.getStructId()) {
            case SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_LOOT_NOTIFICATION_STRUCT_ID:
                player.sendInputInt("Set threshold value:", value -> {
                    final int result = Math.min(value, 500_000_000);
                    player.getVarManager().sendBitInstant(SettingVariables.LOOT_DROP_NOTIFICATIONS_VARBIT_ID,
                            result == 0 ? 0 : 1);
                    player.getVarManager().sendBitInstant(MINIMUM_LOOT_ITEM_VALUE_VARBIT_ID, result);
                });
                break;
            case SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_DROP_ITEM_WARNING_STRUCT_ID:
                player.sendInputInt("Set threshold value:", value -> {
                    final int result = Math.min(value, 500_000_000);
                    player.getVarManager().sendBitInstant(SettingVariables.DROP_ITEM_WARNING_VARBIT_ID, result == 0 ?
                            0 : 1);
                    player.getVarManager().sendBitInstant(MINIMUM_DROP_ITEM_VALUE_VARBIT_ID, result);
                    if (value == 0) {
                        player.getVarManager().sendBitInstant(SettingVariables.UNTRADEABLE_LOOT_NOTIFICATIONS_VARBIT_ID, 0);
                    }
                });
                break;
            case SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_ALCHEMY_SPELLS_WARNING_STRUCT_ID:
                player.sendInputInt("Set threshold value:", value -> {
                            player.getVarManager().sendBitInstant(MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID, value);
                    });
                break;
        }
    }

    private void colour(Player player, Setting setting) {
        player.getVarManager().sendBitInstant(CURRENT_SETTING_VARBIT_ID, setting.getId());
        final int currentColour = SettingVariables.getVariableValue(setting, player);
        final int defaultColour = setting.getDefaultColour();
        player.getInterfaceHandler().sendInterface(GameInterface.COLOUR_PICKER.getId(),
                InterfacePosition.COLOUR_PICKER.getResizableComponent(),
                PaneType.ADVANCED_SETTINGS, false);
        player.getPacketDispatcher().sendClientScript(
                4185,
                (getInterface().getId() << 16) | InterfacePosition.COLOUR_PICKER.getResizableComponent(),
                (currentColour == 0 ? defaultColour : currentColour) - 1
        );
        player.awaitInputInt(value -> {
            player.getInterfaceHandler().closeInterfaceSpecific(
                    InterfacePosition.COLOUR_PICKER.getResizableComponent(),
                    PaneType.ADVANCED_SETTINGS
            );
            if (value != Integer.MAX_VALUE) {
                SettingVariables.setVariableValue(setting, player, value + 1);
            }
            final SettingCategory category = Settings.getCurrentSelectedCategory(player);
            if (category != null) return;
            player.getVarManager().sendBitInstant(SETTINGS_SEARCH_LEFT_VARBIT, 1);
            player.getVarManager().sendBitInstant(SETTINGS_SEARCH_RIGHT_VARBIT, 1);
            player.getPacketDispatcher().sendClientScript(4020);
        });
    }

    private void promptDefaultKeybinds(Player player) {
        player.getInterfaceHandler().closeInterface(getInterface());
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Are you sure you want to reset your keybinds?",
                        new DialogueOption("Yes.", () -> {
                            setDefaultKeybinds(player);
                            GameInterface.ADVANCED_SETTINGS.open(player);
                        }),
                        new DialogueOption("No.", () -> GameInterface.ADVANCED_SETTINGS.open(player)));
            }
        });
    }

    public static void setDefaultKeybinds(Player player) {
        for (final Map.Entry<Setting, Integer> entry : Settings.DEFAULT_KEYBINDS.entrySet()) {
            final Setting setting = entry.getKey();
            final int value = entry.getValue();
            SettingVariables.setVariableValue(setting, player, value);
        }
    }
}
