package com.zenyte.game.content.minigame.inferno.npc.impl.zuk;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Tommeh | 14/12/2019 | 19:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalMejJak extends InfernoNPC {
    private static final Animation spawnAnimation = new Animation(2864);
    private static final Animation interactionAnimation = new Animation(2868);
    private static final Projectile healingProjectile = new Projectile(660, 3, 12, 15, 20, 30, 0, 5);
    private static final Projectile attackProjectile = new Projectile(660, 3, 0, 15, 25, 30, 0, 5);
    private static final Graphics explosion = new Graphics(659);
    private static final SoundEffect flyingSound = new SoundEffect(155, 8, 0);
    private static final SoundEffect hittingSound = new SoundEffect(156, 5, 0);
    private final TzKalZuk zuk;
    private Rectangle aoeArea;

    public JalMejJak(final Location location, final Inferno inferno, final TzKalZuk zuk) {
        super(7708, location, inferno);
        this.zuk = zuk;
        setAoEArea();
        setAttackDistance(64);
    }

    @Override
    public boolean isProjectileClipped(final Position target, final boolean closeProximity) {
        return false;
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(interactionAnimation);
        if (target.equals(zuk)) {
            World.sendSoundEffect(this, flyingSound);
            World.scheduleProjectile(this, target, healingProjectile).schedule(() -> {
                zuk.setHealing(true);
                zuk.applyHit(new Hit(Utils.random(7, 15), HitType.HEALED));
                World.sendSoundEffect(target, hittingSound);
            });
        } else {
            final Location targetLocation = new Location(target.getLocation());
            final ArrayList<Location> tiles = new ArrayList<Location>(3);
            int count = 1000;
            while (--count > 0) {
                final Location tile = getRandomPoint();
                if (!tiles.contains(tile)) {
                    final Location location = targetLocation.getDistance(getLocation()) <= 6 && Utils.random(5) == 0 && !tiles.contains(targetLocation) ? targetLocation : tile;
                    tiles.add(location);
                }
                if (tiles.size() == 3) {
                    break;
                }
            }
            for (final Location tile : tiles) {
                World.sendSoundEffect(this, flyingSound);
                World.sendProjectile(this, tile, attackProjectile);
                World.sendGraphics(new Graphics(659, attackProjectile.getProjectileDuration(this.getLocation(), tile), 0), tile);
                WorldTasksManager.schedule(() -> {
                    World.sendSoundEffect(tile, hittingSound);
                    World.sendGraphics(explosion, tile);
                    if (target.getLocation().withinDistance(tile, 1)) {
                        delayHit(-1, target, new Hit(this, Utils.random(5, 10), HitType.REGULAR));
                    }
                }, attackProjectile.getTime(this, tile));
            }
        }
        return combatDefinitions.getAttackSpeed();
    }

    private Location getRandomPoint() {
        int count = 1000;
        Location location = new Location(0);
        do {
            if (--count <= 0) {
                throw new RuntimeException("Unable to find a valid point in polygon.");
            }
            location.setLocation((int) aoeArea.getMinX() + Utils.random((int) aoeArea.getWidth()), (int) aoeArea.getMinY() + Utils.random((int) aoeArea.getHeight()), getPlane());
        } while (!aoeArea.contains(location.getX(), location.getY()));
        return location;
    }

    private void setAoEArea() {
        final int x = getX();
        final int y = getY();
        final int originalX = inferno.getStaticLocation(getLocation()).getX();
        switch (originalX) {
        case 2262: 
            aoeArea = World.getRectangle(x - 5, x + 1, y - 8, y - 5);
            return;
        case 2266: 
            aoeArea = World.getRectangle(x - 3, x + 7, y - 8, y - 5);
            return;
        case 2276: 
            aoeArea = World.getRectangle(x - 7, x + 3, y - 8, y - 5);
            return;
        case 2280: 
            aoeArea = World.getRectangle(x - 1, x + 5, y - 8, y - 5);
        }
    }

    @Override
    public boolean checkProjectileClip(final Player player, boolean melee) {
        return false;
    }

    @Override
    public void onSpawn() {
        lock();
        setAnimation(spawnAnimation);
        WorldTasksManager.schedule(() -> {
            unlock();
            final Entity target = combat.getTarget();
            if (inferno != null && (target == null || target.equals(inferno.getPlayer()))) {
                setTarget(zuk);
            }
        }, 3);
    }
}
