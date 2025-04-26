package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 2-2-2019 | 18:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Wallbeast extends NPC implements Spawnable {
    private static final Animation RETURN_ANIM = new Animation(1801);
    public static final Animation AWAKENING_ANIM = new Animation(1805);
    private boolean attackReady;

    public Wallbeast(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int getRespawnDelay() {
        return 17;
    }

    @Override
    public boolean isPotentialTarget(final Entity entity) {
        final int entityX = entity.getX();
        final int entityY = entity.getY();
        final int x = getX();
        final int y = getY();
        final int offsetX = x - entityX;
        final int offsetY = y - entityY;
        return (offsetX == -1 || offsetX == 1) || (offsetY == -1 || offsetY == 1);
    }

    @Override
    public boolean canAttack(final Player source) {
        return true;
    }

    @Override
    public boolean canMove(final int fromX, final int fromY, final int direction) {
        return false;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (id == 476) {
            if ((Utils.currentTimeMillis() - getAttackingDelay()) >= 5000) {
                setAnimation(RETURN_ANIM);
                setTransformation(475);
                cancelCombat();
                heal(105);
                setAttackingDelay(Utils.currentTimeMillis());
            }
        }
    }

    @Override
    public void drop(final Location location) {
        final Player player = getDropRecipient();
        if (player == null) {
            return;
        }
        if (player.isLocked()) {
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    if (player.isLocked()) {
                        return;
                    }
                    drop(player.getLocation());
                    stop();
                }
            }, 0, 0);
            return;
        }
        super.drop(player.getLocation());
    }

    @Override
    public NPC spawn() {
        attackReady = true;
        return super.spawn();
    }

    @Override
    public void setRespawnTask() {
        WorldTasksManager.schedule(this::spawn, getRespawnDelay());
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        attackReady = false;
        setTransformation(475);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 475 || id == 476;
    }

    public boolean isAttackReady() {
        return attackReady;
    }
}
