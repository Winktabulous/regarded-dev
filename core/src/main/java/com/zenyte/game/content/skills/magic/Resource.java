package com.zenyte.game.content.skills.magic;

import com.zenyte.game.content.skills.magic.resources.RuneResource;

/**
 * @author Kris | 7. juuli 2018 : 01:46:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Resource {

    private final int runeId;
    private final int amount;
    private final RuneResource source;

    public Resource(final int runeId, final int amount, final RuneResource source) {
        this.runeId = runeId;
        this.amount = amount;
        this.source = source;
    }

    public int getRuneId() {
        return runeId;
    }

    public int getAmount() {
        return amount;
    }

    public RuneResource getSource() {
        return source;
    }

}
