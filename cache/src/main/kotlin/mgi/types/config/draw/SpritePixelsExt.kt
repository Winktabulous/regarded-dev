package mgi.types.config.draw

import mgi.types.draw.sprite.SpritePixels
import java.awt.image.BufferedImage

fun SpritePixels.toBufferedImage() = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB).apply {
    val transPixels =  IntArray(pixels.size)
    for (i in pixels.indices) {
        if (pixels[i] != 0) {
            transPixels[i] = pixels[i] or -0x1000000
        }
    }
    setRGB(0, 0, width, height, transPixels, 0 , width)
}
