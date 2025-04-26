package com.zenyte.game.model.item;

import com.near_reality.game.model.item.degrading.Degradeable;
import com.near_reality.game.world.entity.player.FakePlayer;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.boss.corporealbeast.CorporealBeastDynamicArea;
import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.event.easter2020.area.EggPlantArea;
import com.zenyte.game.content.event.easter2020.area.RabbitWarrenArea;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.LandOfSnowArea;
import com.zenyte.game.world.region.area.plugins.DropPlugin;
import com.zenyte.game.world.region.area.plugins.IDropPlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.plugins.dialogue.DestroyItemDialogue;
import com.zenyte.plugins.dialogue.followers.PetFishDropD;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import kotlinx.datetime.Instant;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * @author Tom
 */
@StaticInitializer
public enum ItemActionHandler {
    ;

    private static final Logger log = LoggerFactory.getLogger(ItemActionHandler.class);
    public static final ItemPlugin defaultAction;

    static {
        defaultAction = new ItemPlugin() {
            @Override
            public void handle() {
            }

            @Override
            public int[] getItems() {
                return new int[0];
            }
        };
        defaultAction.setDefaultHandlers();
    }

    public static void handle(final Player player, final int itemId, final int slotId, final int option) {
        final boolean isDebugEnabled = log.isDebugEnabled();
        if (option == 10) {
            if (isDebugEnabled)
                log.debug("[" + itemId + "] Item examine: " + ItemDefinitions.getOrThrow(itemId).getName() + ".");
            final Item item = player.getInventory().getItem(slotId);
            if (item == null || item.getId() != itemId) {
                return;
            }
            ItemUtil.sendItemExamine(player, item);
            return;
        }
        boolean gamble = itemId == 299 && player.getBooleanTemporaryAttribute("gambling");
        if (!(player instanceof FakePlayer)) {
            if ((player.isLocked() && !gamble) || player.isFullMovementLocked() || !player.getInterfaceHandler().isPresent(GameInterface.INVENTORY_TAB)) {
                return;
            }
        }
        player.stopAll(false, true, true);
        final Item item = player.getInventory().getItem(slotId);
        if (item == null || item.getId() != itemId) {
            return;
        }
        final ItemDefinitions itemDef = item.getDefinitions();
        if (itemDef == null) {
            player.sendMessage("Nothing interesting happens.");
            log.debug("Item (id: {}, slot: {}) did not have a definition! (option: {})", itemId, slotId, option);
            return;
        }
        final String optionName = itemDef.getOption(option - 1);
        if (optionName == null) {
            log.debug("Item (id: {}, slot: {}) option not found for option: {}", itemId, slotId, option);
            return;
        }
        final ItemPlugin action = ItemPlugin.getPlugin(itemId);
        final ItemPlugin.OptionHandler handler = action.getHandler(optionName);
        if (handler != null) {
            final String pluginClassName = action.getClass().getSimpleName();
            if (isDebugEnabled)
                log.debug("[" + (pluginClassName.isEmpty() ? "Absent" : pluginClassName) + "] " + item.getName() + ": "
                        + item.getId() + " x " + item.getAmount() + ", Slot: " + slotId + ", Option: " + (optionName + " [" + option + "]"));
            handler.handle(player, item, player.getInventory().getContainer(), slotId);
            return;
        }
        player.sendMessage("Nothing interesting happens.");
        if (isDebugEnabled)
            log.debug("Item option: " + item.getId() + " x " + item.getAmount() + ", " + item.getName() + ", " + slotId +
                    ", " + option);
    }

    public static final Int2ObjectMap<ItemPlugin> intActions = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<ItemPlugin> deathIntActions = new Int2ObjectOpenHashMap<>();

    public static void add(final Class<?> c) {
        try {
            final ItemPlugin base = (ItemPlugin) c.getDeclaredConstructor().newInstance();
            if (base.getItemDeathHandler() != ItemPlugin.DEFAULT_ITEM_DEATH_HANDLER) {
                for (final int item : base.getItems()) {
                    final ItemPlugin old = deathIntActions.put(item, base);
                    if (old != null) {
                        throw new IllegalStateException("Overriding item plugin: " + item + ", " + old.getClass().getSimpleName() + " with " + c.getSimpleName() + "!");
                    }
                }
            }
            base.handle();
            if (!base.getDelegatedInventoryHandlers().isEmpty()) {
                for (final int item : base.getItems()) {
                    final ItemPlugin old = intActions.put(item, base);
                    if (old != null) {
                        throw new IllegalStateException("Overriding item plugin: " + item + ", " + old.getClass().getSimpleName() + " with " + c.getSimpleName() + "!");
                    }
                }
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void setDefaults() {
        for (ItemPlugin plugin : intActions.values()) {
            plugin.setDefaultHandlers();
        }
    }

    private static final SoundEffect OTHER_SOUND = new SoundEffect(2739);

    public static void dropItem(final Player player, final String option, final int slotId, final int invisibleDelay,
                                final int visibleDelay) {
        final Item item = player.getInventory().getItem(slotId);
        if (item == null) {
            return;
        }
        final RegionArea area = player.getArea();
        if ((area instanceof IDropPlugin && !((IDropPlugin) area).drop(player, item)) || !player.getControllerManager().canDropItem(item)) {
            player.log(LogLevel.INFO, "Area-Dropping item '" + item + "' at " + player.getLocation() + ".");
            logValuableItemDrop(player, item);
            return;
        }
        if (option.equals("Destroy")) {
            player.getDialogueManager().start(new DestroyItemDialogue(player, item, slotId));
            return;
        }
        if (PetWrapper.getByItem(item.getId()) != null) {
            if (player.inArea(LandOfSnowArea.class)) {
                player.sendMessage("Your follower does not seem to be too fond of the festivities and won't come out.");
                return;
            }
            if (player.inArea(RabbitWarrenArea.class) || player.inArea(EggPlantArea.class)) {
                player.sendMessage("Your follower isn't interested in coming out with all those machines making noise" +
                        ".");
                return;
            }
            if (player.getFollower() != null) {
                player.sendMessage("You already have a follower!");
                return;
            }
            if (player.inArea("Corporeal Beast cavern") || player.getArea() instanceof CorporealBeastDynamicArea) {
                player.sendMessage("Your follower hides in fear and won't come out.");
                return;
            }
            final Pet pet = PetWrapper.getByItem(item.getId());
            if (pet.petId() == -1) {
                player.getDialogueManager().start(new PetFishDropD(player, item, slotId));
                return;
            }
            player.getInventory().deleteItem(slotId, item);
            player.setFollower(new Follower(pet.petId(), player));
            player.setAnimation(PetWrapper.DROP_ANIMATION);
            return;
        }
        final Degradeable degradableItem = DegradableItem.ITEMS.get(item.getId());
        if (degradableItem != null) {
            if (player.getAttributes().containsKey("Ignore charged item drop message")) {
                degrade(player, item, slotId, false, invisibleDelay, visibleDelay);
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "Dropping this item will completely degrade it. Are you sure you wish to do so?");
                    options("Drop the item?", new DialogueOption("Yes.", () -> degrade(player, item, slotId, false,
                                    invisibleDelay, visibleDelay)), new DialogueOption("Yes, don't ask this for any " +
                                    "charged " +
                                    "item again.", () -> degrade(player, item, slotId, true, invisibleDelay,
                                    visibleDelay)),
                            new DialogueOption("No."));
                }
            });
            return;
        }
        if (player.getTemporaryAttributes().remove("threshold warning bypass") == null) {
            final int threshold =
                    player.getVarManager().getBitValue(SettingsInterface.MINIMUM_DROP_ITEM_VALUE_VARBIT_ID);
            if (player.getVarManager().getBitValue(SettingVariables.DROP_ITEM_WARNING_VARBIT_ID) == 1 && threshold > 0 && item.getSellPrice() >= threshold) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        item(item,
                                "This item you are trying to drop is considered " + Colour.RS_RED.wrap("valuable") +
                                        ". Are you absolutely sure you want to drop it?");
                        options(item.getName() + ": Really drop it?", new DialogueOption("Drop it.", () -> {
                            player.getTemporaryAttributes().put("threshold warning bypass", true);
                            dropItem(player, option, slotId, invisibleDelay, visibleDelay);
                        }), new DialogueOption("No, don't drop it."));
                    }
                });
                return;
            }
        }
        player.getInventory().deleteItem(slotId, item);
        player.getInterfaceHandler().closeInterfaces();
        player.getPacketDispatcher().sendSoundEffect(OTHER_SOUND);
        if (area instanceof DropPlugin) {
            if (!((DropPlugin) area).dropOnGround(player, item)) {
                return;
            }
        }
        if (GameConstants.WORLD_PROFILE.isPublic()) {
            if (player.getPrivilege().is(PlayerPrivilege.FORUM_MODERATOR)) {
                player.sendMessage("Your item turns into dust as it hits the ground.");
                return;
            }
        }

        player.log(LogLevel.INFO, "Dropping item '" + item + "' at " + player.getLocation() + ".");
        logValuableItemDrop(player, item);
        if (WildernessArea.isWithinWilderness(player)) {
            final boolean consumable =
                    item.getDefinitions().containsOption("Eat") || item.getDefinitions().containsOption("Drink") || Consumable.consumables.containsKey(item.getId());
            World.spawnFloorItem(item, player, !consumable && item.isTradable() ? -1 : invisibleDelay,
                    item.isTradable() ? visibleDelay : -1);
        } else {
            World.spawnFloorItem(item, player, invisibleDelay, item.isTradable() ? visibleDelay : -1);
        }
    }

    private static void logValuableItemDrop(Player player, Item item) {
        final long value = (long) item.getAmount() * item.getSellPrice();
        if(value > 150_000) {
            GameLogger.log(Level.INFO, () -> new GameLogMessage.GroundItem.Drop(
                    Instant.Companion.now(),
                    player.getUsername(),
                    item,
                    player.getLocation()
            ));
        }
    }

    private static void degrade(final Player player, final Item item, final int slotId,
                                final boolean ignoreMessages, final int invisibleDelay, final int visibleDelay) {
        if (player.getInventory().getItem(slotId) != item) {
            return;
        }
        if (ignoreMessages) {
            player.getAttributes().put("Ignore charged item drop message", true);
        }
        final RegionArea area = player.getArea();
        if (area instanceof IDropPlugin) {
            if (!((IDropPlugin) area).dropOnGround(player, item)) {
                return;
            }
        }
        final int degraded = DegradableItem.getCompletelyDegradedId(item.getId());
        player.getInventory().deleteItem(slotId, item);
        player.getInterfaceHandler().closeInterfaces();
        player.getPacketDispatcher().sendSoundEffect(OTHER_SOUND);
        if (degraded != -1) {
            if (WildernessArea.isWithinWilderness(player)) {
                final ItemDefinitions definitions = ItemDefinitions.getOrThrow(degraded);
                final boolean consumable =
                        definitions.containsOption("Eat") || definitions.containsOption("Drink") || Consumable.consumables.containsKey(degraded);
                if (consumable) {
                    World.spawnFloorItem(new Item(degraded, item.getAmount()), player, invisibleDelay + visibleDelay,
                            -1);
                } else {
                    World.spawnFloorItem(new Item(degraded, item.getAmount()), player, item.isTradable() ? -1 :
                            invisibleDelay, item.isTradable() ? visibleDelay : -1);
                }
            } else {
                World.spawnFloorItem(new Item(degraded, item.getAmount()), player, invisibleDelay, item.isTradable()
                        ? visibleDelay : -1);
            }
        }
    }

}
