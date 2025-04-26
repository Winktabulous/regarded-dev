package com.zenyte.game.world.entity.npc.combat.impl;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType;
import com.zenyte.game.world.object.WorldObject;

import java.util.EnumSet;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CombatDummy extends NPC implements Spawnable {

    public CombatDummy(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override public boolean isAlwaysTakeMaxHit(HitType type) { return true; }

    @Override
    protected boolean isMovableEntity() {
        return false;
    }

    @Override
    public boolean isMultiArea() {
        return true;
    }

    @Override
    public boolean isForceAttackable() {
        return true;
    }

    @Override
    public float getXpModifier(Hit hit) {
        return 0;
    }

    @Override
    public void addHitbar() {

    }

    public void heal(final int amount) {

    }

    @Override
    public void removeHitpoints(final Hit hit) {

    }

    @Override
    protected void updateCombatDefinitions() {
        super.updateCombatDefinitions();
        combatDefinitions.setHitpoints(Short.MAX_VALUE);
        combatDefinitions.setImmunityTypes(EnumSet.allOf(ImmunityType.class));
        setHitpoints(combatDefinitions.getHitpoints());
    }

    @Override
    public boolean ignoreUnderneathProjectileCheck() {
        return true;
    }

    private WorldObject dummyObject;

    @Override
    public NPC spawn() {
        final NPC npc = super.spawn();
        dummyObject = new WorldObject(0, 10, 0, getLocation());
        World.spawnObject(dummyObject);
        return npc;
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        World.removeObject(dummyObject);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.COMBAT_DUMMY_16019 || id == NpcId.UNDEAD_COMBAT_DUMMY_16020;
    }
}
