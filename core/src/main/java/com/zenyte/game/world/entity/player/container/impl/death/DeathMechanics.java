package com.zenyte.game.world.entity.player.container.impl.death;

import com.near_reality.api.service.user.UserPlayerHandler;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.near_reality.game.model.item.ItemValueExtKt;
import com.near_reality.game.world.PlayerEvent;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.plugin.Probita;
import com.zenyte.game.content.gravestone.GravestoneExt;
import com.zenyte.game.content.lootkeys.LootkeyConstants;
import com.zenyte.game.content.lootkeys.LootkeySettings;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.thieving.CoinPouch;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.containers.LootingBag;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.login.LoginManager;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.game.world.region.area.wilderness.WildernessResourceArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import kotlin.Pair;
import kotlin.Unit;
import kotlinx.datetime.Instant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.*;

/**
 * @author Kris | 20/01/2019 20:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("ALL")
public class DeathMechanics {
    private static final Logger logger = LoggerFactory.getLogger(DeathMechanics.class);
    private final Logger hcimDeathLogger = LoggerFactory.getLogger("HCIM Death Logger");
    final transient Container kept;
    final transient Container lost;
    private final transient LinkedList<Item> list;
    transient Player player;
    transient Entity killer;
    transient boolean destroyLootingBag;
    transient boolean destroyRunePouch;
    transient boolean destroySecondaryRunePouch;

    public DeathMechanics(final Player player) {
        this.player = player;
        kept = new Container(ContainerPolicy.NORMAL, ContainerType.MAXIMUM_SIZE_CONTAINER, Optional.of(player));
        lost = new Container(ContainerPolicy.NORMAL, ContainerType.MAXIMUM_SIZE_CONTAINER, Optional.of(player));
        list = new LinkedList<>();
    }

    private void set() {
        clear();
        removePets();
        prefill();
        sort();
        fillAlwaysLostItems();
        fillKeptItems();
        fillLostItems();
        sort(kept);
        sort(lost);
    }

    public void death(final Entity killer, final Location tile) {
        if (safe()) {
            return;
        }
        this.killer = killer;
        final Int2ObjectLinkedOpenHashMap<Item> inventory = player.getInventory().getContainer().getItems().clone();
        final Int2ObjectLinkedOpenHashMap<Item> equipment = player.getEquipment().getContainer().getItems().clone();
        final Int2ObjectLinkedOpenHashMap<Item> lootingBag = destroyLootingBag ? player.getLootingBag().getContainer().getItems().clone() : null;
        final Int2ObjectLinkedOpenHashMap<Item> runePouch = destroyRunePouch ? player.getRunePouch().getContainer().getItems().clone() : null;
        final Int2ObjectLinkedOpenHashMap<Item> secondaryRunePouch = destroySecondaryRunePouch ? player.getSecondaryRunePouch().getContainer().getItems().clone() : null;

        final Location location = new Location(tile == null ? player.getLocation() : tile);

        // Bones
        World.spawnFloorItem(new Item(ItemId.BONES), player, 0, 300);

        CoresManager.worldThread.getHooks().post(new PlayerEvent.Died(player, killer));

        Pair<List<Item>, List<Item>> pair = new Pair<>(new ArrayList<Item>(), new ArrayList<Item>());
        if(WildernessArea.isWithinWilderness(player.getPosition())) {
            pair = GravestoneExt.INSTANCE.calculateGravestoneItems(player, true, true);
            GravestoneExt.INSTANCE.moveItemsToDeathsOffice(player);
        } else {
            final Location gravestoneLocation = player.getAreaManager().getGravestoneLocation();
            pair = GravestoneExt.INSTANCE.createGravestone(player, gravestoneLocation != null ? gravestoneLocation : location,
                            killer instanceof Player
                                    ? (Player) killer
                                    : null);
        }



        final List<Item> lostToKiller = pair.getFirst();

        final Int2ObjectLinkedOpenHashMap<Item> cloneKept = kept.getItems().clone();
        final Int2ObjectLinkedOpenHashMap<Item> cloneLost = lost.getItems().clone();
        final List<Item> cloneGraveStone = pair.getSecond().stream().toList();
        final List<Item> cloneLostToKiller = pair.getFirst().stream().toList();

        if (killer instanceof Player) {
            GameLogger.log(Level.INFO, () -> new GameLogMessage.Death.Killed.ByPlayer(
                    Instant.Companion.now(),
                    player.getUsername(),
                    ((Player) killer).getUsername(),
                    location,
                    inventory,
                    equipment,
                    lootingBag,
                    runePouch,
                    secondaryRunePouch,
                    cloneKept,
                    cloneLost,
                    cloneGraveStone,
                    cloneLostToKiller
            ));
        } else if (killer instanceof NPC) {
            GameLogger.log(Level.INFO, () -> new GameLogMessage.Death.Killed.ByNpc(
                    Instant.Companion.now(),
                    player.getUsername(),
                    ((NPC) killer).getId(),
                    location,
                    inventory,
                    equipment,
                    lootingBag,
                    runePouch,
                    secondaryRunePouch,
                    cloneKept,
                    cloneLost,
                    cloneGraveStone,
                    cloneLostToKiller
            ));
        } else {
            GameLogger.log(Level.INFO, () -> new GameLogMessage.Death.Misc(
                    Instant.Companion.now(),
                    player.getUsername(),
                    location,
                    inventory,
                    equipment,
                    lootingBag,
                    runePouch,
                    secondaryRunePouch,
                    cloneKept,
                    cloneLost,
                    cloneGraveStone
            ));
        }

        Player receiver = killer instanceof Player ? (Player) killer : this.player;

        if (receiver != player) {
            if (receiver.getVariables().getTime(TickVariable.TELEBLOCK) > 0 &&
                    ((String) receiver.getTemporaryAttributes().getOrDefault("tb_name", "")).equalsIgnoreCase(player.getName())) {
                receiver.getVariables().resetTeleblock();
                receiver.sendMessage("Your Tele Block has been removed because you killed "+player.getName());
            }
        }

        if (DeveloperCommands.INSTANCE.getEnabledLootKeys()) {
            if (receiver != player) {
                LootkeySettings settings = receiver.getLootkeySettings();
                if (settings != null) {
                    if (settings.isEnabled() && !lostToKiller.isEmpty()) {

                        Int2ObjectLinkedOpenHashMap<Item> lootkeyLoot = new Int2ObjectLinkedOpenHashMap<>();
                        Item lootkeyItem = new Item(LootkeyConstants.LOOT_KEY_ITEM_ID, 1);

                        Iterator iterator = lostToKiller.iterator();
                        int index = -1;
                        while (iterator.hasNext()) {
                            index++;
                            Item iteratorItem = (Item) iterator.next();
                            if (iteratorItem == null || iteratorItem.getId() == LootkeyConstants.LOOT_KEY_ITEM_ID)
                                continue;
                            // If the item is a consumable
                            if (Consumable.gourdDrinks.containsKey(iteratorItem.getId())
                                    || Consumable.consumables.containsKey(iteratorItem.getId())
                                    || Consumable.food.containsKey(iteratorItem.getId())) {

                                if (!settings.isDropFood()) {
                                    lootkeyLoot.put(index, iteratorItem);
                                    iterator.remove();
                                }
                                continue;
                            }

                            if (!settings.isDropValuables()) {
                                lootkeyLoot.put(index, iteratorItem);
                                iterator.remove();
                            } else {
                                int dropThreshold = settings.getThreshold();
                                if (iteratorItem.getDefinitions().getPrice() < dropThreshold) {
                                    lootkeyLoot.put(index, iteratorItem);
                                    iterator.remove();
                                }
                            }
                        }

                        // Here we store food within the players lootkey
                        long totalValueOfKey = 0L;
                        for (var i : lootkeyLoot.int2ObjectEntrySet()) {
                            totalValueOfKey += i.getValue().getDefinitions().getPrice() * i.getValue().getAmount();
                        }

                        Container container = new Container(ContainerPolicy.NORMAL, ContainerType.WILDERNESS_LOOT_KEY
                                , Optional.of(receiver));
                        container.addAll(lootkeyLoot.values());

                        lootkeyItem.setAttribute(LootkeyConstants.LOOT_KEY_ITEM_LOOT_ITEMS_ATTR,
                                LoginManager.gson.get().toJson(container.getItems()));
                        lootkeyItem.setAttribute(LootkeyConstants.LOOT_KEY_ITEM_LOOT_VALUE_ATTR, totalValueOfKey);

                        ContainerResult result = receiver.getInventory().addItem(lootkeyItem);
                        if (result.isFailure()) {
                            result.onFailure(res -> World.spawnFloorItem(res, receiver));
                        } else {
                            receiver.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                        }
                    }
                }
            }
        }
        boolean dropToPker = WildernessArea.isWithinWilderness(location) && killer instanceof Player && !((Player) killer).isIronman();
        lostToKiller.forEach(item -> World.spawnFloorItem(item, location, -1, dropToPker ? receiver : this.player, receiver, 300, 200));

        if (player.inArea(WildernessResourceArea.class)) {
            final int lostFee = PlayerAttributesKt.getWildernessResourceAreaPaidFeeAmount(player);
            PlayerAttributesKt.setWildernessResourceAreaPaidFeeAmount(player, 0);
            if (lostFee > 0 && killer instanceof Player killerPlayer) {
                killerPlayer.getInventory().addOrDrop(new Item(ItemId.COINS_995, lostFee));
                killerPlayer.sendMessage(Colour.RED.wrap("You take the fee paid by " + player.getName() + " from their corpse."));
            }
        }

        final Follower follower = player.getFollower();

        if (follower != null && follower.getPet().itemId() < 30000) {
            player.setFollower(null);
        }

        checkHardcoreIronManDeath();
    }

    public void checkHardcoreIronManDeath() {
        final Entity source = Optional
                .ofNullable(PlayerAttributesKt.getKillingBlowHit(player))
                .map(Hit::getSource)
                .orElse(null);

        final Optional<RegionArea> area = GlobalAreaManager.findArea(player);

        if (area.map(RegionArea::safeHardcoreIronmanDeath).orElse(false))
            return;

        Optional.ofNullable(PlayerAttributesKt.getHardcoreIronGroupDeathHandlingOverride(player))
                .ifPresentOrElse(
                        handler -> {
                            final RegionArea regionArea = area.orElse(null);
                            boolean loseLive = true;
                            if (regionArea instanceof DeathPlugin)
                                loseLive = ((DeathPlugin) regionArea).loseHardcoreIronGroupLive();
                            if (loseLive)
                                handler.invoke(this, player, source);
                        },
                        () -> onHardcoreIronManDeath(source)
                );
    }

    private void onHardcoreIronManDeath(Entity source) {

        if (player.getGameMode() != GameMode.HARDCORE_IRON_MAN)
            return;

        WorldBroadcasts.broadcast(player, BroadcastType.HCIM_DEATH, source);

        UserPlayerHandler.INSTANCE.updateGameMode(player, GameMode.STANDARD_IRON_MAN, (success) -> {
            if (!success) {
                player.log(LogLevel.ERROR, "Failed to update game-mode " + GameMode.STANDARD_IRON_MAN + " in API, setting anyways.");
                player.setGameMode(GameMode.STANDARD_IRON_MAN);
            }
            player.sendMessage("You have fallen as a Hardcore Iron Man, your Hardcore status has been revoked.");
            final String killerLabel = (source == player) ? "self-inflicted damage" : (source instanceof Player) ?
                    ((Player) source).getName() : (source instanceof NPC) ?
                    ((((NPC) source).getDefinitions().getName() + " (lvl-" + ((NPC) source).getDefinitions().getCombatLevel()) + ")") : "unknown damage";
            hcimDeathLogger.info(player.getName() + "(total lvl-" + player.getSkills().getTotalLevel() + ") fell to " + killerLabel + " at " + player.getLocation() + " with a logout type of " + player.getLogoutType() + ".");
            return Unit.INSTANCE;
        });
    }

    public void service(@NotNull final ItemRetrievalService.RetrievalServiceType type, final Entity killer, boolean runHcimDeathHandlingCheck) {
        if (safe()) {
            return;
        }
        this.killer = killer;
        final String inventory = player.getInventory().getContainer().getItems().toString();
        final String equipment = player.getEquipment().getContainer().getItems().toString();
        final String lootingBag = destroyLootingBag
                ? player.getLootingBag().getContainer().getItems().toString()
                : "N/A";
        final String runePouch = destroyRunePouch
                ? player.getRunePouch().getContainer().getItems().toString()
                : "N/A";
        final String secondaryRunePouch = destroySecondaryRunePouch
                ? player.getSecondaryRunePouch().getContainer().getItems().toString()
                : "N/A";

        set();
        clearContainers();
        player.log(LogLevel.INFO,
                "Player death: \nInitial inventory: " + inventory + "\nInitial equipment: " + equipment + "\nInitial " +
                        "lootingbag: " + lootingBag + "\nKept items: " + kept.getItems() + "\nLost items: " + lost.getItems());
        kept.getItems().values().forEach(item -> player.getInventory().addItem(item));
        final ItemRetrievalService service = player.getRetrievalService();
        final Container container = service.getContainer();
        container.clear();
        lost.getItems().values().forEach(item -> {
            final int itemId = item.getId();
            if (CoinPouch.ITEMS.keySet().contains(itemId)) {
                final CoinPouch pouch = CoinPouch.ITEMS.get(itemId);
                item = new Item(995, com.zenyte.plugins.item.CoinPouch.getCoinAmount(pouch, item.getAmount()));
            }
            if (!isLootingBag(itemId)) {
                container.add(item);
            }
        });
        service.setType(type);
        service.setLocked(!type.isFree());
        if (runHcimDeathHandlingCheck)
            checkHardcoreIronManDeath();
    }

    private void removePets() {
        final Follower follower = player.getFollower();
        if (follower != null && follower.getPet().itemId() < 30000) {
            //30000+ = custom pet
            player.setFollower(null);
        }
        final Inventory inventory = player.getInventory();
        for (int slot = 0; slot < 28; slot++) {
            final Item item = inventory.getItem(slot);
            if (item == null) {
                continue;
            }
            final int id = item.getId();
            if (Probita.insurablePets.containsKey(id)) {
                inventory.deleteItem(item);
            }
        }
    }

    private boolean safe() {
        final RegionArea area = player.getArea();
        final DeathPlugin plugin = area instanceof DeathPlugin ? (DeathPlugin) area : null;
        boolean originallySafe = plugin != null && plugin.isSafe();
        boolean originallyUnsafe = plugin != null && !plugin.isSafe();
        if(originallySafe)
            return true;
        else if(originallyUnsafe)
            return false;
        else if(area != null && area instanceof WildernessArea)
            return false;
        else if(player.getGameMode().isHardcore())
            return false;
        return true;
    }

    private void clearContainers() {
        player.getInventory().clear();
        player.getEquipment().clear();
        if (destroyLootingBag)
            player.getLootingBag().clear();
        if (destroyRunePouch)
            player.getRunePouch().clear();
        if (destroySecondaryRunePouch)
            player.getSecondaryRunePouch().clear();
    }

    private void clear() {
        destroyLootingBag = false;
        destroyRunePouch = false;
        destroySecondaryRunePouch = false;
        if (!list.isEmpty()) {
            list.clear();
        }
        if (!kept.isEmpty()) {
            kept.clear();
        }
        if (!lost.isEmpty()) {
            lost.clear();
        }
    }

    private void prefill() {
        clear(player.getInventory().getContainer());
        clear(player.getEquipment().getContainer());

        if (player.getInventory().containsAnyOf(LootingBag.OPENED.getId(), LootingBag.CLOSED.getId())) {
            destroyLootingBag = true;
            player.getLootingBag().getContainer().getItems().values().forEach(item -> lost.add(new Item(item)));
        }

        if (player.getInventory().containsAnyOf(RunePouch.POUCHES)) {
            destroyRunePouch = true;
            clear(player.getRunePouch().getContainer());
        }

        if (player.getInventory().containsItem(RunePouch.TOURNAMENT_RUNE_POUCH)) {
            destroySecondaryRunePouch = true;
            clear(player.getSecondaryRunePouch().getContainer());
        }
    }

    private void clear(Container container) {
        if (!container.isEmpty())
            container.getItems().values().forEach(item -> list.add(new Item(item)));
    }

    private void sort() {
        if (list.isEmpty()) {
            return;
        }
        ItemValueExtKt.sortByProtectionValue(list);
    }

    private void fillKeptItems() {
        if (list.isEmpty() || player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN)) {
            return;
        }
        int count = getKeptCount();
        while (count-- > 0) {
            final Item item = list.peekFirst();
            if (item == null) continue;
            kept.add(new Item(item.getId(), 1, item.getAttributesCopy()));
            final int amount = item.getAmount();
            if (amount == 1) {
                list.removeFirst();
                continue;
            }
            item.setAmount(amount - 1);
        }
    }

    private void fillAlwaysLostItems() {
        if (list.isEmpty()) {
            return;
        }
        list.removeIf(item -> {
            final int itemId = item.getId();
            final boolean lost = isLootingBag(itemId) || isRunePouch(itemId);
            if (lost)
                this.lost.add(item);
            else
                logger.warn("Did not remove {} (\"{}\")", itemId, item.getName());
            return lost;
        });
    }

    private boolean isLootingBag(final int itemId) {
        return destroyLootingBag && (itemId == LootingBag.CLOSED.getId() || itemId == LootingBag.OPENED.getId());
    }

    private boolean isRunePouch(final int itemId) {
        return (destroyRunePouch && (itemId == RunePouch.RUNE_POUCH.getId() || itemId == RunePouch.DIVINE_RUNE_POUCH.getId())) || (destroySecondaryRunePouch && itemId == RunePouch.TOURNAMENT_RUNE_POUCH.getId());
    }

    private void fillLostItems() {
        if (list.isEmpty()) {
            return;
        }
        list.forEach(lost::add);
        list.clear();
    }

    private void sort(final Container container) {
        if (container.isEmpty()) {
            return;
        }
        final List<Item> sorted = ItemValueExtKt.sortedByProtectionValue(container.getItems().values());
        container.clear();
        sorted.forEach(container::add);
    }

    private long getPrice(@NotNull final Item item) {
        return ItemValueExtKt.getProtectionValue(item.getId());
    }

    private int getKeptCount() {
        int count = 0;
        if (!player.getVariables().isSkulled()) {
            count += 3;
        }
        if (player.getPrayerManager().isActive(Prayer.PROTECT_ITEM)) {
            count++;
        }
        return count;
    }
}
