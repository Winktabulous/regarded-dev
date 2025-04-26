package com.zenyte.game.content.area.prifddinas.zalcano.combat.impl;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import com.zenyte.game.content.area.prifddinas.zalcano.combat.ZalcanoAttack;
import com.zenyte.game.content.area.prifddinas.zalcano.formation.ZalcanoRockFormation;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;

import java.util.Optional;

public class HitFormationAttack implements ZalcanoAttack {

    private static final Animation ANIM = new Animation(8432);
    private static final Animation EXPLODE_ANIMATION = new Animation(8449);
    private static final Projectile PROJECTILE = new Projectile(1728, 80, 40, 0, 15, 35, 64, 7);

    @Override
    public void execute(ZalcanoInstance instance) {
        Optional<ZalcanoRockFormation> getLastFormation = instance.getRockFormationHandler().getActiveFormation();
        if (getLastFormation.isEmpty()) {
            instance.activateFormation();
            return;
        }

        var foundFormation = getLastFormation.get();

        instance.getZalcano().setNextHitFormationCycle((int) (WorldThread.getCurrentCycle() + 35));

        instance.getZalcano().faceObject(foundFormation);
        instance.getZalcano().setAnimation(ANIM);
        instance.getZalcano().freeze(5);
        WorldTasksManager.schedule(() -> {
            World.sendProjectile(new Location(instance.getZalcano().getMiddleLocation()), foundFormation.getMiddleLocation(), PROJECTILE);
        }, 1);

        WorldTasksManager.schedule(() -> {
            World.sendObjectAnimation(foundFormation, EXPLODE_ANIMATION);
            instance.getRockFormationHandler().damagePlayers();
        }, 5);

        WorldTasksManager.schedule(() -> {
            instance.getRockFormationHandler().switchActivateFormation();
        }, 6);

    }

    @Override
    public boolean canProcess(ZalcanoInstance instance) {
        return instance.getZalcano().getNextHitFormationCycle() < WorldThread.getCurrentCycle();
    }

    @Override
    public void interrupt() {}
}
