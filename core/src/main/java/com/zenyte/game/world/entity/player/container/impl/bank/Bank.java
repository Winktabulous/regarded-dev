package com.zenyte.game.world.entity.player.container.impl.bank;

import com.zenyte.game.content.skills.thieving.CoinPouch;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.testinterfaces.BankInventoryInterface;
import com.zenyte.game.world.entity.player.Lamp;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.*;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.*;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Kris | 31. dets 2017 : 22:58.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class Bank {

    public static final int VARBIT_FIRST_OP_AMOUNT = 6590;
    public static final int BANK_INTERFACE = 12;
    public static final int MAIN_TAB = 9;
    public static final Set<Integer> unbankableItems = new HashSet<>();

    static {
        for (final Lamp lamp : Lamp.all) {
            unbankableItems.add(lamp.getItem().getId());
        }
        unbankableItems.add(12011);
        unbankableItems.addAll(CoinPouch.ITEMS.keySet());
        unbankableItems.add(ItemId.MIMIC);
        unbankableItems.add(ItemId.DARK_ESSENCE_FRAGMENTS);
        unbankableItems.add(ItemId.HERB_BOX);
        unbankableItems.add(ItemId.OPENED_HERB_BOX);
    }

    int[] tabSizes = new int[10];
    private int settings = 32;
    private int displayType;
    private int currentTab = MAIN_TAB;
    private int lastDepositAmount;
    private BankContainer container;
    private QuantitySelector quantity;

    public Bank() {
        container = new BankContainer(this, ContainerPolicy.ALWAYS_STACK, ContainerType.BANK);
        quantity = QuantitySelector.ONE;
    }

    public Bank(final Bank bank) {
        if (bank == null) {
            return;
        }
        currentTab = bank.currentTab;
        lastDepositAmount = bank.lastDepositAmount;
        settings = bank.settings;
        displayType = bank.displayType;
        container = new BankContainer(this, ContainerPolicy.ALWAYS_STACK, ContainerType.BANK);
        container.setContainer(bank.container);
        container.bank = this;
        tabSizes = bank.tabSizes;
        quantity = bank.quantity == null ? QuantitySelector.ONE : bank.quantity;
    }

    public void setQuantity(final QuantitySelector selector) {
        this.quantity = selector;
        refreshQuantity();
    }

    public abstract void onOpen(@NotNull Player player);
    public abstract void onClose(@NotNull Player player);

    protected abstract void refreshQuantity();
    protected abstract boolean testAddPredicate(@NotNull Predicate<Player> predicate);
    protected abstract void sendMessage(@NotNull String string);
    protected abstract ContainerResult removeItemDelegate(Item item, boolean placeholder);
    protected abstract void requestContainerRefresh(Container container);
    protected abstract void updateVar(BankSetting setting);
    protected abstract void refreshTabSizes();


    public int getCurrentQuantity() {
        return switch (quantity) {
            case ONE -> 1;
            case FIVE -> 5;
            case TEN -> 10;
            case ALL -> Integer.MAX_VALUE;
            case X -> this.getLastDepositAmount();
            default -> throw new RuntimeException("No available quantity unit for type " + quantity + ".");
        };
    }

    public ContainerResult add(final Item requestedItem) {
        return add(requestedItem, currentTab);
    }

    public ContainerResult add(final Item requestedItem, int tab) {
        tab = validateTab(tab);
        final Item item = new Item(requestedItem);
        final ItemDefinitions defs = item.getDefinitions();
        if (defs == null) {
            final ContainerResult result = new ContainerResult(item, ContainerState.ADD);
            result.setResult(RequestResult.FAILURE);
            return result;
        }
        if (defs.isNoted()) {
            item.setId(defs.getNotedId());
        }
        if (unbankableItems.contains(requestedItem.getId())) {
            sendMessage("A magical force prevents you from banking this item!");
            final ContainerResult result = new ContainerResult(item, ContainerState.ADD);
            result.setResult(RequestResult.FAILURE);
            return result;
        }
        final int defId = defs.getUnnotedOrDefault();
        final int placeholderId = ItemDefinitions.getOrThrow(PlaceholderRedirections.builder.getOrDefault(defId, defId)).getPlaceholderId();
        final int placeholderSlot = container.getSlotOf(placeholderId);
        if (item.hasAttributes() || container.getSlotOf(item.getId()) == -1 && (placeholderId == -1 || placeholderSlot == -1)) {
            final ContainerResult result = new ContainerResult(item, ContainerState.ADD);
            if (container.getFreeSlotsSize() == 0) {
                if (placeholderSlot != -1 && item.hasAttributes()) {
                    container.set(placeholderSlot, new Item(requestedItem.getId(), 1, requestedItem.getAttributes()));
                    result.setSucceededAmount(1);
                    result.setResult(RequestResult.SUCCESS);
                    return result;
                }
                result.setResult(RequestResult.NOT_ENOUGH_SPACE);
                return result;
            }
            if (placeholderSlot != -1) {
                container.set(placeholderSlot, item);
                result.setSucceededAmount(item.getAmount());
                result.setResult(RequestResult.SUCCESS);
                return result;
            }
            final int index = getIndexOfLastSlotInTab(tab);
            shiftForwardPastIndex(index);
            if (container.get(index) != null) {
                result.setResult(RequestResult.FAILURE);
                return result;
            }
            container.set(index, item);
            tabSizes[tab]++;
            result.setSucceededAmount(item.getAmount());
            result.setResult(RequestResult.SUCCESS);
            return result;
        }
        return container.add(item);
    }

    public ContainerResult remove(final Item item) {
        return remove(container.getSlotOf(item.getId()), item, false);
    }

    public ContainerResult remove(final Item item, final boolean placeholder) {
        return remove(container.getSlotOf(item.getId()), item, placeholder);
    }

    public ContainerResult remove(final int slot, final int amount, final boolean placeholder) {
        final Item item = container.get(slot);
        return remove(slot, new Item(item == null ? -1 : item.getId(), amount), placeholder);
    }

    public ContainerResult remove(final int slot, final Item item, final boolean placeholder) {

        final ContainerResult result = removeItemDelegate(item, (placeholder || (item.getId() != 20594 && getSetting(BankSetting.ALWAYS_PLACEHOLDER) == 1)));
        if (slot == -1) {
            return result;
        }
        if (container.get(slot) != null) {
            return result;
        }
        shiftBackwardsPastIndex(slot);
        final int tabId = getTab(slot);
        if (--tabSizes[tabId] == 0) {
            if (tabId == MAIN_TAB) {
                collapseTab(MAIN_TAB);
            } else {
                resizeTabs();
            }
        }
        return result;
    }

    public Item get(final int slot) {
        return container.get(slot);
    }

    public void incinerate(final int slotId, final int itemId) {
        if (itemId == -1) return;
        set(slotId - 1, null);
    }

    public void set(final int slot, final Item item) {
        if (slot < 0 || slot > container.getContainerSize()) {
            return;
        }
        final int tabId = getTab(slot);
        if (tabId >= 0 && tabId < 10) {
            if (item == null && container.get(slot) != null) {
                tabSizes[tabId]--;
                if (tabSizes[tabId] == 0) {
                    collapseTab(tabId);
                }
            } else if (item != null && container.get(slot) == null) {
                tabSizes[tabId]++;
            }
        }
        container.set(slot, item);
        container.setFullUpdate(true);
        container.shift();
        requestContainerRefresh(container);
    }


    public boolean containsItem(final Item item) {
        return container.contains(item);
    }

    public int getAmountOf(final int id) {
        return container.getAmountOf(id);
    }

    public void deposit(final Player player, final Container container, final int slotId, final int amount) {
        this.container.deposit(player, container, slotId, amount);
        requestContainerRefresh(this.container);
        requestContainerRefresh(container);
    }

    public void withdraw(final Player player, final Container container, final int slot, final int amount, final boolean placeholder) {
        this.container.withdraw(player, container, slot, amount, placeholder);
        requestContainerRefresh(this.container);
        requestContainerRefresh(container);
        BankInventoryInterface.transmitSlotVarp(player);
    }

    public void switchItem(final BankSwitchType type, final int fromSlot, int toSlot) {
        if (type.equals(BankSwitchType.ITEM_TO_ITEM)) {
            if (toSlot >= this.container.getSize()) {
                toSlot = this.container.getSize() - 1;
            }
            final Item fromItem = container.get(fromSlot);
            final Item toItem = container.get(toSlot);
            if (getSetting(BankSetting.REARRANGE_MODE) == 0) {
                container.set(fromSlot, toItem);
                container.set(toSlot, fromItem);
            } else {
                final int fromTab = getTab(fromSlot);
                final int toTab = getTab(toSlot);
                if (fromTab == toTab) {
                    if (fromSlot < toSlot) {
                        for (int i = fromSlot + 1; i <= toSlot; i++) {
                            container.set(i - 1, container.get(i));
                        }
                        container.set(toSlot, fromItem);
                    } else {
                        for (int i = fromSlot - 1; i >= toSlot; i--) {
                            container.set(i + 1, container.get(i));
                        }
                        container.set(toSlot, fromItem);
                    }
                } else {
                    if (fromSlot > toSlot) {
                        for (int i = fromSlot - 1; i >= toSlot; i--) {
                            container.set(i + 1, container.get(i));
                        }
                        container.set(toSlot, fromItem);
                    } else {
                        for (int i = fromSlot + 1; i < toSlot; i++) {
                            container.set(i - 1, container.get(i));
                        }
                        container.set(toSlot - 1, fromItem);
                    }
                    tabSizes[toTab]++;
                    if (--tabSizes[fromTab] == 0) {
                        resizeTabs();
                    }
                }
            }
            requestContainerRefresh(container);
        } else if (type.equals(BankSwitchType.ITEM_TO_TAB) || type.equals(BankSwitchType.ITEM_TO_END_OF_TAB)) {
            final int fromTab = getTab(fromSlot);
            int toTab = type.equals(BankSwitchType.ITEM_TO_END_OF_TAB) ? toSlot : getTabFromSlot(toSlot);
            if (fromTab == toTab && tabSizes[fromTab] == 1) {
                return;
            }
            final Item fromItem = container.get(fromSlot);
            if (fromItem == null) {
                return;
            }
            container.set(fromSlot, null);
            container.shift();
            if (--tabSizes[fromTab] == 0) {
                collapseTab(fromTab);
                if (fromTab < toTab && toTab != MAIN_TAB || fromTab == MAIN_TAB) {
                    if (--toTab == -1) {
                        toTab = 9;
                    }
                }
            }
            this.add(fromItem, toTab);
            container.setFullUpdate(true);
            requestContainerRefresh(container);
        } else if (type.equals(BankSwitchType.TAB_TO_TAB)) {
            final int fromTab = getTabFromSlot(fromSlot);
            int toTab = getTabFromSlot(toSlot);
            if (tabSizes[toTab] == 0) {
                for (int i = 0; i < 10; i++) {
                    if (tabSizes[i] == 0) {
                        toTab = i - 1;
                        if (toTab < 0) {
                            toTab = 0;
                        }
                        break;
                    }
                }
            }
            if (toTab == fromTab) {
                return;
            }
            final ArrayList<Item> list = new ArrayList<Item>(container.getSize());
            int i;
            int fromStartIndex;
            int toStartIndex;
            final int[] tabs = new int[10];
            if (fromTab < toTab) {
                if (toTab == MAIN_TAB) {
                    fromStartIndex = getIndexOfFirstSlotInTab(fromTab);
                    toStartIndex = getIndexOfFirstSlotInTab(toTab);
                    for (i = toStartIndex; i < toStartIndex + tabSizes[toTab]; i++) {
                        list.add(container.get(i));
                    }
                    for (i = 0; i < fromStartIndex; i++) {
                        list.add(container.get(i));
                    }
                    for (i = fromStartIndex + tabSizes[fromTab]; i < toStartIndex; i++) {
                        list.add(container.get(i));
                    }
                    for (i = fromStartIndex; i < fromStartIndex + tabSizes[fromTab]; i++) {
                        list.add(container.get(i));
                    }
                    int tabIndex = 0;
                    tabs[tabIndex++] = tabSizes[MAIN_TAB];
                    for (i = 0; i < fromTab; i++) {
                        tabs[tabIndex++] = tabSizes[i];
                    }
                    for (i = fromTab + 1; i < 9; i++) {
                        tabs[tabIndex++] = tabSizes[i];
                    }
                    tabs[tabIndex++] = tabSizes[fromTab];
                } else {
                    fromStartIndex = getIndexOfFirstSlotInTab(fromTab);
                    toStartIndex = getIndexOfFirstSlotInTab(toTab + 1);
                    for (i = 0; i < fromStartIndex; i++) {
                        list.add(container.get(i));
                    }
                    for (i = (i + tabSizes[fromTab]); i < toStartIndex; i++) {
                        list.add(container.get(i));
                    }
                    for (i = fromStartIndex; i < fromStartIndex + tabSizes[fromTab]; i++) {
                        list.add(container.get(i));
                    }
                    for (i = toStartIndex; i < container.getContainerSize(); i++) {
                        final Item item = container.get(i);
                        if (item == null) {
                            continue;
                        }
                        list.add(item);
                    }
                    int tabIndex = 0;
                    for (i = 0; i < fromTab; i++) {
                        tabs[tabIndex++] = tabSizes[i];
                    }
                    for (i = fromTab + 1; i < (toTab + 1); i++) {
                        tabs[tabIndex++] = tabSizes[i];
                    }
                    tabs[tabIndex++] = tabSizes[fromTab];
                    for (i = toTab + 1; i < 10; i++) {
                        tabs[tabIndex++] = tabSizes[i];
                    }
                }
            } else {
                fromStartIndex = getIndexOfFirstSlotInTab(toTab);
                toStartIndex = getIndexOfFirstSlotInTab(fromTab);
                for (i = 0; i < fromStartIndex; i++) {
                    list.add(container.get(i));
                }
                for (i = toStartIndex; i < toStartIndex + tabSizes[fromTab]; i++) {
                    list.add(container.get(i));
                }
                for (i = fromStartIndex; i < toStartIndex; i++) {
                    list.add(container.get(i));
                }
                for (i = getIndexOfFirstSlotInTab(fromTab + 1); i < container.getContainerSize(); i++) {
                    list.add(container.get(i));
                }
                int tabIndex = 0;
                for (i = 0; i < toTab; i++) {
                    tabs[tabIndex++] = tabSizes[i];
                }
                tabs[tabIndex++] = tabSizes[fromTab];
                for (i = toTab; i < fromTab; i++) {
                    tabs[tabIndex++] = tabSizes[i];
                }
                for (i = fromTab + 1; i < 10; i++) {
                    tabs[tabIndex++] = tabSizes[i];
                }
            }
            container.clear();
            tabSizes = tabs;
            final int length = list.size();
            for (i = 0; i < length; i++) {
                container.set(i, list.get(i));
            }
            requestContainerRefresh(container);
        }
    }

    public void collapseTab(final int tab) {
        if (tab < 0 || tab > MAIN_TAB) {
            return;
        }
        if (tab == MAIN_TAB) {
            final ArrayList<Item> mainTab = new ArrayList<Item>(tabSizes[0]);
            for (int i = 0; i < tabSizes[0]; i++) {
                mainTab.add(container.get(i));
                container.set(i, null);
            }
            container.shift();
            int total = 0;
            for (int i = 0; i < 9; i++) {
                tabSizes[i] = tabSizes[i + 1];
                total += tabSizes[i];
            }
            tabSizes[MAIN_TAB] = mainTab.size();
            for (int i = total; i < (total + tabSizes[MAIN_TAB]); i++) {
                container.set(i, mainTab.get(i - total));
            }
            return;
        }
        final ArrayList<Item> list = new ArrayList<Item>(tabSizes[tab]);
        final int index = getIndexOfFirstSlotInTab(tab);
        final int length = (index + tabSizes[tab]);
        for (int i = index; i < length; i++) {
            list.add(container.get(i));
            container.set(i, null);
        }
        tabSizes[tab] = 0;
        resizeTabs();
        tabSizes[MAIN_TAB] += list.size();
        container.shift();
        final int startIndex = container.getSize();
        final int size = list.size();
        for (int i = startIndex; i < (startIndex + size); i++) {
            container.set(i, list.remove(0));
        }
        requestContainerRefresh(container);
    }

    public int getTabSize(final int tabId) {
        if (tabId < 0 || tabId >= tabSizes.length) {
            return 0;
        }
        return tabSizes[tabId];
    }

    public void refreshContainer() {
        requestContainerRefresh(container);
    }

    public void releasePlaceholders(final int tab) {
        final int index = tab == -1 ? 0 : getIndexOfFirstSlotInTab(tab);
        final int length = tab == -1 ? container.getContainerSize() : (tabSizes[tab] + index);
        for (int i = index; i < length; i++) {
            final Item item = container.get(i);
            if (item == null) {
                continue;
            }
            final ItemDefinitions defs = item.getDefinitions();
            if (defs == null) {
                continue;
            }
            if (defs.isPlaceholder()) {
                this.remove(i--, 1, false);
            }
        }
        requestContainerRefresh(container);
    }

    public void revalidate() {
        final IntAVLTreeSet slots = container.getAvailableSlots();
        if (slots.isEmpty()) {
            return;
        }
        int tabsSize = 0;
        for (final int tab : tabSizes) {
            tabsSize += tab;
        }
        final int length = container.getSize();
        if (tabsSize > length) {
            loop:
            for (int i = 9; i >= 0; i--) {
                while (tabSizes[i] > 0) {
                    --tabSizes[i];
                    if (--tabsSize <= length) {
                        break loop;
                    }
                }
            }
        }
        if (slots.firstInt() != length) {
            final Int2ObjectAVLTreeMap<Item> map = new Int2ObjectAVLTreeMap<Item>(container.getItems());
            container.getItems().clear();
            container.setWeight(0);
            final IntAVLTreeSet availableSlots = container.getAvailableSlots();
            final IntOpenHashSet modifiedSlots = container.getModifiedSlots();
            for (int i = 0; i < container.getContainerSize(); i++) {
                availableSlots.add(i);
                modifiedSlots.add(i);
            }
            int lastSlot = 0;
            for (final Int2ObjectMap.Entry<Item> entry : map.int2ObjectEntrySet()) {
                container.set(lastSlot++, entry.getValue());
            }
        }
    }

    /**
     * Toggles the bank setting to specified value.
     *
     * @param setting
     * @param on
     */
    public void toggleSetting(final BankSetting setting, final boolean on) {
        final int val = getSetting(setting);
        if (val == 1 && !on) {
            settings &= (~(1 << (setting.ordinal() + 1)));
        } else if (val == 0 && on) {
            settings |= 1 << (setting.ordinal() + 1);
        }
        updateVar(setting);
    }

    /**
     * Gets the value of the setting, either 1 (true) or 0 (false)
     *
     * @param setting
     * @return
     */
    public int getSetting(final BankSetting setting) {
        return (settings >> (setting.ordinal() + 1)) & 1;
    }

    public void resetBank() {
        container.clear();
        requestContainerRefresh(container);
        this.currentTab = MAIN_TAB;
    }

    public void addFillers(int maxAmount) {
        int size = Container.getSize(ContainerType.BANK);
        maxAmount = Math.min(size, maxAmount);
        final Item filler = new Item(20594, 1, new HashMap<>());
        int count = 0;
        while (maxAmount-- > 0 && container.getFreeSlotsSize() != 0) {
            if (++count > size) {
                break;
            }
            this.add(filler, currentTab);
        }
        requestContainerRefresh(container);
    }

    public void removeFillers(final int slot, int amount) {
        if (slot == -1) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                final Item item = container.get(i);
                if (item == null || item.getId() != 20594) {
                    continue;
                }
                this.remove(i--, 1, false);
                if (--amount <= 0) {
                    break;
                }
            }
        } else {
            if (container.get(slot).getId() != 20594) {
                return;
            }
            container.remove(slot, 1);
            shiftBackwardsPastIndex(slot);
            final int tabId = getTab(slot);
            if (--tabSizes[tabId] == 0) {
                if (tabId == MAIN_TAB) {
                    collapseTab(MAIN_TAB);
                } else {
                    resizeTabs();
                }
            }
        }
        requestContainerRefresh(container);
    }

    public int getTabFromSlot(final int slotId) {
        int tab = slotId - 11;
        if (tab == -1) {
            tab = 9;
        }
        return tab;
    }

    private int validateTab(final int tab) {
        if (tab < 0 || tab > 9) {
            return MAIN_TAB;
        }
        if (tab == 9 || tabSizes[9] == 0) {
            return MAIN_TAB;
        }
        for (int i = 0; i < tab; i++) {
            if (tabSizes[i] == 0) {
                return i;
            }
        }
        return tab;
    }

    private void resizeTabs() {
        final int[] sizes = new int[10];
        int index = 0;
        for (int i = 0; i < 9; i++) {
            final int length = tabSizes[i];
            if (length == 0) {
                continue;
            }
            sizes[index++] = length;
        }
        sizes[MAIN_TAB] = tabSizes[MAIN_TAB];
        tabSizes = sizes;
        container.setFullUpdate(true);
    }

    private int getIndexOfFirstSlotInTab(final int tabId) {
        int index = 0;
        for (int i = 0; i < tabId; i++) {
            index += tabSizes[i];
        }
        return index;
    }

    private int getIndexOfLastSlotInTab(final int tab) {
        int index = 0;
        for (int i = 0; i <= tab; i++) {
            index += tabSizes[i];
        }
        return index;
    }

    private void shiftForwardPastIndex(final int index) {
        for (int slot = container.getContainerSize() - 1; slot >= 0; slot--) {
            if (slot > index) {
                final Item existingItem = slot == 0 ? null : container.get(slot - 1);
                container.set(slot, existingItem);
                if (slot > 0) {
                    container.set(slot - 1, null);
                }
            }
        }
    }

    private void shiftBackwardsPastIndex(final int index) {
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            if (slot > index) {
                final Item existingItem = container.get(slot);
                if (slot > 0) {
                    container.set(slot - 1, existingItem);
                }
                container.set(slot, null);
            }
        }
    }

    private int getTab(final int slot) {
        if (slot == -1) {
            return currentTab;
        }
        int size = 0;
        for (int i = 0; i < tabSizes.length; i++) {
            final int tab = tabSizes[i];
            size += tab;
            if (size > slot) {
                return i;
            }
        }
        return currentTab;
    }

    public void refreshBankSizes(@NotNull final Player player) {
        player.getVarManager().sendBit(4170, displayType);
        for (int i = 0; i < 9; i++) {
            player.getVarManager().sendBit(4171 + i, tabSizes[i]);
        }
    }


    public boolean hasFreeSlots() {
        return container.getFreeSlotsSize() > 0;
    }

    public boolean checkSpace() {
        if (!hasFreeSlots()) {
            sendMessage("Not enough space in your bank.");
            return false;
        }
        return true;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public int getLastDepositAmount() {
        return lastDepositAmount;
    }

    public void setLastDepositAmount(int lastDepositAmount) {
        this.lastDepositAmount = lastDepositAmount;
    }

    public BankContainer getContainer() {
        return container;
    }

    public QuantitySelector getQuantity() {
        return quantity;
    }


    public enum BankSwitchType {
        ITEM_TO_ITEM, ITEM_TO_TAB, TAB_TO_TAB, ITEM_TO_END_OF_TAB
    }


    public enum QuantitySelector {
        ONE(0), FIVE(1), TEN(2), X(3), ALL(4);
        private final int value;

        QuantitySelector(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
