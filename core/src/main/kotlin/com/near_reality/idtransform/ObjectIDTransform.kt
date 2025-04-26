package com.near_reality.idtransform

import net.runelite.cache.ObjectManager
import net.runelite.cache.definitions.ObjectDefinition

/**
 * @author Jire
 */
object ObjectIDTransform {

    val oldObjects by lazy {
        ObjectManager(IDTransform.old).apply { load() }
    }
    val newObjects by lazy {
        ObjectManager(IDTransform.new).apply { load() }
    }

    fun obj(oldID: Int) = oldObjects.getObject(oldID)?.new()

    @JvmOverloads
    fun ObjectDefinition.new(newObjects: ObjectManager = ObjectIDTransform.newObjects): ObjectDefinition? {
        val sameID = newObjects.getObject(id)
        if (sameID != null && this matches sameID) return sameID

        return newObjects.objects.firstOrNull { this matches it }
    }

    infix fun ObjectDefinition.matches(other: ObjectDefinition) = sizeX == other.sizeX
            && sizeY == other.sizeY
            && animationID == other.animationID
            && interactType == other.interactType

            && modelSizeX == other.modelSizeX
            && modelSizeY == other.modelSizeY
            && modelSizeHeight == other.modelSizeHeight

            && mapSceneID == other.mapSceneID

            && offsetX == other.offsetX
            && offsetY == other.offsetY
            && offsetHeight == other.offsetHeight

            && isObstructsGround == other.isObstructsGround
            && contouredGround == other.contouredGround
            && ambientSoundId == other.ambientSoundId

            && ambient == other.ambient
            && contrast == other.contrast
            && isShadow == other.isShadow

            && mapAreaId == other.mapAreaId
            && wallOrDoor == other.wallOrDoor

            && isBlocksProjectile == other.isBlocksProjectile
            && blockingMask == other.blockingMask

            && varbitID == other.varbitID
            && varpID == other.varpID

            && supportsItems == other.supportsItems
            && isMergeNormals == other.isMergeNormals
            && isRotated == other.isRotated
            && objectTypes.contentEquals(other.objectTypes)
            && objectModels.contentEquals(other.objectModels)
            && recolorToFind.contentEquals(other.recolorToFind)
            && recolorToReplace.contentEquals(other.recolorToReplace)
            && retextureToFind.contentEquals(other.retextureToFind)
            && textureToReplace.contentEquals(other.textureToReplace)

            && ambientSoundIds.contentEquals(other.ambientSoundIds)

            && actions.contentEquals(other.actions)

            && configChangeDest.contentEquals(other.configChangeDest)

            && isRandomizeAnimStart == other.isRandomizeAnimStart

            && name == other.name

}