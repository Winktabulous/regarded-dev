package com.near_reality.game.content.gauntlet.hunllef;

import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.GauntletPlayerAttributesKt;
import com.near_reality.game.content.gauntlet.GauntletArmorTier;
import com.near_reality.game.content.gauntlet.rewards.GauntletRewardType;
import com.near_reality.game.content.gauntlet.GauntletType;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
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
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.Pair;

import java.util.List;
import java.util.Optional;

/**
 * @author Andys1814.
 * @since 1/21/2022.
 */
public final class Hunllef extends NPC implements CombatScript {

    /**
     * Hunllef switches protection type after being hit 6 times.
     */
    private static final int TYPE_SWAP_INCOMING_THRESHOLD = 6;

    /**
     * Hunllef switches attack types after attacking 4 times.
     */
    private static final int TYPE_SWAP_OUTGOING_THRESHOLD = 4;

    private static final Animation TORNADO_ATTACK_ANIMATION = new Animation(8418);

    private static final int TORNADO_NPC = 9025;

    private static final int TORNADO_NPC_CORRUPTED = 9039;

    private static final int TORNADO_COOLDOWN_TICKS = 56;

    private static final Animation STANDARD_ATTACK_ANIMATION = new Animation(8419);

    private static final Animation TRAMPLE_ANIMATION = new Animation(8420);

    private static final Animation ATTACK_STYLE_SWITCH_ANIMATION = new Animation(8754, 50);

    private static final Projectile MAGIC_PRAYER_DEACTIVATION_PROJECTILE = new Projectile(1713, 50, 20, 25, 15, 45, 64, 5);

    private static final Projectile MAGIC_PRAYER_DEACTIVATION_PROJECTILE_CORRUPTED = new Projectile(1714, 50, 20, 25, 15, 45, 64, 5);

    private static final Projectile MAGIC_PROJECTILE = new Projectile(1707, 50, 20, 25, 15, 45, 90, 5);

    private static final Projectile MAGIC_PROJECTILE_CORRUPTED = new Projectile(1708, 50, 20, 25, 15, 45, 90, 5);

    private static final Projectile RANGED_PROJECTILE = new Projectile(1711, 50, 20, 25, 15, 45, 90, 5);

    private static final Projectile RANGED_PROJECTILE_CORRUPTED = new Projectile(1712, 50, 20, 25, 15, 45, 90, 5);

    private HunllefType type;

    /**
     * Hunllef always started with Ranged attack type.
     */
    private HitType hitType = HitType.RANGED;

    private final GauntletType gauntletType;

    /* Base coordinates of the walkable boss room (not chunk). */
    private final int baseX;
    private final int baseY;

    private int incomingHitCount = 0;

    private int outgoingHitCount = 0;

    private int tornadoCooldown = TORNADO_COOLDOWN_TICKS;

    private int tilePatternCooldown = 4;

    private boolean prayedCorrectly;

    private Gauntlet gauntlet;

    public Hunllef(HunllefType initialType, GauntletType gauntletType, Location location, int baseX, int baseY, Gauntlet gauntlet) {
        super(gauntletType.isCorrupted() ? initialType.getCorruptedNpcId() : initialType.getNpcId(), location, Direction.NORTH, 5);
        this.type = initialType;
        this.gauntletType = gauntletType;
        this.baseX = baseX;
        this.baseY = baseY;
        this.setSpawned(true);
        this.supplyCache = false;
        this.setAttackDistance(5);
        this.gauntlet = gauntlet;
    }

    @Override
    public void processNPC() {

        if (tornadoCooldown > 0)
            tornadoCooldown--;

        if (!isDead()) {
            if (this.combat.getTarget() != null && this.combat.getTarget() instanceof final Player player) {

                tilePatternCooldown--;
                if (tilePatternCooldown == 0) {
                    spawnTilePattern(player);
                    tilePatternCooldown = 30;
                }

                if (this.combat.getCombatDelay() <= 1) {
                    // Trample logic lives in this method because it needs to be considered outside of normal attack cycle.
                    boolean trample = CollisionUtil.collides(getX(), getY(), getSize(), player.getX(), player.getY(), player.getSize());
                    if (trample) {
                        setAnimation(TRAMPLE_ANIMATION);
                        Hit hit = new Hit(Utils.random(8, 35), HitType.MELEE);
                        delayHit(this, 1, player, hit);
                        player.sendMessage("You're trampled beneath the " + (gauntletType.isCorrupted() ? "Corrupted" : "Crystalline") + " Hunllef.");
                        combat.setCombatDelay(combatDefinitions.getAttackSpeed());
                    }
                }
            }
        }
        super.processNPC();
    }

    @Override
    public void sendDeath() {
        final Player source = getMostDamagePlayerCheckIronman();
        if (source == null) {
            super.sendDeath();
            return;
        }

        setFaceEntity(null);
        onDeath(source);
        lock();
        setAnimation(new Animation(8421));
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (ticks == 1) {
                    setInvalidAnimation(new Animation(8422, 50));
                    setTransformation(gauntletType.getDeathBossId());
                } else if (ticks == 2) {
                    completeGauntlet(source);
                } else if (ticks >= 3) {
                    onFinish(source);
                    stop();
                    return;
                }
                ticks++;
            }
        }, 0, 1);
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            if (prayedCorrectly) {
                if (gauntletType == GauntletType.STANDARD) {
                    player.getCombatAchievements().complete(CAType.three_2_1__RANGE);
                    Optional<GauntletArmorTier> tier = GauntletArmorTier.get(player, gauntletType);
                    if (tier.isPresent() && tier.get() == GauntletArmorTier.PERFECTED) {
                        player.getCombatAchievements().complete(CAType.CRYSTALLINE_WARRIOR);
                    }
                    if (gauntlet.attunedWeaponCount < 2) {
                        player.getCombatAchievements().complete(CAType.WOLF_PUNCHER);
                    }
                    if (!player.getBooleanTemporaryAttribute("egniol_potion_made")) {
                        player.getCombatAchievements().complete(CAType.EGNIOL_DIET);
                    }
                    if (!gauntlet.armorMade) {
                        player.getCombatAchievements().complete(CAType.DEFENCE_DOESNT_MATTER);
                    }
                } else {
                    player.getCombatAchievements().complete(CAType.three_2_1__MAGE);
                    Optional<GauntletArmorTier> tier = GauntletArmorTier.get(player, gauntletType);
                    if (tier.isPresent() && tier.get() == GauntletArmorTier.PERFECTED) {
                        player.getCombatAchievements().complete(CAType.CORRUPTED_WARRIOR);
                    }
                    if (gauntlet.attunedWeaponCount < 2) {
                        player.getCombatAchievements().complete(CAType.WOLF_PUNCHER_II);
                    }
                    if (!gauntlet.armorMade) {
                        player.getCombatAchievements().complete(CAType.DEFENCE_DOESNT_MATTER_II);
                    }
                }
            }
        }
    }

    private void completeGauntlet(Entity source) {
        if (source instanceof Player player)
            Optional.ofNullable(GauntletPlayerAttributesKt.getGauntlet(player))
                    .ifPresent(gauntlet -> gauntlet.end(GauntletRewardType.FULLY_COMPLETED, false, false));
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (!(hit.getSource() instanceof Player)) {
            return;
        }

        // Only consider attacks Hunllef is NOT protecting against in the attack counter.
        // This is important for some hit strategies people use on OSRS.
        if (hit.getHitType() == type.getResistance()) {
            hit.setDamage(0);
        } else {
            handleDifferentHit(hit.getHitType());
        }
    }

    public void handleDifferentHit(HitType hitType) {
        incomingHitCount++;
        if (incomingHitCount == TYPE_SWAP_INCOMING_THRESHOLD) {
            type = HunllefType.forHit(hitType); // Always swap to the most recent incoming hit type.
            setTransformation(gauntletType.isCorrupted() ? type.getCorruptedNpcId() : type.getNpcId());
            incomingHitCount = 0;
        }
    }

    @Override
    public void delayHit(NPC npc, int delay, Entity target, Hit... hits) {
        CombatScript.super.delayHit(npc, delay, target, hits);
    }

    @Override
    public boolean isWithinMeleeDistance(NPC npc, Entity target) {
        return CombatScript.super.isWithinMeleeDistance(npc, target);
    }

    @Override
    public int getRandomMaxHit(NPC npc, int maxHit, AttackType attackStyle, Entity target) {
        if (target instanceof final Player player) {
            Optional<GauntletArmorTier> tier = GauntletArmorTier.get(player, gauntletType);
            maxHit = calculateMaxHit(tier.orElse(null));
        }

        return CombatScript.super.getRandomMaxHit(npc, maxHit, attackStyle, target);
    }

    @Override
    public int getRandomMaxHit(NPC npc, int maxHit, AttackType attackStyle, AttackType targetStyle, Entity target) {
        if (target instanceof final Player player) {
            Optional<GauntletArmorTier> tier = GauntletArmorTier.get(player, gauntletType);
            maxHit = calculateMaxHit(tier.orElse(null));
        }
        return CombatScript.super.getRandomMaxHit(npc, maxHit, attackStyle, targetStyle, target);
    }

    @Override
    public void processPoison(NPC npc, Entity target) {
        CombatScript.super.processPoison(npc, target);
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof final Player player)) {
            return 0;
        }

        setAnimation(STANDARD_ATTACK_ANIMATION);

        if (tornadoCooldown == 0) {
            spawnTornadoes(player);
            return getCombatDefinitions().getAttackSpeed() + 4;
        }

        if (hitType == HitType.MAGIC) {
            boolean deactivate = Utils.random(100) <= 15;

            Projectile deactivateProjectile = gauntletType.isCorrupted() ? MAGIC_PRAYER_DEACTIVATION_PROJECTILE_CORRUPTED : MAGIC_PRAYER_DEACTIVATION_PROJECTILE;
            Projectile magicProjectile = gauntletType.isCorrupted() ? MAGIC_PROJECTILE_CORRUPTED : MAGIC_PROJECTILE;

            int delay = World.sendProjectile(this, target, deactivate ? deactivateProjectile : magicProjectile);
            if (deactivate) {
                WorldTasksManager.schedule(() -> {
                    if (player.getPrayerManager().getActivePrayers().size() > 0) {
                        player.sendMessage("<col=ff0000>Your prayers have been disabled!");
                        player.getPrayerManager().deactivateActivePrayers();
                    }
                }, delay);
            }

            Hit hit = new Hit(getRandomMaxHit(this, combatDefinitions.getMaxHit(), AttackType.MAGIC, target), HitType.MAGIC);
            delayHit(this, delay, target, hit);
        } else {
            int delay = World.sendProjectile(this, target, gauntletType.isCorrupted() ? RANGED_PROJECTILE_CORRUPTED : RANGED_PROJECTILE);
            Hit hit = new Hit(getRandomMaxHit(this, combatDefinitions.getMaxHit(), AttackType.RANGED, target), HitType.RANGED);
            delayHit(this, delay, target, hit);
        }

        outgoingHitCount++;

        int attackSpeed = getBaseCombatDefinitions().getAttackSpeed();
        if (outgoingHitCount == TYPE_SWAP_OUTGOING_THRESHOLD) {
            WorldTasksManager.schedule(() -> {
                if (!isDead())
                    setAnimation(new Animation(8754));
            }, 1);
            if (hitType == HitType.RANGED) hitType = HitType.MAGIC;
            else if (hitType == HitType.MAGIC) hitType = HitType.RANGED;
            outgoingHitCount = 0;
            attackSpeed++;
        }

        return attackSpeed;
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return 0.5;
    }

    @Override
    public double getRangedPrayerMultiplier() {
        return 0.5;
    }

    private void spawnTilePattern(Player target) {
        HunllefPhase phase = getPhase();

        List<List<Pair<Integer, Integer>>> patterns = phase.getTilePatterns();
        List<Pair<Integer, Integer>> randomPattern = Utils.random(patterns);

        for (Pair<Integer, Integer> tileOffset : randomPattern) {
            Location tile = new Location(baseX + tileOffset.first(), baseY + tileOffset.second(), 1);

            WorldObject object = World.getObjectWithType(tile, 22);

            object.setId(object.getId() + 1);
            World.spawnObject(object);

            WorldTasksManager.schedule(new HunleffTickTask(this) {

                @Override
                public void onTick(int tick) {

                    if (tick == 11 || target.isNulled() || target.isDead() || target.isFinished()) {
                        stop();
                        return;
                    }

                    if (tick == 5) {
                        object.setId(object.getId() + 1);
                        World.spawnObject(object);
                    }

                    if (tick > 5) {
                        if (target.getLocation().matches(tile)) {
                            target.applyHit(new Hit(Utils.random(10, 20), HitType.DEFAULT));
                        }
                    }
                }

                @Override
                public void stop() {
                    // standard = 36149
                    object.setId(object.getId() - 2);  // Set tile back to standard type.
                    World.spawnObject(object);
                    super.stop();
                }
            }, 0, 0);
        }
    }

    public void spawnTornadoes(Player player) {
        setAnimation(TORNADO_ATTACK_ANIMATION);

        WorldTasksManager.schedule(() -> {
            for (int i = 0; i < getPhase().getNumTornadoes() + (gauntletType.isCorrupted() ? 1 : 0); i++) {


                Location location = findLocation(player);
                NPC npc = new NPC(gauntletType.isCorrupted() ? TORNADO_NPC_CORRUPTED : TORNADO_NPC, location, Direction.SOUTH, 0) {
                    @Override
                    public boolean isEntityClipped() {
                        return false;
                    }
                };
                npc.setSpawned(true);
                npc.spawn();

                WorldTasksManager.schedule(new HunleffTickTask(this) {

                    Location lastPlayerLocation;
                    boolean justSpawned = true;
                    @Override
                    public void onTick(int tick) {
                        if (tick == 15) {
                            stop();
                            return;
                        }
                        if (npc.getLocation().matches(player.getLocation())) {
                            Optional<GauntletArmorTier> tier = GauntletArmorTier.get(player, gauntletType);
                            int damage = calculateRandomTornadoDamage(tier.orElse(null));
                            Hit hit = new Hit(damage, HitType.DEFAULT);
                            player.applyHit(hit);
                            player.setGraphics(new Graphics(1717));
                        }
                        if(!justSpawned && player.getLocation().withinDistance(lastPlayerLocation, 1)) {
                            npc.resetWalkSteps();
                            npc.addWalkSteps(player.getLocation().getX(), player.getLocation().getY());
                        } else {
                            justSpawned = false;
                        }
                        lastPlayerLocation = player.getLocation();
                    }

                    @Override
                    public void stop() {
                        npc.remove();
                        super.stop();
                    }
                }, 0, 0);
            }
        }, 1);

        // Predetermine our next tornado attack.
        tornadoCooldown = TORNADO_COOLDOWN_TICKS;
    }

    private Location findLocation(Player player) {
        @SuppressWarnings("UnusedAssignment") Location potential = new Location(0);
        do {
            int offsetX = Utils.random(0, 11);
            int offsetY = Utils.random(0, 11);
            potential = new Location(baseX + offsetX, baseY + offsetY, 1);
        } while (potential.matches(player.getLocation()));

        return potential;
    }

    @Override
    public void animate() {
        CombatScript.super.animate();
    }

    @Override
    public void attackSound() {
        CombatScript.super.attackSound();
    }

    @Override
    public void playAttackSound(Entity target) {
        CombatScript.super.playAttackSound(target);
    }

    @Override
    protected void postHitProcess(Hit hit) {
        super.postHitProcess(hit);

        if (hit.getSource() instanceof Player player) {
            player.getHpHud().updateValue(hitpoints);
        }
    }

    public HunllefPhase getPhase() {
        if (gauntletType == GauntletType.STANDARD) {
            if (hitpoints >= 400) {
                return HunllefPhase.ONE;
            } else if (hitpoints >= 200) {
                return HunllefPhase.TWO;
            } else {
                return HunllefPhase.THREE;
            }
        } else {
            if (hitpoints >= 666) {
                return HunllefPhase.ONE;
            } else if (hitpoints >= 333) {
                return HunllefPhase.TWO;
            } else {
                return HunllefPhase.THREE;
            }
        }
    }

    private int calculateMaxHit(GauntletArmorTier tier) {////nerfed to compensate the combat system
        if (tier == null)
            return gauntletType.isCorrupted() ? 28 : 24;
        else
            return switch (tier) {
                case BASIC -> gauntletType.isCorrupted() ? 24 : 20;
                case ATTUNED -> gauntletType.isCorrupted() ? 18 : 16;
                case PERFECTED -> gauntletType.isCorrupted() ? 14 : 12;
            };
    }

    private int calculateRandomTornadoDamage(GauntletArmorTier tier) {
        if (tier == null)
            return gauntletType.isCorrupted() ? Utils.random(15, 30) : Utils.random(10, 20);
        else
            return switch (tier) {
                case BASIC -> gauntletType.isCorrupted() ? Utils.random(15, 25) : Utils.random(10, 16);
                case ATTUNED -> gauntletType.isCorrupted() ? Utils.random(10, 20) : Utils.random(7, 13);
                case PERFECTED -> gauntletType.isCorrupted() ? Utils.random(8, 15) : Utils.random(5, 10);
            };
    }

    @Override
    public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);

        final Entity source = hit.getSource();
        if (source instanceof final Player player) {
            final HitType type = hit.getHitType();
            if (type == HitType.MELEE && !player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE) || type == HitType.RANGED && !player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MISSILES) ||
                    type == HitType.MAGIC && !player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                prayedCorrectly = false;
            }
        }
    }

    public void onPlayerEntered() {
        tornadoCooldown = TORNADO_COOLDOWN_TICKS;
    }

    public HunllefType getType() {
        return type;
    }

}
