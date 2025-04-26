package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 */
public class LarrysBoat implements ObjectAction {
    
    private static final int EAST_CRABS_BOAT = 21176;
    private static final int ICEBERG_BOAT = 21175;
    private static final int WEISS_BOAT = 21177;
    
    private static final Location eastCrabsDock = new Location(2707, 3735);
    private static final Location iceberg = new Location(2659, 3988, 1);
    private static final Location weiss = new Location(2850, 3968);
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (object.getId()) {
            case EAST_CRABS_BOAT:
                if (object.getPosition().getPositionHash() != new Location(2708, 3732).getPositionHash()) {
                    return;
                }
                if (option.equalsIgnoreCase("iceberg")) {
                    travel(player, iceberg);
                } else if (option.equalsIgnoreCase("weiss")) {
                    travel(player, weiss);
                } else if (option.equalsIgnoreCase("travel")) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            options("Where would you like to travel?",
                                    new DialogueOption("Weiss", () -> travel(player, weiss)),
                                    new DialogueOption("Iceberg", () -> travel(player, iceberg)),
                                    new DialogueOption("Stay here")
                            );
                        }
                    });
                }
                break;
            case ICEBERG_BOAT:
                if (object.getPosition().getPositionHash() != new Location(2654, 3985, 1).getPositionHash()) {
                    return;
                }
                travel(player, eastCrabsDock);
                break;
            case WEISS_BOAT:
                if (object.getPosition().getPositionHash() != new Location(2843, 3967).getPositionHash()) {
                    return;
                }
                travel(player, eastCrabsDock);
                break;
        }
        
    }
    
    private void travel(final Player player, final Location location) {
        new FadeScreen(player, () -> player.setLocation(location)).fade(4);
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{EAST_CRABS_BOAT, ICEBERG_BOAT, WEISS_BOAT};
    }
    
}
