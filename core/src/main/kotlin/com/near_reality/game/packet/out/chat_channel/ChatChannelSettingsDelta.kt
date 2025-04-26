package com.near_reality.game.packet.out.chat_channel

import com.zenyte.game.packet.GamePacketEncoder
import com.zenyte.game.world.entity.player.LogLevel
import com.zenyte.net.game.ServerProt
import com.zenyte.net.game.packet.GamePacketOut

/**
 * Used to perform partial updates to [ChatChannelSettings] for the specified [clan type][type].
 *
 * @author Stan van der Bend
 *
 * @param field1587 must be incremented by one for each time it is sent per [type] per player starting from [ChatChannelSettings.Add.field1636].
 */
class ChatChannelSettingsDelta(
    private val type: ChatChannelType,
    private val field1588: Long = 0,
    private val field1587: Int,
    private val changes: List<Change>,
) : GamePacketEncoder {

    override fun level(): LogLevel = LogLevel.INFO

    override fun encode(): GamePacketOut = ServerProt.CLANSETTINGS_DELTA.gamePacketOut().apply {
        writeByte(type.packetIdentifier)
        writeLong(field1588)
        writeInt(field1587)
        if (changes.isEmpty())
            writeByte(0)
        else for (change in changes) {
            writeByte(change.opcode)
            change.write(this)
        }
    }

    sealed class Change(val opcode: Int) {

        abstract fun write(packetOut: GamePacketOut)

        sealed class Member(opcode: Int) : Change(opcode) {

            abstract class NameOrHash(
                opcode: Int,
                val hash: Long?,
                val name: String?,
                val joinDay: Int? = null,
            ) : Member(opcode) {
                override fun write(packetOut: GamePacketOut): Unit = packetOut.run {
                    if (hash != null) {
                        writeByte(0)
                        writeLong(hash)
                    } else
                        writeByte(255)
                    name?.run(::writeString)
                    joinDay?.run(::writeShort)
                }
            }

            class Ban(hash: Long?, name: String?) : NameOrHash(3, hash, name)
            class AddNoRank(hash: Long?, name: String?) : NameOrHash(1, hash, name)
            class AddWithRank(hash: Long?, name: String?, joinDay: Int) : NameOrHash(13, hash, name, joinDay)

            abstract class Index(
                opcode: Int,
                private val memberIndex: Int,
                private val encode: GamePacketOut.() -> Unit = {},
            ) : Member(opcode) {
                final override fun write(packetOut: GamePacketOut): Unit = packetOut.run {
                    writeShort(memberIndex)
                    encode(this)
                }
            }

            class UnBan(index: Int) : Index(6, index)
            class Remove(index: Int) : Index(5, index)
            class SetOwner(index: Int) : Index(15, index)
            class SetRank(index: Int, rank: Int) : Index(2, index, encode = { writeByte(rank) })
            class SetMuted(index: Int, muted: Boolean) : Index(14, index, encode = { writeByte(if (muted) 1 else 0) })
            sealed class SetExtraInfo(index: Int, value: Int, byte1: Int, byte2: Int) :
                Index(7, index, encode = ChatChannelBitPackedValue(value, byte1, byte2)::write) {

                class GroupIronManPrestige(memberIndex: Int, prestige: Boolean) :
                    SetExtraInfo(memberIndex, if (prestige) 1 else 0, 28, 28)

                class PVPAIcon(memberIndex: Int, icon: Int) :
                    SetExtraInfo(memberIndex, icon, 20, 31)

                class Unknown(memberIndex: Int, someInt: Int) :
                    SetExtraInfo(memberIndex, someInt, 0, 12)
            }
        }

        class Permissions(
            private val allowGuests: Boolean,
            private val talkRank: Int,
            private val kickRank: Int,
            private val lootShareRank: Int,
            private val coinShareRank: Int,
        ) : Change(4) {
            override fun write(packetOut: GamePacketOut): Unit = packetOut.run {
                writeByte(if (allowGuests) 1 else 0)
                writeByte(talkRank)
                writeByte(kickRank)
                writeByte(lootShareRank)
                writeByte(coinShareRank)
            }

            fun writeChannel(packetOut: GamePacketOut): Unit = packetOut.run {
                writeByte(kickRank)
                writeByte(talkRank)
            }
        }

        sealed class SetParameter<T>(
            opcode: Int,
            private val parameterKey: Int,
            private val parameterValue: T,
        ) : Change(opcode) {
            final override fun write(packetOut: GamePacketOut): Unit = packetOut.run {
                writeInt(parameterKey)
                when (parameterValue) {
                    is Int -> writeInt(parameterValue)
                    is Long -> writeLong(parameterValue)
                    is String -> writeString(parameterValue)
                    is ChatChannelBitPackedValue -> parameterValue.write(this)
                }
            }

            class IntValue(key: Int, int: Int) : SetParameter<Int>(8, key, int)
            class LongValue(key: Int, long: Long) : SetParameter<Long>(9, key, long)
            class StringValue(key: Int, string: String) : SetParameter<String>(10, key, string)
            class BitPackedValue(key: Int, value: Int, byte1: Int, byte2: Int) :
                SetParameter<ChatChannelBitPackedValue>(11, key, ChatChannelBitPackedValue(value, byte1, byte2))
        }

        class SetName(val name: String) : Change(12) {
            override fun write(packetOut: GamePacketOut): Unit = packetOut.run {
                writeString(name)
                writeInt(0)
            }
        }
    }
}

