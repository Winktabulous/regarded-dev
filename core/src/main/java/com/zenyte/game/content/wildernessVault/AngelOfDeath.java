package com.zenyte.game.content.wildernessVault;

import com.zenyte.game.content.godwars.npcs.CommanderZilyana;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.CombatScriptsHandler;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

public class AngelOfDeath extends NPC implements CombatScript {

    public AngelOfDeath(Location location, Direction direction) {
        super(WildernessVaultConstants.MINION_ID, new Location(location), direction, 0);
        this.aggressionDistance = 64;
        this.maxDistance = 64;
        this.supplyCache = false;
        this.spawned = true;
        this.setForceFollowClose(true);
        this.combat = new NPCCombat(this) {
            @Override
            public int combatAttack() {
                if (target == null) {
                    return 0;
                }
                final boolean melee = isMelee();
                if (npc.isProjectileClipped(target, melee)) {
                    return 0;
                }
                if (CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize())) {
                    return 0;
                }
                addAttackedByDelay(target);
                return CombatScriptsHandler.specialAttack(npc, target);
            }

        };
    }

    @Override
    public boolean isForceAggressive() {
        return true;
    }

    @Override
    public int attack(final Entity target) {
        final boolean melee = target.getLocation().withinDistance(location, 1);
        final Hit hit;
        if (melee) {
            hit = melee(target, getCombatDefinitions().getMaxHit());
            setAnimation(CommanderZilyana.meleeAnimation);
            World.sendSoundEffect(getMiddleLocation(),CommanderZilyana.meleeAttackSound);
        } else {
            freeze(2);
            hit = ranged(target, getCombatDefinitions().getMaxHit()).onLand(i -> target.setGraphics(CommanderZilyana.magicGraphics));
            setAnimation(CommanderZilyana.magicAnimation);
            World.sendSoundEffect(new Location(target.getLocation()), CommanderZilyana.specialHittingSound);
        }
        delayHit(this, 0, target, hit);
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
        WildernessVaultHandler.getInstance().minionKilled(this);
    }
}
