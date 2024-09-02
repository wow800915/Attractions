package com.weiyou.attractions.ui.attraction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weiyou.attractions.R

class AttractionAdapter : RecyclerView.Adapter<AttractionAdapter.ImageViewHolder>() {

    // Mutable list to hold image URLs
    private var imageUrls: List<String> = emptyList()

    // ViewHolder class to hold references to each item's views
    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.iv_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        // Inflate the layout for each item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attraction_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Bind the data to each view
        val imageUrl = imageUrls[position]
        // Use Glide or any other image loading library to load the image
        Glide.with(holder.ivImage.context)
            .load(imageUrl)
            .into(holder.ivImage)
    }

    override fun getItemCount(): Int {
        // Return the size of the dataset
        return imageUrls.size
    }

    // Method to set the data for the adapter
    fun setImageUrls(imageUrls: List<String>) {
        this.imageUrls = imageUrls
        notifyDataSetChanged()
    }
}