package com.near_reality.api.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NewsArticle(
    val id: Int? = null,
    val title: String,
    val author: String,
    val content: String,
    val date: LocalDateTime,
    val category: NewsCategory
)

@Serializable
enum class NewsCategory {
    GameUpdates,
    Community,
    StaffUpdates,
    DeveloperBlog
}
