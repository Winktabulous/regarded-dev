package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

public class InfallibleShackles extends Boon {
    public static boolean applies(CombatSpell spell) {
        return spell.hasBindEffect();
    }

    @Override
    public String name() {
        return "Infallible Shackles";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_InfallibleShackles;
    }

    @Override
    public String description() {
        return "Spell attacks with a freeze effect will always land against non-frozen NPCs.";
    }

    @Override
    public int item() {
        return ItemId.SPELL_SCROLL;
    }
}
