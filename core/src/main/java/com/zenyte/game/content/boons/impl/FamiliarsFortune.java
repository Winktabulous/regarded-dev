package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.content.follower.impl.SkillingPet;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

public class FamiliarsFortune extends Boon {
    public static boolean shouldBoostXp(Player player, int skill) {
        if(player == null) return false;


        SkillingPet pet = SkillingPet.getBySkill(skill);

        if(pet == null) return false;

        return player.getPetId() == pet.getPetId();
    }


    @Override
    public String name() {
        return "Familiar's Fortune";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_FamiliarsFortune;
    }

    @Override
    public String description() {
        return "Skilling pets provide a 10% xp boost to their associated skill when equipped";
    }

    @Override
    public int item() {
        return ItemId.ROCKY;
    }
}
