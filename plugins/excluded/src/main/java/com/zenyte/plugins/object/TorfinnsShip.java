package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/04/2019 01:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TorfinnsShip implements ObjectAction {

    /*
        private static final Location RELLEKKA_LOCATION = new Location(2641, 3698, 0);
    private static final Location UNGAEL_LOCATION = new Location(2277, 4035, 0);

    private void sail(final Player player, final boolean toVorkath) {
        new FadeScreenAction(player, 2, () -> player.setLocation(toVorkath ? UNGAEL_LOCATION : RELLEKKA_LOCATION)).run();
        player.sendFilteredMessage("You travel to " + (toVorkath ? "Ungael..." : "Rellekka..."));
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                final boolean toVorkath = player.getLocation().withinDistance(RELLEKKA_LOCATION, 20);
                npc("Good day, " + player.getPlayerInformation().getDisplayname() + ". Would you like to sail to " + (toVorkath ? "Ungael?" : "Rellekka?"));
                options("Sail to " + (toVorkath ? "Ungael?" : "Rellekka?"), "Yes, take me there.", "No, I'd rather not.")
                        .onOptionOne(() -> sail(player, toVorkath)).onOptionTwo(this::finish);
            }
        }));
        bind("Travel", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
        bind("Collect", (player, npc) -> GameInterface.ITEM_RETRIEVAL_SERVICE.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {8131, 8133, 8132, 7504};
    }
     */
    private static final Location RELLEKKA_LOCATION = new Location(2641, 3698, 0);

    private static final Location UNGAEL_LOCATION = new Location(2277, 4035, 0);

    private void sail(final Player player, final boolean toVorkath) {
        new FadeScreen(player, () -> player.setLocation(toVorkath ? UNGAEL_LOCATION : RELLEKKA_LOCATION)).fade(2);
        player.sendMessage("You travel to " + (toVorkath ? "Ungael..." : "Rellekka..."));
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Travel")) {
            sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.FREMENNIK_BOAT_31989, ObjectId.FREMENNIK_BOAT_32461, 29917 };
    }
}
