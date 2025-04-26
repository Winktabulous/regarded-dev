package com.near_reality.game.content.gauntlet.plugins;

import com.near_reality.game.content.gauntlet.GauntletPlayerAttributesKt;
import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.map.GauntletMap;
import com.near_reality.game.content.gauntlet.rewards.GauntletRewardType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Andys1814.
 */
public final class GauntletHunllefBarrier implements ObjectAction {

    private static final int BARRIER = 37339;

    private static final int CORRUPTED_BARRIER = 37337;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
        if (gauntlet == null)
            return;

        final GauntletMap map = gauntlet.getMap();
        final int bossRoomX = map.getBaseXForNode(map.getBossRoomX()) + 2;
        final int bossRoomY = map.getBaseYForNode(map.getBossRoomY()) + 2;

        if (optionId == 1 && player.getLocation().getX() >= bossRoomX && player.getLocation().getY() >= bossRoomY && player.getLocation().getX() <= bossRoomX + 11 && player.getLocation().getY() <= bossRoomY + 11) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(
                            "Leave the boss fight now? You will leave the Gauntlet.",
                            new DialogueOption("Yes, get me out of here.", () -> {
                                gauntlet.end(GauntletRewardType.NONE, true, true);
                            }),
                            new DialogueOption("No.")
                    );
                }
            });
            return;
        }

        if (optionId == 2 && player.getLocation().getX() >= bossRoomX && player.getLocation().getY() >= bossRoomY && player.getLocation().getX() <= bossRoomX + 11 && player.getLocation().getY() <= bossRoomY + 11) {
            gauntlet.end(GauntletRewardType.NONE, true, true);
            return;
        }

        if (optionId == 1) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(
                            "Start the boss fight now? There's no turning back.",
                            new DialogueOption("Yes, I'm ready.", () -> {
                                passBarrier(player, object);
                                gauntlet.startBossPhase();
                            }),
                            new DialogueOption("No.")
                    );
                }
            });
        } else if (optionId == 2) {
            passBarrier(player, object);
            gauntlet.startBossPhase();
        }
    }

    public void passBarrier(Player player, WorldObject object) {
        WorldTasksManager.schedule(() -> {
            player.lock(2);
            player.setRunSilent(true);
            if (object.getFaceDirection() == Direction.NORTH) {
                if (player.getLocation().getX() > object.getX()) {
                    player.addWalkSteps(Direction.WEST, 2, -1, false);
                } else {
                    player.addWalkSteps(Direction.EAST, 2, -1, false);
                }
            } else {
                if (player.getLocation().getY() < object.getY()) {
                    player.addWalkSteps(Direction.NORTH, 2, -1, false);
                } else {
                    player.addWalkSteps(Direction.SOUTH, 2, -1, false);
                }
            }

            WorldTasksManager.schedule(() -> player.setRunSilent(false), 1);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { BARRIER, CORRUPTED_BARRIER };
    }
}
