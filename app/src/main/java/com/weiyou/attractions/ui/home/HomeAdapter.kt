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

    // 添加新項目的方法
    fun addItem(newItem: HomeItem) {
        items.add(newItem)
        notifyItemInserted(items.size - 1) // 通知 RecyclerView 有新項目插入
    }

    // 批量添加新项的方法
    fun addItems(newItems: List<HomeItem>) {
        val startIndex = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startIndex, newItems.size) // 通知 RecyclerView 有新项插入
    }

    fun setItems(newItems: List<HomeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
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
                    Glide.with(holder.itemView)
                        .load(item.attraction.images[0].src)
                        .into(holder.ivAttraction)
                } else {
                    holder.ivAttraction.visibility = View.INVISIBLE
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