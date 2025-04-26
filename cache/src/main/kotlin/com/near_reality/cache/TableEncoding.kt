package com.near_reality.cache

import mgi.utilities.ByteBuffer
import net.runelite.cache.util.BaseVarType

object TableEncoding {
    @JvmStatic fun DBTableIndexDefinition.encodeToBuffer() : ByteBuffer {
        val data = ByteBuffer(5000)
        data.writeProtobufVarInt(this.rowLookupTable.size)
        rowLookupTable.forEach { (baseVarType, rowIdsByValue) ->
            data.writeByte(baseVarType.ordinal)
            data.writeProtobufVarInt(rowIdsByValue.size)
            rowIdsByValue.forEach { (value, rowIds) ->
                when(baseVarType) {
                    BaseVarType.INTEGER -> data.writeInt(value as Int)
                    BaseVarType.LONG -> data.writeLong(value as Long)
                    BaseVarType.STRING -> data.writeString(value as String)
                }
                data.writeProtobufVarInt(rowIds.size)
                rowIds.forEach {
                    data.writeProtobufVarInt(it)
                }
            }
        }
        return data
    }

}