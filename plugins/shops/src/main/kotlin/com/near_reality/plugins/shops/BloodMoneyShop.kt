package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ABYSSAL_WHIP
import com.zenyte.game.item.ItemId.AHRIMS_ARMOUR_SET
import com.zenyte.game.item.ItemId.AMETHYST_ARROW
import com.zenyte.game.item.ItemId.AMULET_OF_FURY
import com.zenyte.game.item.ItemId.ANCIENT_HALO
import com.zenyte.game.item.ItemId.ANCIENT_MACE
import com.zenyte.game.item.ItemId.ANGLERFISH
import com.zenyte.game.item.ItemId.ANNAKARL_TELEPORT
import com.zenyte.game.item.ItemId.ANTIVENOM4_12913
import com.zenyte.game.item.ItemId.ARMADYL_GODSWORD
import com.zenyte.game.item.ItemId.ARMADYL_HALO
import com.zenyte.game.item.ItemId.BANDOS_HALO
import com.zenyte.game.item.ItemId.BARROWS_GLOVES
import com.zenyte.game.item.ItemId.BASTION_POTION4
import com.zenyte.game.item.ItemId.BLESSED_SPIRIT_SHIELD
import com.zenyte.game.item.ItemId.BLIGHTED_ANCIENT_ICE_SACK
import com.zenyte.game.item.ItemId.BLIGHTED_BIND_SACK
import com.zenyte.game.item.ItemId.BLIGHTED_ENTANGLE_SACK
import com.zenyte.game.item.ItemId.BLIGHTED_SURGE_SACK
import com.zenyte.game.item.ItemId.BLIGHTED_TELEPORT_SPELL_SACK
import com.zenyte.game.item.ItemId.BLIGHTED_VENGEANCE_SACK
import com.zenyte.game.item.ItemId.BLUE_DARK_BOW_PAINT
import com.zenyte.game.item.ItemId.BOLT_RACK
import com.zenyte.game.item.ItemId.BRASSICA_HALO
import com.zenyte.game.item.ItemId.BURNING_AMULET5
import com.zenyte.game.item.ItemId.BURNT_PAGE
import com.zenyte.game.item.ItemId.CARRALLANGAR_TELEPORT
import com.zenyte.game.item.ItemId.COOKED_KARAMBWAN
import com.zenyte.game.item.ItemId.CRYSTAL_SEED
import com.zenyte.game.item.ItemId.DAREEYAK_TELEPORT
import com.zenyte.game.item.ItemId.DHAROKS_ARMOUR_SET
import com.zenyte.game.item.ItemId.DIAMOND_DRAGON_BOLTS_E
import com.zenyte.game.item.ItemId.DRAGONSTONE_DRAGON_BOLTS_E
import com.zenyte.game.item.ItemId.DRAGON_ARROW
import com.zenyte.game.item.ItemId.DRAGON_BOOTS
import com.zenyte.game.item.ItemId.DRAGON_CROSSBOW
import com.zenyte.game.item.ItemId.DRAGON_DART
import com.zenyte.game.item.ItemId.DRAGON_JAVELIN
import com.zenyte.game.item.ItemId.DRAGON_KNIFE
import com.zenyte.game.item.ItemId.DRAGON_THROWNAXE
import com.zenyte.game.item.ItemId.FIGHTER_TORSO
import com.zenyte.game.item.ItemId.FROZEN_WHIP_MIX
import com.zenyte.game.item.ItemId.GHORROCK_TELEPORT
import com.zenyte.game.item.ItemId.GRANITE_CLAMP
import com.zenyte.game.item.ItemId.GREEN_DARK_BOW_PAINT
import com.zenyte.game.item.ItemId.GUTHIXIAN_ICON
import com.zenyte.game.item.ItemId.GUTHIX_HALO
import com.zenyte.game.item.ItemId.INFINITY_BOOTS
import com.zenyte.game.item.ItemId.KARILS_ARMOUR_SET
import com.zenyte.game.item.ItemId.KHARYRLL_TELEPORT
import com.zenyte.game.item.ItemId.LASSAR_TELEPORT
import com.zenyte.game.item.ItemId.LAVA_STAFF_UPGRADE_KIT
import com.zenyte.game.item.ItemId.LOOTING_BAG
import com.zenyte.game.item.ItemId.MAGES_BOOK
import com.zenyte.game.item.ItemId.MAGIC_SHORTBOW_SCROLL
import com.zenyte.game.item.ItemId.MASTER_WAND
import com.zenyte.game.item.ItemId.OCCULT_NECKLACE
import com.zenyte.game.item.ItemId.OPAL_DRAGON_BOLTS_E
import com.zenyte.game.item.ItemId.ORNATE_MAUL_HANDLE
import com.zenyte.game.item.ItemId.PADDEWWA_TELEPORT
import com.zenyte.game.item.ItemId.PVP_MYSTERY_BOX
import com.zenyte.game.item.ItemId.REVENANT_CAVE_TELEPORT
import com.zenyte.game.item.ItemId.RING_OF_WEALTH_SCROLL
import com.zenyte.game.item.ItemId.ROYAL_SEED_POD
import com.zenyte.game.item.ItemId.RUBY_DRAGON_BOLTS_E
import com.zenyte.game.item.ItemId.RUNE_POUCH
import com.zenyte.game.item.ItemId.SANFEW_SERUM4
import com.zenyte.game.item.ItemId.SARADOMINS_TEAR
import com.zenyte.game.item.ItemId.SARADOMIN_BREW4
import com.zenyte.game.item.ItemId.SARADOMIN_HALO
import com.zenyte.game.item.ItemId.SCROLL_OF_IMBUING
import com.zenyte.game.item.ItemId.SENNTISTEN_TELEPORT
import com.zenyte.game.item.ItemId.SEREN_HALO
import com.zenyte.game.item.ItemId.STATIUSS_WARHAMMER
import com.zenyte.game.item.ItemId.STEAM_STAFF_UPGRADE_KIT
import com.zenyte.game.item.ItemId.SUPER_COMBAT_POTION4
import com.zenyte.game.item.ItemId.SWIFT_BLADE
import com.zenyte.game.item.ItemId.THREAD_OF_ELIDINIS
import com.zenyte.game.item.ItemId.TOME_OF_FIRE_EMPTY
import com.zenyte.game.item.ItemId.TOXIC_STAFF_UNCHARGED
import com.zenyte.game.item.ItemId.VESTAS_LONGSWORD
import com.zenyte.game.item.ItemId.VESTAS_SPEAR
import com.zenyte.game.item.ItemId.VOLCANIC_WHIP_MIX
import com.zenyte.game.item.ItemId.WARD_UPGRADE_KIT
import com.zenyte.game.item.ItemId.WHITE_DARK_BOW_PAINT
import com.zenyte.game.item.ItemId.YELLOW_DARK_BOW_PAINT
import com.zenyte.game.item.ItemId.ZAMORAK_HALO
import com.zenyte.game.item.ItemId.ZURIELS_STAFF
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BloodMoneyShop : ShopScript() {
    init {
        "Blood money Store"(290, ShopCurrency.BLOOD_MONEY, STOCK_ONLY) {
            VESTAS_LONGSWORD(100, 0, 15000, ironmanRestricted = true)
            STATIUSS_WARHAMMER(100, 0, 10000, ironmanRestricted = true)
            VESTAS_SPEAR(100, 0, 10000, ironmanRestricted = true)
            ZURIELS_STAFF(100, 0, 10000, ironmanRestricted = true)
            TOXIC_STAFF_UNCHARGED(100, 0, 5000, ironmanRestricted = true)
            ARMADYL_GODSWORD(100, 0, 2500, ironmanRestricted = true)
            BLESSED_SPIRIT_SHIELD(100, 0, 2000, ironmanRestricted = true)
            THREAD_OF_ELIDINIS(100, 0, 2000, ironmanRestricted = true)
            32149(100, 0, 200) // larrans booster
            32150(100, 0, 150) // gano booster
            32154(100, 0, 300) // blood money booster
            32166(100, 0, 400) // rev booster
            ORNATE_MAUL_HANDLE(100, 0, 150)
        //    24187(100, 0,2000)
            SWIFT_BLADE(100, 0, 1000)
            GUTHIXIAN_ICON(100, 0, 500)
            GRANITE_CLAMP(100, 0, 100)
            ABYSSAL_WHIP(100, 0, 150)
            MAGES_BOOK(100, 0, 200)
            INFINITY_BOOTS(100, 0, 300)
            TOME_OF_FIRE_EMPTY(100, 0, 500, ironmanRestricted = true)
            BURNT_PAGE(1000, 0, 3, ironmanRestricted = true)
            OCCULT_NECKLACE(100, 0, 1500, ironmanRestricted = true)
            AMULET_OF_FURY(100, 0, 500, ironmanRestricted = true)
            FIGHTER_TORSO(100, 0, 200)
            BARROWS_GLOVES(100, 0, 50)
            DRAGON_BOOTS(100, 0, 75, ironmanRestricted = true)
            DHAROKS_ARMOUR_SET(100, 0, 350, ironmanRestricted = true)
            AHRIMS_ARMOUR_SET(100, 0, 300, ironmanRestricted = true)
            KARILS_ARMOUR_SET(100, 0, 300, ironmanRestricted = true)
            DRAGON_CROSSBOW(100, 0, 750, ironmanRestricted = true)
            DRAGON_KNIFE(100000, 0, 1)
            DRAGON_THROWNAXE(100000, 0, 1)
            DRAGON_JAVELIN(100000, 0, 1)
            DRAGON_DART(100000, 0, 2)
            ANCIENT_MACE(100, 0, 125, ironmanRestricted = true)
            DRAGONSTONE_DRAGON_BOLTS_E(100000, 0,1)
            RUBY_DRAGON_BOLTS_E(100000, 0,1)
            OPAL_DRAGON_BOLTS_E(100000, 0,1)
            DIAMOND_DRAGON_BOLTS_E(100000, 0, 1)
            DRAGON_ARROW(100000, 0,2)
            AMETHYST_ARROW(100000, 0,1)
            BOLT_RACK(100000, 0,1)
            BLIGHTED_ANCIENT_ICE_SACK(100000, 0,1)
            BLIGHTED_TELEPORT_SPELL_SACK(100000, 0,1)
            BLIGHTED_VENGEANCE_SACK(100000, 0,1)
            BLIGHTED_ENTANGLE_SACK(100000, 0,1)
            BLIGHTED_BIND_SACK(100000, 0,1)
            BLIGHTED_SURGE_SACK(100000, 0, 1)
            RING_OF_WEALTH_SCROLL(100, 0,150)
            MAGIC_SHORTBOW_SCROLL(100, 0,200)
            LOOTING_BAG(100, 0,40)
            RUNE_POUCH(100, 0,400)
            ROYAL_SEED_POD(1000, 0,300)
            REVENANT_CAVE_TELEPORT(1000, 0,1)
            BURNING_AMULET5(100, 0,1)
            MASTER_WAND(100, 0,250)
            (SUPER_COMBAT_POTION4+1)(1000, 0,2, ironmanRestricted = true)
            (SARADOMIN_BREW4+1)(1000, 0,3, ironmanRestricted = true)
            (SANFEW_SERUM4+1)(1000, 0,2, ironmanRestricted = true)
            (ANTIVENOM4_12913+1)(1000, 0,3, ironmanRestricted = true)
            (BASTION_POTION4+1)(1000, 0,2, ironmanRestricted = true)
            (COOKED_KARAMBWAN+1)(1000, 0,1, ironmanRestricted = true)
            (ANGLERFISH+1)(1000, 0,2, ironmanRestricted = true)
            BLUE_DARK_BOW_PAINT(100, 0,100)
            GREEN_DARK_BOW_PAINT(100, 0,100)
            YELLOW_DARK_BOW_PAINT(100, 0,100)
            WHITE_DARK_BOW_PAINT(100, 0,100)
            PADDEWWA_TELEPORT(1000, 0,1)
            SENNTISTEN_TELEPORT(1000, 0,1)
            ANNAKARL_TELEPORT(1000, 0,1)
            CARRALLANGAR_TELEPORT(1000, 0,1)
            DAREEYAK_TELEPORT(1000, 0,1)
            GHORROCK_TELEPORT(1000, 0,1)
            KHARYRLL_TELEPORT(1000, 0,1)
            LASSAR_TELEPORT(1000, 0,1)
            FROZEN_WHIP_MIX(100, 0,100)
            VOLCANIC_WHIP_MIX(100, 0,100)
            STEAM_STAFF_UPGRADE_KIT(100, 0,250)
            LAVA_STAFF_UPGRADE_KIT(100, 0,250)
            WARD_UPGRADE_KIT(100, 0,350)
            SCROLL_OF_IMBUING(100, 0,300)
            SARADOMINS_TEAR(100, 0, 200)
            SARADOMIN_HALO(100, 0, 250)
            GUTHIX_HALO(100, 0, 250)
            ZAMORAK_HALO(100, 0, 250)
            ARMADYL_HALO(100, 0, 250)
            BANDOS_HALO(100, 0, 250)
            SEREN_HALO(100, 0, 250)
            ANCIENT_HALO(100, 0, 250)
            BRASSICA_HALO(100, 0, 250)
            CRYSTAL_SEED(100, 0, 250)
            PVP_MYSTERY_BOX(100, 0, 6000)
        }
    }
}