package com.zenyte.game.content.boss.sarachnis;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andys1814
 */
public final class Sarachnis extends NPC implements CombatScript, Spawnable {

    private static final int SARACHNIS = NpcId.SARACHNIS;

    private static final int MELEE_ANIMATION = 8339;

    private static final int RANGED_ANIMATION = 4410;

    private static final Projectile RANGED_PROJECTILE = new Projectile(1686, 50, 20, 25, 15, 60, 90, 5);

    private static final Projectile WEB_PROJECTILE = new Projectile(1687, 50, 20, 25, 15, 35, 90, 5);

    private Set<Location> walkDestinations;

    private int outgoingHitCount = 4;

    private boolean firstSpawns = false;
    private boolean secondSpawns = false;

    private Location lastWalkDestination = null;

    private final List<SarachnisMinion> spawns = new ArrayList<>();
    private AttackType lastAttackType;
    private boolean rangeTwiceInARow;
    private boolean hitAnyone = false;

    @Override
    public NPC spawn() {
        firstSpawns = false;
        secondSpawns = false;
        outgoingHitCount = 0;
        lastWalkDestination = null;
        return super.spawn();
    }

    void setWalkDestinations(Set<Location> walkDestinations) {
        this.walkDestinations = walkDestinations;
    }

    public Sarachnis(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        setAggressionDistance(25);
        this.maxDistance = 50;
        this.attackDistance = 50;
        setForceAggressive(true);
//        NPCCombatDefinitions combatDefinitions = new NPCCombatDefinitions();
//        setCombatDefinitions(combatDefinitions);
//        AttackDefinitions attackDefinitions = new AttackDefinitions();
//        attackDefinitions.setType(AttackType.RANGED);
//        attackDefinitions.setMaxHit(31);
//        combatDefinitions.setAttackDefinitions(attackDefinitions);
    }

    @Override
    public boolean canAttack(final Player source) {
        return true;
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) {
            return 6;
        }

        // Web special attack, happens every 4 standard attacks from Sarachnis.
        if (outgoingHitCount == 4) {
            setAnimation(new Animation(RANGED_ANIMATION));
            setForceTalk(new ForceTalk("Hsss!"));

            int delay = World.sendProjectile(this, target, WEB_PROJECTILE);

            WorldTasksManager.schedule(() -> {
                if (target instanceof Player player) {
                    if (!player.getAraneaBoots().isPlayerWebImmune()) {
                        player.freeze(6);
                        player.sendFilteredMessage("Sarachnis has webbed you in place!");
                    }
                }
                World.spawnTemporaryObject(new WorldObject(34895, WorldObject.DEFAULT_TYPE, WorldObject.DEFAULT_ROTATION, target.getLocation().copy()), 6);
            }, delay);

            Set<Location> destinations = new HashSet<>(walkDestinations);
            if (lastWalkDestination != null) {
                destinations.removeIf(location -> location.equals(lastWalkDestination));
            }

            Location destination = Utils.getRandomCollectionElement(destinations).copy();

            addWalkSteps(destination.getX() - (getSize() / 2), destination.getY() - (getSize() / 2), Integer.MAX_VALUE, false);
            lastWalkDestination = destination;

            outgoingHitCount = 0;
            lastAttackType = null;
            return 8;
        }

        AttackType type = isWithinMeleeDistance(this, target) ? MELEE : RANGED;
        if (RANGED.equals(lastAttackType) && RANGED.equals(type)) {
            rangeTwiceInARow = true;
        } else {
            lastAttackType = type;
        }
        setAnimation(new Animation(type == MELEE ? MELEE_ANIMATION : RANGED_ANIMATION));



        int hitDelay = 0;
        if (type == RANGED) {
            hitDelay = World.sendProjectile(this, target, RANGED_PROJECTILE);
        }

        Hit hit = new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), type, target), type == MELEE ? HitType.MELEE : HitType.RANGED).onLand(h -> {
            if (type == MELEE && !((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
                processHit(new Hit(5, HitType.HEALED));
                ((Player) target).sendFilteredMessage("Sarachnis leeches some health as she damages you.");
            } else if (type == RANGED && !((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MISSILES)) {
                processHit(new Hit(10, HitType.HEALED));
                ((Player) target).sendFilteredMessage("Sarachnis leeches some health as she damages you.");
            }
        });
        delayHit(this, hitDelay, target, hit);

        outgoingHitCount++;
        return outgoingHitCount == 4 ? 3 : 6;
    }


    @Override
    public void autoRetaliate(final Entity source) {
        if (getWalkSteps().isEmpty()) {
            super.autoRetaliate(source);
        }
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (hit.getDamage() > 0) {
            hitAnyone = true;
        }
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        var attacker = hit.getSource();
        if (attacker instanceof final Player player) {
            double percentHp = ((double) (getHitpoints() - hit.getDamage()) / getMaxHitpoints()) * 100;
            if (percentHp <= 66.00 && !firstSpawns) {
                spawnMinions(player);
                firstSpawns = true;
            }
            else if (percentHp <= 33.00 && !secondSpawns) {
                spawnMinions(player);
                secondSpawns = true;
            }
        }
    }

    @Override
    public int getRespawnDelay() {
        return 30;
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);

        List<SarachnisMinion> copy = new ArrayList<>(spawns);
        copy.forEach(NPC::sendDeath);
        spawns.clear();
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getCombatAchievements().checkKcTask("sarachnis", 10, CAType.SARACHNIS_NOVICE);
            player.getCombatAchievements().checkKcTask("sarachnis", 25, CAType.SARACHNIS_CHAMPION);
            if (AttackType.CRUSH.equals(player.getCombatDefinitions().getAttackType()) && player.getWeapon() != null) {
                player.getCombatAchievements().complete(CAType.NEWSPAPER_ENTHUSIAST);
            }
            if (!rangeTwiceInARow) {
                player.getCombatAchievements().complete(CAType.READY_TO_POUNCE);
            }
            if (!hitAnyone) {
                player.getCombatAchievements().complete(CAType.INSPECT_REPELLENT);
            }
        }
    }

    @Override
    public int getAttackDistance() {
        return 20;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == SARACHNIS;
    }

    @Override
    public NPCCombatDefinitions getCombatDefinitions() {
        var defs = super.getCombatDefinitions();
            defs.setAttackDefinitions(new AttackDefinitions());
            defs.getAttackDefinitions().setMaxHit(23);
            defs.getSpawnDefinitions().setDeathAnimation(new Animation(8318));
            defs.getAttackDefinitions().setType(AttackType.RANGED);
        var death = defs.getSpawnDefinitions().getDeathAnimation();
        if (death != null)
            deathDelay = Math.max(Math.min((int) Math.ceil(AnimationUtil.getDuration(death) / 1200.0F), 10), 1);
        return defs;
    }

    private Set<Location> getSpawnLocations() {
        Set<Location> locations = new HashSet<>();
        int baseX = 1836;
        int baseY = 9895;

        for (int x = 0; x <= 12; x++)
            for (int y = 0; y <= 12; y++)
                locations.add(new Location(baseX + x, baseY + y));

        return locations;
    }

    private void spawnMinions(Player player) {
        var locations = getSpawnLocations();
        var meleeLocation = Utils.getRandomCollectionElement(locations);
        var mageLocation = locations.stream()
            .filter(location -> location.getDistance(meleeLocation) >= 5)
            .findAny()
            .orElse(Utils.getRandomCollectionElement(locations));
        var instance = player.mapInstance;

        var locA = instance.getLocation(meleeLocation.getX(), meleeLocation.getY(), player.getPlane());
        var locB = instance.getLocation(mageLocation.getX(), mageLocation.getY(), player.getPlane());

        var melee = new SarachnisMinion(8714, locA, Direction.SOUTH, 10, this);
        var mage = new SarachnisMinion(8715, locB, Direction.SOUTH, 10, this);

        melee.spawn();
        mage.spawn();

        spawns.add(melee);
        spawns.add(mage);
    }

    public List<SarachnisMinion> getSpawns() {
        return spawns;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

}
