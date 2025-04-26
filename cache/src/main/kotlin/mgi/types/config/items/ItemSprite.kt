package mgi.types.config.items

import com.zenyte.CacheManager
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.draw.font.Fonts
import mgi.types.draw.Rasterizer2D
import mgi.types.draw.Rasterizer3D
import mgi.types.draw.model.Model
import mgi.types.draw.sprite.SpritePixels

fun Cache.item(id: Int): ItemDefinitions {

    val item = ItemDefinitions.get(id)
    if (item.notedTemplate != -1) {
        val template = item(item.notedTemplate)
        val note = item(item.notedId)
        item.genCert(template, note)
    }
    return item
}
fun ItemDefinitions.genCert(var1: ItemDefinitions, var2: ItemDefinitions) {
    this.inventoryModelId = var1.inventoryModelId
    this.modelYaw = var1.modelYaw
    this.modelPitch = var1.modelPitch
    this.modelRoll = var1.modelRoll
    this.zoom = var1.zoom
    this.offsetX = var1.offsetX
    this.offsetY = var1.offsetY
    this.originalColours = var1.originalColours
    this.replacementColours = var1.replacementColours
    this.originalTextureIds = var1.originalTextureIds
    this.replacementTextureIds = var1.replacementTextureIds
    this.name = var2.name
    this.isMembers = var2.isMembers
    this.price = var2.price
    this.isStackable = 1
}
fun ItemDefinitions.loadSpritePixels(
    cache : Cache = CacheManager.getCache(),
    scale: Int = 1,
    amount: Int = 1,
    amountDrawType: Int = 2,
    outlineType: Int = 1,
    shadowType: Int = 3153952,
    zoomed: Boolean = false,
    model: Model = loadInventoryModel(cache),
): SpritePixels {
    val transformed = transform(amount)
    if (transformed != this)
        return transformed.loadSpritePixels(cache, scale, amount, amountDrawType, outlineType, shadowType, zoomed)
    val quantityMode = when {
        amount == -1 -> 0
        amountDrawType == 2 && amount != 1 -> 1
        else -> amountDrawType
    }
    val background = when {
        notedTemplate != -1 -> {
            cache.item(notedId).loadSpritePixels(cache, scale, 10, 0, 1, 0, true)
        }
        bindTemplateId != -1 -> {
            cache.item(bindId).loadSpritePixels(cache, scale, amount, 0, outlineType, shadowType, false)
        }
        placeholderTemplate != -1 -> {
            cache.item(placeholderId).loadSpritePixels(cache, scale, amount, 0, 0, 0, false)
        }
        else -> {
            null
        }
    }
    val prePixels = Rasterizer2D.Rasterizer2D_pixels
    val preWidth = Rasterizer2D.Rasterizer2D_width
    val preHeight = Rasterizer2D.Rasterizer2D_height

    val clipping = IntArray(4)
    Rasterizer2D.Rasterizer2D_getClipArray(clipping)
    val pixels = SpritePixels(scale * 36, scale * 32)
    Rasterizer2D.Rasterizer2D_replace(pixels.pixels, scale * 36, scale * 32)
    Rasterizer2D.Rasterizer2D_clear()
    Rasterizer3D.Rasterizer3D_setClipFromRasterizer2D()
    Rasterizer3D.setOffset(scale * 16, scale * 16)
    Rasterizer3D.rasterGouraudLowRes = false

    if (placeholderTemplate != -1)
        background?.drawTransBgAt(0, 0)

    var var16: Int = zoom / scale // L: 400

    if (zoomed) { // L: 401
        var16 = (var16.toDouble() * 1.5).toInt()
    } else if (outlineType == 2) { // L: 402
        var16 = (1.04 * var16.toDouble()).toInt()
    }

    val var17 = var16 * Rasterizer3D.Rasterizer3D_sine[modelPitch] shr 16 // L: 403

    val var18 = var16 * Rasterizer3D.Rasterizer3D_cosine[modelPitch] shr 16 // L: 404

    model.calculateBoundsCylinder() // L: 405

    model.method4272(
        0,
        modelRoll,
        modelYaw,
        modelPitch,
        offsetX,
        model.height / 2 + var17 + offsetY,
        var18 + offsetY
    )
    if (bindTemplateId != -1)
        background?.drawTransBgAt(0, 0)

    if (outlineType >= 1)
        pixels.outline(1)

    if (outlineType >= 2)
        pixels.outline(16777215)

    if (shadowType != 0)
        pixels.shadow(shadowType)

    Rasterizer2D.Rasterizer2D_replace(pixels.pixels, scale * 36, scale * 32) // L: 411

    if (notedTemplate != -1)
        background?.drawTransBgAt(0, 0)

    if (quantityMode == 1 || quantityMode == 2 && getIsStackable() == 1) { // L: 413
        Fonts.plain12.draw(formatAmountText(amount), 0, 9, 16776960, 1) // L: 414
    }
    Rasterizer2D.Rasterizer2D_replace(prePixels, preWidth, preHeight) // L: 417
    Rasterizer2D.Rasterizer2D_setClipArray(clipping) // L: 418
    Rasterizer3D.Rasterizer3D_setClipFromRasterizer2D() // L: 419
    Rasterizer3D.rasterGouraudLowRes = true // L: 420
    return pixels
}

fun formatAmountText(var0: Int): String {
    return when {
        var0 < 100000 -> "<col=ffff00>$var0</col>"
        var0 < 10000000 -> "<col=ffffff>${ var0 / 1000}K</col>"
        else -> "<col=00ff80>${var0 / 1000000}M</col>"
    }
}
