package com.near_reality.cache_tool.dumping

import com.near_reality.cache_tool.cacheTo
import com.zenyte.CacheManager
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.GroupType
import mgi.tools.jagcached.cache.Cache
import mgi.types.Definitions
import mgi.types.config.AnimationDefinitions
import mgi.types.config.SpotAnimationDefinition
import mgi.types.config.ObjectDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import net.runelite.cache.ConfigType
import net.runelite.cache.IndexType
import net.runelite.cache.fs.Store
import net.runelite.cache.io.InputStream
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

object Cache2018Dump {
    var npcIdOnset = 16067
    var modelIdOnset = 61000
    var soundIdOnset = cacheTo.getArchive(ArchiveType.SYNTHS).highestGroupId + 1
    var animationIdOnset = 17_420
    var objectIdOnset = 60022
    var itemIdOnset = 32348
    var spotAnimIdOnset = cacheTo.getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.SPOTANIM).highestFileId + 1
    var frameArchiveIdOnset = 7000
    var frameMapIdOnset = 7000
    val dumpObjectIds = listOf(32547, 32535, 32536)
    val dumpItemIds = listOf(22345, 22346, 22349, 22355, 22358, 22361)
    val dumpSpotAnimIds = 2590..2598
    val modelIdsToDump = mutableSetOf<Int>()
    val modelIdMap = mutableMapOf<Int, Int>()
    val npcIdMap = mutableMapOf<Int, Int>()
    val animationIdMap = mutableMapOf<Int, Int>()
    val soundIdMap = mutableMapOf<Int, Int>()
    val objectIdMap = mutableMapOf<Int, Int>()
    val itemIdMap = mutableMapOf<Int, Int>()
    val spotAnimIdMap = mutableMapOf<Int, Int>()
    val frameArchiveIdMap = mutableMapOf<Int, Int>()
    val skeletonIdMap = mutableMapOf<Int, Int>()
    val npcToEncode = mutableListOf<NPCDefinitions>()
    val animationToEncode = mutableListOf<AnimationDefinitions>()
    val objectToEncode = mutableListOf<ObjectDefinitions>()
    val itemToEncode = mutableListOf<ItemDefinitions>()
    val spotAnimToEncode = mutableListOf<SpotAnimationDefinition>()
    @JvmStatic
    fun main(args: Array<String>) {
        val dumpPath =
            Path("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/cache/assets/osnr/easter_2024")
        val cache2018 = Cache.openCache("/Users/stanvanderbend/.qodat/downloads/2018-03-29-rev167/cache")
        val store = Store(File("/Users/stanvanderbend/.qodat/downloads/2018-03-29-rev167/cache"))
        store.load()
        val storage = store.storage
        val index = store.getIndex(IndexType.CONFIGS)


        val seqArchive = index.getArchive(ConfigType.SEQUENCE.id)
        val seqArchiveData = storage.loadArchive(seqArchive)
        val seqArchiveFiles = seqArchive.getFiles(seqArchiveData)
        val animations = seqArchiveFiles.files.map {
            val sequence = SequenceLoader206().load(it.fileId, it.contents)
            return@map object : AnimationDefinition {
                override val id: String = it.fileId.toString()
                override val frameHashes: IntArray = sequence.frameIDs?: IntArray(0)
                override val frameLengths: IntArray = sequence.frameLenghts?: IntArray(0)
                override val loopOffset: Int = sequence.frameStep
                override val leftHandItem: Int = sequence.leftHandItem
                override val rightHandItem: Int = sequence.rightHandItem
            }
        }.toTypedArray()

        animations.find { it.id == "7981" }?.let {
            println()
        }

        CacheManager.loadCache(cache2018)
        CacheManager.setCache(cache2018)
        ItemDefinitions().load(cache2018)
        AnimationDefinitions().load()
        ObjectDefinitions().load()
        SpotAnimationDefinition().load()
        dumpEasterSh(cache2018, dumpPath)
        cache2018.getArchive(ArchiveType.MAPS)
    }

    private fun dumpEasterSh(cache2018: Cache, dumpPath: Path) {
        val dumpNpcIds = listOf(8163, 8182, 8183, 8184, 8185)

        dumpNpcIds.forEach {
            val npcDef = NPCDefinitions(
                it,
                cache2018.getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.NPC).findFileByID(it).data
            )
            npcDef.models =
                npcDef.models?.map { modelId -> mapModelId(modelId) }?.toIntArray()
            npcDef.walkAnimation = mapAnimationId(npcDef.walkAnimation)
            npcDef.standAnimation = mapAnimationId(npcDef.standAnimation)
            npcDef.rotate90Animation = mapAnimationId(npcDef.rotate90Animation)
            npcDef.rotate180Animation = mapAnimationId(npcDef.rotate180Animation)
            npcDef.rotate270Animation = mapAnimationId(npcDef.rotate270Animation)
            npcDef.rotateLeftAnimation = mapAnimationId(npcDef.rotateLeftAnimation)
            npcDef.rotateRightAnimation = mapAnimationId(npcDef.rotateRightAnimation)
            npcIdMap[it] = npcIdOnset++
            npcToEncode += npcDef
        }
        dumpObjectIds.forEach {
            val objDef = ObjectDefinitions.get(it)
            objDef.models =
                objDef.models?.map { modelId -> mapModelId(modelId) }?.toIntArray()
            objDef.animationId = mapAnimationId(objDef.animationId)
            objectIdMap[it] = objectIdOnset++
            objectToEncode += objDef
        }
        dumpItemIds.forEach {
            val itemDef = ItemDefinitions.get(it)
            itemDef.inventoryModelId = mapModelId(itemDef.inventoryModelId)
            itemDef.primaryMaleModel = mapModelId(itemDef.primaryMaleModel)
            itemDef.primaryFemaleModel = mapModelId(itemDef.primaryFemaleModel)
            itemIdMap[it] = itemIdOnset++
            itemToEncode += itemDef
        }
        dumpSpotAnimIds.forEach {
            val spotAnimDef = SpotAnimationDefinition.get(it)
            if (spotAnimDef == null) {
                println("SpotAnim $it not found")
                return@forEach
            }
            modelIdsToDump += spotAnimDef.modelId
            spotAnimDef.modelId = mapModelId(spotAnimDef.modelId)
            spotAnimDef.animationId = mapAnimationId(spotAnimDef.animationId)
            spotAnimIdMap[it] = spotAnimIdOnset++
            spotAnimToEncode += spotAnimDef
        }

        (7980..7990).forEach(::mapAnimationId)
        animationIdMap.forEach { (source, target) ->
            if (source <= 0)
                return@forEach
            val data = cache2018.getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.SEQUENCE)
                .findFileByID(source).data.apply {
                    position = 0
                }
            val animDef = AnimationDefinitions(source, data)
            if (animDef == null) {
                println("Animation $source not found")
                return@forEach
            }
//            animDef.soundEffects =
//                animDef.soundEffects?.map { soundIdMap.getOrPut(it) { soundIdOnset++ } }?.toIntArray()
            animDef.frameIds = mapFrames(
                cache2018,
                animDef.frameIds
            )
            animDef.extraFrameIds = mapFrames(
                cache2018,
                animDef.extraFrameIds
            )
            animationToEncode += animDef
        }

        itemToEncode.forEach {
            write(dumpPath, it, "items/${itemIdMap[it.id]}.dat")
        }
        npcToEncode.forEach {
            write(dumpPath, it, "npcs/${npcIdMap[it.id]}.dat")
        }

        val baseFlyUp = animationToEncode.find { it.id == 7983 }
        val flyDown = baseFlyUp!!.clone().apply {
            id = animationIdOnset++
            frameIds = baseFlyUp.frameIds.reversedArray()
            frameLengths = baseFlyUp.frameLengths.reversedArray()
            extraFrameIds = baseFlyUp.extraFrameIds?.reversedArray()
        }
        animationToEncode += flyDown
        animationToEncode.forEach {
            write(dumpPath, it, "animations/${animationIdMap[it.id] ?: it.id}.dat")
        }


        objectToEncode.forEach {
            write(dumpPath, it, "objects/${objectIdMap[it.id]}.dat")
        }
        spotAnimToEncode.forEach {
            write(dumpPath, it, "spotanims/${spotAnimIdMap[it.id]}.dat")
        }
        modelIdsToDump.forEach {
            val modelData = cache2018.getArchive(ArchiveType.MODELS).findGroupByID(it).findFileByID(0).data.buffer
            write(dumpPath, modelData, "models/$it.dat")
        }
        soundIdMap.forEach { (source, target) ->
            val soundData = cache2018.getArchive(ArchiveType.SYNTHS).findGroupByID(source).findFileByID(0).data.buffer
            write(dumpPath, soundData, "sounds/$target.dat")
        }
        frameArchiveIdMap.forEach { (source, target) ->
            cache2018.getArchive(ArchiveType.FRAMES).findGroupByID(source)?.files?.forEach {
                val data = it.data.buffer
                val originalSkeletonId = data[0].toInt() and 0xff shl 8 or (data[1].toInt() and 0xff)
                val newSkeletonId = skeletonIdMap[originalSkeletonId]!!
                data[0] = (newSkeletonId shr 8).toByte()
                data[1] = (newSkeletonId and 0xff).toByte()
                write(dumpPath, data, "frames/$target/${it.id}.dat")
            } ?: println("Frame archive $source not found")
        }
        skeletonIdMap.forEach { (source, target) ->
            cache2018.getArchive(ArchiveType.BASES).findGroupByID(source)?.files?.forEach {
                write(dumpPath, it.data.buffer, "bases/$target/${it.id}.dat")
            } ?: println("Skeleton $source not found")
        }
        modelIdMap.forEach { (source, target) ->
            cache2018.getArchive(ArchiveType.MODELS).findGroupByID(source)?.let {
                write(dumpPath, it.findFileByID(0).data.buffer, "models/$target.dat")
            } ?: println("Model $source not found")
        }
        itemIdMap.forEach { (source, target) ->
            println("Item $source -> $target")
        }
        npcIdMap.forEach { (source, target) ->
            println("NPC $source -> $target")
        }
        objectIdMap.forEach { (source, target) ->
            println("Object $source -> $target")
        }
        animationIdMap.forEach { (source, target) ->
            println("Animation $source -> $target")
        }
        println("Fly Down Animation -> ${flyDown.id}")
        spotAnimIdMap.forEach { (source, target) ->
            println("SpotAnim $source -> $target")
        }
        soundIdMap.forEach { (source, target) ->
            println("Sound $source -> $target")
        }
    }

    private fun mapModelId(sourceModelId: Int) = if (sourceModelId != -1)
        modelIdMap.getOrPut(sourceModelId) { modelIdOnset++ }
    else
        -1

    private fun mapAnimationId(sourceAnimationId: Int) = if (sourceAnimationId != -1)
        animationIdMap.getOrPut(sourceAnimationId) { animationIdOnset++ }
    else
        -1

    private fun write(
        dumpPath: Path,
        it: Definitions,
        name: String,
    ) {
        write(dumpPath, it.encode().buffer, name)
    }


    private fun write(
        dumpPath: Path,
        it: ByteArray,
        name: String,
    ) {
        dumpPath.resolve(name).apply {
            if (!parent.exists())
                parent.toFile().mkdirs()
            toFile().writeBytes(it)
        }
    }

    private fun mapFrames(
        cache2018: Cache,
        ints: IntArray?,
    ): IntArray? {
        return ints?.map {
            val (frameArchiveId, frameArchiveFileId) = updateFileAndFrameIdLabel(it)
            val frameData =
                cache2018.getArchive(ArchiveType.FRAMES).findGroupByID(frameArchiveId).findFileByID(frameArchiveFileId).data.buffer
            if (frameData != null) {
                val frameMapId = frameData[0].toInt() and 0xff shl 8 or (frameData[1].toInt() and 0xff)
                val newFrameArchiveId = frameArchiveIdMap.getOrPut(frameArchiveId) { frameArchiveIdOnset++ }
                val newSkeletonId = skeletonIdMap.getOrPut(frameMapId) { frameMapIdOnset++ }

                newFrameArchiveId shl 16 or frameArchiveFileId
            } else
                throw Exception("Frame $it not found")
        }?.toIntArray()
    }
}

private fun updateFileAndFrameIdLabel(hash: Int): Pair<Int, Int> {
    val hexString = Integer.toHexString(hash)
    val fileId = getFileId(hexString)
    val frameId = getFrameId(hexString)
    return fileId to frameId
}
internal fun getFileId(hexString: String): Int =
    Integer.parseInt(hexString.substring(0, hexString.length - 4), 16)

internal fun getFrameId(hexString: String) =
    Integer.parseInt(hexString.substring(hexString.length - 4), 16)

/**
 * Updated loader based of RuneLite's [net.runelite.cache.definitions.loaders.SequenceLoader].
 *
 * @author Stan van der Bend
 */
class SequenceLoader206 {

    fun load(id: Int, b: ByteArray): SequenceDefinition206 {
        val def = SequenceDefinition206(id.toString())
        val `is` = InputStream(b)
        while (true) {
            val opcode = `is`.readUnsignedByte()
            if (opcode == 0) {
                break
            }
            def.decodeValues(opcode, `is`)
        }
        return def
    }

    private fun SequenceDefinition206.decodeValues(opcode: Int, stream: InputStream) {
        val length: Int
        var i: Int
        if (id == "7981") {
            println()
        }
        when (opcode) {
            1 -> {
                length = stream.readUnsignedShort()
                frameLenghts = IntArray(length) {
                    stream.readUnsignedShort()
                }
                frameIDs = IntArray(length) {
                    stream.readUnsignedShort()
                }
                i = 0
                while (i < length) {
                    frameIDs!![i] += stream.readUnsignedShort() shl 16
                    ++i
                }
            }

            2 -> frameStep = stream.readUnsignedShort()
            3 -> {
                length = stream.readUnsignedByte()
                interleaveLeave = IntArray(1 + length) {
                    if (it == length)
                        9999999
                    else
                        stream.readUnsignedByte()
                }
            }

            4 -> stretches = true
            5 -> forcedPriority = stream.readUnsignedByte()
            6 -> leftHandItem = stream.readUnsignedShort()
            7 -> rightHandItem = stream.readUnsignedShort()
            8 -> maxLoops = stream.readUnsignedByte()
            9 -> precedenceAnimating = stream.readUnsignedByte()
            10 -> priority = stream.readUnsignedByte()
            11 -> replyMode = stream.readUnsignedByte()
            12 -> {
                length = stream.readUnsignedByte()
                chatFrameIds = IntArray(length) {
                    stream.readUnsignedShort()
                }
                i = 0
                while (i < length) {
                    chatFrameIds!![i] += stream.readUnsignedShort() shl 16
                    ++i
                }
            }
            13 -> frameSounds = IntArray(stream.readUnsignedByte()) {
                stream.read24BitInt()
            }
            14 -> {
                cachedModelId = stream.readInt()
            }
            15 -> {
                repeat(stream.readUnsignedShort()) {
                    stream.readUnsignedShort()
                    stream.read24BitInt()
                }
            }
            16 -> {
                stream.readUnsignedShort()
                stream.readUnsignedShort()
            }
            17 -> {
                repeat(stream.readUnsignedByte()) {
                    stream.readUnsignedByte()
                }
            }
        }
    }
}


class SequenceDefinition206(override val id: String) : AnimationDefinition {

    var frameIDs : IntArray? = null
    var chatFrameIds: IntArray? = null
    var frameLenghts: IntArray? = null
    var frameSounds: IntArray? = null
    var frameStep = -1
    var interleaveLeave: IntArray? = null
    var stretches = false
    var forcedPriority = 5
    var maxLoops = 99
    var precedenceAnimating = -1
    var priority = -1
    var replyMode = 2
    var cachedModelId = -1

    override val frameHashes: IntArray get() = frameIDs!!
    override val frameLengths: IntArray get() = frameLenghts!!
    override val loopOffset: Int get() = frameStep
    override var leftHandItem = -1
    override var rightHandItem = -1

}
interface AnimationDefinition {

    val id: String
    val frameHashes: IntArray
    val frameLengths : IntArray
    val loopOffset: Int
    val leftHandItem: Int
    val rightHandItem: Int
}
