package com.zenyte.game.content.minigame.warriorsguild.plugins;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.TemporaryDoubleDoor;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.WarriorsGuildCyclopsArea;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 23/03/2019 17:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class UpstairsCyclopsRoomDoor implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Open")) {
            if (!player.inArea("Warriors' guild top floor Cyclops' area")) {
                if (player.getPlane() == 2) {
                    if (!player.getInventory().containsItem(8851, 100) && !SkillcapePerk.ATTACK.isEffective(player)) {
                        player.getDialogueManager().start(new Dialogue(player, 2461) {

                            @Override
                            public void buildDialogue() {
                                World.findNPC(2461, player.getLocation(), 10).ifPresent(npc -> npc.setFaceLocation(player.getLocation()));
                                npc("You don't have enough warrior Guild Tokens to enter the cyclopes enclosure yet, collect at least 100 then come back.");
                            }
                        });
                        return;
                    }
                    player.getDialogueManager().start(new Dialogue(player, 2461) {

                        @Override
                        public void buildDialogue() {
                            World.findNPC(2461, player.getLocation(), 10).ifPresent(npc -> npc.setFaceLocation(player.getLocation()));
                            npc("As you enter the room, the Cyclops will start dropping the best defenders available " + "to you, depending on whichever defenders you carry with you.");
                            npc("You will not need to bother yourself with showing me your defenders.").executeAction(() -> {
                                if (object.getY() == 3546 && player.getY() == 3546 || object.getX() == 2847 && player.getX() == 2847) {
                                    player.addWalkSteps(object.getX(), object.getY(), 1, true);
                                    WorldTasksManager.schedule(() -> {
                                        TemporaryDoubleDoor.handleBarrowsDoubleDoor(player, object);
                                    });
                                    return;
                                }
                                if (!shouldKeepToken(player)) {
                                    player.getInventory().deleteItem(ItemId.WARRIOR_GUILD_TOKEN, 10);
                                }
                                TemporaryDoubleDoor.handleBarrowsDoubleDoor(player, object);
                            });
                        }
                    });
                    return;
                }
            }
            if (object.getY() == 3546 && player.getY() == 3546 || object.getX() == 2847 && player.getX() == 2847) {
                player.addWalkSteps(object.getX(), object.getY(), 1, true);
                WorldTasksManager.schedule(() -> TemporaryDoubleDoor.handleBarrowsDoubleDoor(player, object));
                return;
            }
            TemporaryDoubleDoor.handleBarrowsDoubleDoor(player, object);
        }
    }

    private boolean shouldKeepToken(@NotNull final Player player) {
        return SkillcapePerk.ATTACK.isEffective(player) || player.isMember() && Utils.random(100) <= WarriorsGuildCyclopsArea.getChance(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_24306, ObjectId.DOOR_24309 };
    }
}
