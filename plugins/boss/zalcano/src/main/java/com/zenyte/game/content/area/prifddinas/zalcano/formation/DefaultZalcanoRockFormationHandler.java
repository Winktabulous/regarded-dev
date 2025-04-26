package com.zenyte.game.content.area.prifddinas.zalcano.formation;


import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CAType;

import java.util.Optional;

/**
 * This is the default behaviour for a zalcano instanced rock formation
 */
public class DefaultZalcanoRockFormationHandler implements ZalcanoRockFormationHandler {

    private static final Location NORTH_EAST = new Location(3040, 6057);
    private static final Location NORTH_WEST = new Location(3025, 6057);
    private static final Location SOUTH_WEST = new Location(3025, 6040);
    private static final Location SOUTH_EAST = new Location(3040, 6040);

    private final ZalcanoRockFormation[] formations = new ZalcanoRockFormation[4];
    private final ZalcanoInstance instance;



    public DefaultZalcanoRockFormationHandler(ZalcanoInstance instance) {
        this.instance = instance;
        formations[0] = new ZalcanoRockFormation(3, NORTH_EAST); // NE
        formations[1] = new ZalcanoRockFormation(2, NORTH_WEST); // NW
        formations[2] = new ZalcanoRockFormation(1, SOUTH_WEST); // SW
        formations[3] = new ZalcanoRockFormation(0, SOUTH_EAST); // SE
    }

    @Override
    public ZalcanoRockFormation[] formations() {
        return formations;
    }

    @Override
    public int lastActiveFormation() {
        for (int i = 0; i < formations.length; i++) {
            if (formations[i].getId() == ZalcanoRockFormations.GLOWING.getObjectId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Optional<ZalcanoRockFormation> getActiveFormation() {
        var last = lastActiveFormation();
        if (last < 0) return Optional.empty();
        return Optional.of(formations[last]);
    }

    @Override
    public void deactivateLast() {
        var last = lastActiveFormation();
        deactivate(last);
    }

    @Override
    public void deactivate(int index) {
        if (index < 0 || index >= formations.length) {
            return;
        }
        var formation = formations[index];
        setAndSpawnFormation(index, ZalcanoRockFormations.REGULAR);
    }

    @Override
    public void activateFormation(int index) {
        if (index < 0 || index >= formations.length) {
            return;
        }
        setAndSpawnFormation(index, ZalcanoRockFormations.GLOWING);
    }

    @Override
    public void damagePlayers() {
        var formation = getActiveFormation();

        if (formation.isEmpty()) {
            return;
        }
        for (Player player : instance.getPlayers()) {
            if (formation.get().getMiddleLocation().getTileDistance(player.getPosition()) == 2) { // HERE
                var damageToRecieve = Utils.random(24, 35);
                if (damageToRecieve >= player.getHitpoints()) {
                    damageToRecieve = player.getHitpoints();
                }
                CombatUtilities.processHit(player, new Hit(instance.getZalcano(), damageToRecieve, HitType.DEFAULT));
                player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, 0);
            }
        }
    }


    @Override
    public void switchActivateFormation() {
        int lastActive =  lastActiveFormation();
        int randomIndex = Utils.random(0, formations.length - 1);
        while(randomIndex == lastActive) {
            randomIndex = Utils.random(0, formations.length - 1);
        }

        deactivateLast();
        activateFormation(randomIndex);
    }

    @Override
    public void depleteAllFormations() {
        formations[0] = new ZalcanoRockFormation(3, NORTH_EAST); // NE
        formations[1] = new ZalcanoRockFormation(2, NORTH_WEST); // NW
        formations[2] = new ZalcanoRockFormation(1, SOUTH_WEST); // SW
        formations[3] = new ZalcanoRockFormation(0, SOUTH_EAST); // SW

        World.spawnObject(formations[0]);
        World.spawnObject(formations[1]);
        World.spawnObject(formations[2]);
        World.spawnObject(formations[3]);
    }

    @Override
    public void deactivateAllFormations() {
        formations[0] = new ZalcanoRockFormation(ZalcanoRockFormations.REGULAR, 3, NORTH_EAST); // NE
        formations[1] = new ZalcanoRockFormation(ZalcanoRockFormations.REGULAR, 2, NORTH_WEST); // NW
        formations[2] = new ZalcanoRockFormation(ZalcanoRockFormations.REGULAR, 1, SOUTH_WEST); // SW
        formations[3] = new ZalcanoRockFormation(ZalcanoRockFormations.REGULAR, 0, SOUTH_EAST); // SW

        World.spawnObject(formations[0]);
        World.spawnObject(formations[1]);
        World.spawnObject(formations[2]);
        World.spawnObject(formations[3]);
    }


    private void setAndSpawnFormation(int index, ZalcanoRockFormations formation) {
        var lastPosition = formations[index].getPosition();
        var lastRotation = formations[index].getRotation();

        formations[index] = new ZalcanoRockFormation(formation, lastRotation, lastPosition);
        World.spawnObject(formations[index]);
    }

}
