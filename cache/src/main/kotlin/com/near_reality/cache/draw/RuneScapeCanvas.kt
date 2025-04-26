package com.near_reality.cache.draw

import mgi.types.draw.Rasterizer2D
import mgi.types.draw.Rasterizer3D
import java.awt.color.ColorSpace
import java.awt.image.*
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.exists

class RuneScapeCanvas(val width: Int, val height: Int) {

    private val pixels = IntArray(height * width + 1) // L: 25
    private val image: BufferedImage

    init {
        Rasterizer3D.Rasterizer3D_setBrightness(0.6)
        val dataBufferInt = DataBufferInt(pixels, pixels.size)
        val directColorModel = DirectColorModel(
            ColorSpace.getInstance(ColorSpace.CS_sRGB),
            32, 0xff0000, 0xff00, 0xff, -0x1000000,
            true, DataBuffer.TYPE_INT
        )
        val writableRaster =
            Raster.createWritableRaster(directColorModel.createCompatibleSampleModel(width, height), dataBufferInt, null)
        image = BufferedImage(directColorModel, writableRaster, true, Hashtable<Any?, Any?>())
        apply()
    }

    fun apply() {
        Rasterizer2D.Rasterizer2D_replace(pixels, width, height) // L: 11
    }

    fun export(path: Path) {

        if (path.parent?.exists() == false)
            path.parent.toFile().mkdirs()

        val transPixels = IntArray(pixels.size)
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        for (i in pixels.indices) {
            if (pixels[i] != 0) {
                transPixels[i] = pixels[i] or -0x1000000
            }
        }

        img.setRGB(0, 0, width, height, transPixels, 0, width)

        ImageIO.write(img, "png", path.toFile())
    }
}
