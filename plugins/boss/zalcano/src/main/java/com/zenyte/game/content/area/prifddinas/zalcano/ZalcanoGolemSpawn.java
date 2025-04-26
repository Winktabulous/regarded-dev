package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

public class ZalcanoGolemSpawn extends NPC implements CombatScript, Spawnable {

    public ZalcanoInstance instance;
    public Location lastZalcanoLocation;

    public ZalcanoGolemSpawn(int id, Location tile, Direction facing, int radius) {
      super(id, tile, facing, radius);
    }

    public ZalcanoGolemSpawn(Location tile, Direction facing, ZalcanoInstance instance) {
        super(ZalcanoConstants.ZALCANO_GOLEM, tile, facing, 1);
        this.instance = instance;
        this.lastZalcanoLocation = instance.getZalcano().getLocation().copy();

        this.lock(1);
        this.unclip();
        this.faceEntity(instance.getZalcano());

        this.addWalkSteps(
                lastZalcanoLocation.getX(),
                lastZalcanoLocation.getY());


        this.combatDefinitions.setHitpoints(50);
        this.hitpoints = 50;
    }

    @Override
    public int getMaxHitpoints() {
        return 50;
    }

    @Override
    public NPC spawn() {
        super.spawn();
        combatDefinitions.setAggressionType(null);
        return this;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == ZalcanoConstants.ZALCANO_DEFAULT || id == ZalcanoConstants.ZALCANO_MINABLE;
    }

    @Override
    public boolean checkAggressivity() {
        return false;
    }

    @Override
    public int attack(Entity target) {
        return -1;
    }

    @Override
    public void faceEntity(Entity target) {
        super.faceEntity(instance.getZalcano());
    }

    @Override
    public void processNPC() {
        if (isDead()) return;
        super.processNPC();


        this.setInteractingWith(instance.getZalcano());
        this.resetWalkSteps();
        this.addWalkSteps(
                instance.getZalcano().getX(),
                instance.getZalcano().getY());

        if (CombatUtilities.isWithinMeleeDistance(instance.getZalcano(), this )) {
            //TODO: heal the boss
            this.setAnimation(new Animation(8410));

            this.instance.removeAndFinishGolem(this);
            var hitpoints = getHitpoints();
            this.setHitpoints(0);

            if (instance.getZalcano().isShieldPhase()) {
                instance.getZalcano().heal(hitpoints);
            }
        }
    }


    @Override
    public boolean isMultiArea() {
        return true;
    }

    @Override
    public void autoRetaliate(Entity source) {
        super.autoRetaliate(null);
    }

    @Override
    public boolean matches(Position other) {
        return super.matches(other);
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
    }

}
