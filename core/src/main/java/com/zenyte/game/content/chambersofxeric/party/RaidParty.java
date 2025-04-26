package com.zenyte.game.content.chambersofxeric.party;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.chambersofxeric.MountQuidamortemArea;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.events.ClanLeaveEvent;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Kris | 15. nov 2017 : 18:12.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidParty {
    /**
     * The skill ids ordered in the same exact order that the CS2 accepts.
     */
    private static final int[] orderedSkills = new int[] {SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.RANGED, SkillConstants.MAGIC, SkillConstants.DEFENCE, SkillConstants.HITPOINTS, SkillConstants.PRAYER, SkillConstants.AGILITY, SkillConstants.HERBLORE, SkillConstants.THIEVING, SkillConstants.CRAFTING, SkillConstants.RUNECRAFTING, SkillConstants.MINING, SkillConstants.SMITHING, SkillConstants.FISHING, SkillConstants.COOKING, SkillConstants.FIREMAKING, SkillConstants.WOODCUTTING, SkillConstants.FLETCHING, SkillConstants.SLAYER, SkillConstants.FARMING, SkillConstants.CONSTRUCTION, SkillConstants.HUNTER};
    /**
     * The list of all the advertised raids parties.
     */
    public static final List<RaidParty> advertisedParties = new ObjectArrayList<>((int) (40 / 0.75F));
    /**
     * The username of the player who created the party.
     */
    private String player;
    /**
     * The clan channel which is tied to this raid party.
     */
    private final ClanChannel channel;
    /**
     * The time in epoch milliseconds when the party was initially created.
     */
    private final long milliseconds;
    /**
     * The raid attached to this party.
     */
    private final Raid raid;
    /**
     * The preferred party size, combat level and skill total. These variables do not set any actual restrictions, it is just for visibilitiy purposes so the party owner can see if
     * the caps are reached, and so that the players who join the party can see what the party owner is expecting of them.
     */
    private int preferredPartySize;
    private int preferredCombatLevel;
    private int preferredSkillTotal;
    /**
     * Whether or not the challenge mode version of the raid is enabled.
     */
    private boolean challengeMode;

    /**
     * The constructor for the raid party, requiring a host and a clan channel to be constructed.
     *
     * @param host    the player constructing the raid party.
     * @param channel the clan channel in which the player builds the raid party.
     */
    RaidParty(@NotNull final Player host, @NotNull final ClanChannel channel) {
        player = host.getUsername();
        raid = new Raid(this);
        this.channel = channel;
        milliseconds = Utils.currentTimeMillis();
    }

    /**
     * De-list the raid party from the advertised parties list when the player who constructed the party leaves the clan chat.
     *
     * @param event the clan channel leave event.
     */
    @Subscribe
    public static final void onClanLeave(final ClanLeaveEvent event) {
        final Player player = event.getPlayer();
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null) {
            return;
        }
        if (channel.getRaidParty() == null || !channel.getRaidParty().getPlayer().equals(player.getUsername())) {
            return;
        }
        if (MountQuidamortemArea.appiontAnotherPartyLeader(player)) {
            return;
        }
        advertisedParties.remove(channel.getRaidParty());
    }

    /**
     * Refreshes the raid party interface which shows the contents of the raid party.
     *
     * @param player the player for whom to refresh the interface.
     */
    public void refresh(final Player player) {
        if (!player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
            return;
        }
        boolean partyOwner = this.player.equalsIgnoreCase(player.getUsername());
        final IntArrayList levels = new IntArrayList();
        player.getVarManager().sendBit(5428, partyOwner ? 1 : 0);
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 507);
        player.getVarManager().sendBit(6385, challengeMode ? 1 : 0);
        int size = 0;
        for (final Player p : channel.getMembers()) {
            if (p.isNulled() || !p.inArea(MountQuidamortemArea.class)) {
                continue;
            }
            size++;
            levels.add(p.getSkills().getCombatLevel());
            levels.add(p.getSkills().getTotalLevel());
            levels.add(p.getNumericAttribute(challengeMode ? "challengechambersofxeric" : "chambersofxeric").intValue());
            for (int i : orderedSkills) {
                levels.add(p.getSkills().getLevelForXp(i));
            }
            final StringBuilder arguments = new StringBuilder();
            arguments.append(Colour.WHITE.wrap(p.getName()));
            arguments.append('|');
            for (final Integer level : levels) {
                arguments.append(level).append('|');
            }
            player.getPacketDispatcher().sendClientScript(1517, 0, arguments.toString());
            levels.clear();
        }
        player.getPacketDispatcher().sendClientScript(1524, "Raiding Party of " + StringFormatUtil.formatString(this.player) + " (" + size + ")", !channel.getMembers().contains(player) ? 0 : partyOwner ? 2 : 1, advertisedParties.contains(this) ? 1 : 0, player.getVariables().getRaidAdvertsQuota());
        player.getPacketDispatcher().sendComponentSettings(507, 3, 0, 7, AccessMask.CONTINUE);
        player.getVarManager().sendBit(5433, preferredPartySize);
        player.getVarManager().sendBit(5426, preferredCombatLevel);
        player.getVarManager().sendBit(5427, preferredSkillTotal);
    }

    /**
     * Refreshes the raid party tab which is visible when the player enters the chambers itself.
     *
     * @param player the player for whom to refresh the interface.
     */
    public void refreshTab(@NotNull final Player player) {
        RaidPartyInterface.refresh(player, raid);
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public ClanChannel getChannel() {
        return channel;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public Raid getRaid() {
        return raid;
    }

    public int getPreferredPartySize() {
        return preferredPartySize;
    }

    public void setPreferredPartySize(int preferredPartySize) {
        this.preferredPartySize = preferredPartySize;
    }

    public int getPreferredCombatLevel() {
        return preferredCombatLevel;
    }

    public void setPreferredCombatLevel(int preferredCombatLevel) {
        this.preferredCombatLevel = preferredCombatLevel;
    }

    public int getPreferredSkillTotal() {
        return preferredSkillTotal;
    }

    public void setPreferredSkillTotal(int preferredSkillTotal) {
        this.preferredSkillTotal = preferredSkillTotal;
    }

    public boolean isChallengeMode() {
        return challengeMode;
    }

    public void setChallengeMode(boolean challengeMode) {
        this.challengeMode = challengeMode;
    }


}
