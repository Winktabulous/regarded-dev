package com.zenyte.game.content.kebos.konar.npc;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.Dragonfire;
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.DragonfireProtection;
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.DragonfireType;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.AnimationDefinitions;

/**
 * @author Tommeh | 15/10/2019 | 22:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Drake extends NPC implements CombatScript, Spawnable {
    private static final Animation meleeAttackAnim = new Animation(8275);
    private static final Animation rangedAttackAnim = new Animation(8276);
    private static final Projectile rangedAttackProj = new Projectile(1636, 22, 33, 37, 20, 20, 0, 5);
    private static final Projectile volcanicFlameProj = new Projectile(1637, 22, 10, 37, 35, 80, 0, 5);
    private static final Graphics volcanicFlameOnHitGfx = new Graphics(1638);
    private static final Animation deathAnim = new Animation(8278);
    private int attacks;

    public Drake(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        attacks = 0;
    }

    @Override
    public void setAnimation(final Animation animation) {
        this.animation = animation;
        if (animation == null) {
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    private static final Dragonfire.MessageBuilder builder = (protections, percentage, builder) -> {
        if (protections.isEmpty()) {
            builder.append("You are horribly burnt by the %s!");
        } else {
            builder.append("Your ");
            builder.append(protections.get(0).getProtectionName());
            builder.append(" helps to protect you from the %s!");
        }
    };

    @Override
    public int attack(Entity target) {
        if (attacks >= 7) {
            if (target instanceof Player) {
                final Player player = (Player) target;
                final Location location = new Location(target.getLocation());
                setAnimation(rangedAttackAnim);
                final Dragonfire.DragonfireBuilder dragonfire = new Dragonfire.DragonfireBuilder(DragonfireType.CHROMATIC_DRAGONFIRE, 8, DragonfireProtection.getProtection(this, player), builder) {
                    @Override
                    public int getDamage() {
                        final float tier = getAccumulativeTier();
                        return tier == 0 ? Utils.random(6, 8) : tier <= 1 ? Utils.random(3, 4) : 0;
                    }
                };
                World.sendProjectile(this, location, volcanicFlameProj);
                WorldTasksManager.schedule(new WorldTask() {
                    int ticks;
                    boolean volcanicFlame;
                    @Override
                    public void run() {
                        if (volcanicFlame) {
                            if (ticks > 3) {
                                stop();
                                return;
                            }
                            delayHit(Drake.this, -1, player, new Hit(Drake.this, dragonfire.getDamage(), HitType.REGULAR).onLand(hit -> player.sendFilteredMessage(String.format(dragonfire.getMessage(), "Drake's volcanic breath"))));
                            ticks++;
                            return;
                        }
                        if (target.getLocation().matches(location)) {
                            volcanicFlame = true;
                        } else {
                            World.sendGraphics(volcanicFlameOnHitGfx, location);
                            stop();
                        }
                    }
                }, volcanicFlameProj.getTime(this, location), 0);
            }
            attacks = 0;
        } else {
            if (Utils.random(2) == 0 && isWithinMeleeDistance(this, target)) {
                setAnimation(meleeAttackAnim);
                delayHit(1, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), CRUSH, target), HitType.MELEE));
            } else {
                setAnimation(rangedAttackAnim);
                delayHit(World.sendProjectile(this, target, rangedAttackProj), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), AttackType.RANGED, target), HitType.RANGED));
            }
            attacks++;
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void sendDeath() {
        final Player source = getMostDamagePlayerCheckIronman();
        if (source == null) {
            super.sendDeath();
            return;
        }
        onDeath(source);
        lock();
        final int id = getId();
        setAnimation(deathAnim);
        setTransformation(8613);
        WorldTasksManager.schedule(new WorldTask() {
            private int loop;
            @Override
            public void run() {
                if (loop == 1) {
                    setId(id);
                    onFinish(source);
                    unlock();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 8612 || id == 8613;
    }
}
