package com.zenyte.game.content.gravestones;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.gravestone.GravestoneExt;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.plugins.events.LoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Kris | 13/06/2022
 */
public class Gravestone {
    private transient final Player player;
    private transient GravestoneNPC gravestoneNPC;
    private transient int interval = 0;
    private Location gravestoneLocation;

    public Container getContainer() {
        return container;
    }

    private final Container container;

    public long getCoinsInCoffer() {
        return coinsInCoffer;
    }

    public void setCoinsInCoffer(long coinsInCoffer) {
        this.coinsInCoffer = coinsInCoffer;
    }

    private long coinsInCoffer;

    public int getGravestoneLocationBitpacked() {
        if (gravestoneLocation == null || gravestoneNPC == null) return 0;
        return gravestoneLocation.getPositionHash();
    }

    public Gravestone(final Player player) {
        this.player = player;
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.DEATHS_OFFICE_RETRIEVAL, Optional.of(player));
    }

    static {
        VarManager.appendPersistentVarbit(10465);
        VarManager.appendPersistentVarbit(10467);
    }

    @Subscribe
    public static void onInitialization(@NotNull final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final Gravestone gravestone = savedPlayer.getGravestone();
        if (gravestone == null) {
            return;
        }
        player.getGravestone().gravestoneLocation = gravestone.gravestoneLocation;
        if (gravestone.container != null) player.getGravestone().container.setContainer(gravestone.container);
        player.getGravestone().coinsInCoffer = gravestone.coinsInCoffer;
    }

    @Subscribe
    public static void onLogin(@NotNull final LoginEvent event) {
        final Player player = event.getPlayer();
        final Gravestone gravestone = player.getGravestone();
        if (player.getVarManager().getBitValue(10465) > 0) {
            gravestone.reinstateGravestone();
        }
    }

    public void createGravestone(Location location, final Collection<Item> items) {
        if (items.isEmpty()) return;
        removeGravestone();
        gravestoneLocation = location.copy();
        player.getVarManager().sendBitInstant(10465, 1500);
        reinstateGravestone();
    }

    private void reinstateGravestone() {
        final int slot = GravestoneNPC.allocateGravestone();
        gravestoneNPC = new GravestoneNPC(player.getVarManager().getBitValue(10467), slot, gravestoneLocation);
        gravestoneNPC.spawn();
        player.getPacketDispatcher().sendHintArrow(new HintArrow((gravestoneNPC)));
        player.getVarManager().sendBitInstant(10464, slot + 1);
    }

    public void removeGravestone() {
        if (gravestoneNPC == null) return;
        gravestoneNPC.finish();
        gravestoneNPC = null;
        player.getPacketDispatcher().resetHintArrow();
        player.getVarManager().sendBitInstant(10464, 0);
        player.getVarManager().sendBitInstant(10465, 0);
        if (player.getRetrievalService().getContainer().isEmpty()) return;
        player.sendMessage("<col=ef1020>Your gravestone has expired.</col> Items held there can now be reclaimed at <col=ef1020>Death's Office</col>, accessed from near the respawn points.");
        GravestoneExt.INSTANCE.moveItemsToDeathsOffice(player);
    }

    public void process() {
        int remainingTicks = player.getVarManager().getBitValue(10465);
        if (remainingTicks == 0) return;
        if (++interval % 3 != 0) return;
        final int remaining = Math.max(remainingTicks - 3, 0);
        player.getVarManager().sendBitInstant(10465, remaining);
        if (remaining == 0) {
            removeGravestone();
        }
    }
}
