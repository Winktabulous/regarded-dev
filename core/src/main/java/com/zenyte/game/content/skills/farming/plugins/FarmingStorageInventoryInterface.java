package com.zenyte.game.content.skills.farming.plugins;

import com.google.common.base.Preconditions;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.farming.FarmingStorage;
import com.zenyte.game.content.skills.farming.Storable;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 24/12/2018 23:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FarmingStorageInventoryInterface extends Interface {
    private static final void add(@NotNull final Player player, @NotNull final Storable storable, int option) {
        final FarmingStorage storage = player.getFarming().getStorage();
        final FarmingStorageInterface.Option op = storage.getOption(option);
        final Item item = storage.getItem(storable);
        switch (op) {
        case EXAMINE: 
            if (item == null) {
                ItemUtil.sendItemExamine(player, storable.getBaseId());
            } else {
                ItemUtil.sendItemExamine(player, item);
            }
            return;
        case REMOVE_X: 
            player.sendInputInt("How many would you like to add?", i -> storage.addItem(storable, i));
            return;
        case REMOVE_1: 
            storage.addItem(storable, 1);
            return;
        case REMOVE_5: 
            storage.addItem(storable, 5);
            return;
        case REMOVE_ALL: 
            storage.addItem(storable, Integer.MAX_VALUE);
            return;
        default: 
            throw new RuntimeException();
        }
    }

    @Override
    protected void attach() {
        put(1, "Rake");
        put(2, "Seed dibber");
        put(3, "Spade");
        put(4, "Secateurs");
        put(5, "Watering can");
        put(6, "Gardening trowel");
        put(7, "Plant cure");
        put(8, "Bottomless buckets");
        put(9, "Empty buckets");
        put(10, "Normal compost");
        put(11, "Supercompost");
        put(12, "Ultracompost");
    }

    @Override
    public void open(Player player) {
        Preconditions.checkArgument(player.getInterfaceHandler().isVisible(GameInterface.FARMING_STORAGE.getId()));
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Rake", (player, slotId, itemId, option) -> add(player, Storable.FARMING_RAKE, option));
        bind("Seed dibber", (player, slotId, itemId, option) -> add(player, Storable.SEED_DIBBER, option));
        bind("Spade", (player, slotId, itemId, option) -> add(player, Storable.SPADE, option));
        bind("Secateurs", (player, slotId, itemId, option) -> add(player, Storable.SECATEURS, option));
        bind("Watering can", (player, slotId, itemId, option) -> add(player, Storable.WATERING_CAN, option));
        bind("Gardening trowel", (player, slotId, itemId, option) -> add(player, Storable.GARDENING_TROWEL, option));
        bind("Plant cure", (player, slotId, itemId, option) -> add(player, Storable.PLANT_CURE, option));
        bind("Bottomless buckets", (player, slotId, itemId, option) -> add(player, Storable.BOTTOMLESS_BUCKET, option));
        bind("Empty buckets", (player, slotId, itemId, option) -> add(player, Storable.EMPTY_BUCKET, option));
        bind("Normal compost", (player, slotId, itemId, option) -> add(player, Storable.NORMAL_COMPOST, option));
        bind("Supercompost", (player, slotId, itemId, option) -> add(player, Storable.SUPER_COMPOST, option));
        bind("Ultracompost", (player, slotId, itemId, option) -> add(player, Storable.ULTRACOMPOST, option));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.FARMING_STORAGE_INVENTORY;
    }
}
