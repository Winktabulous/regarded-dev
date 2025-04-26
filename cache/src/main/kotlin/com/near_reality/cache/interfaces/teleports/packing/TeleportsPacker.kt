package com.near_reality.cache.interfaces.teleports.packing

import com.near_reality.cache.interfaces.teleports.TeleportsList.teleports
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import mgi.types.config.StructDefinitions
import mgi.types.config.enums.EnumDefinitions

/**
 * @author Jire
 */
object TeleportsPacker {

    @JvmStatic
    fun pack() {
        val categories = teleports.categories

        val indexEnumDef = EnumDefinitions().apply {
            id = 10000
            setKeyType("int")
            setValueType("int")
            defaultInt = -1
            setValues(HashMap(categories.size))
        }
        val searchEnumDef = EnumDefinitions().apply {
            id = 10008
            setKeyType("int")
            setValueType("int")
            defaultInt = -1
            setValues(HashMap())
        }
        var nextSearchIndex = 0

        for ((ci, category) in categories.withIndex()) {
            val enumID = category.enumID

            val enumDef = EnumDefinitions().apply {
                id = enumID
                setKeyType("int")
                setValueType("int")
                defaultInt = -1

                val destinations = category.destinations
                setValues(HashMap(destinations.size))
            }

            for ((di, destination) in category.destinations.withIndex()) {
                val structID = destination.structID

                val structDef = StructDefinitions(structID).apply {
                    parameters = Int2ObjectOpenHashMap(2)

                    parameters.put(5000, destination.name)
                    val spriteID = destination.spriteID
                    if (spriteID >= 0)
                        parameters.put(5001, spriteID)
                    else if (spriteID < -1)
                        parameters.put(5002, -spriteID)
                }
                structDef.pack()

                enumDef.values[di] = structID

                searchEnumDef.values[nextSearchIndex++] = structID
            }

            enumDef.pack()

            indexEnumDef.values[ci] = enumID
        }

        searchEnumDef.pack()
        indexEnumDef.pack()
    }

}