package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

/**
 * @author Kris | 16. nov 2017 : 2:37.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LargeScavengerRoom extends ScavengerRoom {
    /**
     * The blocking object ids as well as their transformations, if applicable.
     */
    public static final int ROCK = 29738;
    public static final int TREE = 29736;
    public static final int BOULDER = 29740;
    public static final int RUBBLE = 29739;
    public static final int STUB = 29737;
    /**
     * The tiles where the blocking tree, boulder or rocks will spawn at.
     */
    private static final Location[] blockingObjectTiles = new Location[] {new Location(3272, 5222, 1), new Location(3314, 5238, 1), new Location(3353, 5224, 1)};
    /**
     * The blocking object ids that they spawn as.
     */
    private static final int[] blockingObjects = new int[] {ROCK, TREE, BOULDER};

    public LargeScavengerRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public void loadRoom() {
        super.loadRoom();
        final int id = blockingObjects[Utils.random(blockingObjects.length - 1)];
        final int skill = id == ROCK ? SkillConstants.MINING : id == TREE ? SkillConstants.WOODCUTTING : SkillConstants.STRENGTH;
        final int randomLevel = Math.min(99, Utils.random(getLowestLevel(skill), getHighestLevel(skill) + 2));
        World.spawnObject(new BlockingObject(id, 10, getRotation(), getLocation(blockingObjectTiles[index]), randomLevel));
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Large Scavenger room";
    }


    /**
     * The blocking object class used to provide common variables between all the blocking objects.
     */
    public static final class BlockingObject extends WorldObject {
        /**
         * A set of players who are interacting with this object at the time, needed to track due to it giving points to everyone who is interacting with it at the time, not just
         * the person who manages to deplete the object.
         */
        private final Set<Player> interactingPlayers = new ObjectOpenHashSet<>();
        /**
         * The level in a specific determined skill to interact with this object.
         */
        private int levelRequired;

        private BlockingObject(final int id, final int type, final int rotation, final Location tile, final int levelRequired) {
            super(id, type, rotation, tile);
            this.levelRequired = levelRequired;
        }

        public Set<Player> getInteractingPlayers() {
            return interactingPlayers;
        }

        public int getLevelRequired() {
            return levelRequired;
        }

        public void setLevelRequired(int levelRequired) {
            this.levelRequired = levelRequired;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof LargeScavengerRoom.BlockingObject)) return false;
            final LargeScavengerRoom.BlockingObject other = (LargeScavengerRoom.BlockingObject) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            if (this.getLevelRequired() != other.getLevelRequired()) return false;
            final Object this$interactingPlayers = this.getInteractingPlayers();
            final Object other$interactingPlayers = other.getInteractingPlayers();
            return this$interactingPlayers == null ? other$interactingPlayers == null :
                    this$interactingPlayers.equals(other$interactingPlayers);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof LargeScavengerRoom.BlockingObject;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            result = result * PRIME + this.getLevelRequired();
            final Object $interactingPlayers = this.getInteractingPlayers();
            result = result * PRIME + ($interactingPlayers == null ? 43 : $interactingPlayers.hashCode());
            return result;
        }
    }
}
