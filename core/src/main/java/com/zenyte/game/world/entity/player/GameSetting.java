package com.zenyte.game.world.entity.player;

import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.utils.Ordinal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 27-4-2019 | 11:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum GameSetting {
    LEVEL_99_DIALOGUES("Level-up dialogues", "Set the minimum level when level-up dialogues should appear (1-99).", 5
            , VarCollection.LEVEL_UP_DIALOGUES, SettingType.INPUT),
    RARE_DROP_BROADCASTS("Rare drop broadcasts", "Whether or not show broadcasts when players get a rare drop.", 8,
            VarCollection.RARE_DROP_BROADCASTS, SettingType.TOGGLE),
    LEVEL_99_BROADCASTS("Level-99 broadcasts", "Whether or not show broadcasts when players reach 99 in a skill.", 14
            , VarCollection.LEVEL_99_BROADCASTS, SettingType.TOGGLE),
    MAXED_PLAYER_BROADCASTS("Maxed player broadcasts", "Whether or not show broadcasts when players become maxed.",
            20, VarCollection.MAXED_PLAYER_BROADCASTS, SettingType.TOGGLE),
    MAX_SKILL_XP_BROADCASTS("200 million XP broadcasts", "Whether or not show broadcasts when players reach 200 " +
            "million experience in a skill.", 26, VarCollection.MAX_SKILL_XP_BROADCASTS, SettingType.TOGGLE),
    PET_BROADCASTS("Pet broadcasts", "Whether or not show broadcasts when players receive a pet.", 32,
            VarCollection.PET_BROADCASTS, SettingType.TOGGLE),
    MYSTERY_BOX_BROADCASTS("Mystery box broadcasts", "Whether or not show broadcasts when players receive a valuable " +
            "mystery box item.", 38, VarCollection.PET_BROADCASTS, SettingType.TOGGLE),
    HARDCORE_IRONMAN_DEATH_BROADCASTS("Hardcore Ironman death broadcasts", "Whether or not show broadcasts when " +
            "hardcore ironmen die.", 44, VarCollection.HARDCORE_IRONMAN_DEATH_BROADCASTS, SettingType.TOGGLE),
    CONFIRMATION_WHEN_NOTING_OR_UNNOTING("(Un)noting confirmation box", "Whether or not to show a confirmation box " +
            "when (un)noting.", 50, VarCollection.CONFIRMATION_WHEN_NOTING_OR_UNNOTING, SettingType.TOGGLE),
    REMEMBER_COMBAT_STYLES("Remember combat style", "Whether or not to remember and use the new style weapon " +
            "switching.", 56, VarCollection.REMEMBER_COMBAT_SETTINGS, SettingType.TOGGLE),
    DAILY_CHALLENGE_NOTIFICATIONS("Daily Challenge Notifications", "Whether or not to display notifications when you " +
            "progress with your daily challenge.", 62, VarCollection.DAILY_CHALLENGE_NOTIFICATIONS, SettingType.TOGGLE),
    HELPFUL_TIPS("Helpful tips", "Whether or not to show helpful tips in the chatbox.", 68,
            VarCollection.HELPFUL_TIPS, SettingType.TOGGLE),
    HIDE_ITEMS_YOU_CANT_PICK("Hide items", "Hide items you cannot pick up due to ironman restrictions.", 74,
            VarCollection.HIDE_ITEMS, SettingType.TOGGLE),
    SMASH_VIALS("Smash vials", "Smash vials when you finish drinking a potion.", 80, VarCollection.SMASH_VIALS,
            SettingType.TOGGLE),
    YELL_FILTER("Filter yells", "Filter yells from everyone, except your own.", 86, VarCollection.FILTER_YELLS,
            SettingType.TOGGLE),
    ALWAYS_SHOW_LATEST_UPDATE("Latest update message", "Always show latest update message on login, as opposed to " +
            "only showing it the first time around since the update.", 92, VarCollection.UPDATE_MESSAGE,
            SettingType.TOGGLE),
    EXAMINE_NPCS_DROP_VIEWER("Examine drop viewer", "Examining NPCs opens the drop viewer with the npc's drops " +
            "displayed on it, as long as the player is not being attacked.", 98, VarCollection.EXAMINE_NPCS,
            SettingType.TOGGLE),
    TREASURE_TRAILS_BROADCASTS("Treasure Trails broadcasts", "Whether or not to broadcast rare rewards from Treasure " +
            "Trails.", 104, VarCollection.BROADCAST_TREASURE_TRAILS, SettingType.TOGGLE),
    NEW_PLAYERS("New player announcements", "Whether or not to broadcast new players that join the server.", 110, VarCollection.BROADCAST_NEW_PLAYERS, SettingType.TOGGLE),
    ;

    public static final GameSetting[] ALL = values();
    private static final Map<Integer, GameSetting> SETTINGS = new HashMap<>();

    static {
        for (final GameSetting setting : ALL) {
            SETTINGS.put(setting.slotId, setting);
        }
    }

    private final String name;
    private final String description;
    private final int slotId;
    private final VarCollection var;
    private final SettingType type;

    GameSetting(final String name, final String description, final int slotId, final VarCollection var,
                final SettingType type) {
        this.name = name;
        this.description = description;
        this.slotId = slotId;
        this.var = var;
        this.type = type;
    }

    public static GameSetting get(final int slotId) {
        return SETTINGS.get(slotId);
    }

    public void handleSetting(final Player player) {
        if (type.equals(SettingType.TOGGLE)) {
            if (this == HIDE_ITEMS_YOU_CANT_PICK && !player.isIronman()) {
                player.sendMessage("This setting has no effect on non-ironman players.");
                player.getPacketDispatcher().sendClientScript(10200, ordinal(), getName(), getDescription(),
                        getType().ordinal(), player.getNumericAttribute(toString()).intValue());
                return;
            }
            player.toggleBooleanAttribute(toString());
            var.updateSingle(player);
            if (this == HIDE_ITEMS_YOU_CANT_PICK) {
                player.refreshScopedGroundItems(player.getNumericAttribute(GameSetting.HIDE_ITEMS_YOU_CANT_PICK.toString()).intValue() == 0);
            }
        } else {
            player.sendInputInt(description, value -> {
                if (this.equals(LEVEL_99_DIALOGUES) && value < 1 || value > 99) {
                    player.sendMessage("You need to enter a level between 1 and 99.");
                    return;
                }
                player.addAttribute(toString(), value);
                var.updateSingle(player);
                player.getPacketDispatcher().sendClientScript(10200, ordinal(), name, description, type.ordinal(),
                        value);
            });
        }
    }


    @Ordinal
    public enum SettingType {
        TOGGLE,
        INPUT
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSlotId() {
        return slotId;
    }

    public VarCollection getVar() {
        return var;
    }

    public SettingType getType() {
        return type;
    }
}
