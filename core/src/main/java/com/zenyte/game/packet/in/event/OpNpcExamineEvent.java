package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.model.ui.testinterfaces.DropViewerInterface;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.parser.impl.NPCExamineLoader;
import com.zenyte.game.util.Examine;
import com.zenyte.game.world.entity.player.GameSetting;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpNpcExamineEvent implements ClientProtEvent {
    private final int npcId;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Id: " + npcId);
    }

    @Override
    public void handle(Player player) {
        if (player.getNumericAttribute(GameSetting.EXAMINE_NPCS_DROP_VIEWER.toString()).intValue() == 1) {
            if (!player.isUnderCombat(6)) {
                DropViewerInterface.open(player, npcId);
            } else {
                player.sendFilteredMessage("You need to be out of combat to use the drop viewer.");
            }
        }
        final Examine examine = NPCExamineLoader.get(npcId);
        if (examine == null) {
            return;
        }
        player.sendMessage(examine.getExamine(), MessageType.EXAMINE_NPC);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public OpNpcExamineEvent(int npcId) {
        this.npcId = npcId;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
