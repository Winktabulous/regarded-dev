package com.zenyte.game.content.minigame.inferno.npc.impl.zuk;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.content.minigame.inferno.npc.impl.JalTokJad;
import com.zenyte.game.content.minigame.inferno.npc.impl.JalXil;
import com.zenyte.game.content.minigame.inferno.npc.impl.JalZek;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
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
import com.zenyte.game.world.entity.npc.CombatScriptsHandler;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.utils.TimeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommeh | 29/11/2019 | 19:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class TzKalZuk extends InfernoNPC {
    private static final Location spawnLocation = new Location(2268, 5364, 0);
    private static final Location jalZekSpawnLocation = new Location(2266, 5350, 0);
    private static final Location jalXilSpawnLocation = new Location(2275, 5351, 0);
    private static final Location jadSpawnLocation = new Location(2270, 5347, 0);
    private static final SoundEffect attackSound = new SoundEffect(162, 15, 0);
    private static final SoundEffect landSound = new SoundEffect(163, 8, 0);
    private static final Location[] healerSpawnLocations = {new Location(2262, 5363, 0), new Location(2266, 5363, 0), new Location(2276, 5363, 0), new Location(2280, 5363, 0)};
    private static final Graphics healGfx = new Graphics(444, 0, 250);
    private static final Animation attackAnimation = new Animation(7566);
    private static final Projectile attackProjectile = new Projectile(1375, 80, 33, 60, 10, 30, 0, 2);
    private AncestralGlyph glyph;
    private boolean healing;
    private int ticks;
    private final List<JalMejJak> healers = new ArrayList<>(4);
    private boolean started;
    private boolean spawnedJad;
    private boolean spawnedHealers;
    private boolean increasedDelay;
    private long xilAndZekSpawnDelay;

    public TzKalZuk(final Inferno instance) {
        super(7706, instance.getLocation(spawnLocation), instance);
        setAttackDistance(64);
        combat = new NPCCombat(this) {
            @Override
            public int combatAttack() {
                if (target == null) {
                    return 0;
                }
                if (npc.isProjectileClipped(target, false)) {
                    return 0;
                }
                int distance = npc.getAttackDistance();
                if (CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize())) {
                    return 0;
                }
                if (outOfRange(target, distance, target.getSize(), false)) {
                    return 0;
                }
                return CombatScriptsHandler.specialAttack(npc, target);
            }
        };
    }

    @Override
    public boolean isFlinchable() {
        return false;
    }

    @Override
    public void setFaceEntity(final Entity entity) {
    }

    @Override
    public boolean setHitpoints(final int amount) {
        final boolean setHitpoints = super.setHitpoints(amount);
        if (inferno != null) {
            final Player player = inferno.getPlayer();
            player.getVarManager().sendBit(5653, getHitpoints());
        }
        return setHitpoints;
    }

    public void start() {
        combat.setCombatDelay(15);
        started = true;
        xilAndZekSpawnDelay = TimeUnit.SECONDS.toTicks(45);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        ticks++;
        if (!started) {
            return;
        }
        if (!increasedDelay && getHitpoints() <= 600) {
            xilAndZekSpawnDelay += TimeUnit.SECONDS.toTicks(105);
            increasedDelay = true;
        }
        if (!(getHitpoints() >= 480 && getHitpoints() <= 600) && xilAndZekSpawnDelay > 0) {
            xilAndZekSpawnDelay--;
        }
        if (xilAndZekSpawnDelay == 0) {
            final JalXil xil = (JalXil) new JalXil(inferno.getLocation(jalXilSpawnLocation), inferno).spawn();
            final JalZek zek = (JalZek) new JalZek(inferno.getLocation(jalZekSpawnLocation), inferno).spawn();
            xil.setTarget(glyph);
            xil.getCombat().setCombatDelay(8);
            zek.setTarget(glyph);
            zek.getCombat().setCombatDelay(8);
            inferno.add(xil, zek);
            xilAndZekSpawnDelay = TimeUnit.SECONDS.toTicks(210);
        }
        if (!spawnedJad && getHitpoints() <= 480) {
            final JalTokJad jad = new JalTokJad(inferno.getLocation(jadSpawnLocation), inferno);
            jad.setFaceEntity(this);
            jad.spawn();
            jad.setTarget(glyph);
            jad.getCombat().setCombatDelay(8);
            inferno.add(jad);
            spawnedJad = true;
        } else if (!spawnedHealers && getHitpoints() <= 240) {
            inferno.getPlayer().sendMessage("TzKal-Zuk has become enraged and is fighting for his life.");
            combatDefinitions.setAttackSpeed(7);
            for (final Location location : healerSpawnLocations) {
                healers.add((JalMejJak) new JalMejJak(inferno.getLocation(location), inferno, this).spawn());
            }
            spawnedHealers = true;
        }
        if (healing) {
            if (ticks % 4 == 0) {
                setGraphics(healGfx);
            }
            boolean check = false;
            for (final JalMejJak healer : healers) {
                final Entity target = healer.getCombat().getTarget();
                if (target != null && target.equals(this)) {
                    check = true;
                    break;
                }
            }
            healing = check;
        }
    }

    @Override
    public int attack(final Player player) {
        setAnimation(attackAnimation);
        if (isProtected(player)) {
            World.sendProjectile(this, glyph, attackProjectile);
            World.sendSoundEffect(getMiddleLocation(), attackSound);
            WorldTasksManager.schedule(() -> {
                glyph.setAnimation(new Animation(7568));
                World.sendSoundEffect(glyph.getMiddleLocation(), landSound);
            }, attackProjectile.getTime(this, glyph));
        } else {
            combat.addAttackedByDelay(player);
            World.sendSoundEffect(getMiddleLocation(), attackSound);
            delayHit(World.sendProjectile(this, player, attackProjectile), player, new Hit(this, Utils.random(combatDefinitions.getMaxHit()), HitType.REGULAR).onLand(hit -> World.sendSoundEffect(player.getLocation(), landSound)));
        }
        return combatDefinitions.getAttackSpeed();
    }

    private boolean isProtected(final Player player) {
        if (glyph.isFinished() || player.getY() < inferno.getMinShieldY() || player.getY() > inferno.getMaxShieldY()) {
            return false;
        }
        final double distanceX = Math.sqrt(Math.pow(player.getX() - (glyph.getNextPosition(1).getX() + 1), 2));
        return distanceX <= 2;
    }

    @Override
    protected void sendNotifications(final Player player) {
        if (!inferno.isPracticeMode()) {
            super.sendNotifications(player);
        }
    }

    @Override
    protected void onDeath(final Entity source) {
        super.onDeath(source);
        for (final JalMejJak healer : healers) {
            healer.sendDeath();
        }
        inferno.leave(true);
    }

    @Override
    public void onFinish(final Entity source) {
        reset();
        finish();
        if (source instanceof final Player player) {
            sendNotifications(player);
            player.getCombatAchievements().checkKcTask("tzkal-zuk", 1, CAType.INFERNO_GRANDMASTER);
        }
    }

    public void setGlyph(AncestralGlyph glyph) {
        this.glyph = glyph;
    }

    public void setHealing(boolean healing) {
        this.healing = healing;
    }
}
