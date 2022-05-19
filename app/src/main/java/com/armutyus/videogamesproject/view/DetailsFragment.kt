package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.databinding.DetailsFragmentBinding
import com.armutyus.videogamesproject.util.Constants
import com.armutyus.videogamesproject.viewmodel.DetailsViewModel
import com.bumptech.glide.RequestManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class DetailsFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.details_fragment) {

    private var fragmentBinding: DetailsFragmentBinding? = null
    private lateinit var detailsViewModel: DetailsViewModel
    private val gameDetailsItem = Constants.gameItem
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "GameDetails")
        }

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
                    firebaseAnalytics = Firebase.analytics
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(FirebaseAnalytics.Param.ITEM_NAME, "FavoriteSelected")
                    }
                } else {
                    this.setImageResource(R.drawable.ic_favorite_false)
                }
                detailsViewModel.updateGames(gameDetailsItem)
            }


        }

    }

    private fun showDetails() {

        val gameImage = fragmentBinding?.detailsGameImage
        glide.load(gameDetailsItem!!.background_image).centerCrop().into(gameImage!!)
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