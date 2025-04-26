package com.zenyte.game.content.wildernessVault.reward;

import static com.zenyte.game.content.wildernessVault.WildernessVaultConstants.*;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.wildernessVault.WildernessVaultHandler;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;

import java.util.Optional;

public class WildernessVaultRewardHandler {

    private final Player player;
    private final ObjectArrayList<VaultRewardItem> loot;
    private final Container container;
    private boolean looted;

    private final VaultRoomInstance instance;

    public WildernessVaultRewardHandler(Player player) throws OutOfSpaceException {
        this.player = player;
        this.loot = getLoot(3);
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.THEATRE_OF_BLOOD, Optional.of(player));
        for (VaultRewardItem r : loot) {
            if (r == null) continue;
            container.add(new Item(r.getId(), Utils.random(r.getMin(), r.getMax())));
        }
        final AllocatedArea area = MapBuilder.findEmptyChunk(64, 64);
        this.instance = new VaultRoomInstance(area);
        this.instance.constructRegion();
    }

    public void onEnterRoom() {
        player.setLocation(instance.getLocation(CHEST_ENTRANCE));
        World.spawnObject(new WorldObject(rare(loot) ? RARE_CHEST : CHEST, 10, 6, instance.getLocation(CHEST_SPAWN)));
    }

    public Container getContainer() {
        return container;
    }

    public boolean isLooted() {
        return looted;
    }

    public void lootChest() {
        if (!WildernessVaultHandler.getLooted().contains(player.getName())) {
            WildernessVaultHandler.getLooted().add(player.getName());
        }
        player.getPacketDispatcher().sendUpdateItemContainer(container);
        GameInterface.WILDERNESS_VAULT_REWARDS.open(player);
        if (!looted) {
            World.replaceObject(World.getObjectWithType(CHEST_SPAWN.copy().moveLocation(0, 0, player.getIndex() * 4), 10),
                    new WorldObject(new WorldObject(rare(loot) ? RARE_CHEST_OPEN : CHEST_OPEN, 10, 6, instance.getLocation(CHEST_SPAWN))));
            for (VaultRewardItem r : loot) {
                if (r.isRare())
                    WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, new Item(r.getId()), "Wilderness vault");
            }
        }

        looted = true;
    }

    /**
     * Adds the loot to the player's inventory, or drops it under them. Refreshes the containers.
     */
    public void addLoot() {
        if (container.isEmpty()) return;
        final Container inventory = player.getInventory().getContainer();
        container.getItems().int2ObjectEntrySet().fastForEach(entry -> {
            player.getCollectionLog().add(entry.getValue());
            final boolean addToRunePouch;
            if (player.getInventory().containsAnyOf(RunePouch.POUCHES)) {
                final int amountInRunePouch = player.getRunePouch().getAmountOf(entry.getValue().getId());
                addToRunePouch = amountInRunePouch > 0 && (amountInRunePouch + entry.getValue().getAmount()) < 16000;
            } else {
                addToRunePouch = false;
            }final boolean addToQuiver = (player.getEquipment().getId(EquipmentSlot.AMMUNITION) == entry.getValue().getId() || (entry.getValue().isStackable() && player.getEquipment().getId(EquipmentSlot.WEAPON) == entry.getValue().getId()));
            final Container container = addToQuiver ? player.getEquipment().getContainer() : addToRunePouch ? player.getRunePouch().getContainer() : inventory;
            container.add(entry.getValue()).onFailure(remainder -> World.spawnFloorItem(remainder, player));
        });
        player.getRunePouch().getContainer().refresh(player);
        player.getEquipment().getContainer().refresh(player);
        inventory.refresh(player);
        container.refresh(player);
        container.clear();
    }

    public static ObjectArrayList<VaultRewardItem> getLoot(int amt) {
        ObjectArrayList<VaultRewardItem> loot = new ObjectArrayList<>();
        for (int i = 0; i < amt; i++) {
            loot.add(VaultRewardItem.REWARDS.rollItem());
        }
        for (VaultRewardItem alway : VaultRewardItem.ALWAYS) {
            loot.add(alway);
        }
        return loot;
    }

    public static boolean rare(ObjectArrayList<VaultRewardItem> loot) {
        for (VaultRewardItem vaultRewardItem : loot) {
            if(vaultRewardItem.isRare())
                return true;
        }
        return false;
    }




}
