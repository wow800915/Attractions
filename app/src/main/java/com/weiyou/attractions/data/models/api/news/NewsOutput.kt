package com.weiyou.attractions.data.models.api.news

import com.weiyou.attractions.data.models.api.attractions.File
import com.weiyou.attractions.data.models.api.attractions.Link

data class NewsOutput(
    val total: Int,
    val data: List<NewsItem>
)

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val begin: String?,
    val end: String?,
    val posted: String,
    val modified: String,
    val url: String,
    val files: List<File>,
    val links: List<Link>
)

data class File(
    val src: String,
    val subject: String,
    val ext: String
)

data class Link(
    val src: String,
    val subject: String
)