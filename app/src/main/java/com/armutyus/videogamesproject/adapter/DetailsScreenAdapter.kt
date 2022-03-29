package com.armutyus.videogamesproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.armutyus.videogamesproject.R
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class DetailsScreenAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<DetailsScreenAdapter.DetailsScreenViewHolder>() {

    class DetailsScreenViewHolder(view: View): RecyclerView.ViewHolder(view)

    var gameDetails: ArrayList<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsScreenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.details_fragment,parent,false)
        return DetailsScreenViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsScreenViewHolder, position: Int) {
        val videoGamesImage = holder.itemView.findViewById<ImageView>(R.id.detailsGameImage)
        val videoGamesText = holder.itemView.findViewById<TextView>(R.id.detailsGameText)
        val videoGamesMetacriticText = holder.itemView.findViewById<TextView>(R.id.detailsGameMetacriticText)
        val videoGamesReleasedText = holder.itemView.findViewById<TextView>(R.id.detailsGameReleaseText)
        val videoGamesDescriptionText = holder.itemView.findViewById<TextView>(R.id.detailsGameDescriptionText)
        holder.itemView.apply {

        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}