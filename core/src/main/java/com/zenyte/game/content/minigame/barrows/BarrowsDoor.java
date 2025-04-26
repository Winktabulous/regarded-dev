package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.rots.RotsInstance;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.TemporaryDoubleDoor;
import com.zenyte.game.world.object.WorldObject;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 5. dets 2017 : 0:22.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsDoor implements ObjectAction {

    private static final int[] puzzleDoorTiles = new int[] { Location.hash(3551, 9683, 0), Location.hash(3552, 9683, 0), Location.hash(3540, 9695, 0), Location.hash(3540, 9694, 0), Location.hash(3552, 9706, 0), Location.hash(3551, 9706, 0), Location.hash(3563, 9694, 0), Location.hash(3563, 9695, 0) };

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.inArea("Rise of the Six")) {
            RotsInstance area = (RotsInstance) player.getArea();
            if (area.isEntered()) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("There's no going back.");
                    }
                });
                return;
            }

            if (player.getRetrievalService().getType() == ItemRetrievalService.RetrievalServiceType.ROTS && !player.getRetrievalService().getContainer().isEmpty()) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        options("Strange old man has some of your items. Do you still wish to proceed?", new DialogueOption("Yes.", () -> {
                            player.getDialogueManager().finish();
                            enterRots(player, area, object);
                        }), new DialogueOption("No."));
                    }
                });
                return;
            }

            enterRots(player, area, object);
            return;
        }

        if (ArrayUtils.contains(puzzleDoorTiles, player.getLocation().getPositionHash())) {
            if (!player.getBarrows().isPuzzleSolved()) {
                player.sendMessage("The door is locked with a strange puzzle.");
                player.getBarrows().getPuzzle().reset();
                GameInterface.BARROWS_PUZZLE.open(player);
                return;
            }
        }
        TemporaryDoubleDoor.executeBarrowsDoors(player, object, location -> player.getBarrows().sendRandomTarget(location));
    }

    private static void enterRots(Player player, RotsInstance area, WorldObject object) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain(Colour.RED.wrap("Warning: ") + "Once you enter, you are at " + Colour.RED.wrap("risk of death") + ". There is no escape unless you defeat the encounter.");
                options(new DialogueOption("Enter.", () -> {
                    TemporaryDoubleDoor.handleDoubleDoor(player, object);
                    area.spawnBrothers();
                    player.sendMessage("The encounter will start shortly.");
                }), new DialogueOption("Not yet."));
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_20679, ObjectId.DOOR_20680, 20681, 20682, 20683, 20684, 20685, 20686, 20687, 20688, 20689, 20690, 20691, 20692, 20693, 20694, 20695, 20696, ObjectId.DOOR_20698, ObjectId.DOOR_20699, 20700, 20701, 20702, 20703, 20704, 20705, 20706, 20707, 20708, 20709, 20710, 20711, 20712, 20713, 20714, 20715, 20717 };
    }

}
