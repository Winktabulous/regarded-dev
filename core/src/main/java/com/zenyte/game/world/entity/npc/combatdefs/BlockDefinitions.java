package com.zenyte.game.world.entity.npc.combatdefs;

import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;

/**
 * @author Kris | 18/11/2018 02:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BlockDefinitions {
    private Animation animation = Animation.STOP;
    private SoundEffect sound;

    public static BlockDefinitions construct(final BlockDefinitions clone) {
        final BlockDefinitions defs = new BlockDefinitions();
        if (clone == null) return defs;
        defs.animation = clone.animation;
        if (clone.sound != null) {
            defs.sound = new SoundEffect(clone.sound.getId(), clone.sound.getRadius(), clone.sound.getDelay() + 30);
        }
        return defs;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public SoundEffect getSound() {
        return sound;
    }

    public void setSound(SoundEffect sound) {
        this.sound = sound;
    }
}
