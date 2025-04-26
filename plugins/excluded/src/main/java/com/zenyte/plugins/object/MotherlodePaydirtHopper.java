package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.motherlode.PaydirtNPC;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.plugins.dialogue.ItemChat;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class MotherlodePaydirtHopper implements ObjectAction {

    public static final Item PAYDIRT_ITEM = new Item(12011);

    private static final int PAYDIRT_NPC = 6564;

    private static final Location START = new Location(3748, 5671, 0);

    private static final Location northernHopper = new Location(3748, 5673, 0);

    private static final Location easternHopper = new Location(3749, 5672, 0);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(player.getLocation().getDistance(easternHopper) < player.getLocation().getDistance(northernHopper) ? easternHopper : northernHopper), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getInventory().containsItem(PAYDIRT_ITEM)) {
            player.getDialogueManager().start(new ItemChat(player, PAYDIRT_ITEM, "You don\'t have any pay-dirt to put in the hopper."));
            return;
        }
        final MutableInt conveyorAmount = new MutableInt();
        CharacterLoop.forEach(object, 15, NPC.class, npc -> {
            if (npc instanceof PaydirtNPC) {
                final Player target = ((PaydirtNPC) npc).getPlayer();
                if (target != null && !target.isNulled() && target.getUsername().equalsIgnoreCase(player.getUsername())) {
                    conveyorAmount.add(((PaydirtNPC) npc).getPaydirt().size());
                }
            }
        });
        if (conveyorAmount.intValue() > 0) {
            player.getDialogueManager().start(new ItemChat(player, PAYDIRT_ITEM, "You\'ve already got some pay-dirt in the machine.<br>You can put more in once the last batch comes out."));
            return;
        }
        final int MAX_AMOUNT = (player.getBooleanAttribute("motherlode_sack_upgrade") ? 162 : 81);
        int paydirt = player.getInventory().getAmountOf(PAYDIRT_ITEM.getId());
        final int sackAmount = player.getPaydirt().size();
        if (sackAmount + conveyorAmount.intValue() >= MAX_AMOUNT) {
            player.getDialogueManager().start(new ItemChat(player, PAYDIRT_ITEM, "You can\'t fit any more pay-dirt into the sack! Try emptying the sack first."));
            return;
        }
        final ArrayList<Item> list = new ArrayList<Item>();
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null || item.getId() != PAYDIRT_ITEM.getId()) {
                continue;
            }
            list.add(item);
            inventory.deleteItem(i, item);
            if (--paydirt <= 0) {
                break;
            }
        }
        World.spawnNPC(new PaydirtNPC(PAYDIRT_NPC, START, Direction.SOUTH, 0, player, list));
    }

    /**
     * Paydirt in sack management
     */
    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HOPPER_26674 };
    }
}
