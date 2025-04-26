package com.zenyte.game.content.clans;

import com.google.common.eventbus.Subscribe;
import com.zenyte.cores.CoresManager;
import com.zenyte.cores.ScheduledExternalizable;
import com.zenyte.game.packet.out.ClanChannelFull;
import com.zenyte.game.packet.out.ClanChannelMember;
import com.zenyte.game.packet.out.MessageFriendChannel;
import com.zenyte.game.packet.out.ResetFriendChannel;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.PluginManager;
import com.zenyte.plugins.events.ClanLeaveEvent;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.utils.TextUtils;
import com.zenyte.utils.TimeUnit;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static mgi.utilities.StringFormatUtil.formatUsername;

/**
 * A class that handles all I/O elements, as well as managing the clan itself.
 *
 * @author Kris | 22. march 2018 : 23:51.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ClanManager implements ScheduledExternalizable {
    private static final Logger log = LoggerFactory.getLogger(ClanManager.class);

    /**
     * The directory to the file containing clans.
     */
    public static final String CLANS_FILE_DIRECTORY = "data/clans.json";
    /**
     * A map containing the username of the owner of the channel, and the channel object.
     */
    public static final Map<String, ClanChannel> CLAN_CHANNELS = new HashMap<>();

    public static Optional<ClanChannel> getChannel(@NotNull final String name) {
        return Optional.ofNullable(CLAN_CHANNELS.get(formatUsername(name)));
    }

    public static ClanChannel getChannel(Player player) {
        return getChannel(player.getPlayerInformation().getUsername()).orElse(null);
    }


    public static ClanChannel getCurrentChannel(Player player) {
        return player.getSettings().getChannel();
    }

    @Subscribe
    public static void onLogin(@NotNull final LoginEvent event) {
        final Player player = event.getPlayer();
        final String lastClan = player.getSettings().getChannelOwner();
        if (lastClan != null) {
            join(player, lastClan);
        } else {
            join(player, "help");
        }
    }

    /**
     * Attempts to enqueue the player to the requested clan channel.
     *
     * @param player the player trying to join a channel.
     * @param rq     the username of the owner of the channel.
     */
    public static void join(@NotNull final Player player, @NotNull final String rq) {
        player.sendMessage("Attempting to join channel...");
        //Schedule on a task to properly introduce the "delay" that OSRS gets.
        WorldTasksManager.schedule(() -> {
            final Optional<ClanChannel> optional = ClanManager.getChannel(rq);
            if (optional.isEmpty()) {
                player.sendMessage("That player doesn't currently own a clan channel.");
                return;
            }
            final ClanChannel channel = optional.get();
            if (channel.isDisabled()) {
                player.sendMessage("The owner of that clan has disabled their clan channel.");
                return;
            }
            final String formattedUsername = formatUsername(player.getUsername());
            final long time = channel.getBannedMembers().getLong(formattedUsername);
            if (!player.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR) && (time > Utils.currentTimeMillis()
                    || channel.getPermBannedMembers().contains(formattedUsername))) {
                player.sendMessage("You are temporarily banned from this clan channel.");
                return;
            }
            final ClanRank rank = getRank(player, channel);
            if (channel.getMembers().size() >= 95 && !rq.equals("help")) {
                final Player playerToKick = findMember(channel, rank);
                if (playerToKick == null) {
                    player.sendMessage("That clan channel is currently full.");
                    return;
                }
                kick(player, false, playerToKick, false);
            }
            channel.loadOwner(owner -> {
                if (!canEnter(player, owner, channel)) {
                    player.sendMessage("You are not high enough rank to join this clan channel.");
                    return;
                }
                if(!player.getPrivilege().inherits(PlayerPrivilege.SUPPORT) && owner.getSocialManager().getIgnores().contains(player.getUsername())) {
                    player.sendMessage("You cannot join this channel as the owner has ignored you.");
                    return;
                }
                channel.getMembers().add(player);
                refreshPartial(channel, player, true, true);
                player.sendMessage("Now talking in clan channel " + StringFormatUtil.formatString(channel.getPrefix()));
                player.sendMessage("To talk, start each line of chat with the / symbol.");
                player.getSettings().setChannel(channel);
                player.getSettings().setChannelOwner(channel.getOwner());
            });
        });
    }

    public static void permban(final Player player, final String username) {
        final Optional<ClanChannel> channel = ClanManager.getChannel(player.getUsername());
        if (channel.isEmpty()) {
            player.sendMessage("You do not own a clan channel.");
            return;
        }
        final String formattedUsername = formatUsername(username);
        CoresManager.getLoginManager().load(true, username, target -> {
            if (target.isEmpty()) {
                player.sendMessage("Account by the name of " + username + " does not exist.");
                return;
            }
            final ClanChannel ch = channel.get();
            final boolean status = ch.getPermBannedMembers().add(formattedUsername);
            if (status) {
                player.sendMessage("Permanently banned user " + username + " from your clan channel.");
                final Player beingKicked = target.get();
                if (beingKicked.getSettings().getChannel() == ch) {
                    PluginManager.post(new ClanLeaveEvent(beingKicked));
                    beingKicked.getSettings().setChannel(null);
                    beingKicked.getSettings().setChannelOwner(null);
                    beingKicked.send(new ResetFriendChannel());
                    beingKicked.sendMessage("You have been kicked from the channel.");
                }
                refreshPartial(channel.get(), beingKicked, false, true);
            } else {
                player.sendMessage("User " + username + " is already banned from your channel.");
            }
        });
    }

    public static void permunban(final Player player, final String username) {
        final Optional<ClanChannel> channel = ClanManager.getChannel(player.getUsername());
        if (channel.isEmpty()) {
            player.sendMessage("You do not own a clan channel.");
            return;
        }
        final String formattedUsername = formatUsername(username);
        CoresManager.getLoginManager().load(true, username, target -> {
            if (target.isEmpty()) {
                player.sendMessage("Account by the name of " + username + " does not exist.");
                return;
            }
            final boolean status = channel.get().getPermBannedMembers().remove(formattedUsername);
            if (status) {
                player.sendMessage("Unbanned user " + username + " from your clan channel.");
            } else {
                player.sendMessage("User " + username + " is not permanently banned from your channel.");
            }
        });
    }

    private static Player findMember(final ClanChannel channel, final ClanRank clanRank) {
        for (final Player member : channel.getMembers()) {
            if (member.isNulled()) {
                continue;
            }
            final ClanRank rank = getRank(member, channel);
            if (rank.getKickId() < clanRank.getKickId()) {
                return member;
            }
        }
        return null;
    }

    /**
     * Attempts to remove the player from the channel. If the channel is null, returns. If the controller doesn't
     * allow to remove - which is
     * specifically in instances that require a clan - prevents from removing. If the channel doesn't contain the
     * member, returns the code
     * and doesn't do anything.
     *
     * @param player           the player who's leaving the channel.
     * @param resetLastChannel whether to reset the last channel or not (used on login - auto join)
     */
    public static void leave(@NotNull final Player player, final boolean resetLastChannel) {
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null) {
            return;
        }
        if (!player.getControllerManager().canLeaveClanChannel()) {
            return;
        }
        final Set<Player> members = channel.getMembers();
        if (!members.remove(player)) {
            return;
        }
        if (members.isEmpty()) {
            if (!channel.getBannedMembers().isEmpty()) {
                channel.getBannedMembers().clear();
            }
        }
        PluginManager.post(new ClanLeaveEvent(player));
        if (resetLastChannel) {
            player.getSettings().setChannel(null);
            player.getSettings().setChannelOwner(null);
        }
        player.send(new ResetFriendChannel());
        player.sendMessage("You have left the channel.");
        refreshPartial(channel, player, false, true);
    }

    /**
     * Attempts to kick the player from the current channel. If the player hasn't got the rights to kick, they'll be
     * informed so.
     *
     * @param player      the player attempting to kick.
     * @param inform      whether to inform the kicked player that they have been kicked.
     * @param beingKicked
     * @param force
     */
    public static void kick(@NotNull final Player player, final boolean inform, @NotNull final Player beingKicked,
                            boolean force) {
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null || player.isNulled() || beingKicked.isNulled()) {
            return;
        }
        if(player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT))
            force = true;
        if (!force) {
            if (formatUsername(channel.getOwner()).equals(formatUsername(beingKicked.getUsername()))) {
                player.sendMessage("You can't kick the owner from the clan channel.");
                return;
            }
            if (!canKick(player, channel)) {
                player.sendMessage("You aren't high enough rank to kick from this channel.");
                return;
            }
            final ClanRank rankA = getRank(player, channel);
            final ClanRank rankB = getRank(beingKicked, channel);
            if (rankA.getKickId() <= rankB.getKickId()) {
                player.sendMessage("You can only kick members with a lower tier rank than you.");
                return;
            }
        }
        if (!channel.getMembers().remove(beingKicked)) {
            return;
        }
        channel.getBannedMembers().put(formatUsername(beingKicked.getUsername()),
                Utils.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
        beingKicked.getVariables().setLastClanKick(System.currentTimeMillis());
        player.sendMessage("Your request to kick/ban this user was successful.");
        PluginManager.post(new ClanLeaveEvent(beingKicked));
        beingKicked.getSettings().setChannel(null);
        beingKicked.getSettings().setChannelOwner(null);
        beingKicked.send(new ResetFriendChannel());
        if (inform) {
            beingKicked.sendMessage("You have been kicked from the channel.");
        }
        refreshPartial(channel, beingKicked, false, true);
    }

    /**
     * Sends a message to all the members of the channel.
     *
     * @param player  the player sending the message
     * @param message the message being sent.
     */
    public static void message(@NotNull final Player player,
                               @NotNull final String message) {
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null) {
            player.sendMessage("You need to be in a clan channel to send a channel message.");
            return;
        }
        channel.loadOwner(owner -> {
            int talkOutcome = canTalk(player, owner, channel);
            if (talkOutcome != 1) {
                if(talkOutcome == 0)
                    player.sendMessage("You aren't high enough rank to talk in this channel.");
                return;
            }
            final int icon = player.getPrivilege().crown().getId();
            final MessageFriendChannel packet = new MessageFriendChannel(
                    player, channel.getPrefix(), icon, message);
            player.getAttributes().put("cc_last_talk", System.currentTimeMillis());
            for (final Player member : channel.getMembers()) {
                if (member == null || member.isNulled()) {
                    continue;
                }

                member.send(packet);
            }
        });
    }

    /**
     * Gets the rank of the requested player in the requested channel. If it fails to obtain a rank, the rank
     * {@link ClanRank#ANYONE} is
     * returned.
     *
     * @param player  the player whose rank to obtain.
     * @param channel the channel from which to obtain their rank.
     * @return the rank of the player, or {@link ClanRank#ANYONE} is there's none.
     */
    public static ClanRank getRank(@NotNull final Player player, @NotNull final ClanChannel channel) {
        if (isOwner(player, channel)) {
            return ClanRank.OWNER;
        }
        final ClanRank rank = channel.getRankedMembers().get(formatUsername(player.getPlayerInformation().getUsername()));
        if (rank == null) {
            return ClanRank.ANYONE;
        }
        return rank;
    }

    /**
     * Gets the rank of the requested player in the players' current channel. If it fails to obtain a rank, the rank
     * {@link ClanRank#ANYONE}
     * is returned.
     *
     * @param player   the player whose channel to check.
     * @param username the username of the player whose rank to check.
     * @return the rank of the player, or {@link ClanRank#ANYONE} is there's none.
     */
    public static ClanRank getRank(@NotNull final Player player, @NotNull final String username) {
        final Optional<ClanChannel> channel = ClanManager.getChannel(player.getUsername());
        if (channel.isEmpty()) {
            return ClanRank.ANYONE;
        }
        final ClanRank rank = channel.get().getRankedMembers().get(formatUsername(username));
        if (rank == null) {
            return ClanRank.ANYONE;
        }
        return rank;
    }

    /**
     * Refreshes the channel settings and members list.
     *
     * @param channel the channel to refresh.
     */
    public static void refreshChannel(@NotNull final ClanChannel channel) {
        channel.loadOwner(owner -> {
            final Set<Player> members = channel.getMembers();
            members.removeIf(member -> member.isNulled() || member.isFinished());

            members.forEach(member -> {
                try (final ClanChannelBuilder builder = new ClanChannelFullBuilder(channel, owner)) {
                    builder.build();
                    try {
                        member.send(new ClanChannelFull(builder));
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            });
        });
    }

    public static void refreshPartial(@NotNull final ClanChannel channel,
                                      final Player player,
                                      final boolean added,
                                      final boolean split) {
        channel.loadOwner(owner -> {
            final Set<Player> members = channel.getMembers();
            members.removeIf(member -> member.isNulled() || member.isFinished() || !member.isSessionActive());

            for (final Player member : members) {
                if (split && member == player) {
                    continue;
                }
                try (final ClanChannelBuilder builder = new ClanChannelMemberBuilder(player, owner, added, channel)) {
                    builder.build();

                    member.send(new ClanChannelMember(builder));
                }
            }
            if (split && added) {
                if (player.isNulled()) {
                    return;
                }
                try (final ClanChannelFullBuilder fullBuilder = new ClanChannelFullBuilder(channel, owner)) {
                    fullBuilder.build();
                    player.send(new ClanChannelFull(fullBuilder));
                }
            }
        });
    }

    public static void setPrefix(@NotNull final Player player, final boolean active) {
        final Optional<ClanChannel> optional = getChannel(player.getUsername());
        if (optional.isEmpty()) {
            return;
        }
        final ClanChannel channel = optional.get();
        if (active) {
            player.sendInputName("Enter chat prefix:", string -> {
                final String prefix = string.replaceAll("[^a-zA-Z0-9 ]", "").trim();
                if (prefix.length() == 0) {
                    player.sendMessage("Cannot set an empty prefix.");
                    return;
                }
                if (channel.isDisabled()) {
                    player.sendMessage("Your clan channel has now been enabled!");
                    player.sendMessage("Join your channel by clicking 'Join Chat' and typing: " + TextUtils.capitalize(channel.getOwner().replace("_", " ")));
                }
                channel.setPrefix(prefix);
                channel.setDisabled(false);
                refreshChannel(channel);
                player.getPacketDispatcher().sendComponentText(94, 10, TextUtils.capitalize(prefix));
            });
        } else {
            channel.setDisabled(true);
            player.getPacketDispatcher().sendComponentText(94, 10, "Chat disabled");
            for (final Player p : channel.getMembers()) {
                if (p.isNulled()) {
                    continue;
                }
                kick(player, true, p, true);
            }
        }
    }

    /**
     * Checks whether the player is the owner of the channel or not.
     *
     * @param player  the player to compare.
     * @param channel the channel to check.
     * @return whether the player is owner.
     */
    private static boolean isOwner(@NotNull final Player player, @NotNull final ClanChannel channel) {
        if (player.isNulled()) {
            return false;
        }
        return formatUsername(channel.getOwner()).equals(formatUsername(player.getPlayerInformation().getUsername()));
    }

    /**
     * Checks whether the requested player can enter the given channel or not. Administrators can join all clans. The
     * player will be able to
     * join if they're either the owner of the clan, the enter rank requirement is set to anyone, they're a friend of
     * the owner of the
     * clan(if requirement is friends), or their rank is eligible enough to join the channel.
     * <p>
     * Does not check for the current size of the clan.
     *
     * @param player  the player to compare
     * @param channel the channel to check
     * @return whether the player can enter that channel or not.
     */
    private static boolean canEnter(@NotNull final Player player, @NotNull final Player clanOwner,
                                    @NotNull final ClanChannel channel) {
        if (player.isNulled() || clanOwner.isNulled()) {
            return false;
        }
        final ClanRank rank = channel.getEnterRank();
        if (rank == ClanRank.ANYONE || isOwner(player, channel) || player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            return true;
        }

        final String username = player.getPlayerInformation().getUsername();
        if (rank == ClanRank.FRIENDS) {
            return clanOwner.getSocialManager().containsFriend(username);
        }

        final ClanRank memberRank = channel.getRankedMembers().get(formatUsername(username));
        if (memberRank == null) {
            return false;
        }
        return memberRank.ordinal() >= rank.ordinal();
    }

    /**
     * Checks whether the requested player can kick players from the channel or not. Administrators can always kick
     * players. The player will
     * be able to kick if they're either the owner of the clan or their rank is eligible enough to kick from the
     * channel.
     *
     * @param player  the player to compare
     * @param channel the channel to check
     * @return whether the player can kick from that channel or not.
     */
    public static boolean canKick(@NotNull final Player player, @NotNull final ClanChannel channel) {
        if (player.isNulled()) {
            return false;
        }
        final ClanRank rank = channel.getKickRank();
        if (isOwner(player, channel) || player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            return true;
        }
        final ClanRank memberRank = channel.getRankedMembers().get(formatUsername(player.getPlayerInformation().getUsername()));
        if (memberRank == null) {
            return false;
        }
        return memberRank.ordinal() >= rank.ordinal();
    }

    /**
     * Checks whether the requested player can talk in the given channel or not. Administrators can talk in all clans
     * . The player will be
     * able to talk if they're either the owner of the clan, the talk rank requirement is set to anyone, they're a
     * friend of the owner of
     * the clan(if requirement is friends), or their rank is eligible enough to talk in the channel.
     *
     * @param player  the player attempting to talk.
     * @param channel the channel the player is attempting to talk in.
     * @return whether the player can talk or not.
     */
    private static int canTalk(@NotNull final Player player, @NotNull final Player clanOwner,
                                   @NotNull final ClanChannel channel) {
        if(!player.isStaff() && (player.getAttributes().get("cc_last_talk") != null) && (long) player.getAttributes().get("cc_last_talk") >= (System.currentTimeMillis() - (player.getMemberRank().equalToOrGreaterThan(MemberRank.PREMIUM) ? TimeUnit.TICKS.toMillis(2) : TimeUnit.TICKS.toMillis(5)))) {
            player.sendMessage("Please wait a few moments before sending another message.");
            return 2;
        }
        if (player.isNulled()) {
            return 0;
        }
        final ClanRank rank = channel.getTalkRank();
        if (rank == ClanRank.ANYONE
                || isOwner(player, channel)
                || player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            return 1;
        }
        final String username = player.getPlayerInformation().getUsername();
        if (rank == ClanRank.FRIENDS) {
            return clanOwner.getSocialManager().containsFriend(username) ? 1 : 0;
        }
        final ClanRank memberRank = channel.getRankedMembers().get(formatUsername(username));
        if (memberRank == null) {
            return 0;
        }
        return memberRank.ordinal() >= rank.ordinal() ? 1 : 0;
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 5;
    }

    @Override
    public void read(final @NotNull BufferedReader reader) {
        final ClanChannel[] channels = getGSON().fromJson(reader, ClanChannel[].class);
        for (int i = channels.length - 1; i >= 0; i--) {
            final ClanChannel channel = channels[i];
            if (channel == null) {
                continue;
            }
            channel.setTransientVariables();
            CLAN_CHANNELS.put(formatUsername(channel.getOwner()), channel);
        }
    }

    @Override
    public void write() {
        out(getGSON().toJson(CLAN_CHANNELS.values()));
    }

    @Override
    public String path() {
        return CLANS_FILE_DIRECTORY;
    }
}
