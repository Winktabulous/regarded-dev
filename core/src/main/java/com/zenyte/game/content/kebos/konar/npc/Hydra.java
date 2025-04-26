package com.zenyte.game.content.kebos.konar.npc;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.phases.PoisonPhase;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tommeh | 22/10/2019 | 18:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Hydra extends NPC implements CombatScript, Spawnable {
    private static final Projectile magicAttackProj = new Projectile(1662, 22, 12, 37, 10, 30, 0, 5);
    private static final Projectile rangedAttackProj = new Projectile(1663, 42, 32, 37, 2, 15, 0, 5);
    private static final Projectile poisonAttackProj = new Projectile(1644, 42, 0, 37, 2, 15, 0, 5);
    private static final Animation MAGIC_ATTACK_ANIM = new Animation(8263);
    private static final Animation rangedAttackAnim = new Animation(8261);
    private static final Animation poisonAttackAnim = new Animation(8262);
    private static final Graphics poisonSplashGfx = new Graphics(1645);
    private static final List<Byte[]> offsets = new ArrayList<>();

    static {
        for (byte x = -3; x <= 3; x++) {
            for (byte y = -3; y <= 3; y++) {
                offsets.add(new Byte[] {x, y});
            }
        }
    }

    private int attacks;
    private int ticks;
    private int processTicks;
    private boolean poisonAttack;
    private AttackType style;
    private final List<Location> poisonTiles;

    public Hydra(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        style = Utils.random(1) == 0 ? AttackType.MAGIC : AttackType.RANGED;
        poisonTiles = new ArrayList<>(3);
        attacks = 0;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (combat.underCombat()) {
            if (ticks > 0 && ticks % 50 == 0) {
                poisonAttack = true;
            }
            ticks++;
        }
        final Entity target = combat.getTarget();
        if (target instanceof Player && processTicks % 2 == 0) {
            final Player player = (Player) target;
            for (final Location tile : poisonTiles) {
                if (player.getLocation().matches(tile)) {
                    int damage = 4;
                    if (player.getVariables().getTime(TickVariable.POISON_IMMUNITY) > 0) {
                        damage = Utils.random(1, 3);
                        player.sendMessage("Your poison immunity reduces the damage taken from the acidic pool.");
                    }
                    target.applyHit(new Hit(this, damage, HitType.POISON));
                    if (Utils.random(3) == 0) {
                        player.getToxins().applyToxin(Toxins.ToxinType.POISON, 4, this);
                    }
                }
            }
        }
        processTicks++;
    }

    @Override
    public int attack(Entity target) {
        if (poisonAttack) {
            final Location base = new Location(target.getLocation());
            Collections.shuffle(offsets);
            poisonTiles.clear();
            poisonTiles.add(base);
            for (final Byte[] offset : offsets) {
                final Location tile = base.transform(offset[0], offset[1], 0);
                if (World.isFloorFree(tile, 1) && !poisonTiles.contains(tile)) {
                    poisonTiles.add(tile);
                }
                if (poisonTiles.size() == 3) {
                    break;
                }
            }
            final Location hydraCenter = getMiddleLocation();
            setAnimation(poisonAttackAnim);
            for (final Location tile : poisonTiles) {
                World.sendProjectile(this, tile, poisonAttackProj);
                final int direction = getRoundedDirection(DirectionUtil.getFaceDirection(tile.getX() - hydraCenter.getX(), tile.getY() - hydraCenter.getY()), 1024);
                final Direction directionConstant = CollectionUtils.findMatching(Direction.values, dir -> dir.getNPCDirection() == direction);
                assert directionConstant != null;
                final Graphics graphics = PoisonPhase.directionMap.get(directionConstant);
                assert graphics != null;
                WorldTasksManager.schedule(new TickTask() {
                    @Override
                    public void run() {
                        if (ticks == 0) {
                            World.sendGraphics(poisonSplashGfx, tile);
                        } else if (ticks == 1) {
                            poisonTiles.add(tile);
                            World.sendGraphics(graphics, tile);
                        } else if (ticks == 17) {
                            poisonTiles.remove(tile);
                            stop();
                        }
                        ticks++;
                    }
                }, poisonAttackProj.getTime(this, tile), 0);
            }
            poisonAttack = false;
        } else {
            if (attacks >= 3) {
                style = style.equals(AttackType.MAGIC) ? AttackType.RANGED : AttackType.MAGIC;
                attacks = 0;
            }
            if (style.equals(AttackType.MAGIC)) {
                setAnimation(MAGIC_ATTACK_ANIM);
                delayHit(World.sendProjectile(this, target, magicAttackProj), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), AttackType.MAGIC, target), HitType.MAGIC));
            } else {
                setAnimation(rangedAttackAnim);
                delayHit(World.sendProjectile(this, target, rangedAttackProj), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), AttackType.RANGED, target), HitType.RANGED));
            }
            attacks++;
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(KourendDiary.KILL_A_HYDRA);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 8609;
    }
}
