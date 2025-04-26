package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.HintArrowPosition;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import com.zenyte.plugins.item.LightSourceItem;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * @author Kris | 27. aug 2018 : 23:31:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class FaladorMoleLairArea extends PolygonRegionArea implements EquipPlugin, LootBroadcastPlugin {
    private static final Location MIDDLE = new Location(1761, 5185, 0);
    public static final int[] shields = {DiaryReward.FALADOR_SHIELD3.getItem().getId(), DiaryReward.FALADOR_SHIELD4.getItem().getId()};

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1722, 5252}, {1722, 5115}, {1799, 5115}, {1799, 5252}}, 0)};
    }

    @Override
    public void enter(final Player player) {
        locateMole(player);
    }

    private void locateMole(final Player player) {
        final List<NPC> npcs = CharacterLoop.find(MIDDLE, 75, NPC.class, n -> n.getName(player).equalsIgnoreCase("giant mole"));
        if (!npcs.isEmpty()) {
            final NPC mole = npcs.get(0);
            final Location middle = mole.getMiddleLocation();
            if (player.getInventory().containsAnyOf(shields) || player.getEquipment().containsAnyOf(shields)) {
                player.getPacketDispatcher().sendHintArrow(new HintArrow(middle.getX(), middle.getY(), (byte) 100, HintArrowPosition.EAST));
            } else {
                player.getPacketDispatcher().resetHintArrow();
            }
        }
    }

    @Override
    public void leave(final Player player, boolean logout) {
        player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
        player.getPacketDispatcher().resetHintArrow();
    }

    @Override
    public String name() {
        return "Falador Mole Lair";
    }


    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[0];
    }
}
