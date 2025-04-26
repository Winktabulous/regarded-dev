package com.near_reality.cache_tool.packing.custom

import mgi.types.config.enums.EnumDefinitions

object NearRealityRemovePetsFromBossCLs {

    private val index0Pets = listOf(
        2173,
        2112,
        2113,
        2114,
        2115,
        2116,
        2117,
        2119,
        2133,
        2390,
        2120,
        2121,
        2122,
        2132,
        2123,
        2134,
        2125,
        2126,
        2127,
        4097,
        2796,
        4929,
        2344,
        2129,
        2131,
        2134,
        2124,
        2109,
        2130,
        //10025,
        2135,
        2136,
        2137,
        2389,
        2138,
        2169,
    )

    private val indexXPets = listOf(
        Pair(2119, 0), // changes to 0 after reshuffle
        Pair(2119, 0),
    )


    @JvmStatic
    fun pack() = run {
        for(idx in index0Pets) {
            EnumDefinitions.get(idx).apply {
                values[0] = -1
                save()
            }
        }

        for(pair in indexXPets) {
            EnumDefinitions.get(pair.first).apply {
                values[pair.second] = -1
                save()
            }
        }
    }

    private fun EnumDefinitions.reorganizeValues() {
        val tempValues = mutableMapOf<Int, Any>()
        var index = 0
        for(iterate in values) {
            if(iterate.value is Int && iterate.value != -1 && iterate.value != null) {
                tempValues[index] = iterate.value as Int
                index++
            }
        }
        values = tempValues
    }

    private fun EnumDefinitions.save() {
        reorganizeValues()
        pack()
    }
}