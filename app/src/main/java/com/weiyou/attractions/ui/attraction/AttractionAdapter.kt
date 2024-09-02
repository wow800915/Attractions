package com.weiyou.attractions.ui.attraction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weiyou.attractions.R
import com.weiyou.attractions.ui.attraction.AttractionItem.Companion.VIEW_TYPE_IMAGE
import com.weiyou.attractions.ui.attraction.AttractionItem.Companion.VIEW_TYPE_INFO

class AttractionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<AttractionItem>()

    // Mutable list to hold image URLs

    fun setItems(attractionItems: List<AttractionItem>) {
        items.clear()
        items.addAll(attractionItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].attractionType
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.iv_image)
    }

    class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUrl: TextView = view.findViewById(R.id.tv_url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val view = inflater.inflate(R.layout.item_attraction_image, parent, false)
                ImageViewHolder(view)
            }

            VIEW_TYPE_INFO -> {
                val view = inflater.inflate(R.layout.item_attraction_info, parent, false)
                InfoViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                val item = items[position] as AttractionImage
                Glide.with(holder.ivImage.context)
                    .load(item.url)
                    .into(holder.ivImage)
            }

            is InfoViewHolder -> {
                val item = items[position] as AttractionInfo
                holder.tvUrl.text = item.attraction.url
            }

        }
    }

    override fun getItemCount(): Int {
        // Return the size of the dataset
        return items.size
    }
}