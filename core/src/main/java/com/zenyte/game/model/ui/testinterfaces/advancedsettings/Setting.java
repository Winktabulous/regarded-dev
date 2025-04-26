package com.zenyte.game.model.ui.testinterfaces.advancedsettings;

import com.google.common.base.Preconditions;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.IntEnum;
import mgi.types.config.enums.StringEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kris | 10/06/2022
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "SpellCheckingInspection"})
public class Setting {
    private static final int IS_DESKTOP_PARAM = 739;
    private static final int IS_MOBILE_PARAM = 740;
    private static final int IS_NON_IRONMAN_PARAM = 741;
    private static final int IS_IRONMAN_PARAM = 742;
    private static final int ID_PARAM = 1077;
    private static final int TYPE_PARAM = 1078;
    private static final int PRE_REQUIREMENTS_ENUM_ID_PARAM = 1080;
    private static final int PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM = 1081;
    private static final int PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM = 1082;
    private static final int PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM = 1083;
    private static final int NAME_PARAM = 1086;
    private static final int SEARCH_KEYWORDS_PARAM = 1088;
    private static final int DROPDOWN_ENTRIES_PARAM = 1091;
    private static final int DROPDOWN_ENTRIES_MOBILE_PARAM = 1092;
    private static final int SLIDER_NOTCH_COUNT_PARAM = 1101;
    private static final int IS_SLIDER_TRANSMITTED_PARAM = 1105;
    private static final int HAS_CUSTOM_REQUIREMENTS_PARAM = 1115;
    private static final int HAS_TOGGLE_INVERSED_PARAM = 1084;
    private static final int CHOOSE_TRANSMIT_PARAM = 1085;
    private static final int MOBILE_NAME_PARAM = 1087;
    private static final int DESCRIPTION_PARAM = 1096;
    private static final int KEYBIND_SPITE_PARAM = 1098;
    private static final int KEYBIND_SPRITE_SIZE_COORDGRID_PARAM = 1099;
    private static final int SLIDER_SECTORS_PARAM = 1102;
    private static final int SLIDER_SECTOR_TEXT_ENUM_ID_PARAM = 1103;
    private static final int SLIDER_CUSTOM_ON_OP_PARAM = 1106;
    private static final int SLIDER_CUSTOM_SETPOS_PARAM = 1107;
    private static final int IS_SLIDER_DRAGGABLE_PARAM = 1108;
    private static final int SLIDER_DEADZONE_PARAM = 1109;
    private static final int SLIDER_DEADTIME_PARAM = 1110;
    private static final int SLIDER_INPUT_SINGULAR_PARAM = 1111;
    private static final int SLIDER_INPUT_PLURAL_PARAM = 1112;
    private static final int SLIDER_INPUT_ZERO_PARAM = 1113;
    private static final int SLIDER_OP_CHECKER_MESSAGE_PARAM = 1116;
    private static final int SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM = 1117;
    private static final int HAS_COLLAPSIBLE_INFOBOX_PARAM = 1118;
    private static final int HIDE_DESCRIPTION_PARAM = 1119;
    private static final int IS_ENHANCED_CLIENT_PARAM = 1157;
    private static final int CUSTOM_NAME_EXTRA_TEXT_PARAM = 1158;
    private static final int IS_MOBILE_ALWAYS_ENABLED_PARAM = 1186;
    private static final int HAS_CUSTOM_CHECK_PARAM = 1229;
    private static final int DEFAULT_COLOUR_PARAM = 1230;
    private static final int IS_NON_DESKTOP_ONLY_PARAM = 1271;
    private static final int IS_LEAGUE_WORLD_ONLY_PARAM = 1272;
    private static final int IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM = 1273;

    private final int structId;
    private final int id;
    private final int typeId;
    private final String name;
    private final String searchKeywords;
    private final boolean sliderTransmitted;
    private final int sliderNotchCount;
    private final boolean desktop;
    private final boolean mobile;
    private final boolean nonIronman;
    private final boolean ironman;
    private final boolean hasCustomRequirements;
    private final int preRequirementsEnumId;
    private final int preRequirementsValuesEnumId;
    private final int inversedPreRequirementsEnumId;
    private final int inversedPreRequirementsValuesEnumId;
    private final boolean toggleInversed;
    private final boolean chooseTransmit;
    private final String mobileName;
    private final String description;
    private final int keyBindSprite;
    private final int keyBindSpriteCoordGrid;
    private final int sliderSectors;
    private final int sliderSectorsTextEnumId;
    private final boolean sliderCustomOnOpScript;
    private final boolean sliderCustomSetPos;
    private final boolean sliderDraggable;
    private final int sliderDeadZone;
    private final int sliderDeadTime;
    private final String inputSingular;
    private final String inputPlural;
    private final String inputZero;
    private final String opCheckerMessage;
    private final String mobileOpCheckerMessage;
    private final boolean collapsibleInfobox;
    private final boolean hideDescription;
    private final boolean enhancedClientOnly;
    private final boolean customNameExtraText;
    private final boolean mobileAlwaysEnabled;
    private final boolean hasCustomCheck;
    private final int defaultColour;
    private final boolean nonDesktopOnly;
    private final boolean leagueWorldOnly;
    private final boolean leagueWorldEnhancedClientOnly;
    private final int dropdownEntriesEnumId;
    private final int mobileDropDownEntriesEnumId;

    @NotNull
    private final SettingType type;
    @Nullable
    private final IntEnum preRequirementsEnum;
    @Nullable
    private final IntEnum preRequirementsValuesEnum;
    @Nullable
    private final IntEnum inversedPreRequirementsEnum;
    @Nullable
    private final IntEnum inversedPreRequirementsValuesEnum;
    @Nullable
    private final StringEnum sliderSectorsTextEnum;
    @Nullable
    private final StringEnum dropdownEntriesEnum;
    @Nullable
    private final StringEnum mobileDropdownEntriesEnum;

    Setting(StructDefinitions struct) {
        this.structId = struct.getId();
        this.id = struct.getParamAsInt(ID_PARAM);
        this.typeId = struct.getParamAsInt(TYPE_PARAM);
        this.name = struct.getParamAsString(NAME_PARAM);
        this.searchKeywords = struct.getParamAsString(SEARCH_KEYWORDS_PARAM);
        this.sliderTransmitted = struct.getParamAsBoolean(IS_SLIDER_TRANSMITTED_PARAM);
        this.sliderNotchCount = struct.getParamAsInt(SLIDER_NOTCH_COUNT_PARAM);
        this.desktop = struct.getParamAsBoolean(IS_DESKTOP_PARAM);
        this.mobile = struct.getParamAsBoolean(IS_MOBILE_PARAM);
        this.ironman = struct.getParamAsBoolean(IS_IRONMAN_PARAM);
        this.nonIronman = struct.getParamAsBoolean(IS_NON_IRONMAN_PARAM);
        this.hasCustomRequirements = struct.getParamAsBoolean(HAS_CUSTOM_REQUIREMENTS_PARAM);
        this.preRequirementsEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_ENUM_ID_PARAM);
        this.preRequirementsValuesEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM);
        this.inversedPreRequirementsEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM);
        this.inversedPreRequirementsValuesEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM);
        this.toggleInversed = struct.getParamAsBoolean(HAS_TOGGLE_INVERSED_PARAM);
        this.chooseTransmit = struct.getParamAsBoolean(CHOOSE_TRANSMIT_PARAM);
        this.mobileName = struct.getParamAsString(MOBILE_NAME_PARAM);
        this.description = struct.getParamAsString(DESCRIPTION_PARAM);
        this.keyBindSprite = struct.getParamAsInt(KEYBIND_SPITE_PARAM);
        this.keyBindSpriteCoordGrid = struct.getParamAsInt(KEYBIND_SPRITE_SIZE_COORDGRID_PARAM);
        this.sliderSectors = struct.getParamAsInt(SLIDER_SECTORS_PARAM);
        this.sliderSectorsTextEnumId = struct.getParamAsInt(SLIDER_SECTOR_TEXT_ENUM_ID_PARAM);
        this.sliderCustomOnOpScript = struct.getParamAsBoolean(SLIDER_CUSTOM_ON_OP_PARAM);
        this.sliderCustomSetPos = struct.getParamAsBoolean(SLIDER_CUSTOM_SETPOS_PARAM);
        this.sliderDraggable = struct.getParamAsBoolean(IS_SLIDER_DRAGGABLE_PARAM);
        this.sliderDeadZone = struct.getParamAsInt(SLIDER_DEADZONE_PARAM);
        this.sliderDeadTime = struct.getParamAsInt(SLIDER_DEADTIME_PARAM);
        this.inputSingular = struct.getParamAsString(SLIDER_INPUT_SINGULAR_PARAM);
        this.inputPlural = struct.getParamAsString(SLIDER_INPUT_PLURAL_PARAM);
        this.inputZero = struct.getParamAsString(SLIDER_INPUT_ZERO_PARAM);
        this.opCheckerMessage = struct.getParamAsString(SLIDER_OP_CHECKER_MESSAGE_PARAM);
        this.mobileOpCheckerMessage = struct.getParamAsString(SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM);
        this.collapsibleInfobox = struct.getParamAsBoolean(HAS_COLLAPSIBLE_INFOBOX_PARAM);
        this.hideDescription = struct.getParamAsBoolean(HIDE_DESCRIPTION_PARAM);
        this.enhancedClientOnly = struct.getParamAsBoolean(IS_ENHANCED_CLIENT_PARAM);
        this.customNameExtraText = struct.getParamAsBoolean(CUSTOM_NAME_EXTRA_TEXT_PARAM);
        this.mobileAlwaysEnabled = struct.getParamAsBoolean(IS_MOBILE_ALWAYS_ENABLED_PARAM);
        this.hasCustomCheck = struct.getParamAsBoolean(HAS_CUSTOM_CHECK_PARAM);
        this.defaultColour = struct.getParamAsInt(DEFAULT_COLOUR_PARAM);
        this.nonDesktopOnly = struct.getParamAsBoolean(IS_NON_DESKTOP_ONLY_PARAM);
        this.leagueWorldOnly = struct.getParamAsBoolean(IS_LEAGUE_WORLD_ONLY_PARAM);
        this.leagueWorldEnhancedClientOnly = struct.getParamAsBoolean(IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM);
        this.dropdownEntriesEnumId = struct.getParamAsInt(DROPDOWN_ENTRIES_PARAM);
        this.mobileDropDownEntriesEnumId = struct.getParamAsInt(DROPDOWN_ENTRIES_MOBILE_PARAM);

        /* Type-specific versions of the above variables. */
        this.type = Preconditions.checkNotNull(SettingType.MAP.get(this.typeId));
        this.preRequirementsEnum = EnumDefinitions.getIntEnumOrNull(this.preRequirementsEnumId);
        this.preRequirementsValuesEnum = EnumDefinitions.getIntEnumOrNull(this.preRequirementsValuesEnumId);
        this.inversedPreRequirementsEnum = EnumDefinitions.getIntEnumOrNull(this.inversedPreRequirementsEnumId);
        this.inversedPreRequirementsValuesEnum = EnumDefinitions.getIntEnumOrNull(this.inversedPreRequirementsValuesEnumId);
        this.sliderSectorsTextEnum = EnumDefinitions.getStringEnumOrNull(this.sliderSectorsTextEnumId);
        this.dropdownEntriesEnum = EnumDefinitions.getStringEnumOrNull(this.dropdownEntriesEnumId);
        this.mobileDropdownEntriesEnum = EnumDefinitions.getStringEnumOrNull(this.mobileDropDownEntriesEnumId);
    }

    public int getStructId() {
        return structId;
    }

    public int getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public boolean isSliderTransmitted() {
        return sliderTransmitted;
    }

    public int getSliderNotchCount() {
        return sliderNotchCount;
    }

    public boolean isDesktop() {
        return desktop;
    }

    public boolean isMobile() {
        return mobile;
    }

    public boolean isNonIronman() {
        return nonIronman;
    }

    public boolean isIronman() {
        return ironman;
    }

    public boolean isHasCustomRequirements() {
        return hasCustomRequirements;
    }

    public int getPreRequirementsEnumId() {
        return preRequirementsEnumId;
    }

    public int getPreRequirementsValuesEnumId() {
        return preRequirementsValuesEnumId;
    }

    public int getInversedPreRequirementsEnumId() {
        return inversedPreRequirementsEnumId;
    }

    public int getInversedPreRequirementsValuesEnumId() {
        return inversedPreRequirementsValuesEnumId;
    }

    public boolean isToggleInversed() {
        return toggleInversed;
    }

    public boolean isChooseTransmit() {
        return chooseTransmit;
    }

    public String getMobileName() {
        return mobileName;
    }

    public String getDescription() {
        return description;
    }

    public int getKeyBindSprite() {
        return keyBindSprite;
    }

    public int getKeyBindSpriteCoordGrid() {
        return keyBindSpriteCoordGrid;
    }

    public int getSliderSectors() {
        return sliderSectors;
    }

    public int getSliderSectorsTextEnumId() {
        return sliderSectorsTextEnumId;
    }

    public boolean isSliderCustomOnOpScript() {
        return sliderCustomOnOpScript;
    }

    public boolean isSliderCustomSetPos() {
        return sliderCustomSetPos;
    }

    public boolean isSliderDraggable() {
        return sliderDraggable;
    }

    public int getSliderDeadZone() {
        return sliderDeadZone;
    }

    public int getSliderDeadTime() {
        return sliderDeadTime;
    }

    public String getInputSingular() {
        return inputSingular;
    }

    public String getInputPlural() {
        return inputPlural;
    }

    public String getInputZero() {
        return inputZero;
    }

    public String getOpCheckerMessage() {
        return opCheckerMessage;
    }

    public String getMobileOpCheckerMessage() {
        return mobileOpCheckerMessage;
    }

    public boolean isCollapsibleInfobox() {
        return collapsibleInfobox;
    }

    public boolean isHideDescription() {
        return hideDescription;
    }

    public boolean isEnhancedClientOnly() {
        return enhancedClientOnly;
    }

    public boolean isCustomNameExtraText() {
        return customNameExtraText;
    }

    public boolean isMobileAlwaysEnabled() {
        return mobileAlwaysEnabled;
    }

    public boolean isHasCustomCheck() {
        return hasCustomCheck;
    }

    public int getDefaultColour() {
        return defaultColour;
    }

    public boolean isNonDesktopOnly() {
        return nonDesktopOnly;
    }

    public boolean isLeagueWorldOnly() {
        return leagueWorldOnly;
    }

    public boolean isLeagueWorldEnhancedClientOnly() {
        return leagueWorldEnhancedClientOnly;
    }

    public int getDropdownEntriesEnumId() {
        return dropdownEntriesEnumId;
    }

    public int getMobileDropDownEntriesEnumId() {
        return mobileDropDownEntriesEnumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Setting setting = (Setting) o;

        if (structId != setting.structId) return false;
        if (id != setting.id) return false;
        if (typeId != setting.typeId) return false;
        if (sliderTransmitted != setting.sliderTransmitted) return false;
        if (sliderNotchCount != setting.sliderNotchCount) return false;
        if (desktop != setting.desktop) return false;
        if (mobile != setting.mobile) return false;
        if (nonIronman != setting.nonIronman) return false;
        if (ironman != setting.ironman) return false;
        if (hasCustomRequirements != setting.hasCustomRequirements) return false;
        if (preRequirementsEnumId != setting.preRequirementsEnumId) return false;
        if (preRequirementsValuesEnumId != setting.preRequirementsValuesEnumId) return false;
        if (inversedPreRequirementsEnumId != setting.inversedPreRequirementsEnumId) return false;
        if (inversedPreRequirementsValuesEnumId != setting.inversedPreRequirementsValuesEnumId) return false;
        if (toggleInversed != setting.toggleInversed) return false;
        if (chooseTransmit != setting.chooseTransmit) return false;
        if (keyBindSprite != setting.keyBindSprite) return false;
        if (keyBindSpriteCoordGrid != setting.keyBindSpriteCoordGrid) return false;
        if (sliderSectors != setting.sliderSectors) return false;
        if (sliderSectorsTextEnumId != setting.sliderSectorsTextEnumId) return false;
        if (sliderCustomOnOpScript != setting.sliderCustomOnOpScript) return false;
        if (sliderCustomSetPos != setting.sliderCustomSetPos) return false;
        if (sliderDraggable != setting.sliderDraggable) return false;
        if (sliderDeadZone != setting.sliderDeadZone) return false;
        if (sliderDeadTime != setting.sliderDeadTime) return false;
        if (collapsibleInfobox != setting.collapsibleInfobox) return false;
        if (hideDescription != setting.hideDescription) return false;
        if (enhancedClientOnly != setting.enhancedClientOnly) return false;
        if (customNameExtraText != setting.customNameExtraText) return false;
        if (mobileAlwaysEnabled != setting.mobileAlwaysEnabled) return false;
        if (hasCustomCheck != setting.hasCustomCheck) return false;
        if (defaultColour != setting.defaultColour) return false;
        if (nonDesktopOnly != setting.nonDesktopOnly) return false;
        if (leagueWorldOnly != setting.leagueWorldOnly) return false;
        if (leagueWorldEnhancedClientOnly != setting.leagueWorldEnhancedClientOnly) return false;
        if (dropdownEntriesEnumId != setting.dropdownEntriesEnumId) return false;
        if (mobileDropDownEntriesEnumId != setting.mobileDropDownEntriesEnumId) return false;
        if (!name.equals(setting.name)) return false;
        if (!searchKeywords.equals(setting.searchKeywords)) return false;
        if (!mobileName.equals(setting.mobileName)) return false;
        if (!description.equals(setting.description)) return false;
        if (!inputSingular.equals(setting.inputSingular)) return false;
        if (!inputPlural.equals(setting.inputPlural)) return false;
        if (!inputZero.equals(setting.inputZero)) return false;
        if (!opCheckerMessage.equals(setting.opCheckerMessage)) return false;
        return mobileOpCheckerMessage.equals(setting.mobileOpCheckerMessage);
    }

    @Override
    public int hashCode() {
        int result = structId;
        result = 31 * result + id;
        result = 31 * result + typeId;
        result = 31 * result + name.hashCode();
        result = 31 * result + searchKeywords.hashCode();
        result = 31 * result + (sliderTransmitted ? 1 : 0);
        result = 31 * result + sliderNotchCount;
        result = 31 * result + (desktop ? 1 : 0);
        result = 31 * result + (mobile ? 1 : 0);
        result = 31 * result + (nonIronman ? 1 : 0);
        result = 31 * result + (ironman ? 1 : 0);
        result = 31 * result + (hasCustomRequirements ? 1 : 0);
        result = 31 * result + preRequirementsEnumId;
        result = 31 * result + preRequirementsValuesEnumId;
        result = 31 * result + inversedPreRequirementsEnumId;
        result = 31 * result + inversedPreRequirementsValuesEnumId;
        result = 31 * result + (toggleInversed ? 1 : 0);
        result = 31 * result + (chooseTransmit ? 1 : 0);
        result = 31 * result + mobileName.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + keyBindSprite;
        result = 31 * result + keyBindSpriteCoordGrid;
        result = 31 * result + sliderSectors;
        result = 31 * result + sliderSectorsTextEnumId;
        result = 31 * result + (sliderCustomOnOpScript ? 1 : 0);
        result = 31 * result + (sliderCustomSetPos ? 1 : 0);
        result = 31 * result + (sliderDraggable ? 1 : 0);
        result = 31 * result + sliderDeadZone;
        result = 31 * result + sliderDeadTime;
        result = 31 * result + inputSingular.hashCode();
        result = 31 * result + inputPlural.hashCode();
        result = 31 * result + inputZero.hashCode();
        result = 31 * result + opCheckerMessage.hashCode();
        result = 31 * result + mobileOpCheckerMessage.hashCode();
        result = 31 * result + (collapsibleInfobox ? 1 : 0);
        result = 31 * result + (hideDescription ? 1 : 0);
        result = 31 * result + (enhancedClientOnly ? 1 : 0);
        result = 31 * result + (customNameExtraText ? 1 : 0);
        result = 31 * result + (mobileAlwaysEnabled ? 1 : 0);
        result = 31 * result + (hasCustomCheck ? 1 : 0);
        result = 31 * result + defaultColour;
        result = 31 * result + (nonDesktopOnly ? 1 : 0);
        result = 31 * result + (leagueWorldOnly ? 1 : 0);
        result = 31 * result + (leagueWorldEnhancedClientOnly ? 1 : 0);
        result = 31 * result + dropdownEntriesEnumId;
        result = 31 * result + mobileDropDownEntriesEnumId;
        return result;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "structId=" + structId +
                ", id=" + id +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", searchKeywords='" + searchKeywords + '\'' +
                ", sliderTransmitted=" + sliderTransmitted +
                ", sliderNotchCount=" + sliderNotchCount +
                ", desktop=" + desktop +
                ", mobile=" + mobile +
                ", nonIronman=" + nonIronman +
                ", ironman=" + ironman +
                ", hasCustomRequirements=" + hasCustomRequirements +
                ", preRequirementsEnumId=" + preRequirementsEnumId +
                ", preRequirementsValuesEnumId=" + preRequirementsValuesEnumId +
                ", inversedPreRequirementsEnumId=" + inversedPreRequirementsEnumId +
                ", inversedPreRequirementsValuesEnumId=" + inversedPreRequirementsValuesEnumId +
                ", toggleInversed=" + toggleInversed +
                ", chooseTransmit=" + chooseTransmit +
                ", mobileName='" + mobileName + '\'' +
                ", description='" + description + '\'' +
                ", keyBindSprite=" + keyBindSprite +
                ", keyBindSpriteCoordGrid=" + keyBindSpriteCoordGrid +
                ", sliderSectors=" + sliderSectors +
                ", sliderSectorsTextEnumId=" + sliderSectorsTextEnumId +
                ", sliderCustomOnOpScript=" + sliderCustomOnOpScript +
                ", sliderCustomSetPos=" + sliderCustomSetPos +
                ", sliderDraggable=" + sliderDraggable +
                ", sliderDeadZone=" + sliderDeadZone +
                ", sliderDeadTime=" + sliderDeadTime +
                ", inputSingular='" + inputSingular + '\'' +
                ", inputPlural='" + inputPlural + '\'' +
                ", inputZero='" + inputZero + '\'' +
                ", opCheckerMessage='" + opCheckerMessage + '\'' +
                ", mobileOpCheckerMessage='" + mobileOpCheckerMessage + '\'' +
                ", collapsibleInfobox=" + collapsibleInfobox +
                ", hideDescription=" + hideDescription +
                ", enhancedClientOnly=" + enhancedClientOnly +
                ", customNameExtraText=" + customNameExtraText +
                ", mobileAlwaysEnabled=" + mobileAlwaysEnabled +
                ", hasCustomCheck=" + hasCustomCheck +
                ", defaultColour=" + defaultColour +
                ", nonDesktopOnly=" + nonDesktopOnly +
                ", leagueWorldOnly=" + leagueWorldOnly +
                ", leagueWorldEnhancedClientOnly=" + leagueWorldEnhancedClientOnly +
                ", dropdownEntriesEnumId=" + dropdownEntriesEnumId +
                ", mobileDropDownEntriesEnumId=" + mobileDropDownEntriesEnumId +
                ", type=" + type +
                ", preRequirementsEnum=" + preRequirementsEnum +
                ", preRequirementsValuesEnum=" + preRequirementsValuesEnum +
                ", inversedPreRequirementsEnum=" + inversedPreRequirementsEnum +
                ", inversedPreRequirementsValuesEnum=" + inversedPreRequirementsValuesEnum +
                ", sliderSectorsTextEnum=" + sliderSectorsTextEnum +
                ", dropdownEntriesEnum=" + dropdownEntriesEnum +
                ", mobileDropdownEntriesEnum=" + mobileDropdownEntriesEnum +
                '}';
    }

    @NotNull
    public SettingType getType() {
        return type;
    }

    @Nullable
    public IntEnum getPreRequirementsEnum() {
        return preRequirementsEnum;
    }

    @Nullable
    public IntEnum getPreRequirementsValuesEnum() {
        return preRequirementsValuesEnum;
    }

    @Nullable
    public IntEnum getInversedPreRequirementsEnum() {
        return inversedPreRequirementsEnum;
    }

    @Nullable
    public IntEnum getInversedPreRequirementsValuesEnum() {
        return inversedPreRequirementsValuesEnum;
    }

    @Nullable
    public StringEnum getSliderSectorsTextEnum() {
        return sliderSectorsTextEnum;
    }

    @Nullable
    public StringEnum getDropdownEntriesEnum() {
        return dropdownEntriesEnum;
    }

    @Nullable
    public StringEnum getMobileDropdownEntriesEnum() {
        return mobileDropdownEntriesEnum;
    }


    public boolean checkSetting(Player player) {
        if (mobileAlwaysEnabled && player.isOnMobile()) {
            return true;
        }
        if (hasCustomRequirements && !checkCustomRequirement(player)) {
            return false;
        }
        if (preRequirementsEnumId != -1 && !checkOtherSetting(player, false)) {
            return false;
        }
        return inversedPreRequirementsEnumId == -1 || checkOtherSetting(player, true);
    }

    private boolean checkCustomRequirement(Player player) {
        if (SettingStructs.OPAQUE_COLOUR_STRUCTS.contains(structId) ||
                structId == SettingStructs.RESET_OPAQUE_CHAT_COLOURS_STRUCT_ID) {
            return player.getInterfaceHandler().getPane() == PaneType.FIXED || player.getVarManager().getBitValue(SettingVariables.TRANSPARENT_CHATBOX_VARBIT_ID) != 1;
        }
        return true;
    }

    private boolean checkOtherSetting(Player player, boolean inverse) {
        final IntEnum settingsToCheck = inverse ? inversedPreRequirementsEnum : preRequirementsEnum;
        final IntEnum settingValues = inverse ? inversedPreRequirementsValuesEnum : preRequirementsValuesEnum;
        Preconditions.checkNotNull(settingsToCheck);
        Preconditions.checkNotNull(settingValues);
        for (final Int2IntMap.Entry entry : settingsToCheck.getValues().int2IntEntrySet()) {
            final int index = entry.getIntKey();
            final int settingId = entry.getIntValue();
            final Setting settingToCheck = Settings.findSettingByStructId(settingId);
            final int value = settingValues.getValueOrDefault(index);
            if (settingToCheck.preRequirementsEnumId != -1 && !settingToCheck.checkOtherSetting(player, false)) {
                return false;
            }
            if (settingToCheck.inversedPreRequirementsEnumId != -1 && !settingToCheck.checkOtherSetting(player, true)) {
                return false;
            }
            final int currentSettingValue = settingToCheck.getCurrentSettingValue(player);
            if (!inverse) {
                if (value != currentSettingValue) return false;
            } else {
                if (value == currentSettingValue) return false;
            }
        }
        return true;
    }

    private int getCurrentSettingValue(Player player) {
        switch (type) {
            case TOGGLE:
                return getToggleSetting(player);
            case SLIDER:
                return getSlider(player);
            case DROPDOWN:
                return getDropdown(player);
            case KEYBIND:
                return getKeybind(player);
            default:
                return 0;
        }
    }

    // https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_toggle%5D.cs2
    private int getToggleSetting(Player player) {
        switch (structId) {
            case SettingStructs.COMBAT_ACHIEVEMENT_TASKS_REPEAT_FAILURE_STRUCT_ID:
                if (player.getVarManager().getBitValue(SettingVariables.COMBAT_ACHIEVEMENT_TASKS_FAILURE_VARBIT_ID) == 0) {
                    return 0;
                }
                break;
            case SettingStructs.COLLECTION_LOG_NEW_ADDITION_NOTIFICATION_STRUCT_ID:
                return player.getVarManager().getBitValue(SettingsInterface.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID & 0x1);
            case SettingStructs.COLLECTION_LOG_NEW_ADDITION_POPUP_STRUCT_ID:
                return player.getVarManager().getBitValue(SettingsInterface.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID >> 1) & 0x1;
        }
        return SettingVariables.getVariableValue(this, player);
    }

    // https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_slider%5D.cs2
    private int getSlider(Player player) {
        switch (structId) {
            case SettingStructs.VIEW_DISTANCE_STRUCT_ID:
                throw new IllegalStateException("Enhanced client not supported.");
            case SettingStructs.MUSIC_VOLUME_STRUCT_ID:
            case SettingStructs.SOUND_EFFECT_VOLUME_STRUCT_ID:
            case SettingStructs.AREA_SOUND_VOLUME_STRUCT_ID:
            case SettingStructs.SCREEN_BRIGHTNESS_STRUCT_ID:
                return SettingVariables.getVariableValue(this, player) / 5;
        }
        return SettingVariables.getVariableValue(this, player);
    }

    // https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_dropdown%5D.cs2
    private int getDropdown(Player player) {
        switch (structId) {
            case SettingStructs.FRIEND_LOGINLOGOUT_MESSAGES_STRUCT_ID:
                final int timeout = player.getVarManager().getBitValue(SettingsInterface.FRIEND_LOGIN_LOGOUT_MESSAGE_TIMEOUT_VARBIT_ID);
                return timeout != 0 ? timeout : SettingVariables.getVariableValue(this, player);
            case SettingStructs.LIMIT_FRAMERATE_STRUCT_ID:
            case SettingStructs.INTERFACE_SCALING_MODE_STRUCT_ID:
                throw new IllegalStateException("Enhanced and mobile client not supported.");
            case SettingStructs.GAME_CLIENT_LAYOUT_STRUCT_ID:
                if (player.getInterfaceHandler().getPane() == PaneType.FIXED) return 0;
                if (player.getVarManager().getBitValue(SettingsInterface.SIDE_PANELS_VARBIT_ID) == 0) return 1;
                return 2;
        }
        return SettingVariables.getVariableValue(this, player);
    }

    // https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_keybind%5D.cs2
    private int getKeybind(Player player) {
        final int value = SettingVariables.getVariableValue(this, player);
        return Enums.KEYBINDS.getValueOrDefault(value);
    }
}
