package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

import static com.zenyte.utils.TimeUnit.MILLISECONDS;

/**
 * @author Kris | 23/03/2019 00:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LeverPullStructure implements TeleportStructure {
    private static final Animation pull = new Animation(2140, 10);
    private static final Animation START_ANIM = new Animation(714);
    private static final Graphics START_GFX = new Graphics(111, 0, 70);

    @Override
    public Animation getStartAnimation() {
        return START_ANIM;
    }

    @Override
    public Graphics getStartGraphics() {
        return START_GFX;
    }

    @Override
    public void start(final Player player, final Teleport teleport) {
        player.lock();
        player.sendMessage("You pull the lever...");
        player.setAnimation(pull);
        teleport.onUsage(player);
        WorldTasksManager.schedule(() -> startTeleport(player, teleport), 1);
    }

    private void startTeleport(final Player player, final Teleport teleport) {
        final double experience = teleport.getExperience();
        final Animation startAnimation = Utils.getOrDefault(getStartAnimation(), Animation.STOP);
        final Graphics startGraphics = Utils.getOrDefault(getStartGraphics(), Graphics.RESET);
        final SoundEffect sound = getStartSound();

        if (sound != null) {
            World.sendSoundEffect(player, sound);
        }
        if (experience != 0) {
            player.getSkills().addXp(SkillConstants.MAGIC, experience);
        }
        player.setAnimation(startAnimation);
        player.setGraphics(startGraphics);
        int teleportDurationInTicks = (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(startAnimation)) - 1;
        player.blockIncomingHits(teleportDurationInTicks);
        WorldTasksManager.scheduleOrExecute(() -> end(player, teleport), teleportDurationInTicks);
    }
}
