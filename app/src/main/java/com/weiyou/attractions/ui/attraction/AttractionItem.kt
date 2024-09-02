package com.weiyou.attractions.ui.attraction

import com.weiyou.attractions.data.models.api.attractions.Attraction

sealed class AttractionItem {
    abstract val attractionType: Int

    companion object {
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_INFO = 2
    }
}

data class AttractionImage(
    val url: String
) : AttractionItem() {
    override val attractionType: Int = VIEW_TYPE_IMAGE
}

data class AttractionInfo(
    val attraction: Attraction
) : AttractionItem() {
    override val attractionType: Int = VIEW_TYPE_INFO
}
