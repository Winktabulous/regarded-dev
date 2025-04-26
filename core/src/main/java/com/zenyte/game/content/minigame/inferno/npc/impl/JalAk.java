package com.zenyte.game.content.minigame.inferno.npc.impl;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.model.WaveNPC;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Tommeh | 29/11/2019 | 19:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalAk extends InfernoNPC {
    private static final Animation magicAnimation = new Animation(7581);
    private static final Animation meleeAnimation = new Animation(7582);
    private static final Animation rangedAnimation = new Animation(7583);
    private static final SoundEffect meleeSound = new SoundEffect(595);
    private static final Projectile rangedProjectile = new Projectile(1378, 42, 38, 0, 17, 30, 0, 4);
    private static final Projectile magicProjectile = new Projectile(1380, 42, 38, 15, 17, 30, 0, 4);
    public static final Map<Direction, byte[][]> bloblingOffsets = ImmutableMap.<Direction, byte[][]>builder().put(Direction.NORTH, new byte[][] {new byte[] {0, 1}, new byte[] {-1, 0}, new byte[] {1, 0}}).put(Direction.NORTH_EAST, new byte[][] {new byte[] {1, -1}, new byte[] {-1, 1}, new byte[] {1, 1}}).put(Direction.EAST, new byte[][] {new byte[] {0, 0}, new byte[] {1, 1}, new byte[] {1, -1}}).put(Direction.NORTH_WEST, new byte[][] {new byte[] {-1, -1}, new byte[] {-1, 1}, new byte[] {1, 1}}).put(Direction.SOUTH, new byte[][] {new byte[] {0, -1}, new byte[] {-1, 0}, new byte[] {1, 0}}).put(Direction.SOUTH_WEST, new byte[][] {new byte[] {-1, -1}, new byte[] {-1, 1}, new byte[] {1, 1}}).put(Direction.WEST, new byte[][] {new byte[] {-1, 0}, new byte[] {0, 1}, new byte[] {0, -1}}).put(Direction.SOUTH_EAST, new byte[][] {new byte[] {-1, -1}, new byte[] {1, -1}, new byte[] {1, 1}}).build();
    private static final WaveNPC[] bloblings = {WaveNPC.JAL_AKREK_MEJ, WaveNPC.JAL_AKREK_XIL, WaveNPC.JAL_AKREK_KET};

    public JalAk(final Location location, final Inferno inferno) {
        super(7693, location, inferno);
        setAttackDistance(14);
    }

    @Override
    public boolean isRevivable() {
        return true;
    }

    @Override
    public int attack(final Player player) {
        switchStyle(player);
        WorldTasksManager.schedule(() -> {
            if (player.isDead() || player.isFinished() || isDead() || isFinished() || !inferno.inside(player.getLocation())) {
                return;
            }
            final AttackType style = combatDefinitions.getAttackStyle();
            if (style.equals(AttackType.RANGED)) {
                inferno.playSound(new SoundEffect(3528, 0, rangedProjectile.getProjectileDuration(this.getLocation(), player)));
                setAnimation(rangedAnimation);
                delayHit(World.sendProjectile(this, player, rangedProjectile), player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, player), HitType.RANGED));
            } else if (style.equals(AttackType.MAGIC)) {
                inferno.playSound(new SoundEffect(3403, 0, rangedProjectile.getProjectileDuration(this.getLocation(), player)));
                setAnimation(magicAnimation);
                delayHit(World.sendProjectile(this, player, magicProjectile), player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), MAGIC, player), HitType.MAGIC));
            } else {
                if (isWithinMeleeDistance(JalAk.this, player)) {
                    inferno.playSound(meleeSound);
                    setAnimation(meleeAnimation);
                    delayHit(0, player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), CRUSH, player), HitType.MELEE));
                } else {
                    combatDefinitions.setAttackStyle(AttackType.MAGIC);//Switch it to long-range style so the next tick, it can properly pick its next attack style from a-far.
                }
            }
        }, 2);
        return combatDefinitions.getAttackSpeed();
    }

    private void switchStyle(@NotNull final Player player) {
        final PrayerManager prayer = player.getPrayerManager();
        if (prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
            combatDefinitions.setAttackStyle(AttackType.RANGED);
        } else if (prayer.isActive(Prayer.PROTECT_FROM_MISSILES)) {
            combatDefinitions.setAttackStyle(AttackType.MAGIC);
        } else {
            if (isWithinMeleeDistance(JalAk.this, player)) {
                if (Utils.random(1) == 0) {
                    combatDefinitions.setAttackStyle(AttackType.CRUSH);
                } else {
                    combatDefinitions.setAttackStyle(Utils.random(1) == 0 ? AttackType.RANGED : AttackType.MAGIC);
                }
            } else {
                combatDefinitions.setAttackStyle(Utils.random(1) == 0 ? AttackType.RANGED : AttackType.MAGIC);
            }
        }
    }

    @Override
    public void onFinish(Entity source) {
        source = inferno.getPlayer();
        final Location middle = getMiddleLocation();
        final int direction = getRoundedDirection(DirectionUtil.getFaceDirection(source.getX() - middle.getX(), source.getY() - middle.getY()), 1024);
        final Direction directionConstant = CollectionUtils.findMatching(Direction.values, dir -> dir.getNPCDirection() == direction);
        assert directionConstant != null;
        final byte[][] offsets = bloblingOffsets.get(directionConstant);
        try {
            for (int index = 0; index < bloblings.length; index++) {
                final byte[] offset = offsets[index];
                final InfernoNPC blobling = bloblings[index].getClazz().getDeclaredConstructor(Location.class, Inferno.class).newInstance(middle.transform(offset[0], offset[1], 0), inferno);
                blobling.freeze(1);
                blobling.spawn();
                blobling.setTarget(source);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        super.onFinish(source);
    }
}
