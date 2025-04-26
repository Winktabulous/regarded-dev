package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.world.entity.npc.NpcId.BASILISK_SENTINEL;

public class BasiliskSentinel extends SuperiorNPC implements CombatScript {

    private static final int COOLDOWN_TICKS = 50;
    private int lastSpecial;

    public BasiliskSentinel(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, BASILISK_SENTINEL, tile);
    }

    private static final Projectile PROJECTILE = new Projectile(1735, 18, 25, 0, 55);
    private static final Projectile NO_SHIELD = new Projectile(1739, 18, 25, 0, 55);
    private static final Graphics SPLASH_PROJECTILE = new Graphics(1736, 0, 124);



    @Override
    public int attack(final Entity target) {
        final BasiliskSentinel npc = this;
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;

        if(lastSpecial >=  COOLDOWN_TICKS && Utils.random(1) == 0) {
            lastSpecial = 0;
            return specialAttack(player);
        }



        boolean melee = isWithinMeleeDistance(this, player) && Utils.random(1) == 0;
        attackSound();
        if (!SlayerEquipment.MIRROR_SHIELD.isWielding(player)) {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            delayHit(npc, 0, player, new Hit(npc, 13, HitType.REGULAR));
            WorldTasksManager.schedule(() -> {
                if (!player.getLocation().withinDistance(target.getLocation(), 15)) {
                    return;
                }
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.4078947368421053));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.4078947368421053));
                    }
                }
            }, World.sendProjectile(npc, target, NO_SHIELD));
        } else {
            npc.setAnimation(new Animation(8500));
            if(melee) {
                delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
            } else {
                int delay = World.sendProjectile(this, player, PROJECTILE);
                delayHit(this, delay, player, new Hit(this, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).
                        onLand((hit) -> player.setGraphics(SPLASH_PROJECTILE)));
            }
        }
        lastSpecial++;
        return npc.getCombatDefinitions().getAttackSpeed();
    }
    private static final Animation STONE_START = new Animation(8503);
    private static final Animation STONE_MIDDLE = new Animation(8504);
    private static final Animation STONE_END = new Animation(8507);



    private static final Projectile SPECIAL_PROJECTILE = new Projectile(1744, 18, 25, 50, 55);
    private static final Graphics SPECIAL_SPLASH = new Graphics(1738, 0, 124);
    private static final Graphics FREEZE_END_GFX = new Graphics(1743, 0, 0);
    public static final String FROZEN_ATTR = "BREAK_CLICKS";




    public int specialAttack(final Player player) {
        Location aoeLocation = player.getLocation().copy();
        int delay = World.sendProjectile(this, aoeLocation, SPECIAL_PROJECTILE);
        setAnimation(new Animation(8500));

        player.addTemporaryAttribute(FROZEN_ATTR, 1);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if(player.getNumericTemporaryAttribute(FROZEN_ATTR).intValue() >= 5) {
                    ticks = 9;//break free
                }
                if(ticks == 1) {
                    if(!player.getLocation().equals(aoeLocation)) {
                        stop();
                        return;
                    }
                    player.sendMessage("You have been entombed in stone.");
                    player.setGraphics(SPECIAL_SPLASH);
                    player.setAnimation(STONE_START);
                    player.freeze(10);
                } else if(ticks > 1 && ticks <= 8) {
                    player.setAnimation(STONE_MIDDLE);
                } else if(ticks == 9) {
                    player.setGraphics(FREEZE_END_GFX);
                    player.setAnimation(STONE_END);
                    player.unlock();
                    player.resetFreeze();
                    stop();
                    return;
                }
                ticks++;
            }
        }, delay, 0);

        return 12;//double it's usual attack time, couldn't find a video to
    }
}
