package com.zenyte.game.content.skills.hunter;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Kris | 14/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CustomFurClothingInterface extends Interface {

    private enum FurStock {
        POLAR_CAMO_TOP(ItemId.POLAR_CAMO_TOP, 20, new Item(ItemId.POLAR_KEBBIT_FUR, 2)),
        POLAR_CAMO_LEGS(ItemId.POLAR_CAMO_LEGS, 20, new Item(ItemId.POLAR_KEBBIT_FUR, 2)),
        WOOD_CAMO_TOP(ItemId.WOOD_CAMO_TOP, 20, new Item(ItemId.COMMON_KEBBIT_FUR, 2)),
        WOOD_CAMO_LEGS(ItemId.WOOD_CAMO_LEGS, 20, new Item(ItemId.COMMON_KEBBIT_FUR, 2)),
        JUNGLE_CAMO_TOP(ItemId.JUNGLE_CAMO_TOP, 20, new Item(ItemId.FELDIP_WEASEL_FUR, 2)),
        JUNGLE_CAMO_LEGS(ItemId.JUNGLE_CAMO_LEGS, 20, new Item(ItemId.FELDIP_WEASEL_FUR, 2)),
        DESERT_CAMO_TOP(ItemId.DESERT_CAMO_TOP, 20, new Item(ItemId.DESERT_DEVIL_FUR, 2)),
        DESERT_CAMO_LEGS(ItemId.DESERT_CAMO_LEGS, 20, new Item(ItemId.DESERT_DEVIL_FUR, 2)),
        LARUPIA_HAT(ItemId.LARUPIA_HAT, 500, new Item(ItemId.LARUPIA_FUR)),
        LARUPIA_TOP(ItemId.LARUPIA_TOP, 100, new Item(ItemId.LARUPIA_FUR), new Item(ItemId.TATTY_LARUPIA_FUR)),
        LARUPIA_LEGS(ItemId.LARUPIA_LEGS, 100, new Item(ItemId.LARUPIA_FUR), new Item(ItemId.TATTY_LARUPIA_FUR)),
        GRAAHK_HEADDRESS(ItemId.GRAAHK_HEADDRESS, 750, new Item(ItemId.GRAAHK_FUR)),
        GRAAHK_TOP(ItemId.GRAAHK_TOP, 150, new Item(ItemId.GRAAHK_FUR), new Item(ItemId.TATTY_GRAAHK_FUR)),
        GRAAHK_LEGS(ItemId.GRAAHK_LEGS, 150, new Item(ItemId.GRAAHK_FUR), new Item(ItemId.TATTY_GRAAHK_FUR)),
        KYATT_HAT(ItemId.KYATT_HAT, 1000, new Item(ItemId.KYATT_FUR)),
        KYATT_TOP(ItemId.KYATT_TOP, 200, new Item(ItemId.KYATT_FUR), new Item(ItemId.TATTY_KYATT_FUR)),
        KYATT_LEGS(ItemId.KYATT_LEGS, 200, new Item(ItemId.KYATT_FUR), new Item(ItemId.TATTY_KYATT_FUR)),
        GLOVES_OF_SILENCE(ItemId.GLOVES_OF_SILENCE, 600, new Item(ItemId.DARK_KEBBIT_FUR, 2)),
        SPOTTED_CAPE(ItemId.SPOTTED_CAPE, 400, new Item(ItemId.SPOTTED_KEBBIT_FUR, 2)),
        SPOTTIER_CAPE(ItemId.SPOTTIER_CAPE, 800, new Item(ItemId.DASHING_KEBBIT_FUR, 2));
        private static final FurStock[] values = values();
        private final int item;
        private final int fee;
        private final Item fur;
        private final Item alternativeFur;

        FurStock(final int item, final int fee, @NotNull final Item fur) {
            this(item, fee, fur, null);
        }

        FurStock(int item, int fee, Item fur, Item alternativeFur) {
            this.item = item;
            this.fee = fee;
            this.fur = fur;
            this.alternativeFur = alternativeFur;
        }

        public int getItem() {
            return item;
        }

        public int getFee() {
            return fee;
        }

        public Item getFur() {
            return fur;
        }

        public Item getAlternativeFur() {
            return alternativeFur;
        }
    }

    public CustomFurClothingInterface() {
        container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.CUSTOM_FUR_CLOTHING, Optional.empty());
        for (final CustomFurClothingInterface.FurStock furItem : FurStock.values) {
            container.add(new Item(furItem.item, 100));
        }
    }

    private final Container container;

    @Override
    protected void attach() {
        put(6, "Interact with item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);

        //player.getPacketDispatcher().sendClientScript(2524, -1, -1);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), 6, 0, 19, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3,  AccessMask.CLICK_OP4, AccessMask.CLICK_OP5);
    }

    @Override
    protected void build() {

        bind("Interact with item", (player, slotId, itemId, option) -> {
            final CustomFurClothingInterface.FurStock furItem = Objects.requireNonNull(FurStock.values[slotId]);
            final int fee = furItem.fee;
            final Item requirement = furItem.fur;
            final Item alternativeRequirement = furItem.alternativeFur;
            final String name = ItemDefinitions.getOrThrow(furItem.item).getName();
            if (option == 1) {
                player.sendMessage(name + " costs: " + furItem.fee + " coins and " + (alternativeRequirement == null ? "" : "either ") + requirement.getAmount() + " " + requirement.getName().toLowerCase() + (alternativeRequirement == null ? "" : (" or 1 " + alternativeRequirement.getName().toLowerCase())) + ".");
                return;
            }
            final Inventory inventory = player.getInventory();
            final int amount = option == 2 ? 1 : option == 3 ? 5 : 10;
            for (int i = 0; i < amount; i++) {
                if (!inventory.containsItem(ItemId.COINS_995, fee) || (!inventory.containsItem(requirement) && (alternativeRequirement == null || !inventory.containsItem(alternativeRequirement)))) {
                    final String lowercaseName = name.toLowerCase();
                    final String prefix = furItem == FurStock.GLOVES_OF_SILENCE || name.endsWith("legs") ? ("a pair of") : Utils.getAOrAn(name);
                    player.sendMessage("You need at least " + furItem.fee + " coins and " + (alternativeRequirement == null ? "" : "either ") + requirement.getAmount() + " " + requirement.getName().toLowerCase() + (alternativeRequirement == null ? "" : (" or 1 " + alternativeRequirement.getName().toLowerCase())) + " to purchase " + prefix + " " + lowercaseName + ".");
                    return;
                }
                inventory.deleteItem(requirement).onFailure(it -> inventory.deleteItem(alternativeRequirement));
                inventory.deleteItem(ItemId.COINS_995, fee);
                inventory.addOrDrop(new Item(furItem.item));
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.CUSTOM_FUR_CLOTHING;
    }
}
