package com.zenyte.game.content;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.plugins.events.InitializationEvent;
import mgi.types.config.enums.Enums;

import java.util.Optional;

/**
 * @author Kris | 01/11/2018 14:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemRetrievalService {

    public enum RetrievalServiceType {
        ZUL_GWENWYNIG(0, 0),
        PETRIFIED_PETE(1, 2),
        MAGICAL_CHEST(3, 4),
        TORFINN(5, 6),
        LUNAR(7, 8),
        LOW_COST_THEATRE_OF_BLOOD(9, 10),
        THEATRE_OF_BLOOD(11, 12),
        ORRVOR_QUO_MATEN(13, 14),
        ARNO(15, 16),
        MIMIC(17, 18),
        NIGHTMARE(25, 26),
        PHOSANI_NIGHTMARE(27, 28),
        GODWARS(100, 101),
        HAGAVIK(102, 103),
        ROTS(104, 105),
        GRAVESTONE(34, 35),
        ANCIENT_PRISON(36, 37),
        TOMBS_OF_AMASCUT(39, 38)
        ;
        private final int lockedEnumIndex;
        private final int unlockedEnumIndex;

        public boolean isFree() {
            return lockedEnumIndex == unlockedEnumIndex;
        }

        public int getCost() {
            return Enums.ITEM_RETRIEVAL_SERVICE.getValue(lockedEnumIndex).orElseThrow(Enums.exception());
        }

        RetrievalServiceType(int lockedEnumIndex, int unlockedEnumIndex) {
            this.lockedEnumIndex = lockedEnumIndex;
            this.unlockedEnumIndex = unlockedEnumIndex;
        }

        public int getLockedEnumIndex() {
            return lockedEnumIndex;
        }

        public int getUnlockedEnumIndex() {
            return unlockedEnumIndex;
        }
    }

    private static final VarCollection[] varps = new VarCollection[] {VarCollection.ZULRAH_RECLAIM, VarCollection.VORKATH_RECLAIM};

    public static final void updateVarps(final Player player) {
        for (final VarCollection varp : varps) {
            varp.updateSingle(player);
        }
    }

    public static final int TYPE_VAR = 261;

    public ItemRetrievalService(final Player player) {
        this.player = player;
        this.container = new Container(ContainerPolicy.NORMAL, ContainerType.ITEM_RETRIEVAL_SERVICE, Optional.of(player));
    }

    private final transient Player player;
    private RetrievalServiceType type;
    private boolean locked;
    private final Container container;

    @Subscribe
    public static final void onInit(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final ItemRetrievalService service = player.getRetrievalService();
        if (savedPlayer == null) {
            return;
        }
        final ItemRetrievalService savedService = savedPlayer.getRetrievalService();
        if (savedService == null) return;
        service.type = savedService.type;
        service.locked = savedService.locked;
        service.container.setContainer(savedService.container);
    }

    public boolean is(final RetrievalServiceType type) {
        return this.type == type && !container.isEmpty();
    }

    public ItemRetrievalService(Player player, Container container) {
        this.player = player;
        this.container = container;
    }

    public RetrievalServiceType getType() {
        return type;
    }

    public void setType(RetrievalServiceType type) {
        this.type = type;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Container getContainer() {
        return container;
    }
}
