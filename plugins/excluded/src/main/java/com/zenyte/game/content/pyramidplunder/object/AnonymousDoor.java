package com.zenyte.game.content.pyramidplunder.object;

import com.zenyte.game.content.pyramidplunder.PyramidPlunderConstants;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class AnonymousDoor implements ObjectAction {
    public static final int AMOUNT_OF_DOORS = 4;
    private static final ImmutableLocation CORRECT_ROOM = new ImmutableLocation(1934, 4420, 3);
    private static final ImmutableLocation WRONG_ROOM = new ImmutableLocation(1968, 4450, 3);
    private static final Animation pickDoorAnim = new Animation(832);
    private static final SoundEffect pickDoorSound = new SoundEffect(1220);
    private static int currentDoorOffset;

    static {
        shuffleAnonymousDoor();
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new OpenAnonymousDoorAction(object));
    }

    public static void shuffleAnonymousDoor() {
        currentDoorOffset = Utils.random(AMOUNT_OF_DOORS - 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {PyramidPlunderConstants.ANONYMOUS_DOOR_NORTH, PyramidPlunderConstants.ANONYMOUS_DOOR_EAST, PyramidPlunderConstants.ANONYMOUS_DOOR_SOUTH, PyramidPlunderConstants.ANONYMOUS_DOOR_WEST};
    }


    private static class OpenAnonymousDoorAction extends Action {
        private final WorldObject door;
        private int cycle;

        @Override
        public boolean start() {
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            switch (cycle++) {
            case 0: 
                player.lock(3);
                player.setAnimation(pickDoorAnim);
                player.sendSound(pickDoorSound);
                break;
            case 1: 
                player.sendFilteredMessage("You use your thieving skills to search the stone panel.");
                break;
            case 2: 
                player.setAnimation(pickDoorAnim);
                player.sendSound(pickDoorSound);
                break;
            case 3: 
                final boolean isCorrectDoor = door.getId() == 26622 + currentDoorOffset;
                player.sendFilteredMessage("You find a door! " + (isCorrectDoor ? "You open it successfully." : "You open it."));
                if (isCorrectDoor) {
                    player.setLocation(CORRECT_ROOM);
                    player.getSkills().addXp(SkillConstants.THIEVING, 20);
                } else {
                    player.setLocation(WRONG_ROOM);
                }
                player.setFaceLocation(player.getLocation().transform(Direction.NORTH, 1));
                player.lock(1);
                return -1;
            }
            return 0;
        }

        @Override
        public void stop() {
            player.unlock();
        }

        public OpenAnonymousDoorAction(WorldObject door) {
            this.door = door;
        }
    }
}
