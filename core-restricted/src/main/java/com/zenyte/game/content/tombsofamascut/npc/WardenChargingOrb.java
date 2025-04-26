package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.WardenEncounter;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.WalkStep;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions
 */
public class WardenChargingOrb extends NPC {

    private static final int ID = 11769;
    private static final Animation MOVE_ANIMATION = new Animation(7571);
    private static final Animation CHARGE_ANIMATION = new Animation(9735);
    private static final SoundEffect COLLISION_SOUND = new SoundEffect(6595, 9);
    private final WardenEncounter encounter;
    private final StaticWardenNPC staticWardenNPC;
    private final Location[] path;
    private int pathIndex;
    private boolean charged;

    public WardenChargingOrb(Location tile, Direction facing, WardenEncounter encounter, StaticWardenNPC staticWardenNPC, Location[] path) {
        super(ID, tile, facing, 0);
        this.encounter = encounter;
        this.staticWardenNPC = staticWardenNPC;
        for (Location loc : path) {
            final Location instanceLoc = encounter.getLocation(loc);
            addWalkSteps(instanceLoc.getX(), instanceLoc.getY());
        }
        this.path = new Location[walkSteps.size()];
        while(!walkSteps.isEmpty()) {
            final int nextTileHash = walkSteps.remove();
            final int x = WalkStep.getNextX(nextTileHash);
            final int y = WalkStep.getNextY(nextTileHash);
            this.path[pathIndex++] = new Location(x, y, getPlane());
        }
        walkSteps.clear();
        pathIndex = 0;
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override
    public void processNPC() {
        if (charged) {
            finish();
            return;
        }
        if (pathIndex >= path.length) {
            if (staticWardenNPC != null) {
                staticWardenNPC.charge(encounter.isAncientHaste() ? 2 : 1);
            }
            setAnimation(CHARGE_ANIMATION);
            charged = true;
            return;
        }
        for (Player p : encounter.getChallengePlayers()) {
            if (p != null && getLocation().equals(p.getLocation())) {
                p.applyHit(new Hit(this, 3, HitType.DEFAULT));
                World.sendSoundEffect(getLocation(), COLLISION_SOUND);
                finish();
                return;
            }
        }
        final Location nextLocation = path[pathIndex++];
        setForceMovement(new ForceMovement(new Location(getLocation()), 0, nextLocation,
                30, Direction.getDirection(getLocation(), nextLocation).getDirection()));
        setLocation(nextLocation);
        setAnimation(MOVE_ANIMATION);
    }

    @Override public boolean isEntityClipped() { return false; }

    @Override public void setRespawnTask() {}

    @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

    @Override public void setFaceEntity(Entity entity) {}
}
