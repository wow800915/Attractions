package com.weiyou.attractions.ui.home

import com.weiyou.attractions.data.models.api.attractions.Attraction
import com.weiyou.attractions.data.models.api.news.NewsItem

sealed class HomeItem {
    abstract val homeType: Int

    companion object {
        const val VIEW_TYPE_TITLE = 1
        const val VIEW_TYPE_NEWS_ITEM = 2
        const val VIEW_TYPE_ATTRACTION = 3
    }
}

data class HomeTitle(
    val title: String
) : HomeItem() {
    override val homeType: Int = VIEW_TYPE_TITLE
}

data class HomeNewsItem(
    val newsItem: NewsItem
) : HomeItem() {
    override val homeType: Int = VIEW_TYPE_NEWS_ITEM
}

data class HomeAttraction(
    val attraction: Attraction
) : HomeItem() {
    override val homeType: Int = VIEW_TYPE_ATTRACTION
}
