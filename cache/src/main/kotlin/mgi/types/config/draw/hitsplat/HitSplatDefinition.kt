package mgi.types.config.draw.hitsplat

import com.near_reality.cache.configs
import com.near_reality.cache.file
import com.near_reality.cache.hitmarks
import com.near_reality.cache.sprites
import com.zenyte.CacheManager
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import mgi.tools.jagcached.cache.Cache
import mgi.types.draw.sprite.SpriteGroupDefinitions
import mgi.types.draw.sprite.SpritePixels
import mgi.utilities.Buffer

/**
 * Represents the definition for hit splashes.
 *
 * @author  Stan van der Bend
 */
class HitSplatDefinition{

    companion object {

        val definitions: Int2ObjectMap<HitSplatDefinition> = Int2ObjectOpenHashMap()

        fun load(cache: Cache = CacheManager.getCache()) {
            definitions.clear()
            cache.configs.hitmarks.apply {
                files.forEach {
                    definitions[it.id] = HitSplatDefinition().apply {
                        decode(Buffer(it.data.buffer))
                    }
                }
            }
        }

        operator fun get(id: Int): HitSplatDefinition = definitions[id]
    }

    var fontId = -1
    var textColor = 16777215
    var field2071 = 70
    var field2062 = -1
    var field2067 = -1
    var field2064 = -1
    var field2054 = -1
    var field2055 = 0
    var field2074 = 0
    var field2072 = -1
    var field2069 = ""
    var field2070 = -1
    var field2068 = 0
    var transforms: IntArray? = null
    var transformVarbit = -1
    var transformVarp = -1

    fun loadSpritePixel(cache: Cache = CacheManager.getCache()): SpritePixels =
        SpriteGroupDefinitions().let { sprite ->
            sprite.decode(cache.sprites.file(field2067).data)
            SpritePixels().apply {
                width = sprite.width
                height = sprite.height
                xOffset = sprite.offsetsX[0]
                yOffset = sprite.offsetsY[0]
                subWidth = sprite.subWidths[0]
                subHeight = sprite.subHeights[0]
                pixels = IntArray(subWidth * subHeight) {
                    sprite.palette[sprite.subPixels[0][it].toInt() and 255]
                }
            }
        }

//    override fun load() = load(CacheManager.getCache())

    fun decode(buffer: Buffer) {
        while (true) {
            val opcode = buffer.readUnsignedByte()
            if (opcode == 0)
                return
            decode(buffer, opcode)
        }
    }

    fun decode(buffer: Buffer, opcode: Int) {
        when (opcode) {
            1 -> fontId = buffer.readIntSmart()
            2 -> textColor = buffer.readMedium()
            3 -> field2062 = buffer.readIntSmart()
            4 -> field2064 = buffer.readIntSmart()
            5 -> field2067 = buffer.readIntSmart()
            6 -> field2054 = buffer.readIntSmart()
            7 -> field2055 = buffer.readShort()
            8 -> field2069 = buffer.readStringCp1252NullCircumfixed()
            9 -> field2071 = buffer.readUnsignedShort()
            10 -> field2074 = buffer.readShort()
            11 -> field2072 = 0
            12 -> field2070 = buffer.readUnsignedByte()
            13 -> field2068 = buffer.readShort()
            14 -> field2072 = buffer.readUnsignedShort()
            17, 18 -> { // L: 84
                transformVarbit = buffer.readUnsignedShort() // L: 85
                if (transformVarbit == 65535) { // L: 86
                    transformVarbit = -1
                }
                transformVarp = buffer.readUnsignedShort() // L: 87
                if (transformVarp == 65535) { // L: 88
                    transformVarp = -1
                }
                var var3 = -1 // L: 89
                if (opcode == 18) { // L: 90
                    var3 = buffer.readUnsignedShort() // L: 91
                    if (var3 == 65535) { // L: 92
                        var3 = -1
                    }
                }
                val var4 = buffer.readUnsignedByte() // L: 94
                val transforms = IntArray(var4 + 2) // L: 95
                for (var5 in 0..var4) { // L: 96
                    transforms[var5] = buffer.readUnsignedShort() // L: 97
                    if (transforms[var5] == 65535) { // L: 98
                        transforms[var5] = -1
                    }
                }
                transforms[var4 + 1] = var3 // L: 100
                this.transforms = transforms
            }
        }
    }

//    override fun encode(): ByteBuffer = TODO("missing encoding")
//
//    override fun pack() = TODO("Missing pack")
}
