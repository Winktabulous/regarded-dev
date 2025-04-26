package mgi.types.config.draw.font

import com.zenyte.CacheManager
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.draw.SpriteBuffer.SpriteBuffer_decode
import mgi.types.config.draw.SpriteBuffer.SpriteBuffer_pixels
import mgi.types.config.draw.SpriteBuffer.SpriteBuffer_spriteHeights
import mgi.types.config.draw.SpriteBuffer.SpriteBuffer_spritePalette
import mgi.types.config.draw.SpriteBuffer.SpriteBuffer_spriteWidths
import mgi.types.config.draw.SpriteBuffer.SpriteBuffer_xOffsets
import mgi.types.config.draw.SpriteBuffer.SpriteBuffer_yOffsets
import mgi.types.draw.font.Font
import mgi.types.draw.font.FontName

object Fonts {

    private lateinit var fonts : Map<FontName, Font>

    val plain11 by lazyFont(FontName.FontName_plain11)
    val plain12 by lazyFont(FontName.FontName_plain11)
    val bold12 by lazyFont(FontName.FontName_plain11)
    val verdana11 by lazyFont(FontName.FontName_verdana11)
    val verdana13 by lazyFont(FontName.FontName_verdana13)
    val verdana15 by lazyFont(FontName.FontName_verdana15)

    fun loadNamed(cache: Cache = CacheManager.getCache()) {
        val sprites = cache.getArchive(ArchiveType.SPRITES)
        fonts = FontName.all().associateWith {
            val group = sprites.findGroupByName(it.name)
            val file = group.findFileByName("")
            load(cache, group.id, file.id)
        }
    }

    fun load(cache: Cache = CacheManager.getCache(), groupId: Int, fileId: Int): Font {
        val sprites = cache.getArchive(ArchiveType.SPRITES)
        val metrics = cache.getArchive(ArchiveType.FONTMETRICS)
        val spriteData = sprites.findGroupByID(groupId).findFileByID(fileId)
        SpriteBuffer_decode(spriteData.data.buffer)
        val fontData = metrics.findGroupByID(groupId).findFileByID(fileId)
        return Font(
            fontData.data.buffer,
            SpriteBuffer_xOffsets,
            SpriteBuffer_yOffsets,
            SpriteBuffer_spriteWidths,
            SpriteBuffer_spriteHeights,
            SpriteBuffer_spritePalette,
            SpriteBuffer_pixels
        )
    }

    fun hashString(var0: CharSequence): Int {
        val var1 = var0.length // L: 132
        var var2 = 0 // L: 133
        for (var3 in 0 until var1) { // L: 134
            var2 = (var2 shl 5) - var2 + Font.charToByteCp1252(var0[var3])
        }
        return var2 // L: 135
    }

    private fun lazyFont(fontName: FontName) = lazy {
        if (!this::fonts.isInitialized)
            error("font map is not yet initialized, first invoke Fonts.load()")
        fonts[fontName]!!
    }
}

