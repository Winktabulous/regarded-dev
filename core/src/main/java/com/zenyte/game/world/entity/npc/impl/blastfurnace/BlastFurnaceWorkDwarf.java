package com.zenyte.game.world.entity.npc.impl.blastfurnace;

import com.zenyte.game.content.minigame.blastfurnace.BlastFurnaceArea;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Emote;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 *
 * thumpy - 5454
 * stumpy - 7384
 * dumpy - 7386
 * pumpy - 7385
 * numpty - 6602
 */
public class BlastFurnaceWorkDwarf extends NPC implements Spawnable {
    private static final Location NUMPTY_LEFT = new Location(1943, 4960, 0);
    private static final Location NUMPTY_RIGHT = new Location(1947, 4960, 0);
    private static boolean NUMPTY_IDLE = true;
    private static boolean DUMPY_IDLE = true;

    public BlastFurnaceWorkDwarf(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        /** Numpty walking handler */
        if (getId() == 6602 && NUMPTY_IDLE) {
            final int roll = Utils.random(30);
            if (roll == 0) {
                NUMPTY_IDLE = false;
                if (getPosition().getX() == NUMPTY_RIGHT.getX()) addWalkSteps(NUMPTY_LEFT.getX(), NUMPTY_LEFT.getY());
                 else addWalkSteps(NUMPTY_RIGHT.getX(), NUMPTY_RIGHT.getY());
                faceDirection(Direction.NORTH);
                WorldTasksManager.schedule(() -> {
                    setAnimation(Emote.HEADBANG.getAnimation());
                    NUMPTY_IDLE = true;
                }, 3);
            }
        }
        if (getId() == 7386 || getId() == 7387) {
            if (DUMPY_IDLE) {
                DUMPY_IDLE = false;
                WorldTasksManager.schedule(new WorldTask() {
                    private int ticks;
                    @Override
                    public void run() {
                        if (ticks == 0) {
                            faceDirection(Direction.NORTH);
                            setAnimation(BlastFurnaceArea.TAKE_COKE);
                            setTransformation(7387);
                        }
                        if (ticks == 2) {
                            faceDirection(Direction.WEST);
                            setAnimation(BlastFurnaceArea.PLACE_COKE);
                        }
                        if (ticks == 3) {
                            setTransformation(7386);
                            setAnimation(BlastFurnaceArea.DUMPY_IDLE);
                        }
                        if (ticks == 4) {
                            DUMPY_IDLE = true;
                            stop();
                        }
                        ticks++;
                    }
                }, 0, 0);
            }
        }
    }

    /**
     * Dumpy coke handler
     */
    @Override
    public boolean validate(int id, String name) {
        return name.equalsIgnoreCase("dumpy") || name.equalsIgnoreCase("numpty");
    }
}
