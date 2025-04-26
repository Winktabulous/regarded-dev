package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.lootkeys.LootkeyConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.LootingBag;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * @author Tommeh | 24 apr. 2018 | 17:23:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DestroyItemDialogue extends Dialogue {
    private final Item item;
    private final int slot;

    public DestroyItemDialogue(final Player player, final Item item, final int slot) {
        super(player);
        this.item = item;
        this.slot = slot;
    }

    @Override
    public void buildDialogue() {
        destroyItem(item, getMessage(item)).onYes(() -> {
            final Inventory inventory = player.getInventory();
            final Item inSlot = inventory.getItem(slot);
            player.sendSound(2381);
            if (inSlot == item) {
                inventory.deleteItem(slot, item);
                player.log(LogLevel.INFO, "Destroying item '" + item + "'.");
            } else {
                for (final Int2ObjectMap.Entry<Item> entry : inventory.getContainer().getItems().int2ObjectEntrySet()) {
                    if (entry.getValue() == item) {
                        inventory.deleteItem(entry.getIntKey(), item);
                        player.log(LogLevel.INFO, "Destroying item '" + item + "'.");
                        break;
                    }
                }
            }
            handle();
        });
    }

    private String getMessage(final Item item) {
        final int id = item.getId();
        if (id == 22711) {
            return "You can get another Collection log from the Collector in the Varrock Museum.";
        }
        if (id == ItemId.SKELETON_BOOTS || id == ItemId.SKELETON_GLOVES || id == ItemId.SKELETON_LEGGINGS || id == ItemId.SKELETON_MASK || id == ItemId.SKELETON_SHIRT) {
            return "You can reclaim this item from Diango in Draynor Village.";
        }
        return "Destroying is a permanent process. You will be able to reacquire the item where you obtained it in the first place.";
    }

    private void handle() {
        switch (item.getId()) {
            case LootkeyConstants.LOOT_KEY_ITEM_ID:
                player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                break;
            case ItemId.SEED_BOX:
            case ItemId.OPEN_SEED_BOX:
                player.getSeedBox().clear();
                break;
            case ItemId.HERB_SACK:
            case ItemId.OPEN_HERB_SACK:
                player.getHerbSack().clear();
                break;
            case ItemId.GEM_BAG_12020:
            case ItemId.OPEN_GEM_BAG:
                player.getGemBag().clear();
                break;
            case ItemId.RUNE_POUCH:
            case ItemId.DIVINE_RUNE_POUCH:
                player.getRunePouch().clear();
                break;
            case 26306:
            case 26304:
                player.getBonePouch().clear();
                break;
            case ItemId.LOOTING_BAG:
            case ItemId.LOOTING_BAG_22586:
                final boolean inWilderness = WildernessArea.isWithinWilderness(player);
                if (inWilderness) {
                    for (final Int2ObjectMap.Entry<Item> entry : player.getLootingBag().getContainer().getItems().int2ObjectEntrySet()) {
                        final Item item = entry.getValue();
                        final boolean consumable = item.getDefinitions().containsOption("Eat") || item.getDefinitions().containsOption("Drink") || Consumable.consumables.containsKey(item.getId());
                        World.spawnFloorItem(item, player, !consumable && item.isTradable() ? -1 : 300, item.isTradable() ? 500 : -1);
                    }
                }
                player.getLootingBag().clear();
                player.getLootingBag().setOpen(false);
                break;
            case ItemId.DODGY_NECKLACE:
                player.addAttribute("dodgy necklace uses", 0);
                break;
            case ItemId.AMULET_OF_BOUNTY:
                player.addAttribute("amulet of bounty uses", 0);
                break;
            case ItemId.AMULET_OF_CHEMISTRY:
                player.addAttribute("amulet of chemistry uses", 0);
                break;
        }
    }
}
