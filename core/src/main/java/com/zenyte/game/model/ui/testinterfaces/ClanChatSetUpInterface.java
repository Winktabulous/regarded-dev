package com.zenyte.game.model.ui.testinterfaces;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.clans.ClanRank;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.InitPlugin;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.utils.TextUtils;
import mgi.utilities.StringFormatUtil;

import java.util.Objects;
import java.util.Optional;

import static com.zenyte.game.content.clans.ClanManager.refreshChannel;

/**
 * @author Tommeh | 27-10-2018 | 19:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClanChatSetUpInterface extends Interface implements InitPlugin {
    @Override
    protected void attach() {
        put(10, "Disable/set prefix");
        put(13, "Set enter rank");
        put(16, "Set talk rank");
        put(19, "Set kick rank");
    }

    @Override
    public void open(Player player) {
        ClanChannel currentChannel = ClanManager.getCurrentChannel(player);
        if (currentChannel != null && currentChannel.getOwner().equalsIgnoreCase("help")) {
            if(!currentChannel.getRankedMembers().containsKey(player.getUsername().toLowerCase())) {
                player.sendMessage("You are not a ranked member of this chat and do not have access.");
            } else {
                player.getInterfaceHandler().sendInterface(getInterface());
                player.getPacketDispatcher().initFriendsList();
                final PacketDispatcher dispatcher = player.getPacketDispatcher();
                dispatcher.sendComponentText(getInterface(), getComponent("Disable/set prefix"), currentChannel.isDisabled() ? "Chat disabled" : TextUtils.capitalize(currentChannel.getPrefix()));
                dispatcher.sendComponentText(getInterface(), getComponent("Set enter rank"), currentChannel.getEnterRank().getLabel());
                dispatcher.sendComponentText(getInterface(), getComponent("Set talk rank"), currentChannel.getTalkRank().getLabel());
                dispatcher.sendComponentText(getInterface(), getComponent("Set kick rank"), currentChannel.getKickRank().getLabel());
            }
            return;
        }
        player.getInterfaceHandler().sendInterface(getInterface());
        final String username = player.getUsername();
        final String usernameKey = StringFormatUtil.formatUsername(username);
        ClanChannel channel = ClanManager.CLAN_CHANNELS.get(usernameKey);
        if (channel == null) {
            ClanManager.CLAN_CHANNELS.put(usernameKey, channel = new ClanChannel(usernameKey));
        }
        player.getPacketDispatcher().initFriendsList();
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentText(getInterface(), getComponent("Disable/set prefix"), channel.isDisabled() ? "Chat disabled" : TextUtils.capitalize(channel.getPrefix()));
        dispatcher.sendComponentText(getInterface(), getComponent("Set enter rank"), channel.getEnterRank().getLabel());
        dispatcher.sendComponentText(getInterface(), getComponent("Set talk rank"), channel.getTalkRank().getLabel());
        dispatcher.sendComponentText(getInterface(), getComponent("Set kick rank"), channel.getKickRank().getLabel());
    }

    @Override
    protected void build() {
        bind("Disable/set prefix", (player, slotId, itemId, option) -> {
            final ClanChannel channel = ClanManager.getCurrentChannel(player);
            if (option == 1) {
                ClanManager.setPrefix(player, true);
                return;
            }
            if (channel.isDisabled()) {
                player.sendMessage("You've already disabled your clan channel.");
                return;
            }
            ClanManager.setPrefix(player, false);
            player.sendMessage("Your clan channel has now been disabled!");
        });
        bind("Set enter rank", (player, slotId, itemId, option) -> {
            final ClanChannel channel = ClanManager.getCurrentChannel(player);
            final ClanRank rank = ClanRank.VALUES[option - 1];
            if (rank == null) {
                return;
            }
            channel.setEnterRank(rank);
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Set enter rank"), rank.getLabel());
        });
        bind("Set talk rank", (player, slotId, itemId, option) -> {
            final ClanChannel channel = ClanManager.getCurrentChannel(player);
            final ClanRank rank = ClanRank.VALUES[option - 1];
            if (rank == null) {
                return;
            }
            channel.setTalkRank(rank);
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Set talk rank"), rank.getLabel());
        });
        bind("Set kick rank", (player, slotId, itemId, option) -> {
            final ClanChannel channel = ClanManager.getCurrentChannel(player);
            final ClanRank rank = ClanRank.VALUES[option - 1];
            if (rank == null) {
                return;
            }
            channel.setKickRank(rank);
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Set kick rank"), rank.getLabel());
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.CLAN_CHAT_SETUP;
    }

    @Override
    public void init() {
        ClanChannel channel = ClanManager.CLAN_CHANNELS.get("help");
        if (channel == null) {
            ClanManager.CLAN_CHANNELS.put("help", channel = new ClanChannel("help"));
        }
        channel.setDisabled(false);
        channel.setPrefix("help");
        refreshChannel(channel);
    }

    @Subscribe
    public static void onLogin(final LoginEvent event) {
        Player player = event.getPlayer();
        if(!(boolean) player.getAttributes().getOrDefault("manuallyLeftHelpChat", false)) {
            if(player.getSettings().getChannelOwner() == null || Objects.equals(player.getSettings().getChannelOwner(), ""))
                ClanManager.join(player, "help");
        }
        if(player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            ClanManager.getChannel("help").get().getRankedMembers().put(player.getUsername(), ClanRank.ADMINISTRATOR);
        } else if (player.getPrivilege().eligibleTo(PlayerPrivilege.MODERATOR)) {
            ClanManager.getChannel("help").get().getRankedMembers().put(player.getUsername(), ClanRank.GENERAL);
        } else if (player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT)) {
            ClanManager.getChannel("help").get().getRankedMembers().put(player.getUsername(), ClanRank.CAPTAIN);
        }
    }
}
