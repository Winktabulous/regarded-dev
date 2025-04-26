package com.zenyte.game.content.chambersofxeric.party;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.chambersofxeric.MountQuidamortemArea;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 08/07/2019 02:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidingPartyInterface extends Interface {
    @Override
    protected void attach() {
        put(3, 0, "Join/Leave");
        put(3, 1, "Advertise/Delist");
        put(3, 2, "Disband");
        put(3, 3, "Refresh");
        put(3, 4, "Back");
        put(3, 5, "Set preferred party size");
        put(3, 6, "Set preferred combat level");
        put(3, 7, "Set preferred skill total");
        put(3, 8, "Toggle challenge mode");
        put(3, 9, "Toggle large raid");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Join/Leave", player -> {
            final RaidParty party = (RaidParty) player.getTemporaryAttributes().get("viewingRaidParty");
            final ClanChannel channel = player.getSettings().getChannel();
            if (party == null) {
                if (channel == null) {
                    player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting."));
                    return;
                }
                ClanManager.leave(player, true);
                final RaidParty channelParty = channel.getRaidParty();
                if (channelParty != null) {
                    for (final Player p : channel.getMembers()) {
                        channelParty.refresh(p);
                    }
                }
                RaidingPartiesInterface.refresh(player);
                return;
            }
            if (channel == null) {
                ClanManager.join(player, party.getChannel().getOwner());
                for (final Player p : party.getChannel().getMembers()) {
                    party.refresh(p);
                }
                return;
            }
            if (channel != party.getChannel()) {
                player.sendMessage("You need to leave your clan channel before joining another one.");
                return;
            }
            ClanManager.leave(player, true);
            final RaidParty channelParty = channel.getRaidParty();
            if (channelParty != null) {
                for (final Player p : channel.getMembers()) {
                    channelParty.refresh(p);
                }
            }
            RaidingPartiesInterface.refresh(player);
        });
        bind("Advertise/Delist", player -> {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting."));
                return;
            }
            final RaidParty party = channel.getRaidParty();
            if (party == null) {
                player.getDialogueManager().start(new PlainChat(player, "Could not advertise party because party is null."));
                return;
            }
            if (RaidParty.advertisedParties.contains(party)) {
                RaidParty.advertisedParties.remove(party);
            } else {
                if (RaidParty.advertisedParties.size() >= 40) {
                    player.sendMessage("The advertised parties list is currently full!");
                    return;
                }
                final int quota = player.getVariables().getRaidAdvertsQuota();
                if (quota <= 0) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            plain("You have used your quota of adverts for the day.");
                        }
                    });
                    return;
                }
                player.getVariables().setRaidAdvertsQuota(quota - 1);
                RaidParty.advertisedParties.add(party);
            }
            party.refresh(player);
        });
        bind("Disband", player -> {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting."));
                return;
            }
            final RaidParty party = channel.getRaidParty();
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
            player.sendMessage("Your party has been disbanded.");
            RaidParty.advertisedParties.remove(party);
            channel.getMembers().forEach(p -> {
                if (!p.inArea(MountQuidamortemArea.class)) {
                    return;
                }
                p.sendMessage("<col=EF20FF>" + StringFormatUtil.formatString(player.getName()) + " has debanded the party.");
            });
            if (channel.getRaidParty() != null) {
                if (channel.getRaidParty().getRaid() != null) {
                    channel.getRaidParty().getRaid().destroy(true, true);
                }
                channel.setRaidParty(null);
            }
            RaidingPartiesInterface.refresh(player);
        });
        bind("Refresh", player -> {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting."));
                return;
            }
            channel.getRaidParty().refresh(player);
        });
        bind("Back", RaidingPartiesInterface::refresh);
        bind("Set preferred party size", player -> {
            if (verifyConditions(player)) return;
            player.sendInputInt("Set a preferred party size (or 0 to clear it):", amount -> {
                final RaidParty party = player.getSettings().getChannel().getRaidParty();
                if (party.getRaid().getMap() != null) {
                    player.sendMessage("Your party has already entered the chambers, you cannot edit the settings now.");
                    return;
                }
                party.setPreferredPartySize(Math.min(100, amount));
                GameInterface.RAIDING_PARTY.open(player);
                party.refresh(player);
            });
        });
        bind("Set preferred combat level", player -> {
            if (verifyConditions(player)) return;
            player.sendInputInt("Set a preferred combat level (or 0 to clear it):", amount -> {
                final RaidParty party = player.getSettings().getChannel().getRaidParty();
                if (party.getRaid().getMap() != null) {
                    player.sendMessage("Your party has already entered the chambers, you cannot edit the settings now.");
                    return;
                }
                party.setPreferredCombatLevel(Math.min(126, amount));
                GameInterface.RAIDING_PARTY.open(player);
                party.refresh(player);
            });
        });
        bind("Set preferred skill total", player -> {
            if (verifyConditions(player)) return;
            player.sendInputInt("Set a preferred skill total (or 0 to clear it):", amount -> {
                final RaidParty party = player.getSettings().getChannel().getRaidParty();
                if (party.getRaid().getMap() != null) {
                    player.sendMessage("Your party has already entered the chambers, you cannot edit the settings now.");
                    return;
                }
                party.setPreferredSkillTotal(Math.min(99 * SkillConstants.SKILLS.length, amount));
                GameInterface.RAIDING_PARTY.open(player);
                party.refresh(player);
            });
        });
        bind("Toggle challenge mode", player -> {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting."));
                return;
            }
            final RaidParty party = channel.getRaidParty();
            if (party.getRaid().getMap() != null) {
                player.sendMessage("Your party has already entered the chambers, you cannot edit the settings now.");
                return;
            }
            party.setChallengeMode(!party.isChallengeMode());
            party.refresh(player);
        });
        bind("Toggle large raid", player -> {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting."));
                return;
            }
            final RaidParty party = channel.getRaidParty();
            if (party.getRaid().getMap() != null) {
                player.sendMessage("Your party has already entered the chambers, you cannot edit the settings now.");
                return;
            }
            player.sendMessage("Large raids are not available.");
            party.refresh(player);
        });
    }

    /**
     * Verifies the conditions for managing the raid party.
     *
     * @param player the player who is managing the raid party.
     * @return whether or not the party settings should be altered.
     */
    private boolean verifyConditions(@NotNull final Player player) {
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null) {
            player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting."));
            return true;
        }
        if (channel.getRaidParty().getRaid().getMap() != null) {
            player.sendMessage("Your party has already entered the chambers, you cannot edit the settings now.");
            return true;
        }
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        return false;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAIDING_PARTY;
    }
}
