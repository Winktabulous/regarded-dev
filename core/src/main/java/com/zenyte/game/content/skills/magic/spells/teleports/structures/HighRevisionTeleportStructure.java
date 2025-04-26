package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 14/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HighRevisionTeleportStructure implements TeleportStructure {

    private static final Animation startAnimation = new Animation(15243);
    private static final Graphics startGraphics = new Graphics(2515);
    private static final SoundEffect startSound = new SoundEffect(7604);

    private static final Animation endAnimation = new Animation(15245);
    private static final Graphics endGraphics = new Graphics(2516);
    private static final SoundEffect endSound = new SoundEffect(7605);

    @Override
    public Animation getStartAnimation() {
        return startAnimation;
    }

    @Override
    public Graphics getStartGraphics() {
        return startGraphics;
    }

    @Override
    public SoundEffect getStartSound() {
        return startSound;
    }

    @Override
    public Animation getEndAnimation() {
        return endAnimation;
    }

    @Override
    public Graphics getEndGraphics() {
        return endGraphics;
    }

    @Override
    public SoundEffect getEndSound() {
        return endSound;
    }

    @Override
    public void verifyLocation(final Player player, final Location location) {

    }
}
