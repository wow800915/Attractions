package com.weiyou.attractions.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weiyou.attractions.R

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<HomeItem>()

    fun setItems(newItems: List<HomeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].homeType
    }

    class AttractionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val nameTextView: TextView = view.findViewById(R.id.tvAttractionName)
//        val addressTextView: TextView = view.findViewById(R.id.tvAttractionAddress)
    }

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
//        val descriptionTextView: TextView = view.findViewById(R.id.tvNewsDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            2 -> {
                val view = inflater.inflate(R.layout.item_home_news, parent, false)
                NewsViewHolder(view)
            }

            4 -> {
                val view = inflater.inflate(R.layout.item_home_attraction, parent, false)
                AttractionViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AttractionViewHolder -> {
//                val item = items[position] as HomeAttraction
//                holder.nameTextView.text = item.attraction.name
//                holder.addressTextView.text = item.attraction.address
            }

            is NewsViewHolder -> {
                val item = items[position] as HomeNewsItem
                holder.tvTitle.text = item.newsItem.title

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}