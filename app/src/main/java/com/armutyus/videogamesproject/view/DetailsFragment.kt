package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.databinding.DetailsFragmentBinding
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Status
import com.armutyus.videogamesproject.viewmodel.DetailsViewModel
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class DetailsFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.details_fragment) {

    private var fragmentBinding: DetailsFragmentBinding? = null
    private lateinit var detailsViewModel: DetailsViewModel
    private var gameID = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DetailsFragmentBinding.bind(view)
        fragmentBinding = binding

        detailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]

        arguments?.let {
            gameID = DetailsFragmentArgs.fromBundle(it).gameID
        }

        getDetailsFromApi()

    }

    override fun onResume() {
        super.onResume()
        detailsViewModel.gameDetailResponse(gameID)

    }

    private fun getDetailsFromApi() {
        detailsViewModel.gamesDetails.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {

                    val gameDetailImage = it.data!!.background_image
                    val gameDetailDescription = it.data.description
                    val gameDetailRating = it.data.rating
                    val gameDetailId = it.data.id
                    val gameDetailReleased = it.data.released
                    val gameDetailName = it.data.name
                    val gameDetailMetacritic = it.data.metacritic

                    detailsViewModel.updateGames(
                        Games(
                            gameDetailImage,
                            gameDetailName,
                            gameDetailRating,
                            gameDetailReleased,
                            gameDetailMetacritic,
                            gameDetailDescription,
                            false,
                            gameDetailId
                        )
                    )

                    detailsViewModel.getGamesDetailsById(gameID)
                    showDetails()

                }

                Status.ERROR -> {

                    fragmentBinding?.descriptionLayout?.visibility = View.GONE
                }

                Status.LOADING -> {

                    fragmentBinding?.descriptionLayout?.visibility = View.GONE

                }

            }
        }
    }

    private fun showDetails() {

        detailsViewModel.videoGamesDetailsById.observe(viewLifecycleOwner) {

            val gameImage = fragmentBinding?.detailsGameImage
            glide.load(it.background_image).fitCenter().into(gameImage!!)
            fragmentBinding?.detailsGameText?.text = it.name
            fragmentBinding?.detailsGameMetacriticText?.text = it.metacritic.toString()
            fragmentBinding?.detailsGameReleaseText?.text = it.released
            fragmentBinding?.detailsGameDescriptionText?.text = it.description

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }


}