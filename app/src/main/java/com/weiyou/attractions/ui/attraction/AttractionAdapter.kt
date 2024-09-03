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

    private var urlClickListener: OnUrlClickListener? = null

    fun setItems(attractionItems: List<AttractionItem>) {
        items.clear()
        items.addAll(attractionItems)
        notifyDataSetChanged()
    }

    fun setOnUrlListener(listener: OnUrlClickListener) {
        this.urlClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].attractionType
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.iv_image)
    }

    class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOpenTime: TextView = view.findViewById(R.id.tv_open_time)
        val tvAddress: TextView = view.findViewById(R.id.tv_address)
        val tvTel: TextView = view.findViewById(R.id.tv_tel)
        val tvUrl: TextView = view.findViewById(R.id.tv_url)
        val tvIntroduction: TextView = view.findViewById(R.id.tv_introduction)
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
                holder.tvOpenTime.text = holder.itemView.context.getString(
                    R.string.app_attraction_introduction_with_value,
                    item.attraction.open_time
                )
                holder.tvAddress.text = holder.itemView.context.getString(
                    R.string.app_attraction_address_with_value,
                    item.attraction.address
                )
                holder.tvTel.text = holder.itemView.context.getString(
                    R.string.app_attraction_tel_with_value,
                    item.attraction.tel
                )
                holder.tvUrl.text = holder.itemView.context.getString(
                    R.string.app_attraction_web_url_with_value,
                    item.attraction.official_site
                )
                holder.tvIntroduction.text = holder.itemView.context.getString(
                    R.string.app_attraction_introduction_with_value,
                    item.attraction.introduction
                )
                holder.tvUrl.setOnClickListener {
                    item.attraction.official_site?.let { it1 -> urlClickListener?.onUrlClick(it1) }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnUrlClickListener {
        fun onUrlClick(url: String)
    }
}