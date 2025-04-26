package com.near_reality.cache_tool

import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.geom.Arc2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


fun main() {
    dump()
//    CacheManager.loadCache(cacheTo)
//    SpriteGroupDefinitions().load()
////    PackRevivalHeadIconSprites(cacheTo).patch()
////    cacheTo.close()
////    CacheManager.loadCache(cacheTo)
//    SpriteGroupDefinitions.get(439).images.forEach {
//        println(it)
//    }

}

private fun dump() {
    val SPRITE_SIZE = 25
    val TOTAL_STATES = 10

    for (i in 0 until TOTAL_STATES) {
        // Create a BufferedImage
        val sprite = BufferedImage(SPRITE_SIZE, SPRITE_SIZE, BufferedImage.TYPE_INT_ARGB)

        // Get the graphics object
        val g2d = sprite.createGraphics()

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Clear the image with transparency
        g2d.composite = AlphaComposite.Clear
        g2d.fillRect(0, 0, SPRITE_SIZE, SPRITE_SIZE)
        g2d.composite = AlphaComposite.Src

        // Draw a filled arc representing the revival state
        val angle = (360 * (i + 1) / TOTAL_STATES.toDouble()).toInt()
        g2d.color = Color.decode("#006000")
        g2d.fill(
            Arc2D.Double(
                0.0,
                0.0,
                SPRITE_SIZE.toDouble(),
                SPRITE_SIZE.toDouble(),
                90.0,
                -angle.toDouble(),
                Arc2D.PIE
            )
        )

        // Draw a circle outline
        g2d.color = Color.decode("#006000")
        g2d.drawOval(0, 0, SPRITE_SIZE - 1, SPRITE_SIZE - 1)

        // Draw the number inside the circle
        g2d.font = Font("Verdana", Font.PLAIN, 12)
        g2d.color = Color.WHITE
        val number = (TOTAL_STATES - i).toString()
        val metrics = g2d.fontMetrics
        val x = (SPRITE_SIZE - metrics.stringWidth(number)) / 2
        val y = ((SPRITE_SIZE - metrics.height) / 2) + metrics.ascent
        g2d.drawString(number, x, y)

        // Dispose of the graphics object
        g2d.dispose()

        // Save the image as a PNG file
        try {
            val outputFile: File = File("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/cache/assets/osnr/custom_sprites/revival_state_" + (i + 1) + ".png")
            ImageIO.write(sprite, "PNG", outputFile)
        } catch (e: IOException) {
            System.err.println("Error writing image: " + e.message)
        }
    }

    println("Revival sprites generated successfully.")
}
