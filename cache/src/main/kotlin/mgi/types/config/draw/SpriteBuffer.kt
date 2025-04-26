package mgi.types.config.draw

import mgi.utilities.Buffer

object SpriteBuffer {
    lateinit var SpriteBuffer_spriteWidths: IntArray
    lateinit var SpriteBuffer_xOffsets: IntArray
    lateinit var SpriteBuffer_spritePalette: IntArray
    lateinit var SpriteBuffer_pixels: Array<ByteArray?>
    lateinit var SpriteBuffer_spriteHeights: IntArray
    @JvmField
    var SpriteBuffer_spriteCount: Int  = 0
    @JvmField
    var SpriteBuffer_spriteHeight: Int = 0
    @JvmField
    var SpriteBuffer_spriteWidth: Int = 0
    lateinit var SpriteBuffer_yOffsets: IntArray



    @JvmStatic
    fun SpriteBuffer_decode(var0: ByteArray) {
        val var1: Buffer = Buffer(var0)
        var1.offset = var0.size - 2
        SpriteBuffer_spriteCount = var1.readUnsignedShort()
        SpriteBuffer_xOffsets = IntArray(SpriteBuffer_spriteCount)
        SpriteBuffer_yOffsets = IntArray(SpriteBuffer_spriteCount)
        SpriteBuffer_spriteWidths = IntArray(SpriteBuffer_spriteCount)
        SpriteBuffer_spriteHeights = IntArray(SpriteBuffer_spriteCount)
        SpriteBuffer_pixels = arrayOfNulls<ByteArray>(SpriteBuffer_spriteCount)
        var1.offset = var0.size - 7 - SpriteBuffer_spriteCount * 8
        SpriteBuffer_spriteWidth = var1.readUnsignedShort()
        SpriteBuffer_spriteHeight = var1.readUnsignedShort()
        val var2: Int = (var1.readUnsignedByte() and 255) + 1
        var var3 = 0
        while (var3 < SpriteBuffer_spriteCount) {
            SpriteBuffer_xOffsets[var3] = var1.readUnsignedShort()
            ++var3
        }

        var3 = 0
        while (var3 < SpriteBuffer_spriteCount) {
            SpriteBuffer_yOffsets[var3] = var1.readUnsignedShort()
            ++var3
        }

        var3 = 0
        while (var3 < SpriteBuffer_spriteCount) {
            SpriteBuffer_spriteWidths[var3] = var1.readUnsignedShort()
            ++var3
        }

        var3 = 0
        while (var3 < SpriteBuffer_spriteCount) {
            SpriteBuffer_spriteHeights[var3] = var1.readUnsignedShort()
            ++var3
        }

        var1.offset = var0.size - 7 - SpriteBuffer_spriteCount * 8 - (var2 - 1) * 3
        SpriteBuffer_spritePalette = IntArray(var2)

        var3 = 1
        while (var3 < var2) {
            SpriteBuffer_spritePalette[var3] = var1.readMedium()
            if (SpriteBuffer_spritePalette[var3] === 0) {
                SpriteBuffer_spritePalette[var3] = 1
            }
            ++var3
        }

        var1.offset = 0

        var3 = 0
        while (var3 < SpriteBuffer_spriteCount) {
            val var4: Int = SpriteBuffer_spriteWidths[var3]
            val var5: Int = SpriteBuffer_spriteHeights[var3]
            val var6 = var4 * var5
            val var7 = ByteArray(var6)
            SpriteBuffer_pixels[var3] = var7
            val var8: Int = var1.readUnsignedByte()
            var var9: Int
            if (var8 == 0) {
                var9 = 0
                while (var9 < var6) {
                    var7[var9] = var1.readByte()
                    ++var9
                }
            } else if (var8 == 1) {
                var9 = 0
                while (var9 < var4) {
                    for (var10 in 0 until var5) {
                        var7[var9 + var10 * var4] = var1.readByte()
                    }
                    ++var9
                }
            }
            ++var3
        }
    }
}
