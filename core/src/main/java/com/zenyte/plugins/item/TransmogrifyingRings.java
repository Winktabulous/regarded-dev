package com.zenyte.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Corey
 * @since 15/11/18
 */
public class TransmogrifyingRings implements EquipPlugin {
    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        if (player.getDuel() != null && player.getDuel().inDuel()) {
            player.sendMessage("You can't do this during a duel.");
            return false;
        }
        if (player.getTemporaryAttributes().get("greegree") != null) {
            player.sendMessage("You can't do that as a monkey!");
            return false;
        }
        final TransmogrifyingRings.TransmogrifyRing ring = TransmogrifyRing.RINGS.get(item.getId());
        if (ring == null) {
            return false;
        }
        player.getInterfaceHandler().closeInterfaces();
        player.getAppearance().transform(ring.getNpcId());
        player.getInterfaceHandler().sendInterface(GameInterface.UNMORPH_TAB);
        player.resetWalkSteps();
        player.getPacketDispatcher().resetMapFlag();
        player.getAppearance().setRenderAnimation(new RenderAnimation(-1, -1, -1, -1, -1, -1, -1));
        player.setAnimation(Animation.STOP);
        return false;
    }

    @Override
    public int[] getItems() {
        return TransmogrifyRing.RINGS.keySet().toIntArray();
    }


    public enum TransmogrifyRing {
        EASTER_RING(7927, () -> {
            final int[] easterEggs = new int[] {5538, 5539, 5540, 5541, 5542, 5543};
            return easterEggs[Utils.random(0, easterEggs.length - 1)];
        }), RING_OF_STONE(6583, 2188), RING_OF_NATURE(20005, 7314), RING_OF_COINS(20017, 7315);
        public static final TransmogrifyRing[] VALUES = values();
        public static final Int2ObjectOpenHashMap<TransmogrifyRing> RINGS = new Int2ObjectOpenHashMap<>(VALUES.length);

        static {
            for (final TransmogrifyingRings.TransmogrifyRing ring : VALUES) {
                RINGS.put(ring.getItemId(), ring);
            }
        }

        private final int itemId;
        private final TransmogrifyTarget target;

        TransmogrifyRing(int itemId, int npcId) {
            this.itemId = itemId;
            this.target = () -> npcId;
        }

        TransmogrifyRing(int itemId, TransmogrifyTarget target) {
            this.itemId = itemId;
            this.target = target;
        }

        public final int getNpcId() {
            return target.getNpcId();
        }


        private interface TransmogrifyTarget {
            int getNpcId();
        }

        public int getItemId() {
            return itemId;
        }
    }
}
