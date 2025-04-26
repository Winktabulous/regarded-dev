package com.near_reality.scripts.shops

import com.near_reality.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object ShopScriptCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "com.zenyte.game.model.shop.*",

            "com.zenyte.game.model.shop.ShopPolicy",
            "com.zenyte.game.model.shop.ShopPolicy.*",

            "com.zenyte.game.model.shop.ShopCurrency",
            "com.zenyte.game.model.shop.ShopCurrency.*",

            "com.zenyte.game.item.ItemId",
            "com.zenyte.game.item.ItemId.*",

            "com.near_reality.game.content.universalshop.*",
            "com.near_reality.game.content.universalshop.UnivShopItem",
            "com.near_reality.game.content.universalshop.UnivShopItem.*"
        )
    }
)