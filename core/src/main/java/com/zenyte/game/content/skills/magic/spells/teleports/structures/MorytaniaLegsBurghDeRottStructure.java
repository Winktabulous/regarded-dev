package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Tommeh | 04/10/2019 | 15:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class MorytaniaLegsBurghDeRottStructure  implements TeleportStructure {

    private static final Graphics graphics = new Graphics(1236, 0, 92);
    private static final Animation animation = new Animation(3874);

    @Override
    public Animation getStartAnimation() {
        return animation;
    }

    @Override
    public Graphics getStartGraphics() {
        return graphics;
    }
}