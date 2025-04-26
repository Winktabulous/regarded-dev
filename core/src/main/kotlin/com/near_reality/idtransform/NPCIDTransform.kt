package com.near_reality.idtransform

import net.runelite.cache.NpcManager
import net.runelite.cache.definitions.NpcDefinition

/**
 * @author Jire
 */
object NPCIDTransform {

    val oldNPCs by lazy {
        NpcManager(IDTransform.old).apply { load() }
    }

    val newNPCs by lazy {
        NpcManager(IDTransform.new).apply { load() }
    }

    fun npc(oldID: Int) = oldNPCs.get(oldID)?.new()

    @JvmOverloads
    fun NpcDefinition.new(newNPCs: NpcManager = NPCIDTransform.newNPCs): NpcDefinition? {
        val sameID = newNPCs[id]
        if (sameID != null && this matches sameID) return sameID

        return newNPCs.npcs.firstOrNull { this matches it }
    }

    infix fun NpcDefinition.matches(other: NpcDefinition) = size == other.size
            && standingAnimation == other.standingAnimation
            && rotateLeftAnimation == other.rotateLeftAnimation
            && rotateRightAnimation == other.rotateRightAnimation
            && walkingAnimation == other.walkingAnimation
            && rotate180Animation == other.rotate180Animation
            && idleRotateLeftAnimation == other.idleRotateLeftAnimation
            && idleRotateRightAnimation == other.idleRotateRightAnimation
            && runAnimation == other.runAnimation
            && runRotate180Animation == other.runRotate180Animation
            && runRotateLeftAnimation == other.runRotateLeftAnimation
            && runRotateRightAnimation == other.runRotateRightAnimation
/*            && crawlAnimation == other.crawlAnimation
            && crawlRotate180Animation == other.crawlRotate180Animation
            && crawlRotateLeftAnimation == other.crawlRotateLeftAnimation
            && crawlRotateRightAnimation == other.crawlRotateRightAnimation*/

            && widthScale == other.widthScale
            && heightScale == other.heightScale

            && combatLevel == other.combatLevel
//            && headIcon == other.headIcon TODO: fix this post rev 211

            && models.contentEquals(other.models)
            && chatheadModels.contentEquals(other.chatheadModels)
            && recolorToFind.contentEquals(other.recolorToFind)
            && recolorToReplace.contentEquals(other.recolorToReplace)
            && retextureToFind.contentEquals(other.retextureToFind)
            && retextureToReplace.contentEquals(other.retextureToReplace)

            //&& actions.contentEquals(other.actions)

            && configs.contentEquals(other.configs)
            && varbitId == other.varbitId
            && varpIndex == other.varpIndex
            // TODO params

            && name == other.name

/*            && rotationFlag == other.rotationFlag
            && isInteractable == other.isInteractable

            && isPet == other.isPet
            && category == other.category*/

}
