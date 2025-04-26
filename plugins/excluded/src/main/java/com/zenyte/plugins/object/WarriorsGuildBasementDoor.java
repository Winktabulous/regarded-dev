package com.zenyte.plugins.object;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.WarriorsGuildBasementCyclopsArea;

/**
 * @author Kris | 16. dets 2017 : 1:49.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WarriorsGuildBasementDoor implements ObjectAction {

    private static final Location LOCATION = new Location(2912, 9968, 0);

    private static final Location OUTSIDE = new Location(2911, 9968, 0);

    private static final WorldObject DOOR = new WorldObject(24312, 0, 3, OUTSIDE);

    private static final String DEFENDER_SHOWN_ATTRIBUTE = "defender shown";

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        handleDoor(player, object);
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(player.getX() < 2912 && player.getY() > 9965 ? OUTSIDE : LOCATION), () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }));
    }

    private void handleDoor(final Player player, final WorldObject object) {
        if (object.isLocked()) {
            return;
        }
        if (player.getLocation().getPositionHash() == DOOR.getPositionHash()) {
            handle(player, object, LOCATION);
        } else {
            handle(player, object, DOOR);
        }
    }

    private final void handle(final Player player, final WorldObject object, final Location tile) {
        if (player.getLocation().getPositionHash() == OUTSIDE.getPositionHash()) {
            final boolean carryingRuneDefender = player.getInventory().containsItem(ItemId.RUNE_DEFENDER, 1) || player.getInventory().containsItem(ItemId.RUNE_DEFENDER_T, 1) || player.getEquipment().getId(EquipmentSlot.SHIELD) == ItemId.RUNE_DEFENDER || player.getEquipment().getId(EquipmentSlot.SHIELD) == ItemId.RUNE_DEFENDER_T;
            if (!carryingRuneDefender && !player.getBooleanAttribute(DEFENDER_SHOWN_ATTRIBUTE)) {
                player.getDialogueManager().start(new Dialogue(player, 2135) {

                    @Override
                    public void buildDialogue() {
                        World.findNPC(2135, player.getLocation()).ifPresent(npc -> npc.setFaceLocation(player.getLocation()));
                        npc("You need to prove your worth to me before I let you pass through those doors.");
                        npc("Until you show me a rune defender, you can\'t pass.");
                    }
                });
                return;
            }
            if (!player.getInventory().containsItem(8851, 100) && !SkillcapePerk.ATTACK.isEffective(player)) {
                player.getDialogueManager().start(new Dialogue(player, 2135) {

                    @Override
                    public void buildDialogue() {
                        World.findNPC(2461, player.getLocation(), 10).ifPresent(npc -> npc.setFaceLocation(player.getLocation()));
                        npc("You don\'t have enough warrior Guild Tokens to enter the cyclopes enclosure yet, collect at least 100 then come back.");
                    }
                });
                return;
            }
        }
        object.setLocked(true);
        player.lock();
        World.spawnGraphicalDoor(DOOR);
        if (!player.getBooleanAttribute(DEFENDER_SHOWN_ATTRIBUTE)) {
            player.putBooleanAttribute(DEFENDER_SHOWN_ATTRIBUTE, true);
        }
        if (!WarriorsGuildBasementCyclopsArea.shouldKeepToken(player)) {
            player.getInventory().deleteItem(ItemId.WARRIOR_GUILD_TOKEN, 10);
        }
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.addWalkSteps(tile.getX(), tile.getY(), 1, false);
                } else if (ticks == 1) {
                    player.unlock();
                    World.spawnGraphicalDoor(World.getObjectWithType(DOOR, 0));
                } else if (ticks == 2) {
                    object.setLocked(false);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_10043 };
    }
}
