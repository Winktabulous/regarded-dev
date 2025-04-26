package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import com.near_reality.game.item.CustomItemId
import mgi.custom.AnimationBase
import mgi.custom.FramePacker
import mgi.types.config.AnimationDefinitions
import java.util.*

object NearRealityCustomAnimationsPacker {
    @JvmStatic
    fun pack() {
        // push forward something, remove the encoded item
        AnimationDefinitions.get(3000).copy(12_000).apply {
            rightHandItem = -1
            leftHandItem = -1
            pack()
        }
        AnimationDefinitions.get(1658).copy(12_001).apply {
            rightHandItem = CustomItemId.LIME_WHIP_SPECIAL
            pack()
        }
        AnimationDefinitions.get(7514).copy(12_002).apply {
            rightHandItem = CustomItemId.LIME_WHIP_SPECIAL
            pack()
        }
        AnimationDefinitions.get(7208).copy(12_003).apply {
            println(Arrays.toString(frameLengths))
            frameLengths = IntArray(frameLengths.size) { 4 }
            println(Arrays.toString(frameLengths))
            pack()
        }
        AnimationDefinitions.get(7049).copy(12_004).apply {
            rightHandItem = CustomItemId.HOLY_GREAT_WARHAMMER
            pack()
        }
        AnimationDefinitions.get(7215).copy(12_005).apply {
            rightHandItem = CustomItemId.HOLY_GREAT_WARHAMMER
            pack()
        }
        assetsBase("assets/osnr/custom_animations/armadyl_godbow_special/") {
            "sequence_definitions" {
                AnimationDefinitions(id, mgiBuffer).pack()
            }
            FramePacker.reset()
            "sequence_frames" {
                val (groupId, fileId, transformGroupId) = name.split('_').map { it.toInt() }
                val frameID = (groupId shl 16) or fileId
                FramePacker.add(frameID, bytes)
            }
            FramePacker.write()
            "sequence_transforms" {
                AnimationBase.pack(id, bytes)
            }
        }
    }
}
