package com.zenyte.game.model.item.degradableitems;

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalArmour;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalDegradeable;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon;
import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.near_reality.game.model.item.degrading.Degradeable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.item.AbyssalTentacleOr;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.logging.Logger;

/**
 * @author Kris | 28. dets 2017 : 1:44.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ChargesManager {

    private static final Logger log = Logger.getLogger(ChargesManager.class.getName());
    private static final int[] SLOTS = new int[]{EquipmentSlot.HELMET.getSlot(), EquipmentSlot.PLATE.getSlot(), EquipmentSlot.AMULET.getSlot(), EquipmentSlot.LEGS.getSlot(), EquipmentSlot.WEAPON.getSlot(), EquipmentSlot.SHIELD.getSlot()};

    public static final DecimalFormat FORMATTER = new DecimalFormat("#0.0");

    private final Player player;

    public ChargesManager(final Player player) {
        this.player = player;
    }

    /**
     * Loops over the player's worn equipment and removes charges if the item is a degradable item and the type matches.
     */
    public void removeCharges(final DegradeType type) {
        int elvenProtected = -1;
        final Container container = player.getEquipment().getContainer();
        for (int i = SLOTS.length - 1; i >= 0; i--) {
            final int slot = SLOTS[i];
            final Item item = container.get(slot);
            if (item == null) {
                continue;
            }
            final int itemId = item.getId();
            final Degradeable deg = DegradableItem.ITEMS.get(itemId);
            if (deg == null) {
                continue;
            }
            if (deg.getType() != type) {
                continue;
            }
            final int currentCharges = item.getCharges();
            if (currentCharges != deg.getMaximumCharges() && currentCharges <= 0) {
                continue;
            }
            if (deg.getType() == DegradeType.INCOMING_HIT && deg instanceof CrystalArmour) {
                if (elvenProtected == -1) {
                    elvenProtected = rollElvenRing(player);
                }
                if (elvenProtected == 1) {
                    player.sendFilteredMessage("Your Elven Signet prevents your armour from losing a charge.");
                    continue;
                }
            }
            if (deg.getType() == DegradeType.USE) {
                if (deg instanceof CrystalTool) {
                    if (elvenProtected == -1) {
                        elvenProtected = rollElvenRing(player);
                    }
                    if (elvenProtected == 1) {
                        player.sendFilteredMessage("Your Elven Signet prevents your tool from losing a charge.");
                        continue;
                    }
                }
                if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) && Utils.random(19) == 0) {
                    player.sendFilteredMessage("As you've completed the master combat achievements, you have been prevented from losing a charge.");
                    continue;
                }
            }
            if (deg.getType() == DegradeType.OUTGOING_HIT && deg instanceof CrystalWeapon) {
                if (elvenProtected == -1) {
                    elvenProtected = rollElvenRing(player);
                }
                if (elvenProtected == 1 && !(deg instanceof CrystalWeapon.BladeOfSaeldor || deg instanceof CrystalWeapon.BowOfFaerdhinen)) {
                    player.sendFilteredMessage("Your Elven Signet prevents your weapon from losing a charge.");
                    continue;
                }
            }
            if(deg.getType() == DegradeType.OUTGOING_HIT || deg.getType() == DegradeType.SPELL) {
                if(SlayerHelmetEffects.INSTANCE.shouldNotDegrade(player))
                    continue;
            }
            final ItemPlugin chargesPlugin = ItemPlugin.getPlugin(itemId);
            final ContainerType containerType = container.getType();
            if (chargesPlugin instanceof ChargeExtension && (containerType == ContainerType.INVENTORY || containerType == ContainerType.EQUIPMENT)) {
                ContainerWrapper wrapper = containerType == ContainerType.INVENTORY ? player.getInventory() : player.getEquipment();
                ((ChargeExtension) chargesPlugin).removeCharges(player, item, wrapper, slot, 1);
                continue;
            }
            item.setCharges(currentCharges - 1);
            if (item.getCharges() <= deg.getMinimumCharges()) {
                final int next = deg.getNextId();
                if (deg.equals(DegradableItem.ABYSSAL_TENTACLE)) {
                    player.getEquipment().set(slot, null);
                    player.getInventory().addOrDrop(new Item(next));
                } else if (deg.equals(DegradableItem.ABYSSAL_TENTACLE_OR)) {
                    player.getEquipment().set(SLOTS[slot], null);
                    player.getInventory().addOrDrop(new Item(next));
                    player.getInventory().addOrDrop(AbyssalTentacleOr.ORN_KIT);
                } else if (next == -1) {
                    player.getEquipment().set(slot, null);
                } else if (next != itemId) {
                    item.setId(next);
                    final Degradeable nextDeg = DegradableItem.ITEMS.get(next);
                    if (nextDeg != null) {
                        item.setCharges(nextDeg.getMaximumCharges() - (currentCharges - item.getCharges()));
                    }
                }
                player.getEquipment().refresh(slot);
                player.getAppearance().setRenderAnimation(player.getAppearance().generateRenderAnimation());
                final String name = ItemDefinitions.getOrThrow(deg.getItemId()).getName();
                player.sendMessage("Your " + name + " " + (name.contains("legs") ? "have" : "has") + (item.getCharges() == 0 ? " fully" : "") + " degraded" + (deg.getNextId() == -1 ? " and turned to dust." : "."));
            }
        }
    }

    private int rollElvenRing(Player player) {
        final int ringId = player.getEquipment().getId(EquipmentSlot.RING);
        return (ringId == ItemId.ELVEN_SIGNET || ringId == ItemId.CELESTIAL_SIGNET || ringId == ItemId.CELESTIAL_SIGNET_UNCHARGED) && Utils.roll(10) ? 1 : 0;
    }

    /**
     * Removes the defined amount of charges from the item in arguments. The item nor its container can be null.
     *
     * @param item      the item to remove charges from, can't be null, nor can its container.
     */
    public void removeCharges(@NotNull final Item item, final int amount, Container container, int slotId) {
        if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) && Utils.random(19) == 0) {
            player.sendFilteredMessage("As you've completed the master combat achievements, you have been prevented from losing a charge.");
            return;
        }
        final ItemPlugin chargesPlugin = ItemPlugin.getPlugin(item.getId());
        final ContainerType containerType = container.getType();
        if (chargesPlugin instanceof ChargeExtension && (containerType == ContainerType.INVENTORY || containerType == ContainerType.EQUIPMENT)) {
            ContainerWrapper wrapper = containerType == ContainerType.INVENTORY ? player.getInventory() : player.getEquipment();
            ((ChargeExtension) chargesPlugin).removeCharges(player, item, wrapper, slotId, amount);
            return;
        }
        final int charges = item.getCharges();
        final Degradeable deg = DegradableItem.ITEMS.get(item.getId());
        if (deg == null) {
            log.info("Unable to remove charges from item: " + item);
            return;
        }
        final int currentCharges = item.getCharges();
        item.setCharges(charges - amount);
        final boolean equipment = containerType == ContainerType.EQUIPMENT;
        if (item.getCharges() <= deg.getMinimumCharges()) {
            final int nextId = deg.getNextId();
            final Degradeable nextDeg = DegradableItem.ITEMS.get(deg.getNextId());
            if (nextDeg != null) {
                item.setCharges(nextDeg.getMaximumCharges() - (currentCharges - item.getCharges()));
            }
            container.set(slotId, nextId == -1 ? null : new Item(nextId, item.getAmount(), item.getAttributesCopy()));
            container.refresh(player);
            if (equipment) {
                player.getEquipment().refresh();
            }
            final String name = ItemDefinitions.getOrThrow(deg.getItemId()).getName();
            player.sendMessage("Your " + name + " " + (name.contains("legs") ? "have" : "has") + (item.getCharges() == 0 ? " fully" : "") + " degraded" + (deg.getNextId() == -1 ? " and turned to dust." : "."));
        }
    }

    /**
     * Checks the charges of the item and informs the player of it.
     *
     * @param item the item to check the charges of.
     */
    public void checkCharges(@NotNull final Item item) {
        final ItemPlugin chargesPlugin = ItemPlugin.getPlugin(item.getId());
        if (chargesPlugin instanceof ChargeExtension) {
            // default behaviour of checkCharges also invokes notifyPlayerOfChargesLeft(item)
            ((ChargeExtension) chargesPlugin).checkCharges(player, item);
            return;
        }
        notifyPlayerOfChargesLeft(item);
    }

    public void notifyPlayerOfChargesLeft(@NotNull Item item) {
        final int id = item.getId();
        final String name = item.getName();
        final int fullCharges = DegradableItem.getDefaultCharges(id, -1);

        if (item.getCharges() == fullCharges) {
            final String payload = name.toLowerCase().endsWith("s") ? " are fully charged." : " is fully charged.";
            player.sendMessage("Your " + name + payload);
            return;
        }
        if (item.getCharges() <= 0) {
            final String payload = name.toLowerCase().endsWith("s") ? " are completely degraded." : " is completely degraded.";
            player.sendMessage("Your " + name + payload);
            return;
        }

        final Degradeable deg = DegradableItem.ITEMS.get(id);
        if (deg == null) {
            log.info("Unable to check charges of item: " + item);
            return;
        }
        if (deg.getType() == DegradeType.RECOIL || deg.getType() == DegradeType.USE || deg instanceof CrystalDegradeable || DegradableItem.VENATOR_BOW.equals(deg)) {
            player.sendMessage("Your " + name + " has " + StringFormatUtil.formatNumberUS(item.getCharges()) + " charge" + (item.getCharges() == 1 ? "" : "s") + " remaining.");
            return;
        }

        final String percentage = FORMATTER.format(item.getCharges() / (float) deg.getMaximumCharges() * 100);
        player.sendMessage("Your " + name + " has " + Utils.pluralizedFormatted("charge", item.getCharges()) + " remaining.");
    }
}
