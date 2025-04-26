package com.zenyte.game.content.clans;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.io.RSBuffer;
import mgi.utilities.StringFormatUtil;

import java.util.Map;

/**
 * @author Kris | 28/01/2019 16:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ClanChannelMemberBuilder extends ClanChannelBuilder {

    ClanChannelMemberBuilder(final Player player, final Player clanOwner, final boolean add,
                             final ClanChannel channel) {
        super(ServerProt.UPDATE_FRIENDCHAT_CHANNEL_SINGLEUSER, channel, clanOwner);
        this.player = player;
        this.add = add;
    }

    private final Player player;
    private final boolean add;

    @Override
    protected void build(ClanChannel channel, Player clanOwner, RSBuffer buffer) {
        final Map<String, ClanRank> rankedMembers = channel.getRankedMembers();
        buffer.writeString(player.getName());
        buffer.writeShort(GameConstants.WORLD_PROFILE.getNumber());
        if (add) {
            buffer.writeByte(getRank(rankedMembers.get(StringFormatUtil.formatUsername(player.getUsername())), player, clanOwner));
            buffer.writeString("");
        } else {
            buffer.writeByte(-128);
        }
    }

}
