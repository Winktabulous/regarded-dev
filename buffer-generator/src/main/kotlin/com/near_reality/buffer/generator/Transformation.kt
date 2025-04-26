package com.near_reality.buffer.generator

enum class Transformation(
    val suffix: String,
    val preTransform: String,
    val postReadTransform: String,
    val postWriteTransform: String
) {
    IDENTITY("", "", "", ""),
    A("A", "", " - 128", " + 128"),
    C("C", "-", "", ""),
    S("S", "128 - ", "", "")
}
