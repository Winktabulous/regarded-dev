package com.near_reality.cache

import com.near_reality.cache.TableEncoding.encodeToBuffer
import com.zenyte.CacheManager
import mgi.tools.jagcached.ArchiveType
import mgi.types.Definitions
import mgi.types.config.DBRowDefinition
import mgi.types.config.DBTableDefinition
import mgi.utilities.ByteBuffer
import net.runelite.cache.util.BaseVarType

class DBTableIndexDefinition() : Definitions {
    var tableId: Int = 0
    var columnId: Int = 0
    var rowLookupTable: Map<BaseVarType, Map<out Any, List<Int>>> = mapOf()

    constructor(id: Int, buffer: ByteBuffer) : this() {
        tableId = id
        decode(buffer)
    }

    override fun load() {
        val cache = CacheManager.getCache()
        val groups = cache.getArchive(ArchiveType.DBTABLEINDEX)

        for (id in 0 until groups.highestGroupId) {
            val group = groups.groups[id] ?: continue

            val masterFile = group.findFileByID(0) ?: continue
            val buffer = masterFile.data ?: continue
            masterIndexes[id] = DBTableIndexDefinition(id, buffer)
        }
    }

    override fun encode(): ByteBuffer = encodeToBuffer()

    override fun decode(buffer: ByteBuffer) {
        rowLookupTable = buffer.readRowIndices()
    }

    private fun ByteBuffer.readRowIndices(): Map<BaseVarType, Map<out Any, List<Int>>> {
        return buildMap {
            val tupleSize = readProtobufVarInt()
            repeat(tupleSize) {
                val baseVarType = BaseVarType.entries[readUnsignedByte()]
                put(baseVarType, buildMap {
                    val valueCount = readProtobufVarInt()
                    repeat(valueCount) {
                        put(
                            key = when (baseVarType) {
                                BaseVarType.INTEGER -> readInt()
                                BaseVarType.LONG -> readLong()
                                BaseVarType.STRING -> readString()
                            },
                            value = List(readProtobufVarInt()) {
                                readProtobufVarInt()
                            }
                        )
                    }
                })
            }
        }
    }



    fun buildMaster(dbRowDefinitions: List<DBRowDefinition>) {
        rowLookupTable = mapOf(BaseVarType.INTEGER to mapOf(0 to dbRowDefinitions.map { it.id }))
    }

    fun buildColumns(rows : List<DBRowDefinition>, isString: Boolean = false, vararg columns: Int): List<DBTableIndexDefinition> {
        val defs = mutableListOf<DBTableIndexDefinition>()
        for(column in columns) {
            val rowsByValue: Map<Any?, List<Int>> = rows
                .groupBy { row -> row.columns[column]?.values?.get(0)?.defaultInt }
                .mapValues { entry -> entry.value.map { row -> row.id } }
            val rowLookupTable = mutableMapOf<BaseVarType, Map<out Any, List<Int>>>()
            val varType = if(isString) BaseVarType.STRING else BaseVarType.INTEGER
            rowLookupTable[varType] = buildMap {
                rowsByValue.forEach { (value, rows) ->
                    if (value != null)
                        put(value, rows)
                }
            }
            val def = DBTableIndexDefinition()
            def.tableId = tableId
            def.columnId = columnId
            def.rowLookupTable = rowLookupTable
            defs.add(def)
        }
        return defs.toList()
    }

    companion object {
        var masterIndexes: HashMap<Int, DBTableIndexDefinition> = HashMap()
    }
}

fun ByteBuffer.readProtobufVarInt(): Int {
    var value = 0
    var bits = 0
    var read: Int
    do {
        read = readUnsignedByte()
        value = value or (read and 0x7F shl bits)
        bits += 7
    } while (read > 127)
    return value
}