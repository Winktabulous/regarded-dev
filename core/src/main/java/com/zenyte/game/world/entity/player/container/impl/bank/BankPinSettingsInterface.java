package com.zenyte.game.world.entity.player.container.impl.bank;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;

import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;

public class BankPinSettingsInterface extends Interface {

    @Override
    protected void attach() {
        put(0, "Interface base");
        put(6, "Pin status");
        put(8, "Recovery delay");
        put(10, "Pin lock on login");
        put(14, "Message");
        put(18, "PIN pending options");
        put(19, "Set PIN");
        put(20, "Change PIN delay");
        put(21, "Pin enabled options");
        put(22, "Change PIN");
        put(23, "Delete PIN");
        put(24, "Change PIN Delay");
        put(25, "Login Secure Delay");
        put(26, "Cancel PIN option");
        put(28, "Set PIN options");

        put(33, "PIN Alteration Confirm");
        put(36, "PIN Alteration Deny");
    }

    @Override
    public void open(Player player) {
        if (player.getBankPin().requiresVerification(player)) return;
        player.getBankPin().resetPin();
        refreshInterface(player);
        player.sendSound(new SoundEffect(1040));
    }

    @Override
    protected void build() {
        // with no Pin
        bind("Set PIN", (player, slotId, itemId, option) -> player.getBankPin().sendConfirmationScreen(player, 1));
        bind("PIN Alteration Confirm", (player, slotId, itemId, option) -> player.getBankPin().promptPinEntry(player));
        bind("PIN Alteration Deny", (player, slotId, itemId, option) -> refreshInterface(player));

        bind("Change PIN delay", (player, slotId, itemId, option) -> {
            player.getBankPin().toggleShortDelay();
            refreshInterface(player);
        });

        // with a Pin
        bind("Change PIN", (player, slotId, itemId, option) -> player.getBankPin().sendConfirmationScreen(player, 2));
        bind("Delete PIN", (player, slotId, itemId, option) -> player.getBankPin().sendConfirmationScreen(player, 3));
        // Change the delay for PIN deletion
        bind("Change PIN Delay", (player, slotId, itemId, option) -> {
            player.getBankPin().toggleShortDelay();
            refreshInterface(player);
        });
        // Change how long after logout before the account locks
        bind("Login Secure Delay", (player, slotId, itemId, option) -> {
            player.getBankPin().toggleAlwaysSecure();
            refreshInterface(player);
        });
    }

    private void refreshInterface(Player player) {
        var dispatcher = player.getPacketDispatcher();
            dispatcher.sendClientScript(917, "ii", -1, -1);
            dispatcher.sendComponentVisibility(getInterface(), 0, false);
            dispatcher.sendComponentVisibility(getInterface(), 28, true);
            dispatcher.sendComponentText(getInterface(), 6, player.getBankPin().isPinEnabled() ? "Bank Protected" : "Not Protected!");
            dispatcher.sendComponentText(getInterface(), 8, String.format("%d days", player.getBankPin().isShortDelay() ? 3 : 7));
            dispatcher.sendComponentText(getInterface(), 10, player.getBankPin().isAlwaysLocked() ? "Always lock" : "Lock after 5 minuets offline");
            dispatcher.sendComponentText(getInterface(), 14, "Customers are reminded that they should NEVER tell anyone their Bank PINs or passwords, nor should they ever enter their PINs on any website form.");
            dispatcher.sendComponentVisibility(getInterface(), 18, player.getBankPin().isPinEnabled());
            dispatcher.sendComponentVisibility(getInterface(), 21, !player.getBankPin().isPinEnabled());
            dispatcher.sendComponentVisibility(getInterface(), 26, true);

        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BANK_PIN_SETTINGS;
    }
}
