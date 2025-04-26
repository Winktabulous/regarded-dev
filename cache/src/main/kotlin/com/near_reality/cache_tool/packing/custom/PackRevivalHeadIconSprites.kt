package com.near_reality.cache_tool.packing.custom

import mgi.tools.jagcached.cache.Cache
import mgi.types.draw.sprite.SpriteGroupDefinitions
import java.io.File
import javax.imageio.ImageIO

class PackRevivalHeadIconSprites(val cache: Cache) {

    fun patch() {
        // pack into pk head icons
        SpriteGroupDefinitions.get(439).apply {
            repeat(10) {
                val sprite = ImageIO.read(File("assets/osnr/custom_sprites/revival_state_${it+1}.png"))
                setImage(21 + it, sprite)
            }
            encode()
            pack()
        }
    }
}
