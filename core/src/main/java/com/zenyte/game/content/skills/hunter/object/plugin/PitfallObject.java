package com.zenyte.game.content.skills.hunter.object.plugin;

import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.actions.CreatePitfallAction;
import com.zenyte.game.content.skills.hunter.actions.DismantlePitfallAction;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

/**
 * @author Kris | 30/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PitfallObject implements ObjectAction, ItemOnObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option == null || option.equals("null")) {
            return;
        }
        switch (option) {
        case "Trap": 
            player.getActionManager().setAction(new CreatePitfallAction(object, OptionalInt.empty()));
            break;
        case "Jump": 
            jump(player, object);
            break;
        case "Dismantle": 
            final HunterTrap trap = HunterUtils.findTrap(TrapType.PITFALL, object, npc -> HunterUtils.getTrapOwnershipPredicate().test(player, npc)).orElseThrow(RuntimeException::new);
            player.getActionManager().setAction(new DismantlePitfallAction(object, trap));
            break;
        default: 
            throw new IllegalStateException(option);
        }
    }

    @Override
    public int getDelay() {
        return 1;
    }

    /**
     * The jump animation performed by the player when they leap over the trap.
     */
    private static final Animation jumpAnim = new Animation(3067, 20);
    /**
     * The jump sound locally played when the player leaps over the trap.
     */
    private static final SoundEffect sound = new SoundEffect(2635, 5, 20);

    /**
     * Performs the jump sequence over a laid pitfall trap. The {@link com.zenyte.game.content.skills.hunter.npc.PitfallHunterNPC} will follow if possible.
     * @param player the player performing the jump.
     * @param trap the object the player is jumping over.
     */
    public void jump(@NotNull final Player player, @NotNull final WorldObject trap) {
        final int rot = trap.getRotation();
        player.setAnimation(jumpAnim);
        World.sendSoundEffect(player.getLocation(), sound);
        final int px = player.getX();
        final int py = player.getY();
        final Direction dir = (rot & 1) == 0 ? (py > trap.getY() ? Direction.SOUTH : Direction.NORTH) : (px > trap.getX() ? Direction.WEST : Direction.EAST);
        final Location destination = new Location(dir == Direction.WEST ? (px - 3) : dir == Direction.EAST ? (px + 3) : px, dir == Direction.SOUTH ? (py - 3) : dir == Direction.NORTH ? (py + 3) : py, player.getPlane());
        player.setFaceLocation(destination);
        player.autoForceMovement(destination, 35, 90);
        player.lock(4);
        WorldTasksManager.schedule(() -> HunterUtils.getTeasedNPC(player).ifPresent(teased -> teased.follow(player, trap)), 1);
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        //If the object isn't in the default state, we shall not interact with it.
        if (player.getVarManager().getBitValue(object.getDefinitions().getVarbitId()) != 0) {
            player.sendFilteredMessage("Nothing interesting happens.");
            return;
        }
        final TreeDefinitions logs = CollectionUtils.findMatching(TreeDefinitions.values, value -> value.getLogsId() == item.getId());
        player.getActionManager().setAction(new CreatePitfallAction(object, logs != null ? OptionalInt.of(slot) : OptionalInt.empty()));
    }

    @Override
    public Object[] getItems() {
        final ObjectOpenHashSet<Object> list = new ObjectOpenHashSet<>();
        for (final TreeDefinitions tree : TreeDefinitions.values) {
            list.add(tree.getLogsId());
        }
        list.add(ItemId.KNIFE);
        list.remove(-1);
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        final ObjectArrayList<Object> list = new ObjectArrayList<>();
        for (final int id : TrapType.PITFALL.getObjectIds()) {
            list.add(id);
        }
        return list.toArray();
    }
}
