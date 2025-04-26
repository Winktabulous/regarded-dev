package com.zenyte.plugins.item.mysteryboxes;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PvpTourneyMysteryBox extends ItemPlugin {
    public static int totalWeight;
    public static ArrayList<MysteryItem> rewards = new ArrayList<>();

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> openBoxQuick(player));

        rewards.addAll(List.of(
                // Common = 1000
                new MysteryItem(ItemId.GUTHANS_ARMOUR_SET, 1, 1, 750),
                new MysteryItem(ItemId.TORAGS_ARMOUR_SET, 1, 1, 750),
                new MysteryItem(ItemId.AHRIMS_ARMOUR_SET, 1, 1, 750),
                new MysteryItem(ItemId.VERACS_ARMOUR_SET, 1, 1, 750),
                new MysteryItem(ItemId.DHAROKS_ARMOUR_SET, 1, 1, 750),
                new MysteryItem(ItemId.KARILS_ARMOUR_SET, 1, 1, 750),
                new MysteryItem(ItemId.DRAGON_BATTLEAXE, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_LONGSWORD, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_BATTLEAXE, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_MACE, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_SCIMITAR, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_DAGGERP_5698, 1, 1, 1000),
                new MysteryItem(ItemId.ABYSSAL_WHIP, 1, 1, 1000),
                new MysteryItem(CustomItemId.LIME_WHIP, 1, 1, 1000),
                new MysteryItem(CustomItemId.LAVA_WHIP, 1, 1, 1000),
                new MysteryItem(CustomItemId.VESTAS_HELM, 1, 1, 1000),
                new MysteryItem(ItemId.VESTAS_SPEAR, 1, 1, 1000),
                new MysteryItem(ItemId.VESTAS_LONGSWORD, 1, 1, 1000),
                new MysteryItem(ItemId.VESTAS_CHAINBODY, 1, 1, 1000),
                new MysteryItem(ItemId.VESTAS_PLATESKIRT, 1, 1, 1000),
                new MysteryItem(ItemId.BARRELCHEST_ANCHOR, 1, 1, 1000),
                new MysteryItem(ItemId.DHAROKS_HELM, 1, 1, 1000),
                new MysteryItem(ItemId.DHAROKS_GREATAXE, 1, 1, 1000),
                new MysteryItem(ItemId.DHAROKS_PLATELEGS, 1, 1, 1000),
                new MysteryItem(ItemId.DHAROKS_PLATEBODY, 1, 1, 1000),
                new MysteryItem(ItemId.TORAGS_HELM, 1, 1, 1000),
                new MysteryItem(ItemId.TORAGS_HAMMERS, 1, 1, 1000),
                new MysteryItem(ItemId.TORAGS_PLATEBODY, 1, 1, 1000),
                new MysteryItem(ItemId.TORAGS_PLATELEGS, 1, 1, 1000),
                new MysteryItem(ItemId.RUBBER_CHICKEN, 1, 1, 1000),
                new MysteryItem(ItemId.TROLLWEISS, 1, 1, 1000),
                new MysteryItem(ItemId.TORVA_FULLHELM, 1, 1, 1000),
                new MysteryItem(ItemId.TORVA_PLATEBODY, 1, 1, 1000),
                new MysteryItem(ItemId.TORVA_PLATELEGS, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_BOOTS, 1, 1, 1000),
                new MysteryItem(ItemId.BERSERKER_RING_I, 1, 1, 1000),
                new MysteryItem(ItemId.BERSERKER_RING, 1, 1, 1000),
                new MysteryItem(ItemId.WARRIOR_RING, 1, 1, 1000),
                new MysteryItem(ItemId.WARRIOR_RING_I, 1, 1, 1000),
                new MysteryItem(ItemId.GHRAZI_RAPIER, 1, 1, 1000),
                new MysteryItem(ItemId.AVERNIC_DEFENDER, 1, 1, 1000),
                new MysteryItem(ItemId.DHAROKS_GREATAXE, 1, 1, 1000),
                new MysteryItem(CustomItemId.DEATH_CAPE, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_HUNTER_LANCE, 1, 1, 1000),
                new MysteryItem(ItemId.BRIMSTONE_RING, 1, 1, 1000),
                new MysteryItem(CustomItemId.DRAGON_KITE, 1, 1, 1000),
                new MysteryItem(ItemId.ELITE_VOID_KNIGHT_SET, 1, 1, 1000),
                new MysteryItem(CustomItemId.BANDOS_BOW, 1, 1, 1000),
                new MysteryItem(CustomItemId.ZAMORAK_BOW, 1, 1, 1000),
                new MysteryItem(CustomItemId.ARMADYL_BOW, 1, 1, 1000),
                new MysteryItem(CustomItemId.SARADOMIN_BOW, 1, 1, 1000),
                new MysteryItem(ItemId.KARILS_LEATHERTOP, 1, 1, 1000),
                new MysteryItem(ItemId.KARILS_LEATHERSKIRT, 1, 1, 1000),
                new MysteryItem(ItemId.KARILS_COIF, 1, 1, 1000),
                new MysteryItem(ItemId.ARMADYL_GODSWORD, 1, 1, 1000),
                new MysteryItem(ItemId.BANDOS_GODSWORD, 1, 1, 1000),
                new MysteryItem(ItemId.SARADOMIN_GODSWORD, 1, 1, 1000),
                new MysteryItem(ItemId.ZAMORAK_GODSWORD, 1, 1, 1000),
                new MysteryItem(ItemId.ANCIENT_GODSWORD, 1, 1, 1000),
                new MysteryItem(ItemId.ARMADYL_HELMET, 1, 1, 1000),
                new MysteryItem(ItemId.ARMADYL_CHESTPLATE, 1, 1, 1000),
                new MysteryItem(ItemId.ARMADYL_CHAINSKIRT, 1, 1, 1000),
                new MysteryItem(ItemId.BANDOS_CHESTPLATE, 1, 1, 1000),
                new MysteryItem(ItemId.BANDOS_TASSETS, 1, 1, 1000),
                new MysteryItem(ItemId.BANDOS_BOOTS, 1, 1, 1000),
                new MysteryItem(ItemId.SARADOMIN_SWORD, 1, 1, 1000),
                new MysteryItem(CustomItemId.KORASI, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_CLAWS, 1, 1, 1000),
                new MysteryItem(ItemId.VOIDWAKER_27690, 1, 1, 1000),
                new MysteryItem(ItemId.BLADE_OF_SAELDOR, 1, 1, 1000),
                new MysteryItem(ItemId.BOW_OF_FAERDHINEN_C, 1, 1, 1000),
                new MysteryItem(ItemId.DINHS_BULWARK, 1, 1, 1000),
                new MysteryItem(ItemId.BARROWS_GLOVES, 1, 1, 1000),
                new MysteryItem(ItemId.RING_OF_RECOIL, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_2H_SWORD, 1, 1, 1000),
                new MysteryItem(ItemId.ANCIENT_MACE, 1, 1, 1000),
                new MysteryItem(ItemId.STATIUSS_WARHAMMER, 1, 1, 1000),
                new MysteryItem(ItemId.STATIUSS_FULL_HELM, 1, 1, 1000),
                new MysteryItem(ItemId.STATIUSS_PLATEBODY, 1, 1, 1000),
                new MysteryItem(ItemId.STATIUSS_PLATELEGS, 1, 1, 1000),
                new MysteryItem(ItemId.MORRIGANS_THROWING_AXE, 250, 250, 1000),
                new MysteryItem(ItemId.MORRIGANS_JAVELIN, 250, 250, 1000),
                new MysteryItem(ItemId.MORRIGANS_COIF, 1, 1, 1000),
                new MysteryItem(ItemId.MORRIGANS_LEATHER_BODY, 1, 1, 1000),
                new MysteryItem(ItemId.MORRIGANS_LEATHER_CHAPS, 1, 1, 1000),
                new MysteryItem(ItemId.MORRIGANS_JAVELIN, 1, 1, 1000),
                new MysteryItem(ItemId.AMULET_OF_GLORY, 1, 1, 1000),
                new MysteryItem(ItemId.AMULET_OF_TORTURE, 1, 1, 1000),
                new MysteryItem(ItemId.NECKLACE_OF_ANGUISH, 1, 1, 1000),
                new MysteryItem(ItemId.AMULET_OF_FURY, 1, 1, 1000),
                new MysteryItem(ItemId.ELDER_MAUL, 1, 1, 1000),
                new MysteryItem(ItemId.ABYSSAL_DAGGER_P_13271, 1, 1, 1000),
                new MysteryItem(ItemId.ABYSSAL_BLUDGEON, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_HASTA, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_SWORD, 1, 1, 1000),
                new MysteryItem(ItemId.OSMUMTENS_FANG, 1, 1, 1000),
                new MysteryItem(ItemId.SARADOMINS_BLESSED_SWORD, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_KNIFE, 500, 500, 1000),
                new MysteryItem(ItemId.GRANITE_MAUL, 1, 1, 1000),
                new MysteryItem(ItemId.ABYSSAL_TENTACLE, 1, 1, 1000),
                new MysteryItem(ItemId.ANCIENT_WYVERN_SHIELD, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGONFIRE_SHIELD, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGONFIRE_WARD, 1, 1, 1000),
                new MysteryItem(ItemId.BLACK_DHIDE_VAMB, 1, 1, 1000),
                new MysteryItem(ItemId.BLACK_DHIDE_CHAPS, 1, 1, 1000),
                new MysteryItem(ItemId.BLACK_DHIDE_BODY, 1, 1, 1000),
                new MysteryItem(ItemId.TYRANNICAL_RING, 1, 1, 1000),
                new MysteryItem(ItemId.TREASONOUS_RING, 1, 1, 1000),
                new MysteryItem(ItemId.RING_OF_THE_GODS, 1, 1, 1000),
                new MysteryItem(ItemId.AMYS_SAW, 1, 1, 1000),
                new MysteryItem(ItemId.BLISTERWOOD_FLAIL, 1, 1, 1000),
                new MysteryItem(ItemId.CLEAVER, 1, 1, 1000),
                new MysteryItem(ItemId.BLISTERWOOD_FLAIL, 1, 1, 1000),
                new MysteryItem(ItemId.ACCURSED_SCEPTRE_27665, 1, 1, 1000),
                new MysteryItem(ItemId.WEBWEAVER_BOW_27655, 1, 1, 1000),
                new MysteryItem(ItemId.URSINE_CHAINMACE_27660, 1, 1, 1000),
                new MysteryItem(ItemId.POLYPORE_STAFF, 1, 1, 1000),
                new MysteryItem(ItemId.ANCESTRAL_HAT, 1, 1, 1000),
                new MysteryItem(ItemId.ANCESTRAL_ROBE_TOP, 1, 1, 1000),
                new MysteryItem(ItemId.ANCESTRAL_ROBE_BOTTOM, 1, 1, 1000),
                new MysteryItem(ItemId.ZURIELS_HOOD, 1, 1, 1000),
                new MysteryItem(ItemId.ZURIELS_ROBE_TOP, 1, 1, 1000),
                new MysteryItem(ItemId.ZURIELS_ROBE_BOTTOM, 1, 1, 1000),
                new MysteryItem(ItemId.DRAGON_DEFENDER, 1, 1, 1000),
                new MysteryItem(ItemId.ELDRITCH_NIGHTMARE_STAFF, 1, 1, 1000),
                new MysteryItem(ItemId.VOLATILE_NIGHTMARE_STAFF, 1, 1, 1000)
        ));
    }
    static Random rand = new Random();
    @Override
    public int[] getItems() {
        return new int[]{CustomItemId.PVP_TOURNEY_MYSTERY_BOX};
    }

    public static void openBoxQuick(Player player) {
        Item box = new Item(CustomItemId.PVP_TOURNEY_MYSTERY_BOX, 1);
        if(player.hasSpawnRights()) {
            if (!player.getInventory().deleteItem(box).isFailure()) {
                MysteryItem jackpot = rewards.get(rand.nextInt(rewards.size()));
                final Item reward = new Item(jackpot.getId(), Utils.random(jackpot.getMinAmount(), jackpot.getMaxAmount()));
                if(reward.getId() == ItemId.KARILS_ARMOUR_SET || reward.getId() == ItemId.KARILS_CROSSBOW)
                    player.getInventory().addOrDrop(new Item(ItemId.BOLT_RACK, 1000));
                reward.setAttribute("tournament-item", true);
                player.getInventory().addOrDrop(reward);
            }
            return;
        }
        if (!player.getInventoryTemp().deleteItem(box).isFailure()) {
            MysteryItem jackpot = rewards.get(rand.nextInt(rewards.size()));
            final Item reward = new Item(jackpot.getId(), Utils.random(jackpot.getMinAmount(), jackpot.getMaxAmount()));
            if(reward.getId() == ItemId.KARILS_ARMOUR_SET || reward.getId() == ItemId.KARILS_CROSSBOW)
                player.getInventoryTemp().addOrDrop(new Item(ItemId.BOLT_RACK, 1000));
            reward.setAttribute("tournament-item", true);
            player.getInventoryTemp().addOrDrop(reward);
        }
    }
}
