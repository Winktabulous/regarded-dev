package com.near_reality.game.content.dt2.area.obj

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.near_reality.scripts.`object`.actions.ObjectActionScript

class VardorvisEntryObjObjectAction : ObjectActionScript() {
    init {
        48745 {
            if(option == "Enter") {
                val instance = VardorvisInstance.createInstance(player)
                instance.constructRegion()
            }
        }
    }
}