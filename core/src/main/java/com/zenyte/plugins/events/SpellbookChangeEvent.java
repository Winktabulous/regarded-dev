package com.zenyte.plugins.events;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Event;

public class SpellbookChangeEvent implements Event {
    private final Player player;
    private final Spellbook oldSpellbook;

    public SpellbookChangeEvent(Player player, Spellbook oldSpellbook) {
        this.player = player;
        this.oldSpellbook = oldSpellbook;
    }

    public Player getPlayer() {
        return player;
    }

    public Spellbook getOldSpellbook() {
        return oldSpellbook;
    }

    @Override
    public String toString() {
        return "SpellbookChangeEvent(player=" + this.getPlayer() + ", oldSpellbook=" + this.getOldSpellbook() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SpellbookChangeEvent)) return false;
        final SpellbookChangeEvent other = (SpellbookChangeEvent) o;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (this$player == null ? other$player != null : !this$player.equals(other$player)) return false;
        final Object this$oldSpellbook = this.getOldSpellbook();
        final Object other$oldSpellbook = other.getOldSpellbook();
        return this$oldSpellbook == null ? other$oldSpellbook == null : this$oldSpellbook.equals(other$oldSpellbook);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SpellbookChangeEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $oldSpellbook = this.getOldSpellbook();
        result = result * PRIME + ($oldSpellbook == null ? 43 : $oldSpellbook.hashCode());
        return result;
    }
}
