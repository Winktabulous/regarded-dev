package com.zenyte.game.content.kebos.konar.plugins.objects;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Map;

/**
 * @author Tommeh | 18/11/2019 | 19:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class LizardDwelling implements ObjectAction {
    public static final Map<Integer, Location> destinationMap = ImmutableMap.<Integer, Location>builder().put(34402, new Location(1292, 10058, 0)).put(34403, new Location(1314, 10064, 0)).put(34404, new Location(1330, 10070, 0)).put(34405, new Location(1312, 10086, 0)).put(34422, new Location(1292, 3676, 0)).build();

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final boolean strangeHole = object.getId() == 34422;
        player.lock();
        player.getDialogueManager().start(new PlainChat(player, "You cautiously lower yourself into the " + (strangeHole ? "hole." : "the Lizardman dwelling.")));
        new FadeScreen(player, () -> {
            final Location destination = destinationMap.get(object.getId());
            player.unlock();
            player.setLocation(destination);
            if (strangeHole) {
                player.getDialogueManager().start(new PlainChat(player, "After many twists and turns, you find your way out of the temple."));
            } else {
                player.getDialogueManager().start(new PlainChat(player, "You land in a puddle. There is no way back."));
            }
        }).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return destinationMap.keySet().toArray(new Object[destinationMap.size()]);
    }
}
