package com.zenyte.game.content.clans;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 28/01/2019 15:56
 * @author Jire
 */
public abstract class ClanChannelBuilder implements AutoCloseable {

    private final ClanChannel channel;
    private final Player clanOwner;

    private final GamePacketOut buffer;

    ClanChannelBuilder(final ServerProt prot, final ClanChannel channel, final Player clanOwner) {
        this.channel = channel;
        this.clanOwner = clanOwner;

        this.buffer = prot.gamePacketOut();
    }

    protected int getRank(final ClanRank rank, final Player member, final Player owner) {
        if (owner.getUsername().equals(member.getUsername())) {
            return 7;
        } else if (member.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            return 127;
        } else if (rank != null) {
            if (rank == ClanRank.FRIENDS) {
                return owner.getSocialManager().containsFriend(member.getUsername()) ? 0 : -1;
            }
            return rank.getId();
        }
        return owner.getSocialManager().containsFriend(member.getUsername()) ? 0 : -1;
    }

    public final void build() {
        build(channel, clanOwner, buffer);
    }

    protected abstract void build(ClanChannel channel, Player clanOwner, RSBuffer buffer);

    public GamePacketOut encode() {
        return buffer;
    }

    public ClanChannel getChannel() {
        return channel;
    }

    public void retain() {
        buffer.retain();
    }

    @Override
    public void close() {
        buffer.release();
    }

}
