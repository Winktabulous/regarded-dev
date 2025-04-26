package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.firemaking.Firemaking;
import com.zenyte.game.content.skills.woodcutting.AxeDefinitions;
import com.zenyte.game.content.skills.woodcutting.MacheteDefinitions;
import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.object.WorldObjectUtils;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.types.config.ObjectDefinitions;

import java.util.Objects;
import java.util.Optional;

import static com.zenyte.game.content.skills.woodcutting.actions.Woodcutting.BURN_GFX;
import static com.zenyte.game.content.skills.woodcutting.actions.Woodcutting.TREE_FALL_SOUND;

/**
 * @author Kris | 27/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KharaziJungleTreeAndBush implements ObjectAction {
    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final int orientation = object.getRotation();
        final ObjectDefinitions objectDefinition = object.getDefinitions();
        final boolean reverseSizes = (orientation & 1) == 1;
        final int width = reverseSizes ? objectDefinition.getSizeY() : objectDefinition.getSizeX();
        final int height = reverseSizes ? objectDefinition.getSizeX() : objectDefinition.getSizeY();
        final int px = player.getX();
        final int py = player.getY();
        final int ox = object.getX();
        final int oy = object.getY();
        if (px >= ox && px <= (ox + width) && py >= oy && py <= (oy + height)) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(player.getLocation()), getRunnable(player, object, name, optionId, option), getDelay()));
            return;
        }
        player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new Action() {
            private Woodcutting.AxeResult axe;
            private MacheteDefinitions macheteDefinitions;
            private int ticks;
            @Override
            public boolean start() {
                final Optional<Woodcutting.AxeResult> optionalAxe = Woodcutting.getAxe(player);
                if (!optionalAxe.isPresent()) {
                    player.getDialogueManager().start(new PlainChat(player, "You\'ll need an axe to get through this rough jungle. You don\'t think it would be a good idea to continue without one you\'ve got the level to use."));
                    return false;
                }
                final Optional<MacheteDefinitions> optionalMachete = MacheteDefinitions.getMachete(player);
                if (!optionalMachete.isPresent()) {
                    player.getDialogueManager().start(new PlainChat(player, "You\'ll need a machete to get through this rough jungle. You don\'t think it would be a good idea to continue without one you\'ve got the level to use."));
                    return false;
                }
                if (name.toLowerCase().contains("bush")) {
                    this.macheteDefinitions = optionalMachete.get();
                } else {
                    this.axe = optionalAxe.get();
                }
                if (!check()) {
                    return false;
                }
                if (axe != null) {
                    player.sendFilteredMessage("You swing your axe at the " + WorldObjectUtils.getObjectNameOfPlayer(object, player).toLowerCase() + ".");
                } else {
                    player.sendFilteredMessage("You swing your machete at the " + WorldObjectUtils.getObjectNameOfPlayer(object, player).toLowerCase() + ".");
                }
                delay(macheteDefinitions != null ? 1 : axe.getDefinition().getCutTime());
                return true;
            }
            private boolean check() {
                return World.exists(object);
            }
            @Override
            public boolean process() {
                if (ticks++ % (macheteDefinitions != null ? 2 : 4) == 0) player.setAnimation(macheteDefinitions != null ? macheteDefinitions.getEmote() : axe.getDefinition().getTreeCutAnimation());
                return check();
            }
            public boolean success() {
                final int level = player.getSkills().getLevel(SkillConstants.WOODCUTTING) + (macheteDefinitions == null ? 0 : (macheteDefinitions.ordinal() * 10));
                final int advancedLevels = level - 50;
                return Math.min(Math.round(advancedLevels * 0.8F) + 20, 70) > Utils.random(100);
            }
            @Override
            public int processWithDelay() {
                if (macheteDefinitions != null) {
                    player.sendSound(2736);
                }
                if (!success()) {
                    return macheteDefinitions != null ? 0 : axe.getDefinition().getCutTime();
                }
                if (Utils.random(1) == 0) {
                    player.getSkills().addXp(SkillConstants.WOODCUTTING, 100);
                    //Incinerate the logs
                    if (axe != null && axe.getItem().getCharges() > 0 && axe.getDefinition() == AxeDefinitions.INFERNAL && Utils.random(2) == 0) {
                        player.getChargesManager().removeCharges(axe.getItem(), 1, axe.getContainer(), axe.getSlot());
                        player.setGraphics(BURN_GFX);
                        player.sendSound(2596);
                        final Firemaking fm = Objects.requireNonNull(Firemaking.MAP.get(ItemId.LOGS));
                        player.getSkills().addXp(SkillConstants.FIREMAKING, fm.getXp() / 2.0F);
                    } else {
                        player.sendFilteredMessage("You get some wood.");
                        int amount = player.getPerkManager().isValid(PerkWrapper.LUMBERJACK) && Utils.random(100) <= 20 ? 2 : 1;
                        if (player.getEquipment().getItem(EquipmentSlot.HELMET) != null && player.getEquipment().getItem(EquipmentSlot.HELMET).getName().contains("Kandarin headgear")) {
                            amount += 1;
                        }
                        player.getInventory().addItem(ItemId.LOGS, amount).onFailure(remainder -> World.spawnFloorItem(remainder, player));
                    }
                }
                if (axe != null) {
                    player.getPacketDispatcher().sendSoundEffect(TREE_FALL_SOUND);
                }
                final WorldObject stump = new WorldObject(TreeDefinitions.getStumpId(object.getId()), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane());
                World.spawnObject(stump);
                WorldTasksManager.schedule(() -> World.spawnObject(object), 5);
                player.setAnimation(Animation.STOP);
                if (object.matches(player)) {
                    return -1;
                }
                final int distX = object.getX() - player.getX();
                final int distY = object.getY() - player.getY();
                final Location destination = player.getLocation().transform(distX + Integer.compare(distX, 0), distY + Integer.compare(distY, 0), 0);
                final boolean isClipped = !World.isFloorFree(destination, 1);
                if (isClipped) {
                    final WorldObject objectInPath = World.getObjectOfSlot(destination, 10);
                    final String name = objectInPath == null ? null : WorldObjectUtils.getObjectNameOfPlayer(objectInPath, player).toLowerCase();
                    if (name == null || (!name.contains("jungle tree") && !name.contains(" bush") && !name.contains("stump"))) {
                        player.sendMessage("This way is blocked off, no chance to get through here!");
                        return -1;
                    }
                }
                player.sendMessage("You hack your way through the " + WorldObjectUtils.getObjectNameOfPlayer(object, player).toLowerCase() + ".");
                if (destination.getY() < player.getY()) {
                    player.sendMessage("You move deeper into the jungle.");
                }
                if (destination.getTileDistance(player.getLocation()) > 2) {
                    player.lock(1);
                    player.setLocation(destination);
                } else {
                    player.lock(2);
                    player.setRunSilent(2);
                    player.addWalkSteps(destination.getX(), destination.getY(), 2, false);
                }
                return -1;
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.JUNGLE_TREE_2889, ObjectId.JUNGLE_TREE_2890, ObjectId.JUNGLE_BUSH, ObjectId.JUNGLE_BUSH_2893};
    }
}
