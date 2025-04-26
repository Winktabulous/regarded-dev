package mgi.types.config.items

import com.zenyte.CacheManager
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.Cache
import mgi.types.draw.model.Model
import mgi.types.draw.model.ModelData


fun ItemDefinitions.loadInventoryModel(cache: Cache = CacheManager.getCache()): Model {

    val modelDataBytes = cache
        .getArchive(ArchiveType.MODELS)
        .findGroupByID(inventoryModelId)
        .findFileByID(0)
        .data
        .buffer
    val modelData = ModelData(modelDataBytes)
    return toModel(modelData)
}

fun ItemDefinitions.toModel(modelData: ModelData): Model {
    if (resizeX != 128 || resizeY != 128 || resizeZ != 128)
        modelData.resize(resizeX, resizeY, resizeZ)
    originalColours?.indices
        ?.forEach { modelData.recolor(originalColours[it], replacementColours[it]) }
    originalTextureIds?.indices
        ?.forEach { modelData.retexture(originalTextureIds[it], replacementTextureIds[it]) }
    val model = modelData.toModel(ambient + 64, contrast + 768, -50, -10, -50)
    model.isSingleTile = true
    return model
}
