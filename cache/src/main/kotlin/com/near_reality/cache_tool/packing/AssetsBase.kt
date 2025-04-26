package com.near_reality.cache_tool.packing

/**
 * @author Jire
 */
internal class AssetsBase(val baseFolder: String) {

    fun folder(subFolder: String) = "$baseFolder$subFolder"

    @JvmName("use")
    operator fun String.invoke(use: Asset.() -> Unit) =
        assets(folder(this), use)

    fun<T> asset(subAsset: String, use: Asset.() -> T) = Asset(folder(subAsset)).let(use)

    fun assetBytes(subAsset: String) = asset(subAsset) {bytes}

    fun base(subBase: String, use: AssetsBase.() -> Unit) =
        AssetsBase(folder(subBase)).apply(use)

}

internal fun assetsBase(
    baseFolder: String,
    use: AssetsBase.() -> Unit
) = AssetsBase(baseFolder).apply(use)
