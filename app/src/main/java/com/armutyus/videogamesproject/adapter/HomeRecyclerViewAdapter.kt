package com.armutyus.videogamesproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.model.VideoGames
import com.armutyus.videogamesproject.roomdb.Games
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class HomeRecyclerViewAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeRecyclerViewHolder>() {

    class HomeRecyclerViewHolder(view: View): RecyclerView.ViewHolder(view)

    private val diffUtil = object : DiffUtil.ItemCallback<Games>() {
        override fun areItemsTheSame(oldItem: Games, newItem: Games): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Games, newItem: Games): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var videoGamesList: List<Games>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_row, parent, false)
        return HomeRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        val videoGamesImage = holder.itemView.findViewById<ImageView>(R.id.homeImage)
        val videoGamesText = holder.itemView.findViewById<TextView>(R.id.homeGameText)
        val videoGamesRatingText = holder.itemView.findViewById<TextView>(R.id.homeRatingText)
        val videoGamesReleasedText = holder.itemView.findViewById<TextView>(R.id.homeReleasedText)
        val videoGames = videoGamesList[position]
        holder.itemView.apply {
            videoGamesText.text = videoGames.name
            videoGamesRatingText.text = videoGames.rating.toString()
            videoGamesReleasedText.text = videoGames.released
            glide.load(videoGames.background_image).circleCrop().into(videoGamesImage)
        }
    }

    override fun getItemCount(): Int {
        return videoGamesList.size
    }
}