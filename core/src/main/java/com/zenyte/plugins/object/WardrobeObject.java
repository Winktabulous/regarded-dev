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
 * @author Kris | 12/04/2019 15:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WardrobeObject extends SearchableClueObjectPlugin implements ObjectAction {
    private static final Animation animation = new Animation(832);
    public static final Int2IntOpenHashMap map = new Int2IntOpenHashMap();

    static {
        map.put(388, 389);
        map.put(5622, 5623);
        map.put(24668, 24669);
        map.put(24670, 24671);
        map.put(25040, 25041);
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Open")) {
            final Optional<CrypticClue> clue = CrypticClue.getClueFromKeyObject(object);
            if (clue.isPresent()) {
                final int k = ((KeyRequest) clue.get().getChallenge()).getKeyId();
                if (!player.getInventory().containsItem(k, 1)) {
                    player.sendMessage("The wardrobe is firmly shut.");
                    return;
                }
            }
            player.setAnimation(animation);
            player.lock(1);
            player.sendMessage("You open the wardrobe...");
            clue.ifPresent(k -> player.getInventory().deleteItem(((KeyRequest) k.getChallenge()).getKeyId(), 1));
            swapObject(object);
        } else if (option.equalsIgnoreCase("Close") || option.equalsIgnoreCase("Shut")) {
            player.setAnimation(animation);
            player.lock(1);
            player.sendMessage("You shut the wardrobe...");
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
