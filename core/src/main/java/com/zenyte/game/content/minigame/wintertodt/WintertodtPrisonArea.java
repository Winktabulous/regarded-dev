package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.enums.ContainerItem;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.kourend.GreatKourend;
import com.zenyte.game.world.region.area.plugins.*;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;

/**
 * @author Corey
 * @since 10:06 - 22/07/2019
 */
public class WintertodtPrisonArea extends GreatKourend implements CannonRestrictionPlugin, LayableTrapRestrictionPlugin, DropPlugin, RandomEventRestrictionPlugin, LogoutPlugin, LoginPlugin, CycleProcessPlugin {
    static IntSet aoeAttackLocations = new IntOpenHashSet();

    public WintertodtPrisonArea() {
        initialiseWintertodtBoss();
        for (Wintertodt.Corner corner : Wintertodt.Corner.VALUES) {
            corner.getPyromancer().spawn();
        }
    }

    private static void startAOEAttack(final Player player) {
        final Location aoeMiddleLocation = getAOEAttackMiddleLocation(player);
        if (aoeMiddleLocation == null) {
            return;
        }
        final Location middleLocation = new Location(aoeMiddleLocation.getX(), aoeMiddleLocation.getY());
        final HashSet<Location> subLocations = getSubLocations(middleLocation);
        for (Location subLocation : subLocations) {
            aoeAttackLocations.add(subLocation.getPositionHash());
        }
        final HashSet<Location> cornerLocations = new HashSet<>(Direction.intercardinalDirections.length);
        for (Direction direction : Direction.intercardinalDirections) {
            cornerLocations.add(middleLocation.transform(direction, 1));
        }
        World.spawnObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, middleLocation.getX(), middleLocation.getY(), 0));
        for (Location location : cornerLocations) {
            World.spawnObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, location.getX(), location.getY(), 0));
        }
        WorldTasksManager.schedule(() -> {
            if (Wintertodt.betweenRounds()) {
                World.removeObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, middleLocation.getX(), middleLocation.getY(), 0));
                for (Location location : cornerLocations) {
                    World.removeObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, location.getX(), location.getY(), 0));
                }
                return;
            }
            World.sendSoundEffect(middleLocation, new SoundEffect(1487, 2, 0));
            World.spawnTemporaryObject(new WorldObject(29324, 10, 0, middleLocation.getX(), middleLocation.getY(), 0), 7);
            World.sendGraphics(new Graphics(502), middleLocation);
            for (Location location : cornerLocations) {
                World.spawnTemporaryObject(new WorldObject(29325, 10, 0, location.getX(), location.getY(), 0), 7);
                World.sendGraphics(new Graphics(1311), location);
            }
            for (final Player p : CharacterLoop.find(middleLocation, 1, Player.class, p -> !Wintertodt.insideSafeArea(p))) {
                final int damageAmount = 3 * Wintertodt.getBaseAOEDamageAmount(p);
                p.sendSound(new SoundEffect(518));
                p.applyHit(new Hit(damageAmount, HitType.DEFAULT));
                p.sendMessage("The freezing cold attack of the Wintertodt's magic hits you.");
            }
            WorldTasksManager.schedule(() -> {
                for (Location subLocation : subLocations) {
                    aoeAttackLocations.remove(subLocation.getPositionHash());
                }
            }, 8);
        }, 7);
    }

    @Nullable
    private static Location getAOEAttackMiddleLocation(final Player player) {
        Location middleLocation = player.getMiddleLocation();
        HashSet<Location> subLocations = getSubLocations(middleLocation);
        if (canAOEInLocation(subLocations)) {
            return middleLocation;
        }
        return null;
    }

    private static HashSet<Location> getSubLocations(final Location middleLocation) {
        final HashSet<Location> subLocations = new HashSet<Location>(9);
        subLocations.add(middleLocation);
        for (Direction direction : Direction.values) {
            subLocations.add(middleLocation.transform(direction, 1));
        }
        return subLocations;
    }

    private static boolean canAOEInLocation(final HashSet<Location> locations) {
        for (Location location : locations) {
            if (aoeAttackLocations.contains(location.getPositionHash())) {
                return false;
            }
            if (Wintertodt.Corner.BRAZIER_TILES.contains(location.getPositionHash())) {
                return false;
            }
            if (!World.isSquareFree(location.getX(), location.getY(), 0, 1)) {
                return false;
            }
        }
        return true;
    }

    private void initialiseWintertodtBoss() {
        Wintertodt.resetWintertodt();
        Wintertodt.setEnergyToMax();
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1624, 3968}, {1636, 3968}, {1643, 3985}, {1653, 3996}, {1653, 4019}, {1642, 4031}, {1619, 4031}, {1607, 4018}, {1607, 3995}}, 0)};
    }

    @Override
    public void enter(Player player) {
        GameInterface.WINTERTODT.open(player);
        player.getPacketDispatcher().sendClientScript(1433); // unhide overlay components
        Wintertodt.resetPlayer(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getInterfaceHandler().closeInterface(GameInterface.WINTERTODT);
        player.getPacketDispatcher().sendClientScript(1432); // hide overlay components
        Wintertodt.resetPlayer(player);
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        Wintertodt.resetPlayer(player);
    }

    @Override
    public void login(Player player) {
        Wintertodt.resetPlayer(player);
    }

    @Override
    public String name() {
        return "Wintertodt";
    }

    @Override
    public boolean dropOnGround(final Player player, final Item item) {
        // empty vials and jugs disappear when dropped
        return item.getId() != ContainerItem.Type.VIAL.getEmpty().getId() && item.getId() != ContainerItem.Type.JUG.getEmpty().getId();
    }

    @Override
    public void process() {
        Wintertodt.tickRoundTimer();
        if (Wintertodt.betweenRounds()) {
            return;
        }
        processWintertodtEnergy();
        for (final Player player : players) {
            if (Wintertodt.insideSafeArea(player)) {
                // if player is standing inside the cold-free safe area
                continue;
            }
            randomColdDamage(player);
            areaAttack(player);
        }
        if (!Wintertodt.atFullEnergy()) {
            for (Wintertodt.Corner corner : Wintertodt.Corner.VALUES) {
                final Brazier brazier = corner.getBrazier();
                final Pyromancer pyromancer = corner.getPyromancer();
                spawnBrazierProjectile(brazier, pyromancer);
                brazierAttack(brazier);
                pyromancerAttack(pyromancer);
            }
        }
    }

    private void processWintertodtEnergy() {
        double damage = 0;
        for (Wintertodt.Corner corner : Wintertodt.Corner.VALUES) {
            if (corner.getBrazier().isLit() && corner.getPyromancer().isHealthy()) {
                damage += 1.85;
            }
        }
        if (damage == 0) {
            Wintertodt.dealDamage(-0.75); // recover hp
        } else {
            Wintertodt.dealDamage(damage);
        }
    }

    private void spawnBrazierProjectile(final Brazier brazier, final Pyromancer pyromancer) {
        if (!brazier.isFireProjectiles() || !pyromancer.isHealthy()) {
            return;
        }
        brazier.setTicks(brazier.getTicks() + 1);
        if (brazier.getTicks() < 2) {
            // only spawn every other tick
            return;
        } else {
            brazier.setTicks(0);
        }
        final Direction pointingDirection = pyromancer.getSpawnDirection();
        WorldTasksManager.schedule(() -> {
            final Location fromlocation = new Location(brazier.getX(), brazier.getY()).transform(1, 1, 0).transform(pointingDirection, -1);
            final Location toLocation = fromlocation.transform(pointingDirection, 9);
            final NPC npc = World.spawnNPC(new NPC(7373, fromlocation, pointingDirection, 0));
            Objects.requireNonNull(npc).addWalkSteps(toLocation.getX(), toLocation.getY(), -1, false);
            WorldTasksManager.schedule(npc::finish, 7);
        });
    }

    /**
     * @param player
     * @see <a href=https://oldschool.runescape.wiki/w/Wintertodt#Wintertodt&#39;s_attacks>Wintertodt's attacks - Standard attack</a>
     */
    private void randomColdDamage(final Player player) {
        if (player.getTemporaryAttributes().containsKey("wintertodt cold damage")) {
            return;
        }
        int braziersLit = Wintertodt.getNumberOfBraziersLit();
        final int brazierModifier = braziersLit >= 3 ? 3 : 0;
        final boolean isBelowFiftyPercentEnergy = Wintertodt.getCurrentEnergyPercentage() <= 50;
        final int chanceToHit = 35 + (braziersLit * brazierModifier) + (isBelowFiftyPercentEnergy ? 20 : 0);
        if (Utils.random(0, chanceToHit) == 0) {
            braziersLit = Math.min(Wintertodt.getNumberOfBraziersLit(), 3);
            final int randomHitAmount = ((16 - Wintertodt.getNumberOfWarmClothingWorn(player) - (2 * braziersLit)) * (player.getMaxHitpoints() + 1)) / player.getSkills().getLevelForXp(SkillConstants.FIREMAKING);
            player.sendSound(new SoundEffect(518 + Utils.random(0, 3)));
            player.applyHit(new Hit(Math.max(1, randomHitAmount), HitType.DEFAULT));
            player.sendFilteredMessage("The cold of the Wintertodt seeps into your bones.");
            player.addTemporaryAttribute("wintertodt cold damage", true);
            WorldTasksManager.schedule(() -> player.getTemporaryAttributes().remove("wintertodt cold damage"), 5);
        }
    }

    /**
     * @see <a href=https://oldschool.runescape.wiki/w/Wintertodt#Wintertodt&#39;s_attacks>Wintertodt's attacks - Brazier attack</a>
     */
    private void brazierAttack(final Brazier brazier) {
        if (!brazier.isLit() || brazier.isBeingAttacked()) {
            return;
        }
        final int braziersLit = Wintertodt.getNumberOfBraziersLit();
        final boolean isBelowFiftyPercentEnergy = Wintertodt.getCurrentEnergyPercentage() <= 50;
        if (Utils.random(80 + (braziersLit * 17) + (isBelowFiftyPercentEnergy ? 45 : 0)) == 0) {
            brazier.startSmallAttack();
        } else if (Utils.random(165 + (braziersLit * 25) + (isBelowFiftyPercentEnergy ? 50 : 0)) == 0) {
            brazier.startLargeAttack();
        }
    }

    /**
     * @param player
     * @see <a href=https://oldschool.runescape.wiki/w/Wintertodt#Wintertodt&#39;s_attacks>Wintertodt's attacks - Area attack</a>
     */
    private void areaAttack(final Player player) {
        final boolean isBelowFiftyPercentEnergy = Wintertodt.getCurrentEnergyPercentage() <= 50;
        final int playerMultiplier = (int) Math.min(Math.pow(Wintertodt.getAmountOfPlayers(), 1.5), 250);
        if (Utils.random((int) (playerMultiplier + 90 + (Wintertodt.getNumberOfBraziersLit() * 30) / (isBelowFiftyPercentEnergy ? 0.6 : 1))) != 0) {
            return;
        }
        startAOEAttack(player);
    }

    private void pyromancerAttack(final Pyromancer pyromancer) {
        if (!pyromancer.isHealthy() || pyromancer.isBeingAttacked()) {
            return;
        }
        final boolean isBelowFiftyPercentEnergy = Wintertodt.getCurrentEnergyPercentage() <= 50;
        if (Utils.random(0, 150 + (Wintertodt.getNumberOfBraziersLit() * 25) + (isBelowFiftyPercentEnergy ? 50 : 0)) == 0) {
            pyromancer.startColdAttack();
        }
    }
}
