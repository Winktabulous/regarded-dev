package com.zenyte.plugins.object;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.challenges.KeyRequest;
import com.zenyte.game.content.treasuretrails.clues.CrypticClue;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.util.Optional;

/**
 * @author Kris | 08/04/2019 14:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DrawersObject extends SearchableClueObjectPlugin implements ObjectAction {
    private static final Animation animation = new Animation(832);
    public static final Int2IntOpenHashMap map = new Int2IntOpenHashMap();

    static {
        map.put(348, 349);
        map.put(350, 351);
        map.put(352, 353);
        map.put(3190, 3191);
        map.put(3816, 3817);
        map.put(5618, 5619);
        map.put(6875, 351);
        map.put(7194, 7195);
        map.put(7442, 7443);
        map.put(23966, 23967);
        map.put(24681, 24682);
        map.put(25701, 25702);
        map.put(25703, 25704);
        map.put(25705, 25706);
        map.put(25708, 25709);
        map.put(25710, 25711);
        map.put(25712, 25713);
        map.put(25714, 25715);
        map.put(25766, 25767);
        map.put(29073, 29074);
        map.put(29075, 29076);
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Open")) {
            final Optional<CrypticClue> clue = CrypticClue.getClueFromKeyObject(object);
            if (clue.isPresent()) {
                final int k = ((KeyRequest) clue.get().getChallenge()).getKeyId();
                if (!player.getInventory().containsItem(k, 1)) {
                    player.sendMessage("The drawers are firmly shut.");
                    return;
                }
            }
            player.setAnimation(animation);
            player.lock(1);
            player.sendMessage("You open the drawers...");
            clue.ifPresent(k -> player.getInventory().deleteItem(((KeyRequest) k.getChallenge()).getKeyId(), 1));
            swapObject(object);
        } else if (option.equalsIgnoreCase("Close") || option.equalsIgnoreCase("Shut")) {
            player.setAnimation(animation);
            player.lock(1);
            player.sendMessage("You shut the drawers...");
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
