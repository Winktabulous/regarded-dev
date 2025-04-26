package com.zenyte.game.content.magicstorageunit;

import com.zenyte.game.content.magicstorageunit.enums.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StorageUnitCollection {
    private static final Logger log = LoggerFactory.getLogger(StorageUnitCollection.class);
    public static final int MAXIMUM_ELEMENTS_PER_PAGE = 40;
    private static final StorageUnitElement MORE = StorageUnitElement.of(ItemId.MORE);
    private static final StorageUnitElement BACK = StorageUnitElement.of(ItemId.BACK);
    private static final StorageUnitCollection singleton = new StorageUnitCollection();
    private final Map<StorageUnitType, List<List<StorageUnitElement>>> elements;
    private final Map<StorageUnitType, List<Container>> displayedContainers;
    private final Int2ObjectMap<List<StorageUnitElement>> elementPointers;

    public StorageUnitCollection() {
        this.elements = new EnumMap<>(StorageUnitType.class);
        this.displayedContainers = new EnumMap<>(StorageUnitType.class);
        this.elementPointers = new Int2ObjectOpenHashMap<>(1024);
    }

    static {
        try {
            getSingleton().register(StorageUnitType.ARMOUR_CASE, Arrays.asList(StorableArmour.values()));
            getSingleton().register(StorageUnitType.CAPE_RACK, Arrays.asList(StorableCapes.values()));
            getSingleton().register(StorageUnitType.FANCY_DRESS_BOX, Arrays.asList(StorableDresses.values()));
            getSingleton().register(StorageUnitType.MAGIC_WARDROBE, Arrays.asList(StorableOutfits.values()));
            getSingleton().register(StorageUnitType.TOY_BOX, Arrays.asList(StorableToys.values()));
            getSingleton().register(StorageUnitType.TREASURE_CHEST, Arrays.asList(StorableTreasure.values()));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void register(@NotNull final StorageUnitType type, @NotNull final List<? extends StorageUnitElement> unsortedElements) {
        final ObjectArrayList<StorageUnitElement> elements = new ObjectArrayList<>(unsortedElements);
        elements.sort(Comparator.comparing(c -> ItemDefinitions.nameOf(c.getDisplayItem())));
        final ObjectArrayList<List<StorageUnitElement>> listOfLists = new ObjectArrayList<>();
        ObjectArrayList<StorageUnitElement> currentList = new ObjectArrayList<>(MAXIMUM_ELEMENTS_PER_PAGE);
        final int length = elements.size();
        for (int i = 0; i < length; i++) {
            final StorageUnitElement piece = elements.get(i);
            piece.verifyExistence();
            currentList.add(piece);
            map(piece);
            //If the page is about to fill up
            if (currentList.size() == MAXIMUM_ELEMENTS_PER_PAGE - 1) {
                //If the current piece isn't the last in the list
                if (i < length - 2) {
                    currentList.add(MORE);
                    listOfLists.add(Collections.unmodifiableList(currentList));
                    currentList = new ObjectArrayList<>(MAXIMUM_ELEMENTS_PER_PAGE);
                    currentList.add(BACK);
                }
            }
        }
        listOfLists.add(Collections.unmodifiableList(currentList));
        this.elements.put(type, Collections.unmodifiableList(listOfLists));
        pregenerateContainer(type);
    }

    private void pregenerateContainer(@NotNull final StorageUnitType type) {
        final List<List<StorageUnitElement>> elements = this.elements.get(type);
        final ObjectArrayList<Container> listOfContainers = new ObjectArrayList<>();
        displayedContainers.put(type, listOfContainers);
        for (final List<StorageUnitElement> list : elements) {
            final Container container = new Container(ContainerPolicy.NORMAL, ContainerType.MAGIC_STORAGE, Optional.empty());
            listOfContainers.add(container);
            for (final StorageUnitElement element : list) {
                container.add(new Item(element.getDisplayItem()));
            }
        }
    }

    public Container getContainer(@NotNull final StorageUnitType type, final int page) {
        return displayedContainers.get(type).get(page);
    }

    public void map(@NotNull final StorageUnitElement element) {
        for (final StorableSetPiece piece : element.getPieces()) {
            for (final int id : piece.getIds()) {
                List<StorageUnitElement> existingList = elementPointers.get(id);
                if (existingList == null) {
                    existingList = new ObjectArrayList<>();
                    elementPointers.put(id, existingList);
                }
                existingList.add(element);
            }
        }
    }

    public Optional<List<StorageUnitElement>> findElement(final int id) {
        return Optional.ofNullable(elementPointers.get(id));
    }

    public IntSet getAddableItemSet() {
        return elementPointers.keySet();
    }

    public static StorageUnitCollection getSingleton() {
        return singleton;
    }
}
