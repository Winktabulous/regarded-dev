package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;

import static com.zenyte.game.GameInterface.HOUSE_OPTIONS_TAB;
import static com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface.SIDE_PANELS_VARBIT_ID;

/**
 * @author Kris | 24/10/2018 12:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SettingsTabInterface extends Interface {

    @Override
    protected void attach() {
        put(5, "PK Skull Prevention");
        put(55, "Sound Effect Volume");
        put(23, "Brightness");
        put(84, "Game Client Layout");
        put(82, "NPC attack options");
        put(81, "Player attack options");
        put(69, "Area Sound Volume");
        put(41, "Music Volume");
        put(72, "Accept aid");
        put(73, "Toggle run");
        put(74, "House options");
        put(76, "Open bond pouch");
        put(75, "All Settings");
        put(30, "Mute Music");
        put(44, "Mute Sound Effects");
        put(58, "Mute Area Sounds");
        put(71, "Music unlock message");
        put(87, "Disable zooming with scroll wheel");
    }

    @Override
    public void open(Player player) {
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        player.getInterfaceHandler().sendInterface(getInterface());
        dispatcher.sendComponentSettings(getInterface(), getComponent("Sound Effect Volume"), 0, 21, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Brightness"), 0, 21, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Game Client Layout"), 1, 3, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("NPC attack options"), 1, 4, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Player attack options"), 1, 5, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Area Sound Volume"), 0, 21, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Music Volume"), 0, 21, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("PK Skull Prevention", player -> player.getVarManager().flipBit(SettingVariables.PK_SKULL_PREVENTION_VARBIT_ID));
        bind("All Settings", GameInterface.ADVANCED_SETTINGS::open);
        bind("Sound Effect Volume", (player, slotId, itemId, option) -> setSoundEffectVolume(player, slotId * 5));
        bind("Music Volume", (player, slotId, itemId, option) -> setMusicVolume(player, slotId * 5));
        bind("Area Sound Volume", (player, slotId, itemId, option) -> setAreaSoundVolume(player, slotId * 5));
        bind("Player attack options", (player, slotId, itemId, option) -> player.getVarManager().sendVar(SettingVariables.PLAYER_ATTACK_OPTIONS_VARP_ID, slotId - 1));
        bind("NPC attack options", (player, slotId, itemId, option) -> player.getVarManager().sendVar(SettingVariables.NPC_ATTACK_OPTIONS_VARP_ID, slotId - 1));
        bind("Accept aid", player -> {
            if (player.isIronman()) {
                player.getVarManager().sendBit(SettingVariables.ACCEPT_AID_VARBIT_ID, 0);
                player.sendMessage("Ironmen cannot accept aid from other players.");
                return;
            }
            player.getVarManager().flipBit(SettingVariables.ACCEPT_AID_VARBIT_ID);
        });
        bind("Toggle run", player -> {
            player.getInterfaceHandler().closeInterfaces();
            player.setRun(!player.isRun());
        });
        bind("House options", HOUSE_OPTIONS_TAB::open);
        bind("Brightness", (player, slotId, itemId, option) -> player.getVarManager().sendVar(SettingVariables.SCREEN_BRIGHTNESS_VARP_ID, slotId * 5));
        bind("Game Client Layout", (player, slotId, itemId, option) -> {
            if (slotId == 1) {
                if (player.getInterfaceHandler().getPane() != PaneType.FIXED) {
                    player.getInterfaceHandler().sendPane(player.getInterfaceHandler().getPane(), PaneType.FIXED);
                }
            } else if (slotId == 2) {
                if (player.getInterfaceHandler().getPane() != PaneType.RESIZABLE) {
                    player.getInterfaceHandler().sendPane(player.getInterfaceHandler().getPane(), PaneType.RESIZABLE);
                    player.getVarManager().sendBitInstant(SIDE_PANELS_VARBIT_ID, 0);
                }
            } else if (slotId == 3) {
                if (player.getInterfaceHandler().getPane() != PaneType.SIDE_PANELS) {
                    player.getInterfaceHandler().sendPane(player.getInterfaceHandler().getPane(), PaneType.SIDE_PANELS);
                    player.getVarManager().sendBitInstant(SIDE_PANELS_VARBIT_ID, 1);
                }
            }
        });
    }

    private void setMusicVolume(final Player player, final int volume) {
        if (volume != 0) {
            if (player.getVarManager().getValue(SettingVariables.MUSIC_VOLUME_VARP_ID) == 0) {
                player.getMusic().restartCurrent();
            }
        }
        player.getVarManager().sendVarInstant(SettingVariables.MUSIC_VOLUME_VARP_ID, volume);
    }

    private void setSoundEffectVolume(final Player player, final int volume) {
        player.getVarManager().sendVarInstant(SettingVariables.SOUND_EFFECT_VOLUME_VARP_ID, volume);
    }

    private void setAreaSoundVolume(final Player player, final int volume) {
        player.getVarManager().sendVarInstant(SettingVariables.AREA_SOUND_VOLUME_VARP_ID, volume);
    }

    @Override
    public boolean isInterruptedOnLock() {
        return false;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SETTINGS;
    }
}
