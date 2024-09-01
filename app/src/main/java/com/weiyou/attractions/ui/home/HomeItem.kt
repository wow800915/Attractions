package com.weiyou.attractions.ui.home

import com.weiyou.attractions.data.models.api.attractions.Attraction
import com.weiyou.attractions.data.models.api.news.NewsItem

sealed class HomeItem {
    abstract val homeType: Int
}

data class HomeNewsTitleItem(
    val title: String
) : HomeItem() {
    override val homeType: Int = 1//TODO 這邊的數字改一下 改成常數
}

data class HomeNewsItem(
    val newsItem: NewsItem
) : HomeItem() {
    override val homeType: Int = 2
}

data class HomeAttractionsItem(
    val title: String
) : HomeItem() {
    override val homeType: Int = 3
}

data class HomeAttraction(
    val attraction: Attraction
) : HomeItem() {
    override val homeType: Int = 4
}
