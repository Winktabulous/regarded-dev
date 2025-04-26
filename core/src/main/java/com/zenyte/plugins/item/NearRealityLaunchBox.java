package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemChat;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Andys1814
 * @author Jacmob
 */
public final class NearRealityLaunchBox extends ItemPlugin {

    public enum NearRealityLaunchBoxItem {
        //RESOURCES + CONSUMABLES
        DRAGON_DART_TIP(11232, 100, 150, 1000, 0),
        DRAGON_KNIFE(22804, 100, 150, 1000, 0),
        DRAGON_THROWNAXE(20849, 100, 150, 1000, 0),
        RAW_SHARK(384, 100, 225, 1000, 0),
        RAW_ANGLERFISH(13440, 100, 150, 1000, 0),
        RAW_MANTA_RAY(390, 100, 150, 1000, 0),
        UNCUT_DRAGONSTONE(1632, 25, 35, 1000, 0),
        UNCUT_DIAMOND(1618, 40, 60, 1000, 0),
        UNCUT_RUBY(1620, 50, 70, 1000, 0),
        MITHRIL_BAR(2360, 120, 200, 1000, 0),
        ADAMANTITE_BAR(2362, 100, 150, 1000, 0),
        RUNITE_BAR(2364, 50, 75, 1000, 0),
        MAGIC_LOGS(1514, 100, 150, 1000, 0),
        DRAGON_BONES(537, 75, 100, 1000, 0),
        CANNONBALL(2, 400, 500, 1000, 0),
        CHINCHOMPA(10033, 150, 300, 1000, 0),
        RED_CHINCHOMPA(10034, 150, 300, 1000, 0),
        BLACK_CHINCHOMPA(11959, 120, 200, 1000, 0),
        DIAMOND_BOLTS_E(9243, 300, 500, 1000, 0),
        DRAGONSTONE_BOLTS_E(9244, 250, 350, 1000, 0),
        YEW_SEED(5315, 5, 10, 1000, 0),
        MAGIC_SEED(5316, 3, 5, 1000, 0),
        PALM_TREE_SEED(5289, 3, 5, 1000, 0),
        PAPAYA_TREE_SEED(5288, 5, 10, 1000, 0),
        STAMINA_POTION(12626, 15, 25, 1000, 0),
        SUPER_COMBAT(12696, 15, 20, 1000, 0),
        SANFEW_SERUM(10926, 15, 25, 1000, 0),
        ZULRAH_SCALES(ItemId.ZULRAHS_SCALES, 500, 2500, 500, 0),
        BURNT_PAGE(20718, 30, 50, 1000, 0),
        //All items above this line are rolled within the two supply rolls. An additional roll will be done for the entire table afterwards.
        //Crystal Chest
        INFINITY_ROBE_TOP(6916, 1, 1, 500, 50),
        INFINITY_ROBE_BOTTOM(6924, 1, 1, 500, 50),
        INFINITY_GLOVES(6922, 1, 1, 500, 50),
        INFINITY_BOOTS(6920, 1, 1, 500, 50),
        INFINITY_HAT(6918, 1, 1, 500, 50),
        AMULET_OF_ETERNAL_GLORY(19707, 1, 1, 100, 0),
        ANTI_PANTIES(13288, 1, 1, 100, 0),
        GNOME_CHILD_HAT(13655, 1, 1, 100, 0),
        DRAGON_AXE(6739, 1, 1, 1000, 0),
        DRAGON_BOOTS(11840, 1, 1, 1000, 50),
        VOLCANIC_WHIP_MIX(12771, 1, 1, 1000, 0),
        FROZEN_WHIP_MIX(12769, 1, 1, 1000, 0),
        ONYX(6573, 1, 1, 750, 150),
        //BARROWS
        GUTHANS_HELM(4724, 1, 1, 200, 0),
        GUTHANS_WARSPEAR(4726, 1, 1, 200, 0),
        GUTHANS_PLATEBODY(4728, 1, 1, 200, 0),
        GUTHANS_CHAINSKIRT(4730, 1, 1, 200, 0),
        VERACS_HELM(4753, 1, 1, 200, 0),
        VERACS_FLAIL(4755, 1, 1, 200, 0),
        VERACS_BRASSARD(4757, 1, 1, 200, 0),
        VERACS_PLATESKIRT(4759, 1, 1, 200, 0),
        DHAROKS_HELM(4716, 1, 1, 200, 0),
        DHAROKS_GREATAXE(4718, 1, 1, 200, 0),
        DHAROKS_PLATEBODY(4720, 1, 1, 200, 0),
        DHAROKS_PLATELEGS(4722, 1, 1, 200, 0),
        TORAGS_HELM(4745, 1, 1, 200, 0),
        TOAGS_HAMMERS(4747, 1, 1, 200, 0),
        TORAGS_PLATEBODY(4749, 1, 1, 200, 0),
        TORAGS_PLATELEGS(4751, 1, 1, 200, 0),
        AHRIMS_HOOD(4708, 1, 1, 200, 0),
        AHRIMS_STAFF(4710, 1, 1, 200, 0),
        AHRIMS_ROBETOP(4712, 1, 1, 200, 0),
        AHRIMS_ROBESKIRT(4714, 1, 1, 200, 0),
        KARILS_COIF(4732, 1, 1, 200, 0),
        KARILS_CROSSBOW(4734, 1, 1, 200, 0),
        KARILS_LEATHERTOP(4736, 1, 1, 200, 0),
        KARILS_LEATHERSKIRT(4738, 1, 1, 200, 0),
        AMULET_OF_THE_DAMNED(12851, 1, 1, 500, 100),
        //Donation Store Items
        NEAR_REALITY_PARTYHAT(11862, 1, 1, 20, 3000),
        ABYSSAL_WHIP(4151, 1, 1, 100, 75),
        SARADOMIN_SWORD(11838, 1, 1, 100, 100),
        ZAMORAKIAN_SPEAR(11824, 1, 1, 100, 300),
        DRAGON_DEFENDER(12954, 1, 1, 1000, 75),
        FIGHTER_TORSO(10551, 1, 1, 1000, 75),
        FIRE_CAPE(6570, 1, 1, 500, 75),
        AMULET_OF_FURY(6585, 1, 1, 1000, 75),
        MAGES_BOOK(6889, 1, 1, 500, 75),
        CRYSTAL_KEY(990, 1, 10, 1000, 10),
        //Extra
        DRAGONFIRE_SHIELD(11284, 1, 1, 100, 350),
        IMBUED_HEART(20724, 1, 1, 100, 350),
        BLACK_TOURMALINE_CORE(ItemId.BLACK_TOURMALINE_CORE, 1, 1, 50, 500),
        ANCIENT_SHARD(ItemId.ANCIENT_SHARD, 1, 7, 200, 0),
        SPIRIT_SHIELD(ItemId.SPIRIT_SHIELD, 1, 1, 100, 0),
        BLESSED_SPIRIT_SHIELD(ItemId.BLESSED_SPIRIT_SHIELD, 1, 1, 40, 0),
        ENHANCED_ICE_GLOVES(30030, 1, 1, 60, 0),
        DRAGON_PICKAXE(ItemId.DRAGON_PICKAXE, 1, 1, 75, 0),
        DRAGON_CROSSBOW(ItemId.DRAGON_CROSSBOW, 1, 1, 75, 250),
        PET_MYSTERY_BOX(30031, 1, 1, 80, 250),
        DRAGON_PLATEBODY(ItemId.DRAGON_PLATEBODY, 1, 1, 35, 250),
        DRAGON_FULL_HELM(ItemId.DRAGON_FULL_HELM, 1, 1, 35, 250);

        private final int id;
        private final int minAmount;
        private final int maxAmount;
        private final int weight;
        private final int credits;
        public static final NearRealityLaunchBoxItem[] values = values();
        private static int total;

        static {
            for (final NearRealityLaunchBoxItem reward : values) {
                total += reward.weight;
            }
        }

        public static void main(String[] args) {
            final int total = 1_000_000;
            final Map<NearRealityLaunchBoxItem, Integer> count = new HashMap<>();
            for (int i = 0; i < total; i++) {
                final NearRealityLaunchBoxItem mysteryItem =  NearRealityLaunchBoxItem.generate();
                final int previous = count.getOrDefault(mysteryItem, 1);
                count.put(mysteryItem, previous + 1);
            }
            count.entrySet().stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getValue))
                    .forEachOrdered(entry -> System.out.println(entry.getKey()+" - "+entry.getValue() / (total / 100.0)));
        }

        private static final NearRealityLaunchBoxItem generate() {
            final int random = Utils.random(total);
            int current = 0;
            for (NearRealityLaunchBoxItem it : values) {
                if ((current += it.weight) >= random) {
                    return it;
                }
            }
            return null;
        }

        NearRealityLaunchBoxItem(int id, int minAmount, int maxAmount, int weight, int credits) {
            this.id = id;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.weight = weight;
            this.credits = credits;
        }

        public int getId() {
            return id;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public int getWeight() {
            return weight;
        }

        public int getCredits() {
            return credits;
        }
    }

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> {
            player.getInventory().deleteItem(item);
            final StringBuilder builder = new StringBuilder();
            final List<NearRealityLaunchBoxItem> rewards = getRewards(player);
            for (final NearRealityLaunchBoxItem rewardItem : rewards) {
                final Item reward = new Item(rewardItem.id, Utils.random(rewardItem.minAmount, rewardItem.maxAmount));
                builder.append(reward.getAmount()).append(" x ").append(reward.getName()).append(", ");
                player.getInventory().addOrDrop(reward);
                if (rewardItem.credits >= 200) {
                    WorldBroadcasts.broadcast(player, BroadcastType.MYSTERY_BOX_RARE_ITEM, rewardItem.id, item.getName());
                }
            }
            builder.delete(builder.length() - 2, builder.length());
            player.getDialogueManager().start(new ItemChat(player, item, "You open the mystery box and find " + builder + "!"));
        });
    }

    private final List<NearRealityLaunchBoxItem> getRewards(@NotNull final Player player) {
        ArrayList<NearRealityLaunchBoxItem> list = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            list.add(generateItem(player));
        }
        return list;
    }

    @NotNull
    public static final NearRealityLaunchBoxItem generateItem(@NotNull final Player player) {
        NearRealityLaunchBoxItem rewardItem;
        while (true) {
            rewardItem = NearRealityLaunchBoxItem.generate();
            if (rewardItem == NearRealityLaunchBoxItem.FIGHTER_TORSO || rewardItem == NearRealityLaunchBoxItem.FIRE_CAPE || rewardItem == NearRealityLaunchBoxItem.DRAGON_DEFENDER) {
                if (player.containsItem(rewardItem.id)) {
                    continue;
                }
            }
            break;
        }
        return Objects.requireNonNull(rewardItem);
    }

    @Override
    public int[] getItems() {
        return new int[] { 32080 };
    }
}