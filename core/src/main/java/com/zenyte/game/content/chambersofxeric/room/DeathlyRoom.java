package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.DeathlyNPC;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 16. nov 2017 : 2:41.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DeathlyRoom extends RaidArea {
    /**
     * An array of possible messages the protectors of the keystone shout upon crossing the rope.
     */
    public static final ForceTalk[] forceChats = new ForceTalk[] {new ForceTalk("Intruder!"), new ForceTalk("Stop them at all costs!"), new ForceTalk("Protect the keystone!")};
    /**
     * The keystone item that's given when picked up off the ground.
     */
    public static final Item keystoneCrystal = new Item(20884);
    /**
     * The render animation that's executed when walking across the tightrope.
     */
    public static final RenderAnimation renderAnimation = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
    /**
     * The animation that occurs upon placing the keystone in the barrier.
     */
    public static final Animation placingKeystoneAnimation = new Animation(832);
    /**
     * A 2D array defining locations of the deathly mages who protect the keystone.
     */
    private static final Location[][] mageSpawnLocations = new Location[][] {new Location[] {new Location(3285, 5363, 1), new Location(3287, 5363, 1)}, new Location[] {new Location(3317, 5363, 1), new Location(3319, 5363, 1)}, new Location[] {new Location(3340, 5365, 1), new Location(3340, 5367, 1)}};
    /**
     * A 2D array defining the locations of the deathly rangers who protect the keystone.
     */
    private static final Location[][] rangerSpawnLocations = new Location[][] {new Location[] {new Location(3284, 5370, 1), new Location(3286, 5370, 1)}, new Location[] {new Location(3316, 5370, 1), new Location(3318, 5370, 1)}, new Location[] {new Location(3333, 5364, 1), new Location(3333, 5366, 1)}};
    /**
     * A array containing the barrier objects and their locations.
     */
    private static final WorldObject[] barrierLocations = new WorldObject[] {new WorldObject(29749, 10, 0, 3270, 5360, 1), new WorldObject(29749, 10, 1, 3309, 5368, 1), new WorldObject(29749, 10, 2, 3355, 5360, 1)};
    /**
     * A list of the NPCs protecting the keystone.
     */
    private final List<DeathlyNPC> npcs = new ArrayList<>();
    /**
     * The barrier object that blocks the exit.
     */
    private WorldObject barrier;
    /**
     * The Agility requirement for crossing the tightrope. The level is 'lowest agility level in the party' + random(2), capped at the highest agility level in the party.
     */
    private int requirement;

    public DeathlyRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator().appendNPCs(npcs);
    }

    @Override
    public void loadRoom() {
        Pair<Integer, Integer> rangersMagers = ScalingMechanics.getDeathlyMobCount(raid);
        final int rangersAmount = rangersMagers.getFirst();
        final int magersAmount = rangersMagers.getSecond();
        final Location[] mages = mageSpawnLocations[index];
        final Location[] rangers = rangerSpawnLocations[index];
        for (int i = 0; i < magersAmount; i++) {
            npcs.add((DeathlyNPC) new DeathlyNPC(raid, this, NpcId.DEATHLY_MAGE, getLocation(mages[Utils.random(mages.length - 1)])).spawn());
        }
        for (int i = 0; i < rangersAmount; i++) {
            npcs.add((DeathlyNPC) new DeathlyNPC(raid, this, NpcId.DEATHLY_RANGER, getLocation(rangers[Utils.random(rangers.length - 1)])).spawn());
        }
        final WorldObject barrier = barrierLocations[index];
        World.spawnObject(this.barrier = new WorldObject(barrier.getId(), barrier.getType(), (barrier.getRotation() + getRotation()) & 3, getObjectLocation(barrier, 1, 2, (barrier.getRotation() + getRotation()) & 3)));
        this.requirement = Math.min(getLowestLevel(SkillConstants.AGILITY) + Utils.random(2), getHighestLevel(SkillConstants.AGILITY));
    }

    /*@Override
    public boolean canPass(final Player player, final WorldObject object) {
        if (World.containsObjectWithId(barrier, 29749)) {
            player.sendMessage("You need to dispel the barrier before you can move forward.");
            return false;
        }
        return true;
    }*/
    @Override
    public String name() {
        return "Chambers of Xeric: Deathly room";
    }

    public List<DeathlyNPC> getNpcs() {
        return npcs;
    }

    public int getRequirement() {
        return requirement;
    }
}
