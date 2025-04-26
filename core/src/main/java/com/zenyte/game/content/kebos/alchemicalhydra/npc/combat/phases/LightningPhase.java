package com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.phases;

import com.zenyte.game.content.kebos.alchemicalhydra.HydraPhase;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.HydraPhaseSequence;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
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
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.CollectionUtils;

/**
 * @author Tommeh | 02/11/2019 | 20:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class LightningPhase implements HydraPhaseSequence {
    private static final Animation lightningAttackAnim = new Animation(8241);
    private static final Animation rangedAttackAnim = new Animation(8242);
    private static final Animation magicAttackAnim = new Animation(8243);
    private static final Projectile lightningCenterProj = new Projectile(1664, 65, 0, 37, 2, 15, 0, 5);
    private static final Projectile lightningAttackProj = new Projectile(1665, 40, 0, 15, 2, 15, 0, 0);
    private static final Graphics lightningCenterGfx = new Graphics(1664);
    private static final Graphics lightningGfx = new Graphics(1666);
    private static final Location LIGHTNING_CENTRAL_LOCATION = new Location(1367, 10268, 0);
    private static final Location[] LIGHTNING_CHAMBER_CORNERS = {new Location(1372, 10263, 0), new Location(1372, 10272, 0), new Location(1362, 10263, 0), new Location(1361, 10272, 0)};
    private int attacks;
    private boolean initialSpecial;

    private Location getNextFollowLocation(final Location lightning, final Location target) {
        if (lightning.matches(target)) {
            return target;
        }
        final int direction = DirectionUtil.getFaceDirection(target.getX() - lightning.getX(), target.getY() - lightning.getY());
        final int entityDirection = Entities.getRoundedDirection(direction, 0);
        final Direction directionConstant = CollectionUtils.findMatching(Direction.values, dir -> dir.getNPCDirection() == entityDirection);
        assert directionConstant != null;
        return lightning.transform(directionConstant, 1);
    }

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
        }
        attacks++;
        hydra.incrementAutoAttacks();
        return hydra.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean attack(final AlchemicalHydra hydra, final Player player) {
        if (attacks == 3 && !initialSpecial || attacks == 9) {
            final Location center = hydra.getInstance().getLocation(LIGHTNING_CENTRAL_LOCATION);
            hydra.setAnimation(lightningAttackAnim);
            World.sendProjectile(hydra, center, lightningCenterProj);
            WorldTasksManager.schedule(new TickTask() {
                Location corner;
                int previousTick;
                int delay;
                int lightnings;
                @Override
                public void run() {
                    if (!hydra.getPhase().equals(HydraPhase.LIGHTNING)) {
                        stop();
                        return;
                    }
                    if (ticks <= 3) {
                        if (ticks <= 1) {
                            World.sendGraphics(lightningCenterGfx, center);
                        }
                        corner = hydra.getInstance().getLocation(LIGHTNING_CHAMBER_CORNERS[ticks]);
                        delay = World.sendProjectile(center, corner, lightningAttackProj);
                        previousTick = ticks;
                    }
                    if (corner != null && previousTick + delay == ticks) {
                        lightnings++;
                        WorldTasksManager.schedule(new TickTask() {
                            final Location lightning = corner;
                            @Override
                            public void run() {
                                if (ticks <= 11) {
                                    final Location nextLocation = getNextFollowLocation(lightning, player.getLocation());
                                    lightning.setLocation(nextLocation);
                                    World.sendGraphics(lightningGfx, lightning);
                                    if (player.getLocation().matches(lightning)) {
                                        player.sendMessage(Colour.RED.wrap("The electricity temporarily paralyzes you!"));
                                        player.addMovementLock(new MovementLock(System.currentTimeMillis() + 1200,
                                                "You're stunned."));
                                        player.applyHit(new Hit(hydra, Utils.random(20), HitType.REGULAR));
                                        hydra.setPlayerHitByLighteningOnce();
                                        stop();
                                    }
                                }
                                ticks++;
                            }
                        }, 0, 0);
                        if (lightnings == 4) {
                            stop();
                            return;
                        }
                    }
                    ticks++;
                }
            }, lightningCenterProj.getTime(hydra, center), 0);
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
    }

}
