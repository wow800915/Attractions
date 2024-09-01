package com.weiyou.attractions.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weiyou.attractions.R
import com.weiyou.attractions.ui.home.HomeItem.Companion.VIEW_TYPE_ATTRACTION
import com.weiyou.attractions.ui.home.HomeItem.Companion.VIEW_TYPE_NEWS_ITEM
import com.weiyou.attractions.ui.home.HomeItem.Companion.VIEW_TYPE_TITLE

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<HomeItem>()

    private var newsItemClickListener: OnNewsItemClickListener? = null

    fun setItems(newItems: List<HomeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnNewsItemClickListener(listener: OnNewsItemClickListener) {
        this.newsItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].homeType
    }

    class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
    }

    class AttractionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val ivAttraction: ImageView = view.findViewById(R.id.iv_attraction)
    }

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
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
                holder.ivAttraction

                if (!item.attraction.images.isNullOrEmpty() && item.attraction.images[0].src != null) {
                    Glide.with(holder.itemView)
                        .load(item.attraction.images[0].src)
                        .into(holder.ivAttraction)
                } else {
                    holder.ivAttraction.visibility = View.INVISIBLE
                }
            }

            is NewsViewHolder -> {
                val item = items[position] as HomeNewsItem
                holder.tvTitle.text = item.newsItem.title
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
}