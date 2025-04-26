package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Kris | 26/04/2019 17:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StonyBasaltStructure implements TeleportStructure {

    private static final Animation animation = new Animation(8172);
    private static final Graphics graphics = new Graphics(1617);

    @Override
    public Graphics getStartGraphics() {
        return graphics;
    }

    @Override
    public Animation getStartAnimation() {
        return animation;
    }
}
