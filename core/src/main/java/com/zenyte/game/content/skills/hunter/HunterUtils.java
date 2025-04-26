package com.zenyte.game.content.skills.hunter;

import com.zenyte.game.content.skills.hunter.node.NetTrapPair;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.npc.HunterDummyNPC;
import com.zenyte.game.content.skills.hunter.npc.PitfallHunterNPC;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.LayableObjectPlugin;
import com.zenyte.game.world.region.area.plugins.LayableTrapRestrictionPlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author Kris | 25/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HunterUtils {
    public static final int SEEK_DISTANCE = 3;
    private static final String teasedNPCAttributeKey = "Teased hunter npc";
    private static final BiPredicate<Player, HunterDummyNPC> trapOwnershipPredicate = (player, npc) -> {
        final WeakReference<HunterTrap> associatedTrap = npc.getTrap();
        if (associatedTrap == null) {
            return false;
        }
        final HunterTrap trap = associatedTrap.get();
        if (trap == null) {
            return false;
        }
        return trap.getPlayer().get() == player;
    };
    /**
     * A list of directions whose index correlates to the young tree object's rotation.
     * <p>The direction represents which way the location needs to be shifted by one tile.</p>
     */
    private static final List<Direction> youngTreeDirections =
            Collections.unmodifiableList(Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH,
                    Direction.WEST));

    public static void setTeasedNPC(@NotNull final Player player, @Nullable final NPC npc) {
        player.addTemporaryAttribute(teasedNPCAttributeKey, npc == null ? null : new WeakReference<>(npc));
    }

    public static Optional<PitfallHunterNPC> getTeasedNPC(@NotNull final Player player) {
        final Object attribute = player.getTemporaryAttributes().get(teasedNPCAttributeKey);
        if (!(attribute instanceof WeakReference)) {
            return Optional.empty();
        }
        final WeakReference<?> reference = (WeakReference<?>) attribute;
        final Object object = reference.get();
        if (!(object instanceof PitfallHunterNPC)) {
            return Optional.empty();
        }
        return Optional.of((PitfallHunterNPC) object);
    }

    public static int getMaximumLayableTraps(final int level) {
        return 1 + (level / 20);
    }

    @NotNull
    public static Optional<HunterTrap> findTrap(@NotNull final TrapType trapType,
                                                @NotNull final Location location,
                                                @Nullable final Predicate<HunterDummyNPC> predicate) {
        //Deadfall traps are slightly offset.
        final int radius = trapType == TrapType.DEADFALL || trapType.isNetTrap() ? 1 : 0;
        final List<NPC> matchingNPCs = CharacterLoop.find(location, radius, NPC.class,
                npc -> (npc.getId() == trapType.getActiveDummyNpcId() || npc.getId() == trapType.getInactiveDummyNpcId()) && (predicate == null || npc instanceof HunterDummyNPC && predicate.test((HunterDummyNPC) npc)));
        if (matchingNPCs.isEmpty()) {
            return Optional.empty();
        }
        final NPC npc = matchingNPCs.get(0);
        if (!(npc instanceof HunterDummyNPC)) {
            return Optional.empty();
        }
        final WeakReference<HunterTrap> trapReference = ((HunterDummyNPC) npc).getTrap();
        if (trapReference == null) {
            return Optional.empty();
        }
        final HunterTrap trap = trapReference.get();
        return Optional.ofNullable(trap);
    }

    public static OptionalInt findKnifeWithLogs(@NotNull final Player player) {
        final OptionalInt logs = TreeDefinitions.findLogs(player);
        if (!logs.isPresent() || !player.getInventory().containsItem(ItemId.KNIFE, 1)) {
            player.sendMessage("You need both a knife and some logs to set-up this trap.");
            return OptionalInt.empty();
        }
        return logs;
    }

    public static boolean hasMaxTrapsLaid(@NotNull final Player player) {
        final int maximumLayableTraps = getMaximumLayableTraps(player.getSkills().getLevel(SkillConstants.HUNTER))
                + BooleanUtils.toInteger(WildernessArea.isWithinWilderness(player));
        final int trapCount = player.getHunter().getLaidTrapsSize();
        if (trapCount >= maximumLayableTraps) {
            player.sendMessage("You may setup only " + maximumLayableTraps + " trap" + (maximumLayableTraps == 1 ?
                    "" : "s") + " at a time at your Hunter level.");
            return true;
        }
        return false;
    }

    public static boolean isAreaRestricted(@NotNull final Player player, @NotNull final TrapType trapType) {
        final RegionArea area = player.getArea();
        if (area instanceof LayableTrapRestrictionPlugin) {
            player.sendMessage(((LayableTrapRestrictionPlugin) area).trapRestrictionMessage());
            return true;
        }
        if (trapType == TrapType.BIRD_SNARE || trapType == TrapType.BOX_TRAP) {
            if (area instanceof LayableObjectPlugin) {
                return !(((LayableObjectPlugin) area).canLay(player, trapType == TrapType.BIRD_SNARE ?
                        LayableObjectPlugin.LayableObjectType.BIRD_SNARE :
                        LayableObjectPlugin.LayableObjectType.BOX_TRAP));
            }
        }
        return false;
    }

    public static boolean isBelowRequiredLevel(@NotNull final Player player, @NotNull final TrapType trapType) {
        if (player.getSkills().getLevel(SkillConstants.HUNTER) < trapType.getLevel()) {
            player.sendMessage("You need a Hunter level of at least " + trapType.getLevel() + " to lay this trap.");
            return true;
        }
        return false;
    }

    /**
     * Constructs a new data object representing a net trap pair, consisting of a verified young tree and a net trap.
     * <p>Both objects must be rotated in the same direction.</p>
     * <p>One of the objects' name has to be "Net trap" and the other's "Young tree", or else pairing fails.</p>
     *
     * @param eitherObject either the tree or the net trap, a pair will be constructed dynamically based on either of
     *                    them.
     * @return the net trap pair object representing a net trap and a young tree in an identified format.
     * @throws IllegalStateException if the opposite object does not physically exist, isn't rotated in the same way or
     *                               the name of it doesn't meet the required criteria.
     */
    @NotNull
    public static NetTrapPair findNetTrapPair(@NotNull final WorldObject eitherObject) throws IllegalStateException {
        assert World.exists(eitherObject) : "Object does not exist on physical map";
        //Identify the type of the trap first based on the name.
        final String name = eitherObject.getName();
        final boolean isNet = name.equals("Net trap");
        //Verify that the trap is either the net, or the paired young tree.
        assert isNet || name.equals("Young tree") : "Unknown object";
        //The direction in which we need to move the tile by one coordinate to find the net's(or tree's if vice
        // versa) physical location.
        final Direction direction =
                youngTreeDirections.get(eitherObject.getRotation()).getCounterClockwiseDirection(isNet ? 4 : 0);
        //Build a new location object off of this object and move it one tile in the requested direction.
        final Location alternativeObjectCoordinates = eitherObject.transform(direction);
        final WorldObject alternativeObject = World.getObjectWithType(alternativeObjectCoordinates,
                eitherObject.getType());
        if (!World.exists(alternativeObject)) {
            throw new IllegalStateException("Cannot find a net trap pair as the opposite object to the pair does not " +
                    "exist.");
        }
        if (alternativeObject.getRotation() != eitherObject.getRotation()) {
            throw new IllegalStateException("Object rotations do not match up: " + eitherObject + ", " + alternativeObject);
        }
        if (!alternativeObject.getName().equals(isNet ? "Young tree" : "Net trap")) {
            throw new IllegalStateException("Alternative object does not match the criteria of the net trap pair: " + eitherObject + ", " + alternativeObject);
        }
        final WorldObject net = isNet ? eitherObject : alternativeObject;
        final WorldObject tree = isNet ? alternativeObject : eitherObject;
        return new NetTrapPair(net, tree);
    }

    @NotNull
    public static WorldObject buildNetTrap(@NotNull final WorldObject youngTree, @NotNull final TrapType trapType) {
        assert World.exists(youngTree) : "Object does not exist on physical map";
        //The direction in which we need to move the tile by one coordinate to find the net's(or tree's if vice
        // versa) physical location.
        final Direction direction = youngTreeDirections.get(youngTree.getRotation());
        //Build a new location object off of this object and move it one tile in the requested direction.
        final Location location = youngTree.transform(direction);
        return new WorldObject(trapType.getObjectId(), youngTree.getType(), youngTree.getRotation(), location);
    }

    private static final List<Direction> directions = Collections.unmodifiableList(Arrays.asList(Direction.WEST,
            Direction.EAST, Direction.SOUTH, Direction.NORTH));

    public static void walkAway(@NotNull final Player player) {
        player.resetWalkSteps();
        for (final Direction direction : directions) {
            final Location tile = player.getLocation().transform(direction);
            if (player.addWalkSteps(tile.getX(), tile.getY())) {
                break;
            }
        }
    }

    public static BiPredicate<Player, HunterDummyNPC> getTrapOwnershipPredicate() {
        return trapOwnershipPredicate;
    }
}
