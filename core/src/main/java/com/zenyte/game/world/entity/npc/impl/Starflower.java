package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;

public class Starflower extends NPC implements Spawnable {

    public Starflower(int id, Location tile, Direction ignored, int radius) {
        super(id, tile, Utils.getRandomElement(Direction.cardinalDirections), radius);
        resetTimer();
    }

    private int ticks;

    private int random;

    @Override
    public int getRespawnDelay() {
        return 5;
    }

    public void resetTimer() {
        random = Utils.random(200, 500);
        ticks = 0;
    }

    @Override
    public void sendDeath() {
        setTransformation(1856);
        setAnimation(new Animation(4313));
        resetTimer();
    }

    @Override
    public void processNPC() {
        if (getId() == NpcId.STARFLOWER_1857) {
            return;
        }
        if (++ticks >= random) {
            setTransformation(1857);
            resetTimer();
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 1856 || id == 1857;
    }
}
