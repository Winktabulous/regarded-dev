package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RSPolygon;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 18/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InstanceSpinolyp extends NPC implements CombatScript {
    public InstanceSpinolyp(@NotNull final RSPolygon polygon, final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        setForceAggressive(true);
        this.attackDistance = 15;
        this.aggressionDistance = 15;
        this.maxDistance = 30;
        this.polygon = polygon;
    }

    private final RSPolygon polygon;

    protected final boolean canMove(final int fromX, final int fromY, final int direction) {
        final int x = Utils.DIRECTION_DELTA_X[direction] + fromX;
        final int y = Utils.DIRECTION_DELTA_Y[direction] + fromY;
        return (World.getMask(getPlane(), x, y) & 65535) == 0 && !polygon.contains(x, y);
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean checkProjectileClip(final Player player, boolean melee) {
        return false;
    }

    private static final Projectile attackProjectile = new Projectile(294, 20, 25, 25, 30, 28, 5, 5);

    @Override
    public int attack(final Entity target) {
        final boolean ranged = Utils.random(1) == 0;
        setAnimation(combatDefinitions.getAttackAnim());
        if (ranged) {
            World.sendProjectile(this, target, attackProjectile);
            delayHit(this, World.sendProjectile(this, target, attackProjectile), target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.RANGED).onLand(hit -> {
                if (hit.getDamage() > 0) {
                    target.getToxins().applyToxin(Toxins.ToxinType.POISON, 6, this);
                }
            }));
        } else {
            delayHit(this, World.sendProjectile(this, target, CombatSpell.WATER_STRIKE.getProjectile()), target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.MAGIC).onLand(hit -> {
                if (hit.getDamage() > 0 && target instanceof Player) {
                    final Player player = ((Player) target);
                    if (player.getEquipment().getId(EquipmentSlot.SHIELD) == 12821) {
                        if (Utils.random(1) == 0) {
                            player.getPrayerManager().drainPrayerPoints(1);
                        }
                    } else {
                        player.getPrayerManager().drainPrayerPoints(1);
                    }
                }
            }));
        }
        return combatDefinitions.getAttackSpeed();
    }
}
