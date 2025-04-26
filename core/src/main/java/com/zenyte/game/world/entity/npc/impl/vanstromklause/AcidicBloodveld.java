package com.zenyte.game.world.entity.npc.impl.vanstromklause;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.calog.CAType;

public class AcidicBloodveld extends NPC {
    private static final SoundEffect preExplodeSound = new SoundEffect(232, 10);
    private static final SoundEffect explodeSound = new SoundEffect(208, 10);

    AcidicBloodveld(final Location tile, final Player target, final VanstromKlause vanstrom) {
        super(NpcId.ACIDIC_BLOODVELD, tile, Direction.SOUTH, 5);
        freeze(1);
        setForceMultiArea(true);
        setSpawned(true);
        this.target = target;
        this.vanstrom = vanstrom;
        this.supplyCache = false;
    }

    private final Player target;
    private final VanstromKlause vanstrom;
    private boolean exploding = false;

    @Override protected void onDeath(Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            ((Player) source).getCombatAchievements().complete(CAType.THE_DEMONIC_PUNCHING_BAG);
        }
    }

    @Override
    public void processNPC() {
        if (isDead() || target.isDead() || target.isFinished()) {
            return;
        }
        if (!exploding && getLocation().withinDistance(target.getLocation(), 1)) {
            exploding = true;
            final int damage = Utils.random(40, 45);
            explodeSound.sendGlobal(getLocation());
            World.sendGraphics(new Graphics(1791), getMiddleLocation());
            freeze(1);
            setAnimation(getCombatDefinitions().getDeathAnim());
            delay(1, this::sendDeath);
            if (vanstrom.isDead() || vanstrom.isFinished() || !getLocation().withinDistance(target.getLocation(), 30)) return;
            CombatUtilities.delayHit(this, -1, target, new Hit(damage, HitType.REGULAR));
            return;
        }
        if (getLocation().withinDistance(target.getLocation(), 2)) {
            preExplodeSound.sendGlobal(getLocation());
        }
        if (!isFrozen() && !hasWalkSteps()) {
            setFaceEntity(target);
            final RouteResult steps = RouteFinder.findRoute(getX(), getY(), getPlane(), getSize(), new TileStrategy(target.getX(), target.getY()), true);
            final int[] bufferX = steps.getXBuffer();
            final int[] bufferY = steps.getYBuffer();
            resetWalkSteps();
            for (int i = steps.getSteps() - 1; i >= 0; i--) {
                if (!addWalkStepsInteract(bufferX[i], bufferY[i], 25, 1, true)) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        if (hit.getWeapon() == CombatSpell.CRUMBLE_UNDEAD) {
            target.sendMessage("You become unfrozen as you kill the spawn.");
        }
    }

    @Override
    public float getXpModifier(final Hit hit) {
        if (hit.getWeapon() == CombatSpell.CRUMBLE_UNDEAD) {
            hit.setDamage(getHitpoints());
        }
        return 1;
    }

    @Override
    public void sendDeath() {
        super.sendDeath();
        vanstrom.spawnAcid(new Location(getLocation()));
        finish();
    }
}
