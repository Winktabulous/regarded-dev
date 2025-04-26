package com.zenyte.game.content.minigame.puropuro;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.hunter.plugins.ImplingJarPlugin;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mgi.types.config.items.ItemDefinitions;

import java.util.Comparator;
import java.util.Optional;

/**
 * @author Corey
 * @since 01/02/2020
 */
public class ElnocksExchange extends Interface {
    private static final String MAGIC_BUTTERFLY_NET = "Magic butterfly net";
    private static final String JAR_GENERATOR = "Jar generator";
    private static final String IMPLING_JARS = "Three impling jars";
    private static final String ATTRIBUTE = "elnock_selected";
    private static final Object2IntOpenHashMap<String> productMapping = new Object2IntOpenHashMap<>() {
        {
            put(MAGIC_BUTTERFLY_NET, ItemId.MAGIC_BUTTERFLY_NET);
            put(JAR_GENERATOR, ItemId.JAR_GENERATOR);
            put(IMPLING_JARS, ItemId.IMPLING_JAR);
        }
    };
    private static final Int2ObjectOpenHashMap<Item[]> prices = new Int2ObjectOpenHashMap<>() {
        {
            put(ItemId.MAGIC_BUTTERFLY_NET, new Item[]{new Item(ImplingJarPlugin.ImplingJar.GOURMET.getItemId(), 3), new Item(ImplingJarPlugin.ImplingJar.EARTH.getItemId(), 2), new Item(ImplingJarPlugin.ImplingJar.ESSENCE.getItemId(), 1)});
            put(ItemId.JAR_GENERATOR, new Item[]{new Item(ImplingJarPlugin.ImplingJar.ESSENCE.getItemId(), 3), new Item(ImplingJarPlugin.ImplingJar.ECLECTIC.getItemId(), 2), new Item(ImplingJarPlugin.ImplingJar.NATURE.getItemId(), 1)});
        }
    };

    @Override
    protected void attach() {
        put(4, "Select element");
        put(7, "Confirm");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("Select element"), 0, 9, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("Confirm"), 9, 9, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Select element", (player, slotId, itemId, option) -> {
            if (slotId == 0) {
                player.sendMessage("The imps are not interested in taking your implings. There is no need to buy any repellant.");
                player.getVarManager().sendBit(3729, 0);
            } else if (slotId == 3) {
                selectOption(player, MAGIC_BUTTERFLY_NET);
            } else if (slotId == 6) {
                selectOption(player, JAR_GENERATOR);
            } else if (slotId == 9) {
                selectOption(player, IMPLING_JARS);
            }
        });
        bind("Confirm", player -> {
            final Object selected = player.getTemporaryAttributes().get(ATTRIBUTE);
            if (selected == null) {
                player.sendMessage("You need to select an option.");
                return;
            }
            final int product = productMapping.getInt(selected);
            final Item[] price = prices.get(product);
            if (product == ItemId.IMPLING_JAR) {
                if (player.getInventory().getFreeSlots() < 3) {
                    player.sendMessage("You don't have enough inventory space to purchase this.");
                    return;
                }
                final Optional<ImplingJarPlugin.ImplingJar> jar = ImplingJarPlugin.ImplingJar.jars.values().stream().filter(implingJar -> player.getInventory().containsItem(implingJar.getJarItem())).min(Comparator.comparingInt(ImplingJarPlugin.ImplingJar::ordinal));
                if (jar.isPresent()) {
                    player.getInventory().deleteItems(jar.get().getJarItem());
                    player.getInventory().addItem(new Item(ItemId.IMPLING_JAR, 3));
                    player.sendMessage("You have purchased: Impling jar x3");
                } else {
                    player.sendMessage("Your implings are not suitable to be exchanged for that.");
                }
            } else {
                if (player.getInventory().containsItems(price)) {
                    if (product == ItemId.JAR_GENERATOR && player.containsItem(ItemId.JAR_GENERATOR)) {
                        player.sendMessage("You already have one of these. Use up the rest of the charges before purchasing another.");
                        return;
                    }
                    player.getInventory().deleteItems(price);
                    if (product == ItemId.JAR_GENERATOR) {
                        player.getInventory().addItem(new Item(product, 1, 100));
                    } else {
                        player.getInventory().addItem(new Item(product));
                    }
                    player.sendMessage("You have purchased: " + ItemDefinitions.nameOf(product) + " x1");
                } else {
                    player.sendMessage("You don't have the required implings in jars to trade for this.");
                }
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ELNOCKS_EXCHANGE;
    }

    private void selectOption(final Player player, final String option) {
        final Object selected = player.getTemporaryAttributes().get(ATTRIBUTE);
        if (selected != null) {
            if (selected.equals(option)) {
                return;
            }
        }
        player.addTemporaryAttribute(ATTRIBUTE, option);
    }

    @Override
    public void close(Player player, Optional<GameInterface> replacement) {
        super.close(player, replacement);
        player.getTemporaryAttributes().remove(ATTRIBUTE);
    }
}
