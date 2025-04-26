package com.zenyte.game.world.entity.player.collectionlog;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.follower.impl.SkillingPet;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.plugins.events.PostInitializationEvent;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

import static com.zenyte.game.world.entity.player.collectionlog.CollectionLogInterface.STRUCT_POINTER_ENUM_CAT;
import static com.zenyte.game.world.entity.player.collectionlog.CollectionLogInterface.STRUCT_POINTER_SUB_ENUM_CAT;

/**
 * @author Kris | 13/03/2019 21:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@StaticInitializer
public class CollectionLog {
    public static final IntOpenHashSet COLLECTION_LOG_ITEMS = new IntOpenHashSet(Container.getSize(ContainerType.COLLECTION_LOG));
    private static final IntOpenHashSet UNTRADABLE_COLLECTION_LOG_ITEMS = new IntOpenHashSet(350);

    static {
        for (final CLCategoryType category : CLCategoryType.values) {
            final StructDefinitions struct = Objects.requireNonNull(StructDefinitions.get(category.struct()));
            final Optional<?> optional = struct.getValue(STRUCT_POINTER_ENUM_CAT);
            final Object enumId = optional.orElseThrow(RuntimeException::new);
            assert enumId instanceof Integer;
            final IntEnum categoryEnum = EnumDefinitions.getIntEnum((Integer) enumId);
            final ObjectSet<Int2IntMap.Entry> categoryEntries = categoryEnum.getValues().int2IntEntrySet();
            for (final Int2IntMap.Entry listing : categoryEntries) {
                final int bossStructId = listing.getIntValue();
                final StructDefinitions bossStruct = Objects.requireNonNull(StructDefinitions.get(bossStructId));
                final int itemEnumId = Integer.parseInt(bossStruct.getValue(STRUCT_POINTER_SUB_ENUM_CAT).orElseThrow(RuntimeException::new).toString());
                final IntEnum itemsEnum = EnumDefinitions.getIntEnum(itemEnumId);
                final ObjectSet<Int2IntMap.Entry> subEnumEntrySet = itemsEnum.getValues().int2IntEntrySet();
                for (final Int2IntMap.Entry e : subEnumEntrySet) {
                    COLLECTION_LOG_ITEMS.add(e.getIntValue());
                    if (!new Item(e.getIntValue()).isTradable()) {
                        UNTRADABLE_COLLECTION_LOG_ITEMS.add(e.getIntValue());
                    }
                }
            }
        }
    }

    private final transient Player player;
    private final Container container;

    public CollectionLog(Player player) {
        this.player = player;
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.COLLECTION_LOG, Optional.empty());
    }

    @Subscribe
    public static final void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final CollectionLog otherCollectionLog = savedPlayer.getCollectionLog();
        if (otherCollectionLog == null || otherCollectionLog.container == null || otherCollectionLog.container.isEmpty()) {
            return;
        }
        player.getCollectionLog().container.setContainer(otherCollectionLog.container);
    }

    @Subscribe
    public static final void postInit(final PostInitializationEvent event) {
        final Player player = event.getPlayer();
        final Int2IntOpenHashMap allItems = new Int2IntOpenHashMap(500);
        for (final ObjectCollection<Item> itemCollection : Utils.concatenate(player.getInventory().getContainer().getItems().values(), player.getBank().getContainer().getItems().values(), player.getEquipment().getContainer().getItems().values(), player.getRetrievalService().getContainer().getItems().values())) {
            for (final Item item : itemCollection) {
                if (!item.isTradable()) {
                    allItems.addTo(item.getId(), item.getAmount());
                }
            }
        }
        for (int itemId : UNTRADABLE_COLLECTION_LOG_ITEMS) {
            if (player.getCollectionLog().getContainer().contains(itemId, 1)) {
                continue;
            }
            if (allItems.containsKey(itemId)) {
                player.getCollectionLog().add(new Item(itemId, allItems.get(itemId)));
            }
        }
    }

    public void add(@NotNull final Item item) {
        if (item.getDefinitions() == null) {
            return;
        }

        Item transformedItem;
        if (item.getDefinitions().isNoted()) {
            transformedItem = new Item(item.getDefinitions().getUnnotedOrDefault(), item.getAmount());
        } else {
            transformedItem = item;
        }

        if (!COLLECTION_LOG_ITEMS.contains(transformedItem.getId())) {
            return;
        }

        if (!container.contains(transformedItem.getId(), 1)) {
            if ((player.getVarManager().getBitValue(SettingsInterface.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID) & 0x1) != 0) {
                player.sendMessage("New item added to your collection log: " + Colour.RED.wrap(transformedItem.getName()));
            }
            if ((player.getVarManager().getBitValue(SettingsInterface.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID) & 0x2) != 0) {
                player.notification("Collection Log", "New item:<br><br>" + Colour.WHITE.wrap(transformedItem.getName()), 16750623);
            }
        }
        container.add(transformedItem);
    }

    public Container getContainer() {
        return container;
    }
}
