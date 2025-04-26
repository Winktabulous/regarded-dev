package com.zenyte.game.content.minigame.fightcaves.npcs;

import com.zenyte.game.content.minigame.fightcaves.FightCaves;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.GlobalAreaManager;

/**
 * @author Kris | 8. nov 2017 : 21:35.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class TzTokJad extends FightCavesNPC implements CombatScript {
    public static final Animation MELEE_ANIM = new Animation(2655);
    public static final Animation RANGED_ANIM = new Animation(2652);
    public static final Animation MAGIC_ANIM = new Animation(2656);
    public static final Graphics RANGED_GFX = new Graphics(451);
    public static final Graphics MAGIC_GFX = new Graphics(157, 0, 96);
    public static final SoundEffect meleeAttackSound = new SoundEffect(408);
    public static final SoundEffect rangedAttackSound = new SoundEffect(163);
    public static final SoundEffect mageAttackSound = new SoundEffect(162);
    public static final SoundEffect mageLandSound = new SoundEffect(163);
    public static final Projectile MAGIC_PROJ_HEAD = new Projectile(448, 140, 20, 80, 5, 50, 0, 0);
    public static final Projectile MAGIC_PROJ_BODY = new Projectile(449, 140, 20, 85, 5, 50, 0, 0);
    public static final Projectile MAGIC_PROJ_TRAIL = new Projectile(450, 140, 20, 90, 5, 50, 0, 0);

    public static final Projectile[] MAGIC_PROJECTILES = new Projectile[] {MAGIC_PROJ_HEAD, MAGIC_PROJ_BODY, MAGIC_PROJ_TRAIL};
    private final int maximumHealth = getMaxHitpoints() >> 1;
    private boolean spawned;
    private boolean offPrayerHit;

    TzTokJad(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
        super(npc, tile, caves);
        setAttackDistance(15);
    }

    @Override
    protected void postHitProcess(Hit hit) {
        if (isDead()) return;
        if (!spawned && getHitpoints() < maximumHealth) {
            spawned = true;
            final int size = caves.getMonsters().size() - 1;
            for (int i = 0; i < (4 - size); i++) {
                caves.spawnNPC(TzHaarNPC.YT_HUR_KOT);
            }
        }
    }

    @Override
    public void heal(final int amount) {
        if (isDead() || isFinished()) {
            return;
        }
        super.heal(amount);
        if (getHitpoints() >= getMaxHitpoints()) {
            spawned = false;
        }
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        caves.getPlayer().lock(5);
        caves.getPlayer().blockIncomingHits(5);
        WorldTasksManager.schedule(() -> caves.getPlayer().setAnimation(Emote.CHEER.getAnimation()), 3);
        caves.finishTracking();
        if (offPrayerHit) {
            caves.getPlayer().getCombatAchievements().complete(CAType.A_NEAR_MISS);
        }
    }

    @Override
    public void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getCombatAchievements().complete(CAType.FIGHT_CAVES_VETERAN);
            player.getCombatAchievements().checkKcTask("tztok-jad", 5, CAType.FIGHT_CAVES_MASTER);
        }
    }

    @Override
    protected void drop(Location tile) {
        /* empty */
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (hit.getDamage() > 0) {
            offPrayerHit = true;
        }
    }

    @Override
    public int attack(Entity target) {
        final int style = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 1);
        if (style == 2) {
            playSound(meleeAttackSound);
            setAnimation(MELEE_ANIM);
            delayHit(0, target, new Hit(this, getRandomMaxHit(this, 97, STAB, target), HitType.MELEE));
        } else if (style == 1) {
            setAnimation(RANGED_ANIM);
            playSound(meleeAttackSound);
            WorldTasksManager.schedule(() -> {
                if (targetInFightCaves(target)) {
                    target.setGraphics(RANGED_GFX);
                    delayHit(1, target, new Hit(this, getRandomMaxHit(this, 97, RANGED, target), HitType.RANGED).onLand(
                            (hit -> {
                                playSound(mageLandSound);
                                target.setGraphics(MAGIC_GFX);
                            })
                    ));
                }
            }, 2);

        } else {
            playSound(new SoundEffect(mageAttackSound.getId(), mageAttackSound.getRadius(), 20));
            setAnimation(MAGIC_ANIM);
            final int tickTime = MAGIC_PROJECTILES[0].getTime(this, target);
            final int clientTime = MAGIC_PROJECTILES[0].getProjectileDuration(this, target);
            for (final Projectile projectile : MAGIC_PROJECTILES) {
                World.sendProjectile(this, target, projectile);
            }
            target.setGraphics(new Graphics(MAGIC_GFX.getId(), clientTime, 96));
            playSound(new SoundEffect(mageLandSound.getId(), mageLandSound.getRadius(), clientTime));
            WorldTasksManager.schedule(() -> {
                if (targetInFightCaves(target))
                    delayHit(1, target, new Hit(this, getRandomMaxHit(this, 97, MAGIC, target), HitType.MAGIC));
            }, Math.max(tickTime - 1, 0));
        }
        return combatDefinitions.getAttackSpeed();
    }

    private static boolean targetInFightCaves(Entity target) {
        return target instanceof Player playerTarget && playerTarget.getArea() instanceof FightCaves;
    }
}
