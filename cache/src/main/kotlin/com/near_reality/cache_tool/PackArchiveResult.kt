package com.near_reality.cache_tool

/**
 * @author Jire
 */
enum class PackArchiveResult(val message: String) {

    SUCCESS("succeeded"),
    FAILURE("failed"),
    CREATED_NEW("created new, run tool again to pack")

}