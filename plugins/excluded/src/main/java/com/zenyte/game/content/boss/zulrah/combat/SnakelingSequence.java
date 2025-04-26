package com.zenyte.game.content.boss.zulrah.combat;

import com.zenyte.game.content.boss.zulrah.Sequence;
import com.zenyte.game.content.boss.zulrah.SnakelingNPC;
import com.zenyte.game.content.boss.zulrah.ZulrahInstance;
import com.zenyte.game.content.boss.zulrah.ZulrahNPC;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. march 2018 : 20:11.52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SnakelingSequence implements Sequence {

    private static final Animation ANIM = new Animation(5069);
    private static final Projectile SNAKELING_PROJ = new Projectile(1047, 65, 10, 40, 15, 90, 0, 5);
    private final Location tile;
    private static final SoundEffect SOUND_EFFECT = new SoundEffect(788, 15);

    public SnakelingSequence(final Location tile) {
        this.tile = tile;
    }

    @Override
    public void attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
        final Location tile = instance.getLocation(this.tile);
        zulrah.lock(3);
        zulrah.setFaceEntity(null);
        zulrah.setFaceLocation(tile);
        zulrah.setAnimation(ANIM);
        zulrah.getSnakelingsDeathTicks().clear();
        World.sendSoundEffect(zulrah, SOUND_EFFECT);
        World.sendProjectile(zulrah, tile, SNAKELING_PROJ);
        WorldTasksManager.schedule(() -> {
            if (!zulrah.isDead() && !zulrah.isFinished())
                zulrah.getSnakelings().add((SnakelingNPC) new SnakelingNPC(zulrah, target, tile).spawn());
        }, SNAKELING_PROJ.getTime(zulrah, tile));
    }

}
