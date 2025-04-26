package com.zenyte.game.content.clans;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.io.RSBuffer;
import com.zenyte.utils.TextUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author Kris | 28/01/2019 15:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ClanChannelFullBuilder extends ClanChannelBuilder {

    ClanChannelFullBuilder(final ClanChannel channel, final Player clanOwner) {
        super(ServerProt.UPDATE_FRIENDCHAT_CHANNEL_FULL, channel, clanOwner);
    }

    @Override
    protected void build(ClanChannel channel, Player clanOwner, RSBuffer buffer) {
        final Set<Player> members = channel.getMembers();
        final Map<String, ClanRank> rankedMembers = channel.getRankedMembers();
        buffer.writeString(channel.getOwner());
        buffer.writeLong(TextUtils.stringToLong(channel.getPrefix()));
        buffer.writeByte(channel.getKickRank().getId() - 1);
        final int size = members.size();
        buffer.writeByte(size == 0 ? 255 : size);
        for (final Player member : members) {
            buffer.writeString(member.getName());
            buffer.writeShort(GameConstants.WORLD_PROFILE.getNumber());
            buffer.writeByte(getRank(rankedMembers.get(member.getUsername()), member, clanOwner));
            buffer.writeString("");
        }
    }

}
