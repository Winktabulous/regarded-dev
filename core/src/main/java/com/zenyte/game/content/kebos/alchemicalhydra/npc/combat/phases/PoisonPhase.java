package com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.phases;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.HydraPhaseSequence;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entities;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Tommeh | 02/11/2019 | 20:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class PoisonPhase implements HydraPhaseSequence {
    public static final Map<Direction, Graphics> directionMap = ImmutableMap.<Direction, Graphics>builder().put(Direction.NORTH, new Graphics(1659)).put(Direction.NORTH_EAST, new Graphics(1660)).put(Direction.EAST, new Graphics(1661)).put(Direction.SOUTH_EAST, new Graphics(1655)).put(Direction.SOUTH, new Graphics(1655)).put(Direction.SOUTH_WEST, new Graphics(1656)).put(Direction.WEST, new Graphics(1657)).put(Direction.NORTH_WEST, new Graphics(1658)).build();
    public static final List<Byte[]> offsets = new ArrayList<Byte[]>() {
        {
            for (byte x = -3; x <= 3; x++) {
                for (byte y = -3; y <= 3; y++) {
                    add(new Byte[] {x, y});
                }
            }
        }
    };
    private static final Animation poisonAttackAnim = new Animation(8234);
    private static final Animation rangedAttackAnim = new Animation(8235);
    private static final Animation magicAttackAnim = new Animation(8236);
    public static final Graphics poisonSplashGfx = new Graphics(1645);
    public static final Projectile poisonAttackProj = new Projectile(1644, 65, 0, 37, 2, 15, 0, 10);
    private int attacks;
    private boolean initialSpecial;
    private final List<Location> poisonTiles = new ArrayList<>(5);

    @Override
    public int autoAttack(final AlchemicalHydra hydra, final Player player) {
        final AttackType style = hydra.getCombatDefinitions().getAttackStyle();
        final int maxHit = (int) (hydra.getMaxHit() * hydra.getAttackModifier());
        if (style.equals(AttackType.MAGIC)) {
            hydra.setAnimation(magicAttackAnim);
            hydra.delayHit(World.sendProjectile(hydra, player, magicAttackProj), player, new Hit(hydra, hydra.getRandomMaxHit(hydra, maxHit, AttackType.MAGIC, player), HitType.MAGIC));
            hydra.delayHit(World.sendProjectile(hydra.getMagicHeadLocation(player), player, magicAttackProj), player, new Hit(hydra, hydra.getRandomMaxHit(hydra, maxHit, AttackType.MAGIC, player), HitType.MAGIC));
        } else {
            hydra.setAnimation(rangedAttackAnim);
            hydra.delayHit(World.sendProjectile(hydra, player, rangedAttackProj), player, new Hit(hydra, hydra.getRandomMaxHit(hydra, maxHit, AttackType.RANGED, player), HitType.RANGED));
            hydra.delayHit(World.sendProjectile(hydra.getRangedHeadLocation(player), player, rangedAttackProj), player, new Hit(hydra, hydra.getRandomMaxHit(hydra, maxHit, AttackType.RANGED, player), HitType.RANGED));
        }
        attacks++;
        hydra.incrementAutoAttacks();
        return hydra.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean attack(final AlchemicalHydra hydra, final Player player) {
        if (attacks == 3 && !initialSpecial || attacks == 9) {
            final int pools = Utils.random(4, 5);
            final Location base = new Location(player.getLocation());
            final ArrayList<Location> tiles = new ArrayList<Location>(pools);
            Collections.shuffle(offsets);
            poisonTiles.clear();
            tiles.add(base);
            for (final Byte[] offset : offsets) {
                final Location tile = base.transform(offset[0], offset[1], 0);
                if (tiles.contains(tile) || !World.isFloorFree(tile, 1) || ProjectileUtils.isProjectileClipped(null, null, player, tile, true)) {
                    continue;
                }
                tiles.add(tile);
                if (tiles.size() == pools) {
                    break;
                }
            }
            hydra.setAnimation(poisonAttackAnim);
            final Location hydraCenter = hydra.getMiddleLocation();
            for (final Location tile : tiles) {
                World.sendProjectile(hydra, tile, poisonAttackProj);
                final int direction = Entities.getRoundedDirection(DirectionUtil.getFaceDirection(tile.getX() - hydraCenter.getX(), tile.getY() - hydraCenter.getY()), 1024);
                final Direction directionConstant = CollectionUtils.findMatching(Direction.values, dir -> dir.getNPCDirection() == direction);
                assert directionConstant != null;
                final Graphics graphics = directionMap.get(directionConstant);
                assert graphics != null;
                WorldTasksManager.schedule(new TickTask() {
                    @Override
                    public void run() {
                        if (ticks == 0) {
                            World.sendGraphics(poisonSplashGfx, tile);
                        } else if (ticks == 1) {
                            if (!player.getLocation().matches(tile) && player.getLocation().withinDistance(tile, 1)) {
                                applyPoison(hydra, player);
                                hydra.setPlayerHitByPoisonOnce();
                            }
                            poisonTiles.add(tile);
                            World.sendGraphics(graphics, tile);
                        } else if (ticks == 17) {
                            poisonTiles.remove(tile);
                            stop();
                        }
                        ticks++;
                    }
                }, poisonAttackProj.getTime(hydra, tile), 0);
            }
            if (attacks == 3) {
                initialSpecial = true;
                attacks = 0;//Also reset the counter so it uses the lightning special every 9 attacks.
            } else if (attacks == 9) {
                attacks = 0;
            }
            return true;
        }
        autoAttack(hydra, player);
        return false;
    }

    @Override
    public void process(final AlchemicalHydra hydra, final Player player) {
        for (final Location tile : poisonTiles) {
            if (player.getLocation().matches(tile)) {
                applyPoison(hydra, player);
            }
        }
    }

}
