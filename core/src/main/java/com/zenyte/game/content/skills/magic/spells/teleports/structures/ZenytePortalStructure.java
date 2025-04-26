package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Tommeh | 23-3-2019 | 00:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ZenytePortalStructure implements TeleportStructure {

    private static final Animation ANIMATION = new Animation(3865);
    private static final Graphics GRAPHICS = new Graphics(2000);

    @Override
    public Animation getStartAnimation() {
        return ANIMATION;
    }

    @Override
    public Graphics getStartGraphics() {
        return GRAPHICS;
    }
}
