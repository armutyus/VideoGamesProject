package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.databinding.DetailsFragmentBinding
import com.armutyus.videogamesproject.util.Constants
import com.armutyus.videogamesproject.util.Constants.gameItem
import com.armutyus.videogamesproject.viewmodel.DetailsViewModel
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class DetailsFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.details_fragment) {

    private var fragmentBinding: DetailsFragmentBinding? = null
    private lateinit var detailsViewModel: DetailsViewModel
    private val gameDetailsItem = Constants.gameItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DetailsFragmentBinding.bind(view)
        fragmentBinding = binding

        detailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]

        if (gameDetailsItem != null) {
            showDetails()
        }

        fragmentBinding?.favButton?.apply {
            if (gameDetailsItem!!.favorite) {
                this.setImageResource(R.drawable.ic_favorite_true)
            } else {
                this.setImageResource(R.drawable.ic_favorite_false)
            }

            this.setOnClickListener {
                gameDetailsItem.favorite = !gameDetailsItem.favorite
                if (gameDetailsItem.favorite) {
                    this.setImageResource(R.drawable.ic_favorite_true)
                } else {
                    this.setImageResource(R.drawable.ic_favorite_false)
                }
                detailsViewModel.updateGames(gameDetailsItem)
            }


        }

    }

    private fun showDetails() {

        val gameImage = fragmentBinding?.detailsGameImage
        glide.load(gameDetailsItem!!.background_image).fitCenter().into(gameImage!!)
        fragmentBinding?.detailsGameText?.text = gameDetailsItem.name
        fragmentBinding?.detailsGameMetacriticText?.text = gameDetailsItem.metacritic.toString()
        fragmentBinding?.detailsGameReleaseText?.text = gameDetailsItem.released
        fragmentBinding?.detailsGameDescriptionText?.text = gameDetailsItem.description


    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }


}