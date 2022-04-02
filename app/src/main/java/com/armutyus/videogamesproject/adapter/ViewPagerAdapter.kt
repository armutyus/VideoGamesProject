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
import com.armutyus.videogamesproject.model.VideoGames
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Constants.gameItem
import com.armutyus.videogamesproject.view.HomeFragmentDirections
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ViewPagerAdapter @Inject constructor(
    private val glide: RequestManager
): RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(view: View): RecyclerView.ViewHolder(view)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.viewpager_view, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val videoGamesImage = holder.itemView.findViewById<ImageView>(R.id.viewPagerGameImage)
        val videoGamesText = holder.itemView.findViewById<TextView>(R.id.viewPagerGameTitleText)
        val videoGames = videoGamesList[position]
        holder.itemView.apply {
            videoGamesText.text = videoGames.name
            glide.load(videoGames.background_image).centerCrop().into(videoGamesImage)
        }

        holder.itemView.setOnClickListener {
            gameItem = videoGames
            val action = HomeFragmentDirections.actionNavigationHomeToDetailsFragment(videoGames.id!!)
            Navigation.findNavController(it).navigate(action)

        }
    }

    override fun getItemCount(): Int {
        return videoGamesList.size
    }
}