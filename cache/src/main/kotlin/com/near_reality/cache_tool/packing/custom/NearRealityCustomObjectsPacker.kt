package com.near_reality.cache_tool.packing.custom

import com.zenyte.game.world.`object`.ObjectId
import mgi.tools.parser.TypeParser
import mgi.types.config.ObjectDefinitions

object NearRealityCustomObjectsPacker {

    @JvmStatic
    fun pack(){
        TypeParser.cloneObject(ObjectId.ZAMORAK_PORTAL, 35015).apply {
            name = "Private portal"
            sizeX /= 2
            sizeY /= 2
            modelSizeX /= 2
            modelSizeY /= 2
            modelSizeHeight /= 2
            mapSceneId = 64
            setOption(0, "Use")
            pack()
        }

        ObjectDefinitions.get(47567).apply {
            setOption(1, "Take")
            pack()
        }

        ObjectDefinitions.get(47568).apply {
            setOption(1, "Take")
            pack()
        }

    }
}
