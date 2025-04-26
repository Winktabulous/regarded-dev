package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Kris | 28/04/2019 19:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CropCircleStructure implements TeleportStructure {

    private static final Animation animation = new Animation(6601);
    private static final Graphics graphics = new Graphics(1118);
    
    @Override
    public Graphics getStartGraphics() {
        return graphics;
    }
    
    @Override
    public Animation getStartAnimation() {
        return animation;
    }
    
    @Override
    public SoundEffect getStartSound() {
        return new SoundEffect(3727);
    }
}
