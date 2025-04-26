package com.zenyte.game.world.entity.player.container.impl.bank;

import com.zenyte.game.util.TimeUtils;
import com.zenyte.game.world.entity.player.Player;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zenyte.game.GameInterface.BANK_PIN_SETTINGS;
import static com.zenyte.game.GameInterface.BANK_PIN_VERIFICATION;

public class BankPin {

    private enum PinRecoveryDelay {
        THREE_DAYS,
        SEVEN_DAYS
    }

    private enum PinLoginSetting {
        ALWAYS_LOCK,
        LOCK_AFTER_FIVE_MINUTES
    }

    private enum PinStatus {
        ENABLED,
        PENDING_ENABLING,
        DISABLED,
        PENDING_DISABLING
    }

    private PinRecoveryDelay pinRecoveryDelay = PinRecoveryDelay.THREE_DAYS;
    private PinLoginSetting pinLoginSetting = PinLoginSetting.ALWAYS_LOCK;
    private PinStatus pinStatus = PinStatus.DISABLED;
    private long delay;
    private transient boolean unlocked;
    private int enteredPin = -1;
    private int pin = -1;
    private String lastVerificationIp;

    private int configType = -1;
    private long lockAt;

    public void loggedIn(Player player) {
        if (player.getBankPin().pin == -1 || lockAt <= 0)  return;
        if (!player.getIP().equals(lastVerificationIp) || System.currentTimeMillis() >= lockAt) return;
        player.getBankPin().setUnlocked();
    }

    public void loggedOut(Player player) {
        if (lockAt == -1 || !player.getBankPin().isUnlocked()) return;
        lockAt = System.currentTimeMillis() + TimeUtils.getMinutesToMillis(5);
        lastVerificationIp = player.getIP();
    }

    public boolean requiresVerification(Player player) {
        // if the account is unlocked, then no verification needed
        if (isUnlocked())
            return false;
        // verify the account
        promptPinEntry(player);
        openPinEntryUI(player);
        player.awaitInputInt(value -> {
            if (value == 12345) { // exit
                player.getInterfaceHandler().closeInterface(BANK_PIN_VERIFICATION);
                return;
            }
            if (Objects.equals(player.getBankPin().getPin(), value))
                player.getBankPin().setUnlocked();
        });
        return player.getBankPin().isUnlocked();
    }

    private void openPinEntryUI(Player player) {
        player.getInterfaceHandler().sendInterface(BANK_PIN_VERIFICATION);
        var dispatcher = player.getPacketDispatcher();
            dispatcher.sendClientScript(917, "ii", -1, -1);
            dispatcher.sendComponentVisibility(BANK_PIN_VERIFICATION.getId(), 14, true);
        if (enteredPin == -1) {
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 2, "Enter your PIN");
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 7, "Please choose a new FOUR DIGIT PIN using the buttons below.");
        }
        else {
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 2, "Confirm your PIN");
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 7, "Now please enter that number again.");
        }
    }

    // Used when creating an initial / new PIN
    void promptPinEntry(Player player) {
        // if we're Deleting this PIN
        if (configType == 3) {
            handleDeleteInput(player);
            return;
        }
        openPinEntryUI(player);
        player.awaitInputInt(value -> {
            if (value == 12345) { // exit
                player.getInterfaceHandler().sendInterface(BANK_PIN_SETTINGS);
                return;
            }
            if (enteredPin != -1) {
                if (value == enteredPin) {
                    player.getBankPin().setPin(enteredPin);
                    player.getBankPin().setUnlocked();
                    player.sendMessage("Your new PIN has been set, please don't forget it!");
                    player.getInterfaceHandler().closeInterface(BANK_PIN_VERIFICATION);
                }
                else
                    player.sendMessage("Those numbers did not match, please be sure the PIN you setup is something you can remember!");
                player.getInterfaceHandler().sendInterface(BANK_PIN_SETTINGS);
                return;
            }
            enteredPin = value;
            promptPinEntry(player);
        });
    }

    private void handleDeleteInput(Player player) {
        openPinEntryUI(player);
        player.awaitInputInt(value -> {
            if (value == 12345) { // exit
                player.getInterfaceHandler().closeInterface(BANK_PIN_VERIFICATION);
                return;
            }
            if (player.getBankPin().enteredPin != -1)
                if (Objects.equals(value, player.getBankPin().enteredPin)) {
                    pinRecoveryDelay = PinRecoveryDelay.THREE_DAYS;
                    pinLoginSetting = PinLoginSetting.ALWAYS_LOCK;
                    pinStatus = PinStatus.DISABLED;
                    delay = -1;
                    unlocked = false;
                    enteredPin = -1;
                    pin = -1;
                    configType = -1;
                    player.sendMessage("Your PIN has been removed.");
                }
                else {
                    player.sendMessage("Those numbers did not match, please be sure the PIN you setup is something you can remember!");
                }
            player.getInterfaceHandler().closeInterface(BANK_PIN_VERIFICATION);
        });
    }

    protected void sendConfirmationScreen(Player player, int type) {
        // Type: 1 = setup | 2 = change | 3 = delete
        player.getInterfaceHandler().sendInterface(BANK_PIN_SETTINGS);
        configType = type;
        var dispatcher = player.getPacketDispatcher();
            dispatcher.sendClientScript(917, "ii", -1, -1);
            dispatcher.sendComponentVisibility(BANK_PIN_SETTINGS.getId(), 0, true);
            dispatcher.sendComponentVisibility(BANK_PIN_SETTINGS.getId(), 28, false);
        if (type == 1) {
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 30, "Do you really wish to set a PIN to protect your bank?");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 33, "Yes, I really want a PIN. I will never forget it!");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 36, "No, I might forget it!");
        }
        else if (type == 2) {
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 30, "Do you really wish to change your PIN?");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 33, "Yes, I really want to change my PIN!");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 36, "No, I want to keep my current PIN!");
        }
        else {
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 30, "Do you really wish to delete your PIN?");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 33, "Yes, I really want to delete my PIN!");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 36, "No, keep my PIN, I want to be safe!");
        }
    }


    // Getters / Setters below
    public int getPin() {
        return pin;
    }
    public void resetPin() {
        pin = -1;
    }
    public void setPin(int pin) {
        this.pin = pin;
    }

    public boolean isUnlocked() {
        return unlocked;
    }
    public void setUnlocked() {
        unlocked = true;
        pinStatus = PinStatus.ENABLED;

    }
    public void lock() {
        unlocked = false;
    }

    public boolean isAlwaysLocked() {
        return pinLoginSetting == PinLoginSetting.ALWAYS_LOCK;
    }
    public boolean isPinEnabled() {
        return pinStatus == PinStatus.ENABLED;
    }
    public boolean isShortDelay() {
        return pinRecoveryDelay == PinRecoveryDelay.THREE_DAYS;
    }

    public void toggleShortDelay() {
        if (pinRecoveryDelay == PinRecoveryDelay.THREE_DAYS)
            pinRecoveryDelay = PinRecoveryDelay.SEVEN_DAYS;
        else
            pinRecoveryDelay = PinRecoveryDelay.THREE_DAYS;
    }
    public void toggleAlwaysSecure() {
        if (pinLoginSetting == PinLoginSetting.ALWAYS_LOCK)
            pinLoginSetting = PinLoginSetting.LOCK_AFTER_FIVE_MINUTES;
        else
            pinLoginSetting = PinLoginSetting.ALWAYS_LOCK;
    }
}
