package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameConstants;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 24/11/2018 22:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Hans extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new com.zenyte.game.world.entity.player.dialogue.impl.Hans(player, npc));
        });
        bind("Age", (player, npc) -> {
            final int seconds = (int) (player.getVariables().getPlayTime() * 0.6);
            final int days = seconds / 86400;
            final int hours = (seconds / 3600) - (days * 24);
            final int minutes = (seconds / 60) - (days * 1440) - (hours * 60);
            player.getAchievementDiaries().update(LumbridgeDiary.LEARN_YOUR_AGE);
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You've spent " + days + (days > 1 || days == 0 ? " days" : " day") + ", " + hours + (hours > 1 || hours == 0 ? " hours" : " hour") + ", " + minutes + (minutes > 1 || minutes == 0 ? " minutes" : " minute") + " in the world since you arrived in " + GameConstants.SERVER_NAME + ", " + StringFormatUtil.format(player.getPlayerInformation().getDaysSinceRegistry()) + " days ago."));
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.HANS };
    }
}
