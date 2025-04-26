package com.zenyte.game.content.boss.wildernessbosses.spiders.spindel;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Cresinkel
 */
public class SpindelSpiderling extends NPC implements CombatScript {


    private final Spindel spindel;

    public SpindelSpiderling(Location tile, int radius, Spindel spindel) {
        super(NpcId.SPINDELS_SPIDERLING, tile, Direction.NORTH, radius);
        setSpawned(true);
        this.spindel = spindel;
        spindel.spiderlingAlive++;
        setAttackDistance(25);
        setAggressionDistance(25);
        setMaxDistance(50);
    }

    @Override
    public void processNPC() {
        if (spindel.isDead() || spindel.isDying() || spindel.isFinished()) {
            sendDeath();
            return;
        }
        super.processNPC();
    }

    @Override
    public int attack(Entity target) {
        animate();
        attackSound();
        delayHit(0, target, melee(target, 3).onLand(hit -> {
            if (target instanceof Player player) {
                player.getPrayerManager().drainPrayerPoints(1);
                player.sendFilteredMessage("You feel yourself drained by the spiderling");
            }
        }));
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public void sendDeath() {
        spindel.spiderlingAlive--;
        super.sendDeath();
    }

    @Override
    public boolean isForceAttackable() {
        return true;
    }
}
