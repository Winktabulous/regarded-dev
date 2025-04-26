package com.near_reality.api.dao

import com.near_reality.api.model.NewsArticle
import com.near_reality.api.model.NewsCategory
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime


object NewsArticles : LongIdTable("news_articles") {
    val title = varchar("title", 255).index()
    val author = username("author").index()
    val content = text("content")
    val publishedAt = datetime("published_at").index()
    val category = enumeration<NewsCategory>("category").index()
}

class NewsArticleEntity(id: EntityID<Long>) : ModelEntity<NewsArticle>(id) {
    companion object : LongEntityClass<NewsArticleEntity>(NewsArticles) {
        fun new(article: NewsArticle): NewsArticleEntity {
            return new {
                setFrom(article)
            }
        }
        fun edit(id: Int, article: NewsArticle) = findById(id.toLong())?.apply {
            setFrom(article)
        }
    }

    var title by NewsArticles.title
    var author by NewsArticles.author
    var content by NewsArticles.content
    var publishedAt by NewsArticles.publishedAt
    var category by NewsArticles.category

    fun setFrom(article: NewsArticle) {
        title = article.title
        author = article.author
        content = article.content
        publishedAt = article.date
        category = article.category
    }
    override fun toModel(): NewsArticle = NewsArticle(
        id = id.value.toInt(),
        title = title,
        author = author,
        content = content,
        date = publishedAt,
        category = category
    )
}
