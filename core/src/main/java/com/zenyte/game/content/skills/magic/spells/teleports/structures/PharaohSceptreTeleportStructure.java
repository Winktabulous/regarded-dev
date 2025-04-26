package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

public class PharaohSceptreTeleportStructure implements TeleportStructure {
    private static final Animation teleportAnim = new Animation(2881);
    private static final Graphics teleportGfx = new Graphics(715);

    @Override
    public Animation getStartAnimation() {
        return teleportAnim;
    }

    @Override
    public Graphics getStartGraphics() {
        return teleportGfx;
    }
}
