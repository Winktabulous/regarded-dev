package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tommeh | 17/11/2019 | 19:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class ZeahBoatObject implements ObjectAction {

    private enum ZeahBoat {

        MOLCH(new Location(1342, 3646, 0)),
        BATTLEFRONT(new Location(1384, 3665, 0)),
        MOLCH_ISLAND(new Location(1369, 3639, 0)),
        SHAYZIEN(new Location(1408, 3612, 0));

        private static final ZeahBoat[] values = values();

        private final Location location;

        ZeahBoat(Location location) {
            this.location = location;
        }

        private static ZeahBoat get(final Player player) {
            double previousDistance = 50;
            ZeahBoat previousBoat = null;
            for (final ZeahBoatObject.ZeahBoat boat : values) {
                final double distance = player.getLocation().getDistance(boat.getLocation());
                if (distance < previousDistance) {
                    previousBoat = boat;
                    previousDistance = distance;
                }
            }
            return previousBoat;
        }

        @Override
        public String toString() {
            return TextUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
        }

        public Location getLocation() {
            return location;
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final ZeahBoatObject.ZeahBoat currentBoat = ZeahBoat.get(player);
        if (currentBoat == null) {
            return;
        }
        final List<ZeahBoat> boats = new ArrayList<>(4);
        Collections.addAll(boats, ZeahBoat.values);
        boats.remove(currentBoat);
        final List<String> options = new ArrayList<>(4);
        boats.forEach(b -> options.add(b.toString()));
        options.add("Cancel");
        final List<Runnable> runnables = new ArrayList<>(4);
        boats.forEach(b -> runnables.add(() -> {
            player.lock();
            player.getDialogueManager().start(new PlainChat(player, "You travel to " + (b.equals(ZeahBoat.BATTLEFRONT) ? "the " : "") + b + ".", false));
            new FadeScreen(player, () -> {
                player.unlock();
                player.setLocation(b.getLocation());
                player.getInterfaceHandler().closeInterfaces();
            }).fade(3);
        }));
        player.getDialogueManager().start(new OptionDialogue(player, "Where would you like to go?", options.toArray(new String[0]), runnables.toArray(new Runnable[0])));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BOATY };
    }
}
