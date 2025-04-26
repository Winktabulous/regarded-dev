package com.zenyte.game.content.treasuretrails.npcs.mimic;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CAType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Kris | 27/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class Mimic extends NPC implements CombatScript {
    private static final Logger log = LoggerFactory.getLogger(Mimic.class);


    /**
     * An enum containing all of the four candy types the Mimic can throw around.
     */
    private enum Candy {
        PINK(1670, 1674), GREEN(1671, 1675), RED(1672, 1676), BLUE(1673, 1677);
        private static final List<Candy> immutableCandyList = Arrays.asList(PINK, PINK, PINK, RED, GREEN, BLUE);

        Candy(final int projectile, final int graphics) {
            this.projectile = new Projectile(projectile, 30, 10, 5, 75, 50, 0, 10);
            this.graphics = new Graphics(graphics);
        }

        @NotNull
        private final Projectile projectile;
        @NotNull
        private final Graphics graphics;

        /**
         * Executes the candy throwing sequence and accepts the consumer once the projectile has reached the destination.
         *
         * @param middle   the center of the Mimic.
         * @param tile     the tile to which the candy is thrown.
         * @param consumer the consumer which accepts the destination tile when the projectile reaches it.
         */
        private final void execute(@NotNull final Location middle, @NotNull final Location tile, @NotNull final Consumer<Location> consumer) {
            final int time = Candy.PINK.projectile.getProjectileDuration(middle, tile);
            World.sendGraphics(new Graphics(graphics.getId(), time, 0), tile);
            World.scheduleProjectile(middle, tile, projectile).schedule(() -> consumer.accept(tile));
        }
    }

    /**
     * The melee animation of the Mimic.
     */
    private static final Animation melee = new Animation(8308);
    /**
     * The candy-throwing animation of the Mimic.
     */
    private static final Animation candy = new Animation(8309);
    /**
     * An array of three third-age monsters which the Mimic summons with the candy-throwing attack.
     * There can only be one of each attack-style third-age monster at a time.
     * <p>Indexing of the array is based on subtracting {@value ThirdAgeMonster#WARRIOR} from the monster's id.</p>
     */
    private final ThirdAgeMonster[] thirdAgeMonsters = new ThirdAgeMonster[3];
    @NotNull
    private final MimicInstance instance;
    public static final int CHALLENGE_MIMIC = 7979;
    public static final int ATTACKABLE_MIMIC = 8633;

    Mimic(@NotNull Location tile, @NotNull final MimicInstance instance) {
        super(CHALLENGE_MIMIC, tile, Direction.SOUTH, 0);
        spawned = true;
        this.instance = instance;
        this.combat = new NPCCombat(this) {
            /**
             * Special overridden check-all method that ignores the normal behaviour of trying to move outside from underneath a player when one is standing underneath the Mimic, as the Mimic has a
             * special mechanic where it starts dealing stomp damage to whomever stands beneath it, increasing rapidly with each tick.
             * @return whether or not the combat should be terminated.
             */
            @Override
            protected boolean checkAll() {
                if (target == null || target.isFinished() || target.isNulled() || target.isDead() || npc.isDead() || npc.isFinished() || npc.isLocked() || npc.getPlane() != target.getPlane()) {
                    return false;
                }
                if (npc.getRetreatMechanics().process(target) || !attackable(target)) {
                    return false;
                }
                if (npc.isMovementRestricted() || colliding()) {
                    return true;
                }
                return appendMovement();
            }
        };
    }

    /**
     * Finds a list of up to 6 locations around the player which are within the area's boundaries, in sight, do not overlap and do not collide with the Mimic itself.
     *
     * @param tile the tile around which to seek for a radius of three tiles.
     * @return a list of anywhere from 1 to 6 locations; 6 are ideally always given as long as nothing blocks the path.
     */
    @NotNull
    private List<Location> findLocations(@NotNull final Location tile) {
        final ObjectArrayList<Location> list = new ObjectArrayList<Location>();
        int tryCount = 500;
        list.add(tile);
        while (list.size() < 6) {
            if (--tryCount <= 0) {
                break;
            }
            final Location location = RandomLocation.create(tile,  3);
            if (CollisionUtil.collides(getLocation(), getSize(), location, 1, 0) || list.contains(location) || ProjectileUtils.isProjectileClipped(null, null, tile, location, true) || !World.isFloorFree(location, 1)) {
                continue;
            }
            list.add(location);
        }
        return list;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        iterateCollidingPlayers();
    }

    @Override
    public void onFinish(@NotNull final Entity source) {
        super.onFinish(source);
        clearThirdAgeMonsters();
    }

    @Override
    public void onDeath(@NotNull final Entity source) {
        try {
            instance.player().ifPresent(player -> {
                player.getBossTimer().finishTracking("Mimic");
                player.getCombatAchievements().complete(CAType.MIMIC_VETERAN);
            });
            instance.finish();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * Checks to see whether or not the player is the owner of the instance.
     *
     * @param player the player we're checking.
     * @return whether or not the player is the owner of the instance.
     */
    public boolean isOwner(@NotNull final Player player) {
        return instance.getUsername().equalsIgnoreCase(player.getUsername());
    }

    /**
     * Executes the death sequence on every remaining alive third-age monster in the field.
     */
    private void clearThirdAgeMonsters() {
        for (final ThirdAgeMonster monster : thirdAgeMonsters) {
            if (monster == null || monster.isDead() || monster.isFinished()) {
                continue;
            }
            monster.setHitpoints(0);
        }
    }

    @Override
    protected void sendNotifications(final Player player) {
        player.getNotificationSettings().increaseKill("mimic");
        player.getNotificationSettings().sendBossKillCountNotification("mimic");
    }

    /**
     * Iterates over all the players within the Mimic instance and applies repeated damage to everyone standing underneath the Mimic.
     * The damage increases every three attacks up to a maximum of 6 damage per tick. Damage resets to 1 if a player leaves from underneath the Mimic.
     */
    private void iterateCollidingPlayers() {
        for (final Player player : instance.getPlayers()) {
            if (!CollisionUtil.collides(getX(), getY(), getSize(), player.getX(), player.getY(), player.getSize())) {
                continue;
            }
            final int damageCounter = player.getNumericTemporaryAttribute("mimic damage counter").intValue();
            final long lastTick = player.getNumericTemporaryAttribute("mimic last damage tick").longValue();
            final boolean reset = lastTick != WorldThread.WORLD_CYCLE;
            final int damage = Math.min(6, 1 + (reset ? 0 : (damageCounter / 3)));
            CombatUtilities.delayHit(this, 0, player, new Hit(damage, HitType.REGULAR));
            final Map<Object, Object> temporaryAttributes = player.getTemporaryAttributes();
            temporaryAttributes.put("mimic last damage tick", WorldThread.WORLD_CYCLE + 1);
            temporaryAttributes.put("mimic damage counter", reset ? 0 : (damageCounter + 1));
        }
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public int attack(@NotNull Entity target) {
        final int attackType = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 1);
        if (attackType == 2) {
            setAnimation(melee);
            delayHit(0, target, melee(target, 23));
        } else if (attackType == 1) {
            final Location tile = new Location(target.getLocation());
            calcFollow(tile, 10, true, false, false);
            return getLocation().getTileDistance(tile);
        } else {
            setAnimation(candy);
            final Location middle = getMiddleLocation();
            WorldTasksManager.schedule(() -> {
                final List<Location> tiles = findLocations(new Location(target.getLocation()));
                for (int i = tiles.size() - 1; i >= 0; i--) {
                    final Mimic.Candy candy = Candy.immutableCandyList.get(i);
                    candy.execute(middle, tiles.get(i), tile -> {
                        if (candy == Candy.PINK) {
                            if (target.getLocation().matches(tile)) {
                                delayHit(0, target, new Hit(Utils.random(8, 12), HitType.REGULAR));
                            }
                            return;
                        }
                        spawnIfAbsent(tile, candy == Candy.RED ? ThirdAgeMonster.WARRIOR : candy == Candy.GREEN ? ThirdAgeMonster.RANGER : ThirdAgeMonster.MAGE);
                    });
                }
            }, 1);
            return combatDefinitions.getAttackSpeed() << 1;
        }
        return combatDefinitions.getAttackSpeed();
    }

    /**
     * Spawns a third-age monster if one is not present in the field at the time.
     *
     * @param tile the tile where the third-age monster is supposed to rise at.
     * @param id   the id of the third-age monster.
     */
    private void spawnIfAbsent(@NotNull final Location tile, @MagicConstant(valuesFromClass = ThirdAgeMonster.class) final int id) {
        final int index = id - ThirdAgeMonster.WARRIOR;
        ThirdAgeMonster monster = thirdAgeMonsters[index];
        if (monster == null || monster.isFinished()) {
            thirdAgeMonsters[index] = monster = new ThirdAgeMonster(id, tile);
            monster.spawn();
            //If the Mimic has already flipped over, kill this third-age creature straight after.
            if (isDead() || isFinished()) {
                WorldTasksManager.schedule(() -> thirdAgeMonsters[index].setHitpoints(0));
            }
        }
    }
}
