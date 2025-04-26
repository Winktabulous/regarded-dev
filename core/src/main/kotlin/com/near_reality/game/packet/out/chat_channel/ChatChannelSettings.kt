package com.near_reality.game.packet.out.chat_channel

import com.zenyte.game.packet.GamePacketEncoder
import com.zenyte.game.world.entity.player.LogLevel
import com.zenyte.net.game.ServerProt
import com.zenyte.net.game.packet.GamePacketOut

/**
 *
 * @param talkRank ACTIVECLANSETTINGS_GETRANKTALK
 */
sealed class ChatChannelSettings(val type: ChatChannelType) : GamePacketEncoder {

    override fun level(): LogLevel = LogLevel.INFO

    override fun encode(): GamePacketOut = ServerProt.CLANSETTINGS_FULL.gamePacketOut().apply {
        writeByte(type.packetIdentifier)
        encode(this)
    }

    open fun encode(packetOut: GamePacketOut) = Unit

    class Remove(type: ChatChannelType) : ChatChannelSettings(type)

    class Add(
        type: ChatChannelType,
        val version: Int,
        val useNames: Boolean,
        val useHashes: Boolean,
        val field1636: Int,
        val field1638: Int,
        val members: List<ChatChannelMember>,
        val bannedMembers: List<ChatChannelBannedMember>,
        val name: String,
        val permissions: ChatChannelSettingsDelta.Change.Permissions,
        val variables: Map<ChatChannelVar, Any>
    ) : ChatChannelSettings(type) {
        init {
            require(version in 1..6) { "version is out of bounds" }
        }
        override fun encode(packetOut: GamePacketOut): Unit = packetOut.run {
            writeByte(version)
            var useFlag = 0
            if (useHashes)
                useFlag = useFlag or 1
            if (useNames)
                useFlag = useFlag or 2
            writeByte(useFlag)
            writeInt(field1636)
            writeInt(field1638)
            writeShort(members.size)
            writeByte(bannedMembers.size)
            writeString(name)
            if (version >= 4)
                writeInt(0)
            permissions.write(this)
            for (member in members) {
                if (useHashes)
                    writeLong(member.hash)
                if (useNames)
                    writeString(member.name)
                writeByte(member.rank.ordinal)
                if (version >= 2)
                    writeInt(member.extraInfo)
                if (version >= 5)
                    writeShort(member.field1649)
                if (version >= 6)
                    writeByte(if (member.field1650) 1 else 0)
            }
            for (bannedMember in bannedMembers) {
                if (useHashes)
                    writeLong(bannedMember.hash)
                if (useNames)
                    writeString(bannedMember.name)
            }
            if (version >= 3) {
                writeShort(variables.size)
                for ((v, value) in variables.entries) {
                    writeInt(v.index or (v.type.index shl 30))
                    when (v.type.index) {
                        0 -> {
                            if (value is Double) {
                                writeInt(value.toInt())
                            } else {
                                writeInt(value as Int)
                            }
                        }
                        1 -> {
                            writeLong(value as Long)
                        }
                        2 -> {
                            writeString(value as String)
                        }
                    }
                }
            }
        }
    }
}
