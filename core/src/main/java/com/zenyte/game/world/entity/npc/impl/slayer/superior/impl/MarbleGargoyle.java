package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.Gargoyle;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.AnimationDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MarbleGargoyle extends SuperiorNPC implements CombatScript {
    private static final Projectile stoneProjectile = new Projectile(1453, 50, 30, 40, 0, 80, 64, 5);
    private static final Projectile rockProjectile = new Projectile(276, 50, 30, 30, 0, 30, 64, 5);

    public MarbleGargoyle(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7407, tile);
    }

    private boolean dying;

    @Override
    public int attack(final Entity target) {
        final int attack = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 1);
        if (attack == 0) {
            setAnimation(new Animation(7815));
            final Location tile = new Location(target.getLocation());
            final int delay = World.sendProjectile(this, tile, stoneProjectile);
            WorldTasksManager.schedule(() -> {
                if (target.getLocation().matches(tile)) {
                    delayHit(-1, target, new Hit(Utils.random(38), HitType.REGULAR).onLand(hit -> {
                        target.stun(4);
                        if (target instanceof Player) {
                            ((Player) target).sendMessage("You have been trapped in stone!");
                        }
                    }));
                }
            }, delay);
            return 6;
        } else if (attack == 1) {
            setAnimation(new Animation(7814));
            delayHit(World.sendProjectile(this, target, rockProjectile), target, ranged(target, 38));
        } else if (attack == 2) {
            setAnimation(new Animation(7814));
            delayHit(0, target, melee(target, 38));
        }
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public NPC spawn() {
        dying = false;
        return super.spawn();
    }

    private static final Item ROCK_HAMMER = new Item(4162);

    @Override
    public void sendDeath() {
        if (dying) {
            return;
        }
        final Player source = getMostDamagePlayerCheckIronman();
        if (source == null) {
            super.sendDeath();
            return;
        }
        final boolean isUnlocked = source.getSlayer().isUnlocked("Gargoyle smasher");
        if (getHitpoints() == 0 && (!isUnlocked || (!source.getInventory().containsItem(ROCK_HAMMER) && !source.getInventory().containsItem(Gargoyle.GRANITE_HAMMER) && !source.getInventory().containsItem(21754, 1)))) {
            heal(1);
        } else {
            dying = true;
            onDeath(getMostDamagePlayerCheckIronman());
            lock(3);
            setAnimation(new Animation(7813));
            final int id = getId();
            setTransformation(7408);
            if (!source.getInventory().containsItem(ROCK_HAMMER) && !source.getInventory().containsItem(Gargoyle.GRANITE_HAMMER)) {
                source.getInventory().deleteItem(21754, 1);
            }
            WorldTasksManager.schedule(new WorldTask() {
                private int loop;
                @Override
                public void run() {
                    if (loop == 1) {
                        setId(id);
                        onFinish(source);
                        setId(id);
                        stop();
                    }
                    loop++;
                }
            }, 0, 1);
            source.sendFilteredMessage("The gargoyle cracks apart.");
        }
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
}
