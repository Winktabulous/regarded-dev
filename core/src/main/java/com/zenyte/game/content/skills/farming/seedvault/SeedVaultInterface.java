package com.zenyte.game.content.skills.farming.seedvault;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.zenyte.game.util.AccessMask.*;

@StaticInitializer
public class SeedVaultInterface extends Interface implements SwitchPlugin {
    public static final int AMOUNT_VAR = 2195;
    private static final int CATEGORY_VARBIT = 8171;
    private static final int INPUT_SCREEN_CLOSE_SCRIPT = 101;
    private static final int SEARCH_TYPE = 11;

    static {
        VarManager.appendPersistentVarp(AMOUNT_VAR);
    }

    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        final Player player = event.getPlayer();
        if (!FavouriteSlot.isSet(player)) {
            FavouriteSlot.reset(player);
            FavouriteSlot.setInitialized(player);
            player.getVarManager().sendVar(AMOUNT_VAR, 1);
        }
    }

    @Override
    protected void attach() {
        put(8, "Categories");
        put(15, "Container");
        put(19, "Withdraw-1");
        put(20, "Withdraw-5");
        put(21, "Withdraw-10");
        put(22, "Withdraw-X");
        put(23, "Withdraw-All");
        put(24, "Search");
        put(25, "Deposit-All");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        final int vaultSize = Container.getSize(ContainerType.SEED_VAULT);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Categories"), 0, 10, CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Container"), 0, vaultSize, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP6, CLICK_OP7, CLICK_OP8, CLICK_OP9, CLICK_OP10, DRAG_DEPTH2, DRAG_TARGETABLE);
        GameInterface.SEED_VAULT_INVENTORY.open(player);
        player.getVarManager().sendBit(CATEGORY_VARBIT, 0);
        final SeedVaultContainer container = player.getSeedVault().getContainer();
        container.setFullUpdate(true);
        container.refresh(player);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        player.getPacketDispatcher().sendClientScript(INPUT_SCREEN_CLOSE_SCRIPT, SEARCH_TYPE);
    }

    @Override
    protected void build() {
        bind("Categories", ((player, slotId, itemId, option) -> player.getVarManager().sendBit(CATEGORY_VARBIT, slotId)));
        bind("Container", ((player, seedSlot, itemId, optionId) -> {
            final SeedVaultExchangeOption option = SeedVaultExchangeOption.of(optionId);
            switch (option) {
            case REMOVE_ALL_PLACE: 
                player.getSeedVault().releasePlaceholders();
                break;
            case NOTE_OR_REMOVE_PLACE: 
                final ItemDefinitions def = ItemDefinitions.getOrThrow(itemId);
                if (def.isPlaceholder()) {
                    player.getSeedVault().releasePlaceholder(seedSlot);
                } else {
                    withdraw(player, seedSlot, player.getVarManager().getValue(AMOUNT_VAR), true);
                }
                break;
            case SELECTED: 
                withdraw(player, seedSlot, player.getVarManager().getValue(AMOUNT_VAR), false);
                break;
            case X: 
                player.sendInputInt("How much would you like to deposit?", amt -> withdraw(player, seedSlot, amt, false));
                break;
            case EXAMINE: 
                break;
            case FAVORITE: 
                final Optional<FavouriteSlot> favouriteSlot = FavouriteSlot.getBySeedSlot(player, seedSlot);
                if (favouriteSlot.isPresent()) {
                    unfavourite(player, favouriteSlot.get(), seedSlot);
                } else {
                    favourite(player, seedSlot);
                }
                break;
            default: 
                withdraw(player, seedSlot, option.getAmount(), false);
                break;
            }
        }));
        bind("Container", "Container", this::switchItem);
        bind("Withdraw-1", player -> player.getVarManager().sendVar(AMOUNT_VAR, 1));
        bind("Withdraw-5", player -> player.getVarManager().sendVar(AMOUNT_VAR, 5));
        bind("Withdraw-10", player -> player.getVarManager().sendVar(AMOUNT_VAR, 10));
        bind("Withdraw-X", player -> {
            //Start off by resetting it to a quantity of 1.
            player.getVarManager().sendVar(AMOUNT_VAR, 1);
            player.sendInputInt("Enter amount:", amount -> player.getVarManager().sendVar(AMOUNT_VAR, Math.max(1, amount)));
        });
        bind("Withdraw-All", player -> player.getVarManager().sendVar(AMOUNT_VAR, Integer.MAX_VALUE));
        bind("Deposit-All", player -> {
            final Container inventory = player.getInventory().getContainer();
            for (int i = 0; i < 28; i++) {
                final Item item = inventory.get(i);
                if (item == null) {
                    continue;
                }
                SeedVaultInventoryInterface.deposit(player, i, item.getAmount(), false);
            }
        });
    }

    private void switchItem(final Player player, final int fromSlot, final int toSlot) {
        // Moving from corresponding seed slot to favourite slot
        if (toSlot == Container.getSize(ContainerType.SEED_VAULT)) {
            final Optional<FavouriteSlot> existingSlot = FavouriteSlot.getBySeedSlot(player, fromSlot);
            if (existingSlot.isPresent()) {
                unfavourite(player, existingSlot.get(), fromSlot);
                return;
            }
            final Optional<FavouriteSlot> favouriteSlot = FavouriteSlot.getFreeSlot(player);
            if (favouriteSlot.isPresent()) {
                favourite(player, fromSlot);
                return;
            }
            player.sendMessage("Your favourite slots are full.");
            player.getSeedVault().getContainer().refresh(player);
            return;
        }
        final SeedVault vault = player.getSeedVault();
        final SeedVaultContainer container = vault.getContainer();
        final Item fromItem = container.get(fromSlot);
        final Item toItem = container.get(toSlot);
        if (fromItem == null || toItem == null) {
            return;
        }
        final boolean categoryMatches = isSameCategory(fromItem, toItem);
        final boolean bothFavourites = FavouriteSlot.getBySeedSlot(player, fromSlot).isPresent() && FavouriteSlot.getBySeedSlot(player, toSlot).isPresent();
        if (categoryMatches || bothFavourites) {
            vault.switchItem(fromSlot, toSlot);
        } else {
            player.sendMessage("You can only swap seeds and saplings within their category.");
        }
    }

    private void withdraw(final Player player, final int slotId, final int amount, final boolean note) {
        final SeedVaultContainer seedVault = player.getSeedVault().getContainer();
        //player.sendMessage("Not enough space in your " + container.getType().getName() + ".")
        final int inVault = seedVault.get(slotId).getAmount();
        seedVault.withdraw(player, player.getInventory().getContainer(), slotId, amount, note, true);
        final Item remaining = seedVault.get(slotId);
        if (remaining != null && inVault == remaining.getAmount()) {
            player.sendMessage("Not enough space in your inventory.");
            return;
        }
        seedVault.refresh(player);
        player.getInventory().refreshAll();
    }

    private void favourite(final Player player, final int seedSlot) {
        final Optional<FavouriteSlot> favouriteSlot = FavouriteSlot.getFreeSlot(player);
        if (!favouriteSlot.isPresent()) {
            player.sendMessage("You can only have eight types of seeds as favorites.");
            return;
        }
        final Item seed = player.getSeedVault().getContainer().get(seedSlot);
        player.sendMessage("You add " + getSeedName(seed) + " to your favourites.");
        player.getVarManager().sendBit(favouriteSlot.get().getVarbit(), seedSlot);
    }

    private void unfavourite(final Player player, final FavouriteSlot favouriteSlot, final int seedSlot) {
        final Item seed = player.getSeedVault().getContainer().get(seedSlot);
        player.getVarManager().sendBit(favouriteSlot.getVarbit(), 255);
        player.sendMessage("You remove " + getSeedName(seed) + " from your favourites.");
    }

    private String getSeedName(final Item seed) {
        final ItemDefinitions seedDef = seed.getDefinitions();
        if (seedDef.isPlaceholder()) {
            return ItemDefinitions.getOrThrow(seedDef.getPlaceholderId()).getName();
        }
        return seed.getName();
    }

    private boolean isSameCategory(final Item from, final Item to) {
        return getCategory(from) == getCategory(to);
    }

    private int getCategory(@NotNull final Item item) {
        final String categoryString = getCategoryString(item).orElse("NAN");
        if (!NumberUtils.isDigits(categoryString)) {
            return -1;
        }
        return Integer.parseInt(categoryString);
    }

    public static Optional<String> getCategoryString(@NotNull final Item item) {
        final ItemDefinitions definitions = ItemDefinitions.getOrThrow(item.getDefinitions().getUnnotedOrDefault());
        final Int2ObjectMap<Object> params = definitions.getParameters();
        if (params == null) {
            return Optional.empty();
        }
        final Object category = params.get(709);
        if (category == null) {
            return Optional.empty();
        }
        return Optional.of(category.toString());
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SEED_VAULT;
    }
}
