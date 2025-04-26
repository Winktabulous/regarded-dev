package com.zenyte.plugins.object;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.challenges.KeyRequest;
import com.zenyte.game.content.treasuretrails.clues.CrypticClue;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.util.Optional;

/**
 * @author Kris | 12/04/2019 15:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClosedChestObject extends SearchableClueObjectPlugin implements ObjectAction {

    public static final Animation openAnimation = new Animation(832);
    public static final Int2IntOpenHashMap map = new Int2IntOpenHashMap();

    static {
        map.put(ObjectId.CLOSED_CHEST, ObjectId.OPEN_CHEST);
        map.put(ObjectId.CLOSED_CHEST_375, ObjectId.OPEN_CHEST_378);
        map.put(ObjectId.CLOSED_CHEST_376, ObjectId.OPEN_CHEST_378);
        map.put(ObjectId.CHEST_1994, ObjectId.CHEST_1995);
        map.put(ObjectId.CLOSED_CHEST_2436, 2437);
        map.put(ObjectId.CLOSED_CHEST_5108, ObjectId.OPEN_CHEST_5109);
        map.put(ObjectId.CLOSED_CHEST_7350, ObjectId.OPEN_CHEST_7351);
        map.put(ObjectId.CLOSED_CHEST_8797, ObjectId.OPEN_CHEST_8798);
        map.put(ObjectId.CLOSED_CHEST_9754, ObjectId.OPEN_CHEST_9755);
        map.put(ObjectId.CLOSED_CHEST_9756, ObjectId.OPEN_CHEST_9757);
        map.put(ObjectId.CLOSED_CHEST_9760, ObjectId.OPEN_CHEST_9761);
        map.put(ObjectId.CLOSED_CHEST_12120, ObjectId.OPEN_CHEST_12121);
        map.put(ObjectId.CLOSED_CHEST_12735, ObjectId.OPEN_CHEST_12736);
        map.put(ObjectId.CLOSED_CHEST_12768, ObjectId.OPEN_CHEST_12769);
        map.put(ObjectId.CLOSED_CHEST_16116, ObjectId.OPEN_CHEST_16117);
        map.put(ObjectId.CLOSED_CHEST_16118, ObjectId.OPEN_CHEST_16119);
        map.put(ObjectId.CLOSED_CHEST_25592, ObjectId.OPEN_CHEST_25593);
        map.put(ObjectId.CLOSED_CHEST_25793, ObjectId.OPEN_CHEST_25794);
        map.put(ObjectId.CLOSED_CHEST_31987, ObjectId.OPEN_CHEST_31988);
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Open")) {
            final Optional<CrypticClue> clue = CrypticClue.getClueFromKeyObject(object);
            if (clue.isPresent()) {
                final int k = ((KeyRequest) clue.get().getChallenge()).getKeyId();
                if (!player.getInventory().containsItem(k, 1)) {
                    player.sendMessage("The chest is firmly shut.");
                    return;
                }
            }
            player.sendSound(52);
            player.setAnimation(openAnimation);
            player.lock(1);
            player.sendMessage("You open the chest...");
            clue.ifPresent(k -> player.getInventory().deleteItem(((KeyRequest) k.getChallenge()).getKeyId(), 1));
            swapObject(object);
        } else if (option.equalsIgnoreCase("Close") || option.equalsIgnoreCase("Shut")) {
            player.setAnimation(openAnimation);
            player.sendSound(51);
            player.lock(1);
            player.sendMessage("You shut the chest...");
            swapObject(object);
        } else if (option.equalsIgnoreCase("Search")) {
            if (TreasureTrail.searchKeyObject(player, object, option) || TreasureTrail.search(player, object, option)) {
                return;
            }
            player.sendMessage("You find nothing.");
        }
    }

    @Override
    public Object[] getObjects() {
        final IntOpenHashSet set = new IntOpenHashSet();
        set.addAll(map.keySet());
        set.addAll(map.values());
        return set.toArray();
    }

    @Override
    protected Int2IntOpenHashMap map() {
        return map;
    }
}
