package com.zenyte.game.model.ui.testinterfaces;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.preset.Preset;
import com.zenyte.game.content.preset.PresetManager;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.UnmodifiableItem;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.container.impl.bank.BankSetting;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.events.InventoryItemSwitchEvent;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tommeh | 27/05/2019 | 14:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
@SuppressWarnings("DuplicatedCode")
public class PresetManagerInterface extends Interface implements SwitchPlugin {
    private static final int HIGHLIGHT_SPELLBOOK_CLIENTSCRIPT = 10904;
    private static final int BUILD_EQUIPMENT_AND_INVENTORY_CONTAINER_CLIENTSCRIPT = 10901;
    private static final int SELECT_PRESET_CLIENTSCRIPT = 10909;
    private static final int HIDE_SPELLBOOK_BUTTONS_CLIENTSCRIPT = 10902;
    private static final int BUILD_PRESET_INTERFACE_BASE_CLIENTSCRIPT = 10900;
    private static final int REFRESH_PRESET_LIST_CLIENTSCRIPT = 10906;
    private static final int ACTIVE_SELECTED_PRESET_VARP = 262;
    private static final int SELECT_SPELLBOOK_SOUND_EFFECT = 2266;
    private static final int MAX_WORD_LENGTH = 14;
    private static final int CAPACITY_TOOLTIP_SCRIPT = 1495;
    private static final String NAME_FILTER_REGEX = "[^a-zA-Z0-9$#€£()+\\-/*!,.'_ \\[\\]@{}]";
    private static final String[] equipmentSlotLabels = {"Helm", "Cape", "Amulet", "Body", "Shield", "Legs", "Hands", "Feet", "Ring", "Ammunition"};
    private static final Map<Integer, Integer> equipmentSlotComponentMap = ImmutableMap.<Integer, Integer>builder().put(17, EquipmentSlot.HELMET.getSlot()).put(18, EquipmentSlot.CAPE.getSlot()).put(19, EquipmentSlot.AMULET.getSlot()).put(20, EquipmentSlot.WEAPON.getSlot()).put(21, EquipmentSlot.PLATE.getSlot()).put(22, EquipmentSlot.SHIELD.getSlot()).put(23, EquipmentSlot.LEGS.getSlot()).put(24, EquipmentSlot.HANDS.getSlot()).put(25, EquipmentSlot.BOOTS.getSlot()).put(26, EquipmentSlot.RING.getSlot()).put(27, EquipmentSlot.AMMUNITION.getSlot()).build();

    private static void forceSetSpellbook(@NotNull final Player player, @Nullable final Spellbook book) {
        final GameInterface inter = GameInterface.PRESET_MANAGER;
        final Interface plugin = inter.getPlugin().orElseThrow(RuntimeException::new);
        final int interfaceId = inter.getId();
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        for (final Spellbook spellbook : Spellbook.VALUES) {
            final int componentHash = interfaceId << 16 | plugin.getComponent(spellbook + " Spellbook");
            dispatcher.sendClientScript(HIGHLIGHT_SPELLBOOK_CLIENTSCRIPT, spellbook == book ? 1 : 0, componentHash);
        }
    }

    @Subscribe
    public static final void onInventoryItemSwitch(final InventoryItemSwitchEvent event) {
        final Player player = event.getPlayer();
        final boolean containsPresetManagerInterface = player.getInterfaceHandler().isPresent(GameInterface.PRESET_MANAGER);
        if (!containsPresetManagerInterface) {
            return;
        }
        final PresetManager presetManager = player.getPresetManager();
        final List<Preset> availablePresets = presetManager.getPresets();
        if (availablePresets.isEmpty()) {
            refresh(player, OptionalInt.empty(), false);
        }
    }

    private static void refresh(final Player player, @NotNull final OptionalInt optionalSlot, final boolean reselectPreset) {
        final PresetManager presetManager = player.getPresetManager();
        final boolean isViewingPreset = optionalSlot.isPresent();
        if (isViewingPreset) {
            final int slot = optionalSlot.getAsInt();
            if (slot < 0 || slot >= presetManager.getTotalPresets()) {
                throw new IllegalStateException();
            }
        }
        final Preset preset = isViewingPreset ? presetManager.getPreset(optionalSlot.getAsInt()) : null;
        final Spellbook spellbook = isViewingPreset ? Objects.requireNonNull(preset).getSpellbook() : player.getCombatDefinitions().getSpellbook();
        final Container container = new Container(ContainerPolicy.NORMAL, ContainerType.TOURNAMENT, Optional.empty());
        final Map<Integer, ? extends Item> inventoryMap = isViewingPreset ? preset.getInventory() : player.getInventory().getContainer().getItems();
        final Map<Integer, ? extends Item> equipmentMap = isViewingPreset ? preset.getEquipment() : player.getEquipment().getContainer().getItems();
        for (int i = Equipment.SIZE + Inventory.SIZE - 1; i >= 0; i--) {
            container.set(i, i < Equipment.SIZE ? equipmentMap.get(i) : inventoryMap.get(i - Equipment.SIZE));
        }
        final int index = optionalSlot.orElse(-1);
        player.getPacketDispatcher().sendUpdateItemContainer(container);
        player.getPacketDispatcher().sendClientScript(BUILD_EQUIPMENT_AND_INVENTORY_CONTAINER_CLIENTSCRIPT);
        if (reselectPreset) {
            player.getPacketDispatcher().sendClientScript(SELECT_PRESET_CLIENTSCRIPT, index, presetManager.getTotalPresets(), presetManager.getMaximumPresets());
        }
        forceSetSpellbook(player, !player.getMemberRank().equalToOrGreaterThan(Preset.SPELLBOOK_MINIMUM_MEMBER_RANK) ? null : spellbook);
        player.getVarManager().sendVar(ACTIVE_SELECTED_PRESET_VARP, index);
        if (!player.getMemberRank().equalToOrGreaterThan(Preset.SPELLBOOK_MINIMUM_MEMBER_RANK)) {
            player.getPacketDispatcher().sendClientScript(HIDE_SPELLBOOK_BUTTONS_CLIENTSCRIPT);
        }
    }

    @Override
    protected void attach() {
        put(8, "Preset");
        for (int index = 0; index < equipmentSlotLabels.length; index++) {
            put(17 + index, equipmentSlotLabels[index]);
        }
        put(28, "Inventory");
        put(33, "Normal Spellbook");
        put(34, "Ancient Spellbook");
        put(35, "Lunar Spellbook");
        put(36, "Arceuus Spellbook");
        put(39, "Create Preset");
        put(41, "Apply");
        put(43, "Rename");
        put(45, "Delete");
        put(47, "Always set placeholders");
        put(49, "Bank");
        put(50, "Move preset up");
        put(51, "Move preset down");
        put(52, "Preset count");
        put(54, "Preset cap");
        put(56, "Capacity tooltip");
    }

    @Override
    public void open(final Player player) {
        final PresetManager presetManager = player.getPresetManager();
        presetManager.revalidatePresets();
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendClientScript(BUILD_PRESET_INTERFACE_BASE_CLIENTSCRIPT);
        refresh(player, presetManager.getTotalPresets() == 0 ? OptionalInt.empty() : OptionalInt.of(presetManager.getDefaultPreset()), true);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Inventory"), 0, 28, AccessMask.CLICK_OP10, AccessMask.DRAG_DEPTH1, AccessMask.DRAG_TARGETABLE);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Preset"), 0, 50, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP6);
        refreshPresetsList(player);
        refreshSize(player);
        final int interfaceId = getInterface().getId();
        player.getPacketDispatcher().sendClientScript(CAPACITY_TOOLTIP_SCRIPT, "Default slots: 2<br><br>Additional member slots: 2 per rank", getComponent("Preset cap") | (interfaceId << 16), getComponent("Capacity tooltip") | (interfaceId << 16));
    }

    private void refreshSize(@NotNull final Player player) {
        final PresetManager presetManager = player.getPresetManager();
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Preset count"), presetManager.getTotalPresets());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Preset cap"), presetManager.getMaximumPresets());
    }

    @Override
    protected void build() {
        bind("Create Preset", player -> {
            final PresetManager presetManager = player.getPresetManager();
            if (presetManager.getTotalPresets() >= presetManager.getMaximumPresets()) {
                player.sendMessage("You have reached your maximum quota of presets.");
                return;
            }
            player.sendInputString("What should this preset be called?", name -> {
                if (presetManager.getTotalPresets() >= presetManager.getMaximumPresets()) {
                    throw new IllegalStateException();
                }
                final Optional<String> filteredName = provideValidName(name);
                if (!filteredName.isPresent()) {
                    player.sendMessage("You may not set that as a preset name.");
                    return;
                }
                final String presetName = filteredName.get();
                if (isTooLong(presetName)) {
                    player.sendMessage("The provided name exceeds maximum character length.");
                    return;
                }
                int indexOfActivePreset = indexOfActivePreset(player);
                if (indexOfActivePreset < 0 || indexOfActivePreset > presetManager.getTotalPresets()) {
                    indexOfActivePreset = 0;
                }
                final int index = presetManager.getTotalPresets() == 0 ? 0 : (indexOfActivePreset + 1);
                presetManager.addPreset(index, presetName);
                refreshPresetsList(player);
                refresh(player, OptionalInt.of(index), true);
                refreshSize(player);
            });
        });
        bind("Preset", (player, slotId, itemId, option) -> {
            if (option == 1) {
                final int activeIndex = indexOfActivePreset(player);
                if (activeIndex == slotId) {
                    return;
                }
                refresh(player, OptionalInt.of(slotId), true);
            } else if (option == 2) {
                overwrite(player, slotId);
            } else if (option == 3) {
                load(player, slotId);
            } else if (option == 4) {
                rename(player, slotId);
            } else if (option == 5) {
                final Preset preset = player.getPresetManager().getPreset(slotId);
                if (preset == null) {
                    return;
                }
                if (!preset.isAvailable()) {
                    player.sendMessage("You may not set locked presets as default preset.");
                    return;
                }
                player.getPresetManager().setDefaultPreset(slotId);
                refreshPresetsList(player);
            } else if (option == 6) {
                delete(player, slotId);
            }
        });
        bind("Normal Spellbook", player -> selectSpellbook(player, Spellbook.NORMAL));
        bind("Ancient Spellbook", player -> selectSpellbook(player, Spellbook.ANCIENT));
        bind("Lunar Spellbook", player -> selectSpellbook(player, Spellbook.LUNAR));
        bind("Arceuus Spellbook", player -> selectSpellbook(player, Spellbook.ARCEUUS));
        bind("Apply", player -> load(player, indexOfActivePreset(player)));
        bind("Rename", player -> rename(player, indexOfActivePreset(player)));
        bind("Delete", player -> delete(player, indexOfActivePreset(player)));
        bind("Inventory", (player, slotId, itemId, option) -> examine(player, false, itemId));
        for (final String slot : equipmentSlotLabels) {
            bind(slot, (player, slotId, itemId, option) -> examine(player, true, getComponent(slot)));
        }
        bind("Always set placeholders", player -> player.getBank().toggleSetting(BankSetting.ALWAYS_PLACEHOLDER, player.getBank().getSetting(BankSetting.ALWAYS_PLACEHOLDER) == 0));
        bind("Bank", GameInterface.BANK::open);
        bind("Inventory", "Inventory", (player, fromSlot, toSlot) -> {
            final PresetManager presetManager = player.getPresetManager();
            final List<Preset> availablePresets = presetManager.getPresets();
            if (availablePresets.isEmpty()) {
                player.getInventory().switchItem(fromSlot, toSlot);
                return;
            }
            final int activeIndex = indexOfActivePreset(player);
            final Preset preset = presetManager.getPreset(activeIndex);
            if (preset == null) {
                return;
            }
            preset.switchInventoryItem(fromSlot, toSlot);
            refresh(player, OptionalInt.of(activeIndex), false);
        });
        bind("Move preset up", (player, slotId, itemId, option) -> {
            final PresetManager presetManager = player.getPresetManager();
            if (presetManager.getTotalPresets() == 0) {
                player.sendMessage("You do not have any presets yet.");
                return;
            }
            final int activeIndex = indexOfActivePreset(player);
            final Preset preset = presetManager.getPreset(activeIndex);
            if (preset == null) {
                return;
            }
            if (activeIndex <= 0) {
                return;
            }
            final Preset abovePreset = presetManager.getPreset(activeIndex - 1);
            if (abovePreset == null) {
                return;
            }
            if (!preset.isAvailable() || !abovePreset.isAvailable()) {
                player.sendMessage("You may not move locked presets.");
                return;
            }
            presetManager.setPreset(activeIndex, abovePreset);
            presetManager.setPreset(activeIndex - 1, preset);
            refresh(player, OptionalInt.of(activeIndex - 1), true);
            if (presetManager.getDefaultPreset() == activeIndex) {
                presetManager.setDefaultPreset(activeIndex - 1);
            } else if (presetManager.getDefaultPreset() == activeIndex - 1) {
                presetManager.setDefaultPreset(activeIndex);
            }
            refreshPresetsList(player);
        });
        bind("Move preset down", (player, slotId, itemId, option) -> {
            final PresetManager presetManager = player.getPresetManager();
            if (presetManager.getTotalPresets() == 0) {
                player.sendMessage("You do not have any presets yet.");
                return;
            }
            final int activeIndex = indexOfActivePreset(player);
            final Preset preset = presetManager.getPreset(activeIndex);
            if (preset == null) {
                return;
            }
            if (activeIndex >= presetManager.getTotalPresets() - 1) {
                return;
            }
            final Preset belowPreset = presetManager.getPreset(activeIndex + 1);
            if (belowPreset == null) {
                return;
            }
            if (!preset.isAvailable() || !belowPreset.isAvailable()) {
                player.sendMessage("You may not move locked presets.");
                return;
            }
            presetManager.setPreset(activeIndex, belowPreset);
            presetManager.setPreset(activeIndex + 1, preset);
            refresh(player, OptionalInt.of(activeIndex + 1), true);
            if (presetManager.getDefaultPreset() == activeIndex) {
                presetManager.setDefaultPreset(activeIndex + 1);
            } else if (presetManager.getDefaultPreset() == activeIndex + 1) {
                presetManager.setDefaultPreset(activeIndex);
            }
            refreshPresetsList(player);
        });
    }

    private Optional<String> provideValidName(@NotNull final String string) {
        final String filteredName = string.replaceAll(NAME_FILTER_REGEX, "").trim();
        if (filteredName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(filteredName);
    }

    private boolean isTooLong(@NotNull final String filteredString) {
        if (filteredString.length() > MAX_WORD_LENGTH * 3) {
            return true;
        }
        final String[] splitName = filteredString.split(" ");
        for (final String namePart : splitName) {
            //If any of the "words"(word = phrase split by space in client terms) has a length greater than 15, do not allow the name
            //as the client would render the <br> upon rightclicking the preset.
            if (namePart.length() > MAX_WORD_LENGTH) {
                return true;
            }
        }
        return false;
    }

    private int indexOfActivePreset(@NotNull final Player player) {
        if (player.getPresetManager().getTotalPresets() == 0) {
            return -1;
        }
        return player.getVarManager().getValue(ACTIVE_SELECTED_PRESET_VARP);
    }

    private void selectSpellbook(@NotNull final Player player, @NotNull final Spellbook clickedSpellbook) {
        if (!player.getMemberRank().equalToOrGreaterThan(Preset.SPELLBOOK_MINIMUM_MEMBER_RANK)) {
            player.sendMessage("You need to be a Ruby member or above to use this feature.");
            return;
        }
        final PresetManager presetManager = player.getPresetManager();
        if (presetManager.getTotalPresets() == 0) {
            player.sendFilteredMessage("You must select a preset from the menu on the left first.");
            return;
        }
        final int activeIndex = indexOfActivePreset(player);
        final Preset preset = presetManager.getPreset(activeIndex);
        if (preset == null) {
            return;
        }
        final Spellbook currentSpellbook = preset.getSpellbook();
        final Spellbook highlightedSpellbook = clickedSpellbook == currentSpellbook ? null : clickedSpellbook;
        preset.setSpellbook(highlightedSpellbook);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        final int interfaceId = getInterface().getId();
        for (final Spellbook spellbook : Spellbook.VALUES) {
            final int componentHash = interfaceId << 16 | getComponent(spellbook + " Spellbook");
            dispatcher.sendClientScript(HIGHLIGHT_SPELLBOOK_CLIENTSCRIPT, spellbook == highlightedSpellbook ? 1 : 0, componentHash);
        }
        player.sendSound(SELECT_SPELLBOOK_SOUND_EFFECT);
    }

    private void refreshPresetsList(final Player player) {
        final PresetManager presetManager = player.getPresetManager();
        final StringBuilder builder = new StringBuilder();
        final int size = presetManager.getTotalPresets();
        final List<Preset> presets = presetManager.getPresets();
        for (int i = 0; i < size; i++) {
            final Preset preset = presets.get(i);
            if (i == presetManager.getDefaultPreset()) {
                builder.append("<img=13>");
            }
            builder.append(preset.isAvailable() ? "" : "<col=000000>").append(preset.getName()).append(preset.isAvailable() ? "" : "</col>").append("|");
        }
        player.getPacketDispatcher().sendClientScript(REFRESH_PRESET_LIST_CLIENTSCRIPT, presetManager.getTotalPresets(), presetManager.getMaximumPresets(), builder.toString());
    }

    public static void load(final Player player, final int index) {
        final PresetManager presetManager = player.getPresetManager();
        if (presetManager.getTotalPresets() == 0) {
            player.sendMessage("You haven't set any presets yet.");
            return;
        }
        final Preset preset = presetManager.getPreset(index);
        if (preset == null) {
            return;
        }
        if (!preset.isAvailable()) {
            player.sendMessage("You may not load locked presets.");
            return;
        }
        final boolean scheduledPresetLoad = player.getTemporaryAttributes().put("queued preset for load", preset) != null;
        if (!scheduledPresetLoad) {
            player.addPostProcessRunnable(() -> {
                final Preset finalPreset = (Preset) player.getTemporaryAttributes().remove("queued preset for load");
                player.getAttributes().put("last preset loaded", index);
                finalPreset.load(player);
            });
        }
    }

    private void rename(final Player player, final int index) {
        final PresetManager presetManager = player.getPresetManager();
        if (presetManager.getTotalPresets() == 0) {
            player.sendMessage("You haven't set any presets yet.");
            return;
        }
        final Preset preset = presetManager.getPreset(index);
        if (preset == null) {
            return;
        }
        if (!preset.isAvailable()) {
            player.sendMessage("You may not rename locked presets.");
            return;
        }
        final String oldName = preset.getName();
        player.sendInputString("Rename this preset:", name -> {
            final Optional<String> filteredName = provideValidName(name);
            if (!filteredName.isPresent()) {
                player.sendMessage("You may not set that as a preset name.");
                return;
            }
            final String presetName = filteredName.get();
            if (isTooLong(presetName)) {
                player.sendMessage("The provided name exceeds maximum character length.");
                return;
            }
            preset.rename(presetName);
            refreshPresetsList(player);
            player.sendMessage("Preset " + Colour.RS_PURPLE.wrap(oldName) + " has been renamed to " + Colour.RS_PURPLE.wrap(preset.getName()) + ".");
        });
    }

    private void overwrite(final Player player, final int index) {
        final PresetManager presetManager = player.getPresetManager();
        if (presetManager.getTotalPresets() == 0) {
            player.sendMessage("You haven't set any presets yet.");
            return;
        }
        final Preset preset = presetManager.getPreset(index);
        if (preset == null) {
            return;
        }
        if (!preset.isAvailable()) {
            player.sendMessage("You may not rename locked presets.");
            return;
        }
        final String name = preset.getName();
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Overwrite " + name + "?", new DialogueOption("Yes.", () -> {
                    presetManager.setPreset(index, new Preset(name, player));
                    GameInterface.PRESET_MANAGER.open(player);
                    refresh(player, OptionalInt.of(index), true);
                }), new DialogueOption("No."));
            }
        });
    }

    private void delete(final Player player, final int index) {
        final PresetManager presetManager = player.getPresetManager();
        if (presetManager.getTotalPresets() == 0) {
            player.sendMessage("You haven't set any presets yet.");
            return;
        }
        if (index < 0 || index >= presetManager.getTotalPresets()) {
            return;
        }
        final Preset preset = presetManager.getPreset(index);
        if (preset == null) {
            throw new IllegalStateException();
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Remove the preset " + preset.getName() + "?", new DialogueOption("Yes.", () -> {
                    if (presetManager.getPreset(index) != preset) {
                        return;
                    }
                    presetManager.getPresets().remove(index);
                    refreshPresetsList(player);
                    refresh(player, presetManager.getTotalPresets() == 0 ? OptionalInt.empty() : OptionalInt.of(0), true);
                    player.sendMessage("Preset " + Colour.RS_PURPLE.wrap(preset.getName()) + " has been deleted.");
                    if (index <= presetManager.getDefaultPreset()) {
                        presetManager.setDefaultPreset(presetManager.getDefaultPreset() == index ? 0 : (presetManager.getDefaultPreset() - 1));
                    }
                    presetManager.revalidatePresets();
                    GameInterface.PRESET_MANAGER.open(player);
                }), new DialogueOption("No."));
            }
        });
    }

    private void examine(final Player player, final boolean equipment, final int id) {
        if (id == RunePouch.RUNE_POUCH.getId() || id == RunePouch.DIVINE_RUNE_POUCH.getId()) {
            ItemUtil.sendItemExamine(player, id);
            final PresetManager presetManager = player.getPresetManager();
            final int activeIndex = presetManager.getTotalPresets() == 0 ? -1 : indexOfActivePreset(player);
            if (activeIndex > -1) {
                final Preset preset = presetManager.getPreset(activeIndex);
                if (preset != null) {
                    final Map<Integer, UnmodifiableItem> pouch = preset.getRunePouch();
                    if (pouch != null) {
                        if (pouch.isEmpty()) {
                            player.sendMessage("Rune pouch: " + Colour.RED.wrap("Empty"));
                        } else {
                            final StringBuilder builder = new StringBuilder();
                            int i = 0;
                            for (final UnmodifiableItem rune : pouch.values()) {
                                builder.append(rune.getAmount()).append(" x ").append(ItemDefinitions.nameOf(rune.getId()));
                                if (i == pouch.size() - 2) {
                                    builder.append(" and ");
                                } else if (i < pouch.size() - 2) {
                                    builder.append(", ");
                                }
                                i++;
                            }
                            player.sendMessage("Rune pouch: " + Colour.RS_GREEN.wrap(builder.toString()));
                        }
                    }
                }
            }
            return;
        }
        if (equipment) {
            final PresetManager presetManager = player.getPresetManager();
            final int activeIndex = presetManager.getTotalPresets() == 0 ? -1 : indexOfActivePreset(player);
            final Integer slot = equipmentSlotComponentMap.get(id);
            if (activeIndex > -1) {
                final Preset preset = presetManager.getPreset(activeIndex);
                if (preset != null && slot != null) {
                    final UnmodifiableItem item = preset.getEquipment().get(slot);
                    ItemUtil.sendItemExamine(player, item);
                }
            } else {
                ItemUtil.sendItemExamine(player, player.getEquipment().getItem(slot));
            }
        } else {
            ItemUtil.sendItemExamine(player, id);
        }
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PRESET_MANAGER;
    }
}
