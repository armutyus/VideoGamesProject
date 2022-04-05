package com.armutyus.videogamesproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Constants
import com.armutyus.videogamesproject.view.FavoritesFragmentDirections
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class FavoritesRecyclerViewAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<FavoritesRecyclerViewAdapter.FavoritesRecyclerViewHolder>() {

    class FavoritesRecyclerViewHolder(view: View): RecyclerView.ViewHolder(view)

    private val diffUtil = object : DiffUtil.ItemCallback<Games>() {
        override fun areItemsTheSame(oldItem: Games, newItem: Games): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Games, newItem: Games): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var favoriteVideoGamesList: List<Games>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesRecyclerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favorites_row, parent, false)
        return FavoritesRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesRecyclerViewHolder, position: Int) {
        val videoGamesImage = holder.itemView.findViewById<ImageView>(R.id.favoritesImage)
        val videoGamesText = holder.itemView.findViewById<TextView>(R.id.favoritesGameText)
        val videoGamesRatingText = holder.itemView.findViewById<TextView>(R.id.favoritesRatingText)
        val videoGamesReleasedText = holder.itemView.findViewById<TextView>(R.id.favoritesReleasedText)
        val videoGames = favoriteVideoGamesList[position]
        holder.itemView.apply {
            videoGamesText.text = videoGames.name
            videoGamesRatingText.text = videoGames.rating.toString()
            videoGamesReleasedText.text = videoGames.released
            glide.load(videoGames.background_image).circleCrop().into(videoGamesImage)
        }

        holder.itemView.setOnClickListener {
            Constants.gameItem = videoGames
            val action = FavoritesFragmentDirections.actionNavigationFavoritesToDetailsFragment()
            Navigation.findNavController(it).navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return favoriteVideoGamesList.size
    }
}