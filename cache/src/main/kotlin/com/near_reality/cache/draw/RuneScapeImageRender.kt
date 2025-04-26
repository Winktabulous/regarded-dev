package com.near_reality.cache.draw

import com.zenyte.CacheManager
import com.zenyte.game.item.ItemId
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.draw.font.Fonts
import mgi.types.config.draw.hitsplat.HitSplatDefinition
import mgi.types.config.draw.toBufferedImage
import mgi.types.config.draw.toFile
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.items.item
import mgi.types.config.items.loadSpritePixels
import mgi.types.config.npcs.NPCDefinitions
import mgi.types.draw.Rasterizer3D
import mgi.types.draw.model.TextureProvider
import java.nio.file.Paths

fun main() {
    val cache = CacheManager.loadCache(Cache.openCache("cache/data/cache"))

    ItemDefinitions().load(cache)

    NPCDefinitions().load(cache)

    Fonts.loadNamed(cache)

    HitSplatDefinition.load(cache)

    val textureProvider = TextureProvider(cache, 20, 0.6, 128)
    Rasterizer3D.Rasterizer3D_setTextureLoader(textureProvider)
    Rasterizer3D.Rasterizer3D_setBrightness(0.6)

//    val canvas = RuneScapeCanvas(256, 256)


    cache.item(ItemId.FIRE_CAPE).loadSpritePixels(amount = 100_000).toBufferedImage().toFile(Paths.get("/Users/stanvanderbend/IdeaProjects/near-reality/cache_dumps/item/sprites", "item.png"))

//    ItemDefinitions.getDefinitions().filter {
//        it != null && !it.isPlaceholder
//    }.map {
//        it to cache.item(it.id).loadSpritePixels(scale = 2)
//    }.parallelStream().forEach { (item, sprites) ->
//        sprites.toBufferedImage().toFile(Paths.get("/Users/stanvanderbend/IdeaProjects/near-reality/cache_dumps/item/sprites", "${item.id}.png"))
//    }//15252

//    HitSplatDefinition[40].loadSpritePixel().drawTransAt(0, 100, 256)
//    HitSplatDefinition[41].loadSpritePixel().drawTransAt(30, 100, 256)
//    HitSplatDefinition[42].loadSpritePixel().drawTransAt(60, 100, 256)
//    HitSplatDefinition[33].loadSpritePixel().drawTransAt(90, 100, 256)
//    HitSplatDefinition[7].loadSpritePixel().drawTransAt(120, 100, 256)
//    HitSplatDefinition[3].loadSpritePixel().drawTransAt(150, 100, 256)
//    HitSplatDefinition[1].loadSpritePixel().drawTransAt(180, 100, 256)

//    Fonts.plain11.draw("10", 0, 0, 0,0)

//    canvas.export(Paths.get("bla.png"))
//    val npc = NPCDefinitions.get(CustomNpcId.GANODERMIC_BEAST)

//    renderItems(cache)

}

