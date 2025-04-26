package com.zenyte.game.content.boss.skotizo.npc;

import com.zenyte.game.content.boss.skotizo.instance.SkotizoInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

/**
 * @author Tommeh | 06/03/2020 | 18:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class AwakenedAltar extends NPC {
    private static final Animation deathAnimation = new Animation(1475);
    private static final Animation awakenAnimation = new Animation(1477);
    private final transient SkotizoInstance instance;
    private WorldObject altar;
    private boolean awakened;
    private long altarAwakeningDelay;

    public AwakenedAltar(final int index, final Location location, final SkotizoInstance instance) {
        super(NpcId.AWAKENED_ALTAR, location, Direction.SOUTH, 0);
        this.spawned = true;
        this.instance = instance;
        final int rotation = index == 0 ? 1 : index == 1 ? 3 : index == 2 ? 0 : 2;
        World.spawnObject(altar = new WorldObject(28924, 10, rotation, location));
    }

    public void resetAwakeningDelay() {
        this.altarAwakeningDelay = Utils.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Utils.random(this.altarAwakeningDelay == 0 ? 15 : 30, 60));
    }

    @Override
    public void setRespawnTask() {
    }

    @Override
    protected boolean canMove(int fromX, int fromY, int direction) {
        return false;
    }

    @Override
    public boolean checkProjectileClip(final Player player, boolean melee) {
        return false;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        if (hit.getHitType() != HitType.MELEE) {
            return;
        }
        final Object weapon = hit.getWeapon();
        if (weapon instanceof Item) {
            if (((Item) weapon).getId() == ItemId.ARCLIGHT) {
                hit.setDamage(100);
            }
        }
    }

    @Override
    public float getXpModifier(final Hit hit) {
        return 0;
    }

    public void awaken() {
        final Skotizo skotizo = instance.getSkotizo();
        setAwakened(true);
        instance.setAltarSpawnCounter(instance.getAltarSpawnCounter() + 1);
        if (instance.getAltarSpawnCounter() >= instance.getAltarSpawnCap()) {
            instance.getSkotizo().setDisableAltarRespawning(true);
        }
        World.sendObjectAnimation(altar, awakenAnimation);
        WorldTasksManager.schedule(() -> {
            if (skotizo.isDead() || skotizo.isFinished()) {
                return;
            }
            World.spawnObject(altar = new WorldObject(28923, 10, altar.getRotation(), location));
            spawn();
            instance.refreshOverlay();
        }, 5);
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
        final AwakenedAltar lastSlainAltar = instance.getLastSlainAltar();
        instance.setLastSlainAltar(this);
        instance.getSkotizo().setDemonSpawnDelay(Utils.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
        World.sendObjectAnimation(altar, deathAnimation);
        if (!instance.getSkotizo().isDisableAltarRespawning()) {
            if (lastSlainAltar != null) {
                //If last one is still dead and last altar is on the same vertical or horizontal axis as this one, disable it.
                if (!lastSlainAltar.isAwakened() && (lastSlainAltar.altar.getRotation() & 1) == (altar.getRotation() & 1)) {
                    instance.getSkotizo().setDisableAltarRespawning(true);
                }
            }
        }
        resetAwakeningDelay();
        WorldTasksManager.schedule(() -> {
            World.spawnObject(altar = new WorldObject(28924, 10, altar.getRotation(), location));
            setAwakened(false);
            instance.refreshOverlay();
        }, 2);
    }

    public boolean isAwakened() {
        return awakened;
    }

    public void setAwakened(boolean awakened) {
        this.awakened = awakened;
    }

    public long getAltarAwakeningDelay() {
        return altarAwakeningDelay;
    }
}
