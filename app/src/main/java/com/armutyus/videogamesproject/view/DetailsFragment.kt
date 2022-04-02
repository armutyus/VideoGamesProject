package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.databinding.DetailsFragmentBinding
import com.armutyus.videogamesproject.util.Constants.gameItem
import com.armutyus.videogamesproject.viewmodel.DetailsViewModel
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class DetailsFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.details_fragment) {

    private var fragmentBinding: DetailsFragmentBinding? = null
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DetailsFragmentBinding.bind(view)
        fragmentBinding = binding

        detailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]

        if (gameItem != null) {
            showDetails()
        }

    }

    private fun showDetails() {

        val gameImage = fragmentBinding?.detailsGameImage
        glide.load(gameItem!!.background_image).fitCenter().into(gameImage!!)
        fragmentBinding?.detailsGameText?.text = gameItem!!.name
        fragmentBinding?.detailsGameMetacriticText?.text = gameItem!!.metacritic.toString()
        fragmentBinding?.detailsGameReleaseText?.text = gameItem!!.released
        fragmentBinding?.detailsGameDescriptionText?.text = gameItem!!.description


    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }


}