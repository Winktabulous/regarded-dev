package com.zenyte.game.content.xamphur;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

public class PhantomHand extends NPC implements CombatScript {
    public PhantomHand(int id, Location tile, Direction facing) {
        super(id, tile, facing, 0);
        this.attackDistance = 25;
        this.maxDistance = 25;
        this.aggressionDistance = 25;
        checkAggressivity();
    }

    @Override
    public int attack(Entity target) {
        delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, this.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        setAnimation(getCombatDefinitions().getAttackAnim());
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void handleOutgoingHit(Entity target, Hit hit) {
        if(!(target instanceof Player))
            return;

        Player player = ((Player) target);

        if(hit.getDamage() > 0)
            PhantomHandCorruptionKt.applyCorruptionEffect(player);

        super.handleOutgoingHit(target, hit);
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
        Xamphur.handNpcs.remove(this);
    }

    @Override
    public void setRespawnTask() {

    }
}
