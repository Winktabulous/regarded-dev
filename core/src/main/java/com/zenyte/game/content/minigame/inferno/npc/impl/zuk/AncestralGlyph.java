package com.zenyte.game.content.minigame.inferno.npc.impl.zuk;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 12/12/2019 | 22:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class AncestralGlyph extends InfernoNPC {
    public static final Location spawnLocation = new Location(2270, 5363, 0);
    private static final Location westernEndLocation = new Location(2257, 5361, 0);
    private static final Location easternEndLocation = new Location(2283, 5361, 0);
    private boolean movable;
    private final Location west;
    private final Location east;

    public AncestralGlyph(final Inferno inferno) {
        super(7707, inferno.getLocation(spawnLocation), inferno);
        west = inferno.getLocation(westernEndLocation);
        east = inferno.getLocation(easternEndLocation);
        setHitpoints(600);
    }

    @Override
    public void autoRetaliate(final Entity source) {
    }

    @Override
    public boolean checkAggressivity() {
        return false;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (movable) {
            movable = false;
            WorldTasksManager.schedule(new TickTask() {
                int movementLock;
                boolean movementLockSet;
                boolean first;
                @Override
                public void run() {
                    if (isDead() || isFinished()) {
                        stop();
                        return;
                    }
                    if (!hasWalkSteps()) {
                        if ((location.matches(west) || location.matches(east)) && !movementLockSet) {
                            movementLock = 4;
                            movementLockSet = true;
                        }
                        if (movementLock == 0) {
                            movementLockSet = false;
                            if (!first) {
                                final Location firstDir = Utils.random(1) == 0 ? west : east;
                                addWalkSteps(firstDir.getX(), firstDir.getY(), -1, false);
                                first = true;
                            } else {
                                if (location.matches(west)) {
                                    addWalkSteps(east.getX(), east.getY(), -1, false);
                                } else {
                                    addWalkSteps(west.getX(), west.getY(), -1, false);
                                }
                            }
                        }
                        if (movementLock > 0) {
                            movementLock--;
                        }
                    }
                }
            }, 0, 0);
        }
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }
}
