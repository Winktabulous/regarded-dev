package com.zenyte.game.content.chambersofxeric.npc;

import com.google.common.collect.Iterables;
import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.room.VasaNistirioRoom;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.WorldTask;
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
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 10. jaan 2018 : 3:58.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class VasaNistirio extends RaidNPC<VasaNistirioRoom> {

    private static final Animation rise = new Animation(7408);

    private static final Animation swirl = new Animation(7409);

    private static final Animation explode = new Animation(7410);

    private static final Animation teleportAnimation = new Animation(3865);

    private static final Graphics teleportGraphics = new Graphics(1296);

    private static final Graphics stunGraphics = new Graphics(80, 0, 96);

    private static final Graphics explosion = new Graphics(1328);

    private static final Projectile projectile = new Projectile(1327, 70, 3, 0, 15, 150, 0, 0);

    private static final Graphics impactGraphics = new Graphics(1330);

    private static final Projectile attackProjectile = new Projectile(1329, 60, 15, 0, 15, 90, 64, 0);

    public VasaNistirio(final Raid raid, final VasaNistirioRoom room, final Location tile) {
        super(raid, room, 7565, tile);
        combat = new VasaNistirioCombatHandler(this);
        targets = new ArrayList<>(raid.getPlayers().size());
        nearbyPlayers = new ArrayList<>(raid.getPlayers().size() / 2);
        distantPlayers = new ArrayList<>(raid.getPlayers().size() / 2);
        setForceMultiArea(true);
        setForceAggressive(true);
        setAggressionDistance(20);
        setAttackDistance(32);
        setMaxDistance(32);
    }

    private int stage = -1;

    private int substage;

    private int ticksTilDestroy = 67;

    private int delay;

    private boolean explosionAttack;

    private int damageForNextExplosion;

    private int currentCrystal = Utils.random(3);

    private int teleportCount = 0;

    private final Location center = getMiddleLocation();

    private final List<Player> targets;

    private final List<Player> nearbyPlayers;

    private final List<Player> distantPlayers;

    @Override
    public double getRangedPrayerMultiplier() {
        return 0.33;
    }

    private static final SoundEffect walkSound = new SoundEffect(3834, 10, 0);

    private static final SoundEffect siphonSound = new SoundEffect(2885, 10, 0);

    private static final SoundEffect throwingRockSound = new SoundEffect(781, 10, 0);

    private static final SoundEffect rockHittingGround = new SoundEffect(1021, 10, 0);

    private static final SoundEffect performingTeleportSound = new SoundEffect(199, 10, 0);

    private static final SoundEffect performingSpecialSound = new SoundEffect(1781, 10, 0);

    private static final SoundEffect stunSound = new SoundEffect(2727, 10, 0);

    private static final SoundEffect deathSound = new SoundEffect(782, 10, 0);

    @Override
    public void processNPC() {
        if (isDead()) {
            return;
        }
        if (hasWalkSteps()) {
            World.sendSoundEffect(getMiddleLocation(), walkSound);
        }
        if (stage >= 2) {
            for (final Player p : raid.getPlayers()) {
                if (p.getPlane() == getPlane() && CollisionUtil.collides(p.getX(), p.getY(), p.getSize(), getX(), getY(), getSize())) {
                    p.applyHit(new Hit(this, Utils.random(1, 5), HitType.DEFAULT));
                }
            }
        }
        switch(stage) {
            case -1:
                for (final Player p : raid.getPlayers()) {
                    if (p.getLocation().withinDistance(center, 7)) {
                        stage++;
                        room.start();
                        return;
                    }
                }
                return;
            case 0:
                switch(substage++) {
                    case 0:
                        setAnimation(rise);
                        return;
                    case 5:
                        setAnimation(swirl);
                        return;
                    case 10:
                        setAnimation(explode);
                        return;
                    case 12:
                        setTransformation(7566);
                        stage++;
                        return;
                }
                return;
            case 1:
                sendExplosionAttack();
                substage = 0;
                stage++;
                return;
            case 2:
                if (substage++ == 2) {
                    stage++;
                    teleportTargets();
                    substage = 0;
                    return;
                }
                return;
            case 3:
                switch(substage++) {
                    case 0:
                        World.sendSoundEffect(getMiddleLocation(), performingSpecialSound);
                        break;
                    case 2:
                        sendProjectiles();
                        return;
                    case 7:
                        sendExplosions();
                        return;
                    case 9:
                        stage = 4;
                        substage = 0;
                        delay = 0;
                        ticksTilDestroy = 67;
                        refreshNextCrystal();
                        walkToCrystal();
                        return;
                }
                return;
            case 4:
                boolean siphoning = false;
                if (room.getCrystals()[currentCrystal] != null) {
                    siphoning = !room.getCrystals()[currentCrystal].isDead() && !room.getCrystals()[currentCrystal].isFinished();
                }
                if (!hasWalkSteps() && ticksTilDestroy-- <= 0) {
                    siphoning = false;
                    explosionAttack = true;
                    room.getCrystals()[currentCrystal].sendDeath();
                } else if (!siphoning) {
                    explosionAttack = false;
                }
                if (hasWalkSteps()) {
                    sendSparks();
                }
                if (!hasWalkSteps() && siphoning) {
                    if (getId() != 7567 && ++delay >= 3) {
                        setTransformation(7567);
                    }
                    if (ticksTilDestroy % 3 == 0) {
                        World.sendSoundEffect(getMiddleLocation(), siphonSound);
                    }
                    heal(Math.max(1, (int) (getMaxHitpoints() * 0.005F)));
                } else if (!siphoning) {
                    setTransformation(7566);
                    stage++;
                }
                return;
            case 5:
                walkToCenter();
                substage = 0;
                stage = explosionAttack ? 6 : 7;
                delay = 0;
                return;
            case 7:
                if (!hasWalkSteps()) {
                    if (delay == 1) {
                        final Location middle = getMiddleLocation();
                        setFaceLocation(new Location(middle.getX(), 0, middle.getPlane()));
                    }
                    if (delay++ >= 3) {
                        stage = 4;
                        substage = 0;
                        delay = 0;
                        refreshNextCrystal();
                        walkToCrystal();
                    }
                } else {
                    sendSparks();
                }
                return;
            case 6:
                if (!hasWalkSteps()) {
                    final Location middle = getMiddleLocation();
                    setFaceLocation(new Location(middle.getX(), 0, middle.getPlane()));
                    stage = 1;
                    substage = 0;
                } else {
                    sendSparks();
                }
        }
    }

    private void sendSparks() {
        if (substage++ % 4 == 0) {
            final Location middle = getMiddleLocation();
            World.sendSoundEffect(middle, throwingRockSound);
            for (final Entity p : getPossibleTargets(EntityType.PLAYER)) {
                if (CollisionUtil.collides(getX(), getY(), getSize(), p.getX(), p.getY(), p.getSize())) {
                    continue;
                }
                final Location pTile = new Location(p.getLocation());
                World.sendProjectile(middle, pTile, attackProjectile);
                WorldTasksManager.schedule(new WorldTask() {

                    @Override
                    public void run() {
                        if (room.getRaid().isDestroyed()) {
                            stop();
                            return;
                        }
                        World.sendGraphics(impactGraphics, pTile);
                        World.sendSoundEffect(pTile, rockHittingGround);
                        for (final Player r : raid.getPlayers()) {
                            if (r.getLocation().withinDistance(pTile, 1)) {
                                if (r.isDead()) {
                                    continue;
                                }
                                final int damage = CombatUtilities.getRandomMaxHit(VasaNistirio.this, getMaxHit(18), CombatScript.RANGED, r);
                                final Hit hit = new Hit(VasaNistirio.this, damage, HitType.RANGED);
                                CombatUtilities.delayHit(VasaNistirio.this, -1, r, hit);
                            }
                        }
                    }
                }, 2);
            }
        }
    }

    boolean cannotHit(final int index) {
        if (getId() != NpcId.VASA_NISTIRIO_7567) {
            return true;
        }
        return index != currentCrystal;
    }

    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        if (stage == 1 || stage == 2 || (stage == 3 && substage <= 7)) {
            hit.setDamage(0);
            if (hit.getSource() instanceof Player) {
                ((Player) hit.getSource()).sendMessage("Vasa Nistirio is preparing a powerful spell and can ignore your puny attack.");
            }
        }
    }

    @Override
    public float getXpModifier(final Hit hit) {
        return getId() == 7567 || (stage == 1 || stage == 2 || (stage == 3 && substage <= 7)) ? 0 : 1;
    }

    private void refreshNextCrystal() {
        final IntArrayList availableCrystals = new IntArrayList(4);
        if (ticksTilDestroy <= 0) {
            ticksTilDestroy = 67;
        }
        for (int i = 0; i < room.getCrystals().length; i++) {
            final Crystal crystal = room.getCrystals()[i];
            if (crystal != null && !crystal.isDead() && !crystal.isFinished()) {
                availableCrystals.add(i);
            }
        }
        int count = 50;
        if (availableCrystals.size() == 0) {
            while (true) {
                final int crystal = Utils.random(3);
                if (crystal != currentCrystal || --count <= 0) {
                    room.resetCrystal(currentCrystal = crystal);
                    return;
                }
            }
        }
        while (true) {
            final int crystal = availableCrystals.getInt(Utils.random(availableCrystals.size() - 1));
            if (crystal != currentCrystal || --count <= 0) {
                currentCrystal = crystal;
                break;
            }
        }
    }

    private void walkToCenter() {
        final Location tile = room.getNpcCenter();
        final RouteResult steps = RouteFinder.findRoute(getX(), getY(), getPlane(), getSize(), new TileStrategy(tile.getX(), tile.getY()), true);
        final int[] bufferX = steps.getXBuffer();
        final int[] bufferY = steps.getYBuffer();
        resetWalkSteps();
        for (int i = steps.getSteps() - 1; i >= 0; i--) {
            if (!addWalkSteps(bufferX[i], bufferY[i], 25, false)) {
                break;
            }
        }
    }

    private void sendProjectiles() {
        setAnimation(explode);
        final Location center = getMiddleLocation();
        final int hash = center.getPositionHash();
        for (int x = -1; x <= 5; x += 3) {
            for (int y = -1; y <= 5; y += 3) {
                final Location tile = new Location(getLocation().getX() + x, getLocation().getY() + y, center.getPlane());
                if (tile.getPositionHash() == hash) {
                    continue;
                }
                World.sendProjectile(center, tile, projectile);
            }
        }
    }

    private void sendExplosions() {
        final Location center = getMiddleLocation();
        final int hash = center.getPositionHash();
        nearbyPlayers.removeIf(player -> player.isDead() || player.isFinished() || !room.getPlayers().contains(player));
        distantPlayers.removeIf(player -> player.isDead() || player.isFinished() || !room.getPlayers().contains(player));
        final int count = Math.max(nearbyPlayers.size(), CharacterLoop.find(center, 4, Player.class, player -> true).size());
        if (count == 0) {
            return;
        }
        final int damage = damageForNextExplosion / count;
        for (int x = -1; x <= 5; x += 3) {
            for (int y = -1; y <= 5; y += 3) {
                final Location tile = new Location(getLocation().getX() + x, getLocation().getY() + y, center.getPlane());
                if (tile.getPositionHash() == hash) {
                    continue;
                }
                World.sendGraphics(explosion, tile);
            }
        }
        distantPlayers.forEach(player -> {
            if (player.getLocation().withinDistance(center, 4)) {
                final int amount = !player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC) ? damage : (damage / 2);
                player.applyHit(new Hit(this, Math.min(99, amount), HitType.DEFAULT));
            }
        });
        nearbyPlayers.forEach(player -> player.applyHit(new Hit(this, Math.min(99, damage), HitType.DEFAULT)));
    }

    @Override
    public boolean isFreezeable() {
        return false;
    }

    @Override
    public void sendDeath() {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        resetWalkSteps();
        combat.removeTarget();
        setAnimation(null);
        room.removeMagicalFire();
        if (teleportCount < 2) {
            getRaid().getPlayers().forEach(p -> p.getCombatAchievements().complete(CAType.STOP_DROP_AND_ROLL));
        }
        for (final Crystal crystal : room.getCrystals()) {
            if (crystal == null) {
                continue;
            }
            crystal.sendDeath();
        }
        if (stage == 2 || stage == 3) {
            Iterables.concat(nearbyPlayers, distantPlayers).forEach(player -> {
                player.removeStun();
                player.setAnimation(Animation.STOP);
            });
        }
        WorldTasksManager.schedule(new WorldTask() {

            private int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setAnimation(defs.getSpawnDefinitions().getDeathAnimation());
                    World.sendSoundEffect(getMiddleLocation(), deathSound);
                } else if (loop == deathDelay) {
                    drop(getMiddleLocation());
                    World.spawnObject(new WorldObject(30020, 10, 0, new Location(getMiddleLocation())));
                    reset();
                    finish();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    protected void drop(final Location tile) {
        final Player killer = getMostDamagePlayerCheckIronman();
        if (killer == null)
            return;
        announce(killer);
        onDrop(killer);
        final List<DropProcessor> processors = DropProcessorLoader.get(id);
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, killer);
            }
        }
        final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
        if (drops == null) {
            return;
        }
        NPCDrops.rollTable(killer, drops, drop -> dropItem(killer, drop, tile));
    }

    @Override
    public void autoRetaliate(final Entity source) {
    }

    private void sendExplosionAttack() {
        retrieveTargets();
        if (!nearbyPlayers.isEmpty()) {
            nearbyPlayers.clear();
        }
        if (!distantPlayers.isEmpty()) {
            distantPlayers.clear();
        }
        final int size = targets.size();
        if (size == 0) {
            return;
        }
        nearbyPlayers.add(targets.remove(Utils.random(size - 1)));
        while (!targets.isEmpty()) {
            final Player t = targets.remove(Utils.random(targets.size() - 1));
            if (distantPlayers.size() < nearbyPlayers.size()) {
                distantPlayers.add(t);
            } else {
                nearbyPlayers.add(t);
            }
        }
        for (final Player player : nearbyPlayers) {
            player.stun(10);
        }
        World.sendSoundEffect(getMiddleLocation(), performingTeleportSound);
        Iterables.concat(nearbyPlayers, distantPlayers).forEach(player -> {
            player.setGraphics(teleportGraphics);
            player.setAnimation(teleportAnimation);
        });
    }

    private void teleportTargets() {
        teleportCount++;
        setAnimation(swirl);
        final Location middle = getMiddleLocation();
        final List<Player> toRemove = new ArrayList<>();
        distantPlayers.forEach(p -> {
            if (p.isDead() || p.isFinished() || !room.getPlayers().contains(p)) {
                return;
            }
            final Location tile = getDistantLocation(middle);
            if (tile == middle) {
                toRemove.add(p);
                return;
            }
            p.setLocation(tile);
            p.setAnimation(Animation.STOP);
        });
        distantPlayers.removeAll(toRemove);
        // nearbyPlayers.addAll(toRemove); Apparently if a player runs from afar towards the center, it will not be included as a nearby player, they won't be stunned.
        nearbyPlayers.forEach(p -> {
            if (p.isDead() || p.isFinished() || !room.getPlayers().contains(p)) {
                return;
            }
            p.setLocation(getNearbyLocation(middle));
            p.setAnimation(Animation.STOP);
            p.getTemporaryAttributes().put("prayerDelay", Utils.currentTimeMillis() + 5000);
            p.getPrayerManager().deactivateActivePrayers();
            p.setGraphics(stunGraphics);
            p.sendSound(stunSound);
        });
        damageForNextExplosion = 0;
        for (final Player p : nearbyPlayers) {
            if (p.isNulled() || p.isDead() || p.isFinished() || !room.getPlayers().contains(p)) {
                continue;
            }
            damageForNextExplosion += Math.max(0, Math.min(99, p.getHitpoints()) - 5);
        }
    }

    private Location getNearbyLocation(final Location middle) {
        int count = 50;
        while (true) {
            if (count-- <= 0) {
                return middle;
            }
            final Location tile = new Location(Utils.random(-4, 4) + middle.getX(), Utils.random(-4, 4) + middle.getY(), middle.getPlane());
            if (tile.withinDistance(middle, 2)) {
                continue;
            }
            if (World.getMask(tile.getPlane(), tile.getX(), tile.getY()) != 0) {
                continue;
            }
            return tile;
        }
    }

    private Location getDistantLocation(final Location middle) {
        int count = 50;
        while (true) {
            if (count-- <= 0) {
                return middle;
            }
            final Location tile = new Location(Utils.random(-14, 14) + middle.getX(), Utils.random(-14, 14) + middle.getY(), middle.getPlane());
            if (tile.withinDistance(middle, 10)) {
                continue;
            }
            if (World.getMask(tile.getPlane(), tile.getX(), tile.getY()) != 0) {
                continue;
            }
            return tile;
        }
    }

    private void walkToCrystal() {
        calcFollow(room.getCrystals()[currentCrystal], -1, true, true, false);
    }

    private final void retrieveTargets() {
        if (!targets.isEmpty()) {
            targets.clear();
        }
        targets.addAll(room.getPlayers());
    }

    private static final class VasaNistirioCombatHandler extends NPCCombat {

        VasaNistirioCombatHandler(final NPC npc) {
            super(npc);
        }

        @Override
        public void setTarget(final Entity target, TargetSwitchCause cause) {
        }
    }
}
