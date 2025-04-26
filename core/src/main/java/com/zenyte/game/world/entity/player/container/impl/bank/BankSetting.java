package com.zenyte.game.world.entity.player.container.impl.bank;

import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.game.model.ui.testinterfaces.BankInterface.VAR_CURRENT_TAB;

/**
 * @author Kris | 20. mai 2018 : 16:41:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum BankSetting {
    REARRANGE_MODE(3959),
    WITHDRAW_MODE(3958),
    ALWAYS_PLACEHOLDER(3755),
    INCINERATOR(5102),
    DEPOSIT_WORN_ITEMS(5364, true),
    DEPOSIT_INVENTOY_ITEMS(8352, true),
    FILL_BANK(-1);
    private static final BankSetting[] VALUES = values();
    private final int varbitId;
    private final boolean inverted;

    BankSetting(final int varbitId) {
        this(varbitId, false);
    }

    BankSetting(final int varbitId, final boolean inverted) {
        this.varbitId = varbitId;
        this.inverted = inverted;
    }

    public void updateVar(final Player player) {
        if (varbitId == -1) return;
        final Bank bank = player.getBank();
        final int curSetting = bank.getSetting(this);
        final int result = inverted ? (curSetting == 1 ? 0 : 1) : curSetting;
        player.getVarManager().sendBit(varbitId, result);
        if (this == WITHDRAW_MODE) {
            player.getVarManager().sendBit(VAR_CURRENT_TAB, (bank.getCurrentTab() + 1) % 10);
        }
    }

    public static final void update(final Player player) {
        final Bank bank = player.getBank();
        for (final BankSetting setting : BankSetting.VALUES) {
            if (setting.varbitId == -1) continue;
            final int curSetting = bank.getSetting(setting);
            final int result = setting.inverted ? (curSetting == 1 ? 0 : 1) : curSetting;
            player.getVarManager().sendBit(setting.varbitId, result);
        }
        bank.refreshQuantity();
    }
}
