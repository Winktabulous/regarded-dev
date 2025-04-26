package com.zenyte.game.content.treasuretrails.stash;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.IntEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.IntConsumer;

/**
 * @author Kris | 28/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StashUnitInterface extends Interface {
    private static final int HIDEY_BUILD_SCRIPT = 1475;

    @Override
    protected void attach() {
    }

    private final void iterateEnums(@NotNull final IntEnum intEnum, @NotNull final Stash stash, @NotNull final IntConsumer consumer) {
        for (final Int2IntMap.Entry entry : intEnum.getValues().int2IntEntrySet()) {
            final int objectId = entry.getIntKey();
            final int coordGrid = entry.getIntValue();
            final StashUnit unit = StashUnit.getMap().get(objectId);
            if (unit == null) {
                throw new IllegalStateException("Stash unit not located for object " + objectId + "!");
            }
            if (stash.isFilled(unit)) {
                consumer.accept(coordGrid);
            }
        }
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final Stash stash = player.getStash();
        final IntEnum containerEnum = Enums.STASH_UNIT_BUILD_STAGES_CONTAINER;
        final Container container = new Container(ContainerPolicy.NEVER_STACK, ContainerType.STASH_UNIT_BUILD_STAGES, Optional.empty());
        container.setFullUpdate(true);
        iterateEnums(containerEnum, stash, coordGrid -> container.set(((coordGrid >> 14) & 16383), new Item(0)));
        player.getPacketDispatcher().sendUpdateItemContainer(container);
        final int[] arguments = new int[3];
        final IntEnum varsEnum = Enums.STASH_UNIT_BUILD_STAGES_VARS;
        iterateEnums(varsEnum, stash, coordGrid -> arguments[((coordGrid >> 14) & 16383) - 1] |= 1 << (coordGrid & 16383));
        //Exception in CS2.
        if (stash.isFilled(StashUnit.TOP_WATCHTOWER)) {
            arguments[2] |= 1 << 10;
        }
        player.getPacketDispatcher().sendClientScript(HIDEY_BUILD_SCRIPT, arguments[0], arguments[1], arguments[2]);
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.STASH_UNIT;
    }
}
