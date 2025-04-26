package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

import static com.zenyte.utils.TimeUnit.MILLISECONDS;

/**
 * @author Tommeh | 23-3-2019 | 00:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ZenyteTabletStructure implements TeleportStructure {
    private static final int DELAY = AnimationUtil.getSynchronizedAnimationDelay(4069);
    private static final Animation BREAK_ANIM = new Animation(4069, DELAY);
    private static final SoundEffect SOUND = new SoundEffect(965, 5, DELAY);
    private static final Graphics SHRINK_GFX = new Graphics(2001);
    private static final Animation SHRINK_ANIM = new Animation(4071);

    @Override
    public Animation getStartAnimation() {
        return BREAK_ANIM;
    }

    @Override
    public void start(final Player player, final Teleport teleport) {
        final double experience = teleport.getExperience();
        final Animation startAnimation = Utils.getOrDefault(getStartAnimation(), Animation.STOP);
        final Graphics startGraphics = Utils.getOrDefault(getStartGraphics(), Graphics.RESET);
        World.sendSoundEffect(player, SOUND);
        player.lock();
        teleport.onUsage(player);
        if (experience != 0) {
            player.getSkills().addXp(SkillConstants.MAGIC, experience);
        }
        player.setAnimation(startAnimation);
        player.setGraphics(startGraphics);
        final int breakDuration = (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(BREAK_ANIM)) - 1;
        final int shrinkDuration = (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(SHRINK_ANIM));
        WorldTasksManager.scheduleOrExecute(() -> {
            player.setAnimation(SHRINK_ANIM);
            player.setGraphics(SHRINK_GFX);
        }, breakDuration);
        WorldTasksManager.scheduleOrExecute(() -> end(player, teleport), breakDuration + shrinkDuration);
    }

    @Override
    public void end(final Player player, final Teleport teleport) {
        if (isTeleportPrevented(player, teleport) || isAreaPrevented(player, teleport) || isRestricted(player, teleport)) {
            stop(player, teleport);
            return;
        }
        final Animation endAnimation = Utils.getOrDefault(getEndAnimation(), Animation.STOP);
        final Graphics endGraphics = Utils.getOrDefault(getEndGraphics(), Graphics.RESET);
        final Location location = getRandomizedLocation(player, teleport);
        verifyLocation(player, location);
        teleport.onArrival(player);
        player.setLocation(location);
        player.setAnimation(endAnimation);
        player.setGraphics(endGraphics);
        player.getInterfaceHandler().closeInterfaces();
        int teleportDurationInTicks = (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(endAnimation)) + EXTRA_DELAY - 1;
        player.blockIncomingHits(teleportDurationInTicks);
        WorldTasksManager.scheduleOrExecute(() -> stop(player, teleport), teleportDurationInTicks);
        updateDiaries(player, teleport);
    }
}
