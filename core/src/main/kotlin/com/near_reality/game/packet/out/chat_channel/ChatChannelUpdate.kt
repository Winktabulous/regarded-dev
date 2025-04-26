package com.near_reality.game.packet.out.chat_channel

import com.zenyte.game.packet.GamePacketEncoder
import com.zenyte.game.world.entity.player.LogLevel
import com.zenyte.net.game.ServerProt
import com.zenyte.net.game.packet.GamePacketOut

sealed class ChatChannelUpdate(val type: ChatChannelType) : GamePacketEncoder {

    override fun level(): LogLevel = LogLevel.INFO

    override fun encode(): GamePacketOut = ServerProt.CLANCHANNEL_FULL.gamePacketOut().apply {
        writeByte(type.packetIdentifier)
        encode(this)
    }

    open fun encode(packetOut: GamePacketOut) = Unit

    class Remove(type: ChatChannelType) : ChatChannelUpdate(type)

    class Add(
        type: ChatChannelType,
        val version: Int = 2,
        val useNames: Boolean,
        val useHashes: Boolean,
        val key: Long,
        val field1709: Long,
        val members: List<ChatChannelMember>,
        val name: String,
        val permissions: ChatChannelSettingsDelta.Change.Permissions
    ) : ChatChannelUpdate(type) {
        override fun encode(packetOut: GamePacketOut): Unit = packetOut.run {
            var useFlag = 0
            if (useHashes)
                useFlag = useFlag or 1
            if (useNames)
                useFlag = useFlag or 2
            if (version != 2) {
                useFlag = useFlag or 4
            }
            writeByte(useFlag)
            if (version != 2) {
                writeByte(version)
            }
            writeLong(key)
            writeLong(field1709)
            writeString(name)
            writeByte(0)
            permissions.writeChannel(this)
            writeShort(members.size)
            for (member in members) {
                if (useHashes)
                    writeLong(member.hash)
                if (useNames)
                    writeString(member.name)
                writeByte(member.rank.ordinal)
                writeShort(member.world)
                if (version >= 3) {
                    writeByte(0)
                }
            }
        }
    }
}
