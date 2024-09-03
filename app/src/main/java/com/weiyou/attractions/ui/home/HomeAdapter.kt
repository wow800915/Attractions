package com.weiyou.attractions.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weiyou.attractions.R
import com.weiyou.attractions.data.models.api.attractions.Attraction
import com.weiyou.attractions.ui.home.HomeItem.Companion.VIEW_TYPE_ATTRACTION
import com.weiyou.attractions.ui.home.HomeItem.Companion.VIEW_TYPE_NEWS_ITEM
import com.weiyou.attractions.ui.home.HomeItem.Companion.VIEW_TYPE_TITLE

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<HomeItem>()

    private var newsItemClickListener: OnNewsItemClickListener? = null
    private var attractionClickListener: OnAttractionClickListener? = null

    fun setItems(newItems: List<HomeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<HomeItem>) {
        val startIndex = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startIndex, newItems.size)
    }

    fun setOnNewsItemClickListener(listener: OnNewsItemClickListener) {
        this.newsItemClickListener = listener
    }

    fun setOnAttractionClickListener(listener: OnAttractionClickListener) {
        this.attractionClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].homeType
    }

    class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
    }

    class AttractionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvIntroduction: TextView = view.findViewById(R.id.tv_introduction)
        val ivAttraction: ImageView = view.findViewById(R.id.iv_attraction)
    }

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_TITLE -> {
                val view = inflater.inflate(R.layout.item_home_title, parent, false)
                TitleViewHolder(view)
            }

            VIEW_TYPE_NEWS_ITEM -> {
                val view = inflater.inflate(R.layout.item_home_news, parent, false)
                NewsViewHolder(view)
            }

            VIEW_TYPE_ATTRACTION -> {
                val view = inflater.inflate(R.layout.item_home_attraction, parent, false)
                AttractionViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> {
                val item = items[position] as HomeTitle
                holder.tvTitle.text = item.title
            }

            is AttractionViewHolder -> {
                val item = items[position] as HomeAttraction
                holder.tvTitle.text = item.attraction.name
                holder.tvIntroduction.text = item.attraction.introduction

                if (!item.attraction.images.isNullOrEmpty() && item.attraction.images[0].src != null) {
                    holder.ivAttraction.visibility = View.VISIBLE
                    Glide.with(holder.itemView)
                        .load(item.attraction.images[0].src)
                        .placeholder(android.R.drawable.stat_notify_sync)
                        .error(android.R.drawable.stat_notify_sync)
                        .thumbnail(0.1f)  // 使用 thumbnail 功能可以先加載一個縮略圖，提升用戶體驗。
                        .into(holder.ivAttraction)
                } else {
                    holder.ivAttraction.visibility = View.INVISIBLE
                    Glide.with(holder.itemView).clear(holder.ivAttraction)  // 清除舊的加載任務。
                }

                holder.itemView.setOnClickListener {
                    attractionClickListener?.onAttractionClick(item.attraction)
                }
            }

            is NewsViewHolder -> {
                val item = items[position] as HomeNewsItem
                holder.tvTitle.text = item.newsItem.title
                holder.tvDescription.text = item.newsItem.description
                holder.itemView.setOnClickListener {
                    newsItemClickListener?.onNewsItemClick(item.newsItem.url)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnNewsItemClickListener {
        fun onNewsItemClick(url: String)
    }

    interface OnAttractionClickListener {
        fun onAttractionClick(attraction: Attraction)
    }
}