package com.zenyte.game.content.chambersofxeric.party;

import com.near_reality.game.model.ui.chat_channel.ChatChannelInterfaceType;
import com.near_reality.game.model.ui.chat_channel.ChatChannelPlayerExtKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Kris | 07/07/2019 03:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidPartyInterface extends Interface {
    /**
     * Refreshes the raid party tab which is visible when the player enters the chambers itself.
     *
     * @param player the player for whom to refresh the interface.
     * @param raid   the raid which we are refreshing.
     */
    public static final void refresh(@NotNull final Player player, @NotNull final Raid raid) {
        final boolean started = raid.getStage() > 0;
        final Set<Player> collection = started ? raid.getPlayers() : raid.getParty().getChannel().getMembers();
        player.getVarManager().sendBit(5423, player.getUsername().equalsIgnoreCase(raid.getParty().getPlayer()) ? 1 : 0);
        player.getVarManager().sendBit(5424, raid.getPlayers().size());
        player.getVarManager().sendBit(5425, raid.getStage());
        final MutableInt mutableInt = new MutableInt();
        player.getPacketDispatcher().sendClientScript(1547, collection.size());
        collection.forEach(p -> player.getPacketDispatcher().sendClientScript(1548, mutableInt.getAndIncrement(), (!started || raid.getPlayers().contains(p) ? "<col=ffffff>" : "<col=969696>") + p.getName() + "</col>|" + p.getSkills().getCombatLevel() + "|" + p.getSkills().getTotalLevel()));
        //If player has kick permissions, allow them to kick every player(by sending the max slot id as party size), else send value as 0.
        player.getPacketDispatcher().sendClientScript(1553, ClanManager.canKick(player, raid.getParty().getChannel()) ? raid.getPlayers().size() : 0);
    }

    @Override
    protected void attach() {
        put(2, "Refresh");
        put(14, "Start raid");
    }

    @Override
    public void open(final Player player) {
        ChatChannelPlayerExtKt.getChatChannelInterfaceType(player).sendTabInterface(player, getInterface());
        player.getRaid().ifPresent(raid -> raid.getParty().refreshTab(player));
    }

    @Override
    protected void build() {
        bind("Refresh", player -> refresh(player, player.getRaid().orElseThrow(RuntimeException::new)));
        bind("Start raid", player -> player.getRaid().ifPresent(raid -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("No-one may join the party after the raid begins.", new DialogueOption("Begin the raid.", raid::load), new DialogueOption("Don't begin the raid yet."));
            }
        })));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAID_PARTY_TAB;
    }
}
