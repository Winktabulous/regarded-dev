package com.zenyte.game.world.entity.player.action.combat;

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon;
import com.near_reality.game.content.custom.GodBow;
import com.zenyte.game.item.ItemId;

public final class BowItemIds {

    public static final int[] CRYSTAL_BOW_ITEM_IDS = new int[]{
            4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 11748, 11749, 11750, 11751, 11752, 11753, 11754, 11755, 11756, 11757, 11758,
            23901, 23902, 23903, 23855, 23856, 23857,
            CrystalWeapon.Bow.INSTANCE.getProductItemId(), CrystalWeapon.BowOfFaerdhinen.INSTANCE.getProductItemId(),
            CrystalWeapon.Bow.INSTANCE.getInactiveId(), CrystalWeapon.BowOfFaerdhinen.INSTANCE.getInactiveId(),
            ItemId.BOW_OF_FAERDHINEN_C,
            ItemId.BOW_OF_FAERDHINEN_C_25884,
            ItemId.BOW_OF_FAERDHINEN_C_25886,
            ItemId.BOW_OF_FAERDHINEN_C_25888,
            ItemId.BOW_OF_FAERDHINEN_C_25890,
            ItemId.BOW_OF_FAERDHINEN_C_25892,
            ItemId.BOW_OF_FAERDHINEN_C_25894,
            ItemId.BOW_OF_FAERDHINEN_C_25896,
            GodBow.Armadyl.INSTANCE.getItemId(),
            GodBow.Bandos.INSTANCE.getItemId(),
            GodBow.Saradomin.INSTANCE.getItemId(),
            GodBow.Zamorak.INSTANCE.getItemId()
    };
}
