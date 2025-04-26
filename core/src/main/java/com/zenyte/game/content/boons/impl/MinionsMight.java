package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

public class MinionsMight extends Boon {
    public static boolean shouldBoostCombat(Player player, NPC npc) {
        if(player == null) return false;
        if(!player.hasBoon(MinionsMight.class)) return false;

        if(npc == null) return false;
        BossPet pet = BossPet.getByBossNPC(npc.getId());

        if(pet == null) return false;
        return player.getPetId() == pet.getPetId();
    }

    @Override
    public String name() {
        return "Minion's Might";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_MinionsMight;
    }

    @Override
    public String description() {
        return "Boss pets give a 10% damage & accuracy boost to the associated boss when equipped";
    }

    @Override
    public int item() {
        return ItemId.PET_KRAKEN;
    }
}
