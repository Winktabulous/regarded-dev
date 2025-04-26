package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SpikeyChainWarning;

import java.util.HashMap;
import java.util.Map;

public class SlayerTowerSpikeyChain implements Shortcut {
    public static final Location NOSEPEG_BOTTOM = new Location(3422, 3550, 0);
    private static final Map<Integer, Integer> CHAINS = new HashMap<Integer, Integer>();
    private static final int NOSEPEG = 4168;
    private static final Animation CLIMB = new Animation(828);
    private static final Location NOSEPEG_TOP = new Location(3422, 3550, 1);
    private static final Location NORTH_TOP = new Location(3447, 3576, 2);
    private static final Location NORTH_BOTTOM = new Location(3447, 3576, 1);

    static {
        CHAINS.put(NOSEPEG_TOP.getPositionHash(), 0);
        CHAINS.put(NOSEPEG_BOTTOM.getPositionHash(), 1);
        CHAINS.put(NORTH_TOP.getPositionHash(), 1);
        CHAINS.put(NORTH_BOTTOM.getPositionHash(), 2);
    }

    public static void climb(final Player player, final int hash) {
        final Integer chain = CHAINS.get(hash);
        if (chain == null) {
            return;
        }
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(SlayerTowerSpikeyChain.CLIMB);
                } else if (ticks == 1) {
                    if (hash == NORTH_BOTTOM.getPositionHash()) {
                        player.getAchievementDiaries().update(MorytaniaDiary.CLIMB_SPIKED_CHAIN_IN_SLAYER_TOWER);
                    }
                    player.setLocation(new Location(player.getX(), player.getY(), chain));
                    player.getSkills().addXp(SkillConstants.AGILITY, 3);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        if (object.getPositionHash() == NOSEPEG_BOTTOM.getPositionHash() && !SlayerEquipment.NOSE_PEG.isWielding(player)) {
            player.getDialogueManager().start(new SpikeyChainWarning(player));
        } else {
            climb(player, object.getPositionHash());
        }
    }

    @Override
    public int getLevel(final WorldObject object) {
        return object.getX() == 3447 && object.getY() == 3576 ? 71 : 61;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {16537, 16538};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
