package com.zenyte.game.content.flowerpoker;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.StringFormatUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class FlowerPokerManager {

    public static FlowerPokerManager get(Player player) {
        return (FlowerPokerManager) player.getTemporaryAttributes().computeIfAbsent(":content:flowerpoker:fpm",
                (Function<Object, FlowerPokerManager>) o -> new FlowerPokerManager(player));
    }

    public static final Map<FlowerPokerAreas, FlowerPokerSession> FLOWER_POKER_AREAS = new HashMap<>();

    static {
        for (FlowerPokerAreas area : FlowerPokerAreas.VALUES) {
            FLOWER_POKER_AREAS.put(area, null);
        }
    }

    public static FlowerPokerAreas getAvailableArea() {
        ObjectArrayList<FlowerPokerAreas> available = new ObjectArrayList<>();
        FLOWER_POKER_AREAS.forEach((a, s) -> {
            if(s == null)
                available.add(a);
        });
        if(available.isEmpty())
            return null;

        return available.get(0);
    }

    public static final int SIZE = 28;
    public static final int INTERFACE = 335;
    public static final int SECOND_INTERFACE = 334;
    public static final int INVENTORY_INTERFACE = 336;

    public static boolean FLOWER_POKER_ENABLED = true;

    public final Player player;

    public Player requested;

    public FlowerPokerManager(Player player) {
        this.player = player;
    }

    public boolean accepted;

    public boolean started;

    public FlowerPokerSession session;

    public Container staked_items;

    public boolean inInterface;

    public void request(Player o) {
        if (!FLOWER_POKER_ENABLED) {
            player.sendMessage("Flower poker is currently disabled.");
            return;
        }

        if (TempPlayerStatePlugin.enableTempState(player, TempPlayerStatePlugin.StateType.INVENTORY)) {
            player.sendMessage("Cannot flower poker right now.");
            return;
        }

        if(GambleBan.isGambleBanValid(player)) {
            player.sendMessage("You are currently gamble banned and cannot gamble.");
            return;
        }
        if(GambleBan.isGambleBanValid(o)) {
            player.sendMessage("They are currently gamble banned and cannot gamble.");
            return;
        }

        if (player.isIronman() && (!player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR) && !o.hasPrivilege(PlayerPrivilege.ADMINISTRATOR))) {
            player.sendMessage("You're an Iron Man. You stand alone.");
            return;
        }

        if (World.isUpdating() && World.getUpdateTimer() < TimeUnit.MINUTES.toTicks(3)) {
            player.sendMessage("The server is updating soon, please try this after reboot.");
        }

        if (o.isIronman() && (!player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR) && !o.hasPrivilege(PlayerPrivilege.ADMINISTRATOR))) {
            player.sendMessage(o.getName() + " is an Iron Man. He stands alone.");
            return;
        }

        if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
            player.sendMessage("You can't do this now!");
            return;
        }

        if (o.isLocked() || o.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
            player.sendMessage("Other player is busy at the moment.");
            return;
        }

        if (!player.getInventory().containsItem(299, 5)) {
            player.sendMessage("You don't have enough mithril seeds to start fp.");
            return;
        }

        if (!o.getInventory().containsItem(299, 5)) {
            player.sendMessage("The other player doesn't have enough mithril seeds to start fp.");
            return;
        }

//        if (!FlowerPokerArea.correctPosition(player, o)) {
//            player.sendMessage("Both players must be standing on the right position of the arena in order to flower " +
//                    "poker!");
//            return;
//        }

        requested = o;

        if (get(o).requested != null && get(o).requested.getName().equals(player.getName())) {
            player.getInterfaceHandler().closeInterfaces();
            o.getInterfaceHandler().closeInterfaces();
            openFPScreen();
            get(o).openFPScreen();
            return;
        }
        o.sendMessage("You have received a flower poker request from " + player.getName(), MessageType.GAMBLE_REQUEST, player.getUsername());
        player.sendMessage("You have sent a flower poker request to " + o.getName());
    }

    public void openFPScreen() {
        if (!FLOWER_POKER_ENABLED) {
            player.sendMessage("Flower poker is currently disabled.");
            return;
        }
        inInterface = true;
        staked_items = new Container(ContainerPolicy.NORMAL, ContainerType.TRADE, Optional.of(player));
        player.stopAll();
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE);
        player.getInterfaceHandler().sendInterface(InterfacePosition.SINGLE_TAB, INVENTORY_INTERFACE);
        sendDefault();
        refreshStake();
        refreshMessages(0);
    }

    private void sendDefault() {
        player.getPacketDispatcher().sendComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, AccessMask.CLICK_OP1,
                AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5,
                AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendComponentSettings(INTERFACE, 25, 0, 27, AccessMask.CLICK_OP1,
                AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5,
                AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendComponentSettings(INTERFACE, 28, 0, 27, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendClientScript(149, 22020096, 93, 4, 7, 0, -1, "Offer<col=ff9040>", "Offer-5" +
                "<col=ff9040>", "Offer-10<col=ff9040>", "Offer-All<col=ff9040>", "Offer-X<col=ff9040>");
        player.getPacketDispatcher().sendClientScript(1217, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        player.getPacketDispatcher().sendClientScript(1216, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        requested.getPacketDispatcher().sendComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, AccessMask.CLICK_OP1,
                AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5,
                AccessMask.CLICK_OP10);
        requested.getPacketDispatcher().sendComponentSettings(INTERFACE, 25, 0, 27, AccessMask.CLICK_OP1,
                AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5,
                AccessMask.CLICK_OP10);
        requested.getPacketDispatcher().sendComponentSettings(INTERFACE, 28, 0, 27, AccessMask.CLICK_OP10);
        requested.getPacketDispatcher().sendClientScript(149, 22020096, 93, 4, 7, 0, -1, "Offer<col=ff9040>", "Offer" +
                "-5<col=ff9040>", "Offer-10<col=ff9040>", "Offer-All<col=ff9040>", "Offer-X<col=ff9040>");
        requested.getPacketDispatcher().sendClientScript(1217, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
                , -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        requested.getPacketDispatcher().sendClientScript(1216, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
                , -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
    }

    private void refreshStake() {
        player.getPacketDispatcher().sendUpdateItemContainer(staked_items);
        requested.getPacketDispatcher().sendUpdateItemContainer(90, -2, 0, staked_items);
    }

    private void refreshMessages(final int stage) {
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 30 : 4
                , getStatusMessage(stage));
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 31 :
                30, stage <= 1 ? "Gambling with: " + requested.getPlayerInformation().getDisplayname() : "Gambling " +
                "with:<br>" + requested.getPlayerInformation().getDisplayname());
        if (stage <= 1) {
            player.getPacketDispatcher().sendComponentText(INTERFACE, 9,
                    requested.getPlayerInformation().getDisplayname() + " has " + requested.getInventory().getFreeSlots() + "<br> free inventory slots.");
        }
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 24 :
                23, stage <= 1 ? "You offer to stake:<br>(Value: <col=ffffff>" + (isLots(getValue(player)) ? "Lots" +
                "!</col>)" : StringFormatUtil.format(getValue(player)) + "</col> coins)") : "You are about to stake:<br>(Value: " +
                "<col=ffffff>" + (isLots(getValue(player)) ? "Lots!</col>)" : StringFormatUtil.format(getValue(player)) + " " +
                "coins)"));
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 27 :
                24, stage <= 1 ? requested.getPlayerInformation().getDisplayname() + " offers to stake:<br>(Value: " +
                "<col=ffffff>" + (isLots(getValue(requested)) ? "Lots!</col>)" : StringFormatUtil.format(getValue(requested)) +
                "</col> coins)") :
                "In return you will offer to stake:<br>(Value: <col=ffffff>" + (isLots(getValue(player)) ? "Lots" +
                        "!</col>)" : StringFormatUtil.format(getValue(requested)) + " coins)"));
    }

    private String getStatusMessage(final int stage) {
        if (accepted) {
            return "Waiting for other player...";
        } else if (get(requested).accepted) {
            return "Other player has accepted.";
        }
        return stage <= 1 ? "" : "Are you sure you want to make this stake?";
    }

    public void accept(final int stage) {
        if (!FLOWER_POKER_ENABLED) {
            player.sendMessage("Flower poker is currently disabled.");
            return;
        }

        if (!isStaking()) {
            return;
        } else if (get(requested).accepted) {
            if (stage == 1) {
                for (final Item item : get(requested).staked_items.getItems().values()) {
                    if (player.getInventory().getAmountOf(item.getId()) + item.getAmount() < 0) {
                        player.sendMessage("You are holding too many of the same item to continue this flower poker " +
                                "game.");
                        player.getInterfaceHandler().closeInterfaces();
                        inInterface = false;
                        get(requested).inInterface = false;
                        return;
                    }
                }
                for (final Item item : staked_items.getItems().values()) {
                    if (requested.getInventory().getAmountOf(item.getId()) + item.getAmount() < 0) {
                        requested.sendMessage("You are holding too many of the same item to continue this flower " +
                                "poker game.");
                        requested.getInterfaceHandler().closeInterfaces();
                        inInterface = false;
                        get(requested).inInterface = false;
                        return;
                    }
                }
                if (nextStage()) {
                    get(requested).nextStage();
                }
            } else {
                started = true;
                get(requested).started = true;
                closeStake(FlowerPokerStatus.SUCCESS);
                //player.setCloseInterfacesEvent(null);
                player.getInterfaceHandler().closeInterfaces();
            }
            return;
        }
        accepted = true;
        refreshMessages(stage);
        get(requested).refreshMessages(stage);
    }

    private boolean nextStage() {
        if (!isStaking()) {
            return false;
        } else if (player.getInventory().getContainer().getItems().size() + (SIZE - get(requested).staked_items.getFreeSlotsSize()) > 28) {
            //player.setCloseInterfacesEvent(null);
            closeStake(FlowerPokerStatus.NO_SPACE);
            player.getInterfaceHandler().closeInterfaces();
            return false;
        }
        accepted = false;
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, SECOND_INTERFACE);
        player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
        refreshMessages(2);
        get(requested).refreshMessages(2);
        return true;
    }

    public void closeStake(final FlowerPokerStatus stage) {
        if (!isStaking() || !get(requested).isStaking()) {
            return;
        }
        final com.zenyte.game.world.entity.player.Player partner = this.requested;
        inInterface = false;
        get(partner).inInterface = false;
        if (!started && !stage.equals(FlowerPokerStatus.SUCCESS)) {
            reset(false);
            get(partner).reset(false);
        }
        //partner.setCloseInterfacesEvent(null);
        partner.getInterfaceHandler().closeInterfaces();
        if (!started && !stage.equals(FlowerPokerStatus.SUCCESS)) {
            player.log(LogLevel.INFO,
                    "Declined flower poker game of items: \nPlayer items: " + staked_items.getItems() + "\nPartner " +
                            "items: " + get(partner).staked_items.getItems());
            for (final Item item : staked_items.getItems().values()) {
                if (item == null) {
                    continue;
                }
                player.getInventory().addOrDrop(item);
            }
            for (final Item item : get(partner).staked_items.getItems().values()) {
                if (item == null) {
                    continue;
                }
                partner.getInventory().addOrDrop(item);
            }
            get(partner).staked_items.clear();
            staked_items.clear();
            player.sendMessage("Gamble declined.");
            partner.sendMessage("Gamble declined.");
        } else if (!player.getActionManager().hasSkillWorking()) {
            FlowerPokerAreas availableArea = getAvailableArea();

            if(availableArea == null) {
                player.sendMessage("There are currently no flower poker lanes available, please try again later.");
                requested.sendMessage("There are currently no flower poker lanes available, please try again later.");
                for (final Item item : staked_items.getItems().values()) {
                    if (item == null) {
                        continue;
                    }
                    player.getInventory().addOrDrop(item);
                }
                for (final Item item : get(partner).staked_items.getItems().values()) {
                    if (item == null) {
                        continue;
                    }
                    partner.getInventory().addOrDrop(item);
                }
                get(partner).staked_items.clear();
                staked_items.clear();
                reset(false);
                return;
            }
            session = new FlowerPokerSession(player, requested, availableArea, staked_items, get(requested).staked_items);
            get(requested).session = this.session;
            FLOWER_POKER_AREAS.replace(session.arena, session);
//            final it.unimi.dsi.fastutil.objects.ObjectCollection<Item> givenValues =
//            staked_items.getItems().values();
//            final it.unimi.dsi.fastutil.objects.ObjectCollection<Item> receivedValues =
//            get(partner).staked_items.getItems().values();
//            player.log(LogLevel.INFO, "Accepted flower poker game of items: \nPlayer items: " + staked_items
//            .getItems() + "\nPartner items: " + get(partner).staked_items.getItems());
//            final List<TradedItem> given = new ArrayList<>();
//            final List<TradedItem> received = new ArrayList<>();
//            for (final Item item : givenValues) {
//                if (item == null) continue;
//                partner.getInventory().addOrDrop(item);
//                given.add(new TradedItem(item.getId(), item.getAmount(), item.getName()));
//            }
//            for (final Item item : receivedValues) {
//                if (item == null) continue;
//                player.getInventory().addOrDrop(item);
//                received.add(new TradedItem(item.getId(), item.getAmount(), item.getName()));
//            }
//            // api trade logging
//            if (Constants.WORLD_PROFILE.getApi().isEnabled() && !Constants.WORLD_PROFILE.isPrivate() && !Constants
//            .WORLD_PROFILE.isDevelopment() && !Constants.WORLD_PROFILE.isBeta()) {
//                CoresManager.getServiceProvider().submit(() -> new SubmitTradeLog(new TradeLog(player.getUsername()
//                , player.getIP(), given, partner.getUsername(), partner.getIP(), received, Constants.WORLD_PROFILE
//                .getNumber())).execute());
//            }
            player.sendMessage("Accepted flower poker game.");
            partner.sendMessage("Accepted flower poker game.");

            player.lock();
            requested.lock();
            reached = 0;

            player.setRouteEvent(new TileEvent(player, new TileStrategy(availableArea.getOne()), () -> {
                reached++;
                startGame();
            }));
            requested.setRouteEvent(new TileEvent(requested, new TileStrategy(availableArea.getTwo()), () -> {
                reached++;
                startGame();
            }));

        }
    }

    private int reached = 0;

    public void startGame() {
        if (!FLOWER_POKER_ENABLED) {
            player.sendMessage("Flower poker is currently disabled.");
            return;
        }
        if(reached >= 2) {
            player.addWalkSteps(session.arena.getDirection(), 1, 1, false);
            requested.addWalkSteps(session.arena.getDirection(), 1, 1, false);
            FlowerPokerPlanting flowerPokerPlanting = new FlowerPokerPlanting(player, requested, session);
            session.planting = flowerPokerPlanting;
            WorldTasksManager.schedule(flowerPokerPlanting, 1, 1);
            player.putBooleanTemporaryAttribute("gambling", true);
            requested.putBooleanTemporaryAttribute("gambling", true);
            player.setForceTalk("Total pot:"+formatPot(getTotalPot()));
            requested.setForceTalk("Total pot:"+formatPot(getTotalPot()));
        }
    }

    private void cancelAccepted() {
        if (accepted) {
            accepted = false;
        } else if (get(requested).accepted) {
            get(requested).accepted = false;
        }
        refreshMessages(1);
        get(requested).refreshMessages(1);
    }

    private long getTotalPot() {
        return (long) getValue(player) + (long) getValue(requested);
    }

    public static String formatPot(long amount) {
        String format = "Too high!";
        if (amount >= 0 && amount < 1000) {
            format = String.valueOf(amount);
        } else if (amount >= 1000 && amount < 1000000) {
            format = amount / 1000 + "K";
        } else if (amount >= 1000000 && amount < 1000000000L) {
            format = amount / 1000000 + "M";
        } else if (amount >= 1000000000L && amount < 1000000000000L) {
            format = amount / 1000000000 + "B";
        } else if (amount >= 1000000000000L && amount < 10000000000000000L) {
            format = amount / 1000000000000L + "T";
        } else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
            format = amount / 1000000000000000L + "QD";
        } else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
            format = amount / 1000000000000000000L + "QT";
        }
        return format;
    }

    private int getValue(final Player player) {
        final com.zenyte.game.world.entity.player.container.Container container =
                player.getIndex() == this.player.getIndex() ? this.staked_items : get(requested).staked_items;
        if (player == null || container == null) {
            return 0;
        }
        int value = 0;
        for (final Item item : container.getItems().values()) {
            if (item == null || (value == Integer.MAX_VALUE || value < 0)) {
                continue;
            }
            value += item.getAmount() * item.getSellPrice();
        }
        return value;
    }

    private boolean isLots(final int value) {
        return value < 0;
    }

    public boolean isStaking() {
        return requested != null;
    }

    public void reset(boolean removeFlowers) {
        requested = null;
        accepted = false;
        started = false;
        if (removeFlowers) {
            session.removeFlowers();
        }
    }

    public void reset() {
        reset(true);
    }

    public void addItem(final int slot, final int amount) {
        if (!isStaking()) {
            return;
        }
        Item item = player.getInventory().getItem(slot);
        if (item == null) {
            return;
        }
        if (!item.isTradable()) {
            player.sendMessage("This item is untradeable.");
            return;
        }
        if (amount < player.getInventory().getAmountOf(item.getId())) {
            item = new Item(item.getId(), amount, item.getAttributes());
        } else {
            item = new Item(item.getId(), player.getInventory().getAmountOf(item.getId()), item.getAttributes());
        }
        if (!player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR) && !this.requested.hasPrivilege(PlayerPrivilege.ADMINISTRATOR) && !item.isTradable()) {
            player.sendMessage("You can't stake that item.");
            return;
        }
        player.log(LogLevel.INFO, "Adding item '" + item + "' into a flower poker game.");
        staked_items.add(item);
        player.getInventory().deleteItem(item);
        refreshStake();
        refreshMessages(1);
        get(requested).refreshMessages(1);
        cancelAccepted();
    }

    public void removeItem(final int slot, final int amount) {
        if (!isStaking()) {
            return;
        }
        Item item = staked_items.get(slot);
        if (item == null) {
            return;
        }
        if (amount < staked_items.getAmountOf(item.getId())) {
            item = new Item(item.getId(), amount, item.getAttributes());
        } else {
            item = new Item(item.getId(), staked_items.getAmountOf(item.getId()), item.getAttributes());
        }

        if (!player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR) && !this.requested.hasPrivilege(PlayerPrivilege.ADMINISTRATOR) && !item.isTradable()) {
            player.sendMessage("You can't stake that item.");
            return;
        }

        player.log(LogLevel.INFO, "Removing item '" + item + "' from a flower poker game.");
        staked_items.remove(item);
        player.getInventory().addItem(item);
        refreshStake();
        refreshMessages(1);
        get(requested).refreshMessages(1);
        cancelAccepted();
        for (final java.lang.Integer i : staked_items.getModifiedSlots()) {
            player.getPacketDispatcher().sendClientScript(765, 0, i);
            requested.getPacketDispatcher().sendClientScript(765, 1, i);
        }
    }
}
