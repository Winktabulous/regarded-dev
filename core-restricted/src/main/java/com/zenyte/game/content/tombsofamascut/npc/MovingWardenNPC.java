package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.encounter.WardenEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.HitbarDefinitions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Savions
 */
public class MovingWardenNPC extends TOANPC implements CombatScript, IMovingWarden {

    private static final Animation AWAKE_ANIMATION = new Animation(9663);
    private static final Animation MELEE_ANIMATION = new Animation(9659);
    private static final Animation DEATH_ANIMATION = new Animation(9670);
    private static final Animation REVIVE_ANIMATION = new Animation(9672);
    private static final Animation MAGE_THROW_ANIMATION = new Animation(9661);
    private static final Animation RANGE_THROW_ANIMATION = new Animation(9660);
    private static final Graphics IMPRISONMENT_GROUND_GFX = new Graphics(1447);
    private static final Graphics IMPRISONMENT_LAND_GFX = new Graphics(2212);
    private static final Projectile MAGE_PROJECTILE = new Projectile(2224, 100, 25, 0, 12, 90, 128, 0);
    private static final Projectile RANGE_PROJECTILE = new Projectile(2241, 100, 25, 0, 12, 90, 128, 0);
    private static final Projectile IMPRISONMENT_PROJECTILE = new Projectile(2210, 100, 10, 0, 12, 120, 64, 0);
    private static final Projectile CORE_PROJECTILE = new Projectile(2240, 95, 12, 0, 12, 60, 124, 0);
    private static final SoundEffect MAGE_IMPACT_SOUND = new SoundEffect(156);
    private static final SoundEffect RANGE_IMPACT_SOUND = new SoundEffect(6257);
    private final WardenEncounter encounter;
    private final EntityHitBar yellowHitBar = new EntityHitBar(this) {

        @Override public int getType() {
            return 38;
        }

        @Override public int getPercentage() {
            final int multiplier = getMultiplier();
            final float mod = (float) getMaxHitpoints() / (multiplier);
            return multiplier - Math.min((int) ((hitpoints + mod) / mod), multiplier);
        }

        public int getMultiplier() {
            final int type = getType();
            return HitbarDefinitions.get(type).getSize();
        }
    };

    private boolean canAttack;
    private boolean canDoSpecial;
    private int cycles;
    private final int shieldHitPoints;
    private final int realTotalHealth;
    private int currentRealHealth;
    private int previousId;
    private boolean isDown;

    public MovingWardenNPC(int id, Location tile, Direction facing, WardenEncounter encounter) {
        super(id, tile, facing, 64, encounter);
        this.encounter = encounter;
        shieldHitPoints = getCombatDefinitions().getHitpoints();
        realTotalHealth = (int) (4500 * (1F + (toaRaidArea.getStartTeamSize() - 1) * .9F));
        currentRealHealth = realTotalHealth;
        setCantInteract(true);
        setAttackDistance(8);
        setMaxDistance(64);
        setAnimation(AWAKE_ANIMATION);
        setForceAggressive(true);
        super.hitBar = yellowHitBar;
    }

    @Override public List<Entity> getPossibleTargets(EntityType type, int radius) {
        if (!possibleTargets.isEmpty()) {
            possibleTargets.clear();
        }
        final List<Entity> possibleTargets = new ArrayList<>();
        final Player[] players = encounter.getChallengePlayers();
        if (players.length > 0) {
            Player target = null;
            int distance = 64;
            for (Player p : players) {
                final int pDistance = getMiddleLocation().getTileDistance(p.getLocation());
                if (pDistance < distance) {
                    target = p;
                    distance = pDistance;
                }
            }
            if (target != null) {
                possibleTargets.add(target);
            }
        }
        super.possibleTargets.addAll(possibleTargets);
        return possibleTargets;
    }

    @Override public void listenScheduleHit(Hit hit) {
        super.listenScheduleHit(hit);
        if (!((HitType.RANGED.equals(hit.getHitType()) && (id == WardenEncounter.ELIDINIS_RANGE_ID || id == WardenEncounter.TUMEKEN_RANGE_ID))
            || (HitType.MAGIC.equals(hit.getHitType()) && (id == WardenEncounter.ELIDINIS_MAGE_ID || id == WardenEncounter.TUMEKEN_MAGE_ID)))) {
            hit.setDamage(0);
        }
        hit.setHitType(HitType.WARDENS);
    }

    @Override
    public boolean setHitpoints(int amount) {
        final boolean set = super.setHitpoints(amount);
        if (encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
            encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(isDown ? hitpoints : (getMaxHitpoints() - hitpoints)));
        }
        if (isDown && amount <= 0 && encounter != null) {
            encounter.killMovingWarden();
        }
        return set;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (canAttack && getCombat().getCombatDelay() <= 0) {
            getCombat().setCombatDelay(attack(getCombat().getTarget()));
        }
        if (isDown && encounter.getWardenCoreNPC() != null && !encounter.getWardenCoreNPC().isFinished()) {
            getHitBars().clear();
            getHitBars().add(super.hitBar);
            getUpdateFlags().flag(UpdateFlag.HIT);
        }
    }

    public boolean isCanAttack() { return canAttack; }

    public void setCanAttack(boolean canAttack) {
        if (canAttack) {
            getCombat().setCombatDelay(2);
        }
        this.canAttack = canAttack;
    }

    @Override
    public float getPointMultiplier() { return 2F; }

    @Override public void setRespawnTask() {}

    @Override
    public boolean freeze(int freezeTicks) {
        return false;
    }

    @Override
    public boolean freeze(int freezeTicks, int immunityTicks, @Nullable Consumer<Entity> onFreezeConsumer) {
        return false;
    }

    @Override
    public void sendDeath() {
        if (!canAttack) {
            return;
        }
        setAnimation(DEATH_ANIMATION);
        setCantInteract(true);
        canAttack = false;
        cycles++;
        if (encounter.getObelisk() != null) {
            encounter.getObelisk().resetObelisk();
        }
        WorldTasksManager.schedule(encounter.addRunningTask(() -> {
            if (EncounterStage.STARTED.equals(encounter.getStage())) {
                isDown = true;
                for (Player p : encounter.getChallengePlayers()) {
                    if (p != null) {
                        p.getHpHud().resetColors();
                        p.getHpHud().sendMaxHitPoints(realTotalHealth);
                    }
                }
                getCombatDefinitions().setHitpoints(realTotalHealth);
                setHitpoints(currentRealHealth);
                final Direction direction = Direction.getClosestDirection(getDirection());
                Location coreLoc = getMiddleLocation().transform(direction.getOffsetX() * 3, direction.getOffsetY() * 3);
                radiusLoop: for(int radius = 0; radius < 10; radius++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        for (int dy = -radius; dy <= radius; dy++) {
                            final Location potLoc = coreLoc.transform(dx, dy);
                            if ((World.getMask(potLoc) & Flags.OBJECT) == 0) {
                                coreLoc = potLoc;
                                break radiusLoop;
                            }
                        }
                    }
                }
                previousId = id;
                setTransformation(id == WardenEncounter.TUMEKEN_MAGE_ID || id == WardenEncounter.TUMEKEN_RANGE_ID ? WardenEncounter.TUMEKEN_DOWN_ID : WardenEncounter.ELIDINIS_DOWN_ID);
                getHitBars().clear();
                getHitBars().add(new RemoveHitBar(hitBar.getType()));
                getUpdateFlags().flag(UpdateFlag.HIT);
                super.hitBar = new EntityHitBar(MovingWardenNPC.this) {
                    @Override
                    public int getType() { return 21; }
                };
                final Location coreFinalLoc = coreLoc;
                World.scheduleProjectile(MovingWardenNPC.this, coreLoc, CORE_PROJECTILE).schedule(() -> {
                    if (EncounterStage.STARTED.equals(encounter.getStage())) {
                        encounter.spawnCore(coreFinalLoc, direction, getCoreTicks());
                    }
                });
            }
        }), 4);
    }

    private int getCoreTicks() {
        float percentage = (float) currentRealHealth / realTotalHealth;
        if (percentage > .8F) {
            return 21;
        } else if (percentage > .6F) {
            return 29;
        } else if (percentage > .4F) {
            return 37;
        } else if (percentage > .2F) {
            return 45;
        } else {
            return 53;
        }
    }

    public void revive() {
        isDown = false;
        setAnimation(REVIVE_ANIMATION);
        for (Player p : encounter.getChallengePlayers()) {
            if (p != null) {
                p.getHpHud().sendColorChange(WardenEncounter.WARDEN_HUD_COLOURS[0], WardenEncounter.WARDEN_HUD_COLOURS[1], WardenEncounter.WARDEN_HUD_COLOURS[2]);
                p.getHpHud().sendMaxHitPoints(shieldHitPoints);
            }
        }
        currentRealHealth = hitpoints;
        getCombatDefinitions().setHitpoints(shieldHitPoints);
        setHitpoints(shieldHitPoints);
        getHitBars().clear();
        getHitBars().add(new RemoveHitBar(hitBar.getType()));
        getUpdateFlags().flag(UpdateFlag.HIT);
        super.hitBar = yellowHitBar;
        if (previousId == WardenEncounter.TUMEKEN_RANGE_ID) {
            setTransformation(WardenEncounter.TUMEKEN_MAGE_ID);
        } else if (previousId == WardenEncounter.TUMEKEN_MAGE_ID) {
            setTransformation(WardenEncounter.TUMEKEN_RANGE_ID);
        } else if (previousId == WardenEncounter.ELIDINIS_RANGE_ID) {
            setTransformation(WardenEncounter.ELIDINIS_MAGE_ID);
        } else {
            setTransformation(WardenEncounter.ELIDINIS_RANGE_ID);
        }
        WorldTasksManager.schedule(encounter.addRunningTask(() -> {
            if (EncounterStage.STARTED.equals(encounter.getStage())) {
                setCantInteract(false);
                canAttack = true;
            }
        }), 3);
    }

    @Override
    public void autoRetaliate(Entity source) {
        if (canAttack) {
            super.autoRetaliate(source);
        }
    }

    @Override
    public void setTarget(Entity target, TargetSwitchCause cause) {
        if (canAttack && getCombat() != null) {
            getCombat().setTarget(target, cause);
        }
    }

    @Override
    public void resetWalkSteps() {
        if (canAttack) {
            super.resetWalkSteps();
        }
    }

    @Override
    public boolean isIntelligent() { return true; }

    @Override
    public double getRangedPrayerMultiplier() {
        return .15F;
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return .15F;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return .15F;
    }

    private void sendDelayedAttackProjectile(DivineType divineType, Projectile projectile, SoundEffect onHitSound, HitType hitType) {
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                final Player[] players = encounter.getChallengePlayers();
                if (!canAttack || players.length < 1 || !EncounterStage.STARTED.equals(encounter.getStage())) {
                    stop();
                    return;
                }
                if (ticks++ == 0) {
                    for (Player p : players) {
                        if (p != null) {
                            if (divineType != null) {
                                World.sendProjectile(MovingWardenNPC.this, p, divineType.getProjectile());
                                p.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MELEE);
                                p.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MAGIC);
                                p.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MISSILES);
                                p.sendMessage("<col=ff3045>Your protection prayers have been disabled!</col>");
                                p.sendMessage(divineType.getMessage());
                            } else {
                                World.sendProjectile(MovingWardenNPC.this, p, projectile);
                            }
                        }
                    }
                } else if (ticks == (divineType == null ? 4 : 5)) {
                    for (Player p : players) {
                        if (p != null) {
                            if (divineType != null) {
                                p.sendSound(divineType.getSoundEffect());
                                final boolean isActive = p.getPrayerManager().isActive(divineType.getPrayer());
                                p.applyHit(new Hit(MovingWardenNPC.this, isActive ? 0 : (getMaxHit(22) + Utils.random(10)), isActive ? HitType.MISSED : HitType.DEFAULT));
                            } else {
                                p.sendSound(onHitSound);
                                delayHit(-1, p, new Hit(MovingWardenNPC.this, getRandomMaxHit(
                                        MovingWardenNPC.this, getMaxHit(22), HitType.MAGIC.equals(hitType) ? AttackType.MAGIC : AttackType.RANGED, p), hitType));
                            }
                        }
                    }
                    stop();
                }
            }
        }), 1, 0);
    }

    public void sendImprisonmentAttack() {
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
            int ticks;
            final List<Location> stoneTiles = new ArrayList<>();

            @Override
            public void run() {
                final Player[] players = encounter.getChallengePlayers();
                if (!canAttack || players.length < 1 || !EncounterStage.STARTED.equals(encounter.getStage())) {
                    stop();
                    return;
                }
                if (ticks++ == 0) {
                    for (Player p : players) {
                        if (p != null) {
                            final Location loc = new Location(p.getLocation());
                            if (!stoneTiles.contains(loc)) {
                                stoneTiles.add(loc);
                            }
                        }
                    }
                    stoneTiles.forEach(loc -> {
                        World.sendProjectile(MovingWardenNPC.this, loc, IMPRISONMENT_PROJECTILE);
                        World.sendGraphics(IMPRISONMENT_GROUND_GFX, loc);
                    });
                } else if (ticks == 5) {
                    stoneTiles.forEach(loc -> {
                        boolean hitPlayer = false;
                        for (Player p : players) {
                            if (p != null && loc.equals(p.getLocation())) {
                                encounter.turnIntoStone(p);
                                hitPlayer = true;
                            }
                        }
                        if (!hitPlayer) {
                            World.sendGraphics(IMPRISONMENT_LAND_GFX, loc);
                        }
                    });
                    stop();
                }
            }
        }), 1, 0);
    }

    @Override
    public boolean canAttack(Player source) {
        return !encounter.isStoned(source) && super.canAttack(source);
    }

    @Override
    public void setUnprioritizedAnimation(Animation animation) {

    }

    @Override
    public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
        return !isDead() && !isDown && super.addWalkStep(nextX, nextY, lastX, lastY, check);
    }

    @Override
    public int attack(Entity target) {
        if (target != null && isWithinMeleeDistance(this, target) && Utils.random(3) == 0) {
            setAnimation(MELEE_ANIMATION);
            delayHit(0, target, new Hit(MovingWardenNPC.this, getRandomMaxHit(
                    MovingWardenNPC.this, getMaxHit(16), AttackType.CRUSH, target), HitType.MELEE));
        } else {
            final Player[] players = encounter.getChallengePlayers();
            if (players.length > 0) {
                if (canDoSpecial && Utils.random(5) == 0) {
                    canDoSpecial = false;
                    setAnimation(MAGE_THROW_ANIMATION);
                    if (cycles < 1 || Utils.random(1) == 0) {
                        sendDelayedAttackProjectile(DivineType.values[Utils.random(DivineType.values.length - 1)], null, null, null);
                    } else {
                        sendImprisonmentAttack();
                    }
                } else {
                    if (Utils.random(1) == 0) {
                        setAnimation(MAGE_THROW_ANIMATION);
                        sendDelayedAttackProjectile(null, MAGE_PROJECTILE,  MAGE_IMPACT_SOUND, HitType.MAGIC);
                    } else {
                        setAnimation(RANGE_THROW_ANIMATION);
                        sendDelayedAttackProjectile(null, RANGE_PROJECTILE, RANGE_IMPACT_SOUND, HitType.RANGED);
                    }
                    canDoSpecial = true;
                }
            }
        }
        return combatDefinitions.getAttackSpeed();
    }

    public boolean isDown() { return isDown; }

    enum DivineType {

        MELEE(Prayer.PROTECT_FROM_MELEE, "<col=ff3045>The warden throws an arcane scimitar.</col>", new SoundEffect(217), new Projectile(2204, 100, 25, 0, 12, 120, 64, 0)),
        MAGIC(Prayer.PROTECT_FROM_MAGIC, "<col=a53fff>The warden launches an arcane spell.</col>", new SoundEffect(208), new Projectile(2208, 100, 25, 0, 12, 120, 64, 0)),
        RANGED(Prayer.PROTECT_FROM_MISSILES, "<col=229628>The warden fires an arcane arrow.</col>", new SoundEffect(129), new Projectile(2206, 100, 25, 0, 12, 120, 64, 0));

        public static DivineType[] values = values();
        private final Prayer prayer;
        private final String message;
        private final SoundEffect soundEffect;
        private final Projectile projectile;

        DivineType(Prayer prayer, String message, SoundEffect soundEffect, Projectile projectile) {
            this.prayer = prayer;
            this.message = message;
            this.soundEffect = soundEffect;
            this.projectile = projectile;
        }

        public Prayer getPrayer() { return prayer; }

        public String getMessage() { return message; }

        public SoundEffect getSoundEffect() { return soundEffect; }

        public Projectile getProjectile() { return projectile; }
    }
}
