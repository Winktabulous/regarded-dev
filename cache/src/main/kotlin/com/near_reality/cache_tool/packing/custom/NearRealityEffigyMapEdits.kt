package com.near_reality.cache_tool.packing.custom

import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.Regions
import mgi.tools.parser.TypeParser
import java.util.function.Predicate

object NearRealityEffigyMapEdits {

    @JvmStatic
    fun apply() {
        edit(12088) {
            ObjectId.ROCKS_11381(3009, 3594, 0)
            ObjectId.ROCKS_11381(3009, 3595, 0)
            ObjectId.ROCKS_11381(3011, 3598, 0)
            ObjectId.ROCKS_11381(3012, 3599, 0)
            ObjectId.ROCKS_11381(3013, 3598, 0)
            ObjectId.ROCKS_11381(3016, 3601, 0)
            ObjectId.ROCKS_11381(3017, 3601, 0)
            ObjectId.ROCKS_11381(3025, 3594, 0)
            ObjectId.ROCKS_11381(3026, 3595, 0)
            ObjectId.ROCKS_11381(3020, 3585, 0)
            ObjectId.ROCKS_11381(3021, 3584, 0)
            ObjectId.ROCKS_11381(3017, 3591, 0)
            ObjectId.ROCKS_11381(3018, 3591, 0)
        }

        edit(11681) {
            replace(46702, 46701)
        }
    }

    class MapEdit(val regionId: Int) {
        private val objects = mutableListOf<WorldObject>()
        val replacements = mutableMapOf<Int, Int>()
        operator fun Int.invoke(x: Int, y: Int, z: Int, type: Int = 10, rotation: Int = 0) {
            objects += WorldObject(this, type, rotation, Location(x, y, z))
        }

        fun replace(oldId: Int, newId: Int) {
            replacements[oldId] = newId
        }

        private fun buildPredicate() : Predicate<WorldObject> =
            Predicate<WorldObject> {
                if(replacements.containsKey(it.id))
                    it.id = replacements[it.id]!!
                false
            }

        fun pack() {
            TypeParser.packMapPre209(regionId, null, Regions.inject(regionId, buildPredicate(), *objects.toTypedArray()))
        }
    }

    fun edit(regionId: Int, block: MapEdit.() -> Unit) {
        MapEdit(regionId).apply(block).pack()
    }
}
