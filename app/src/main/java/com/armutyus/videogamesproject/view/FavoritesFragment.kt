package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.adapter.FavoritesRecyclerViewAdapter
import com.armutyus.videogamesproject.databinding.FragmentFavoritesBinding
import com.armutyus.videogamesproject.viewmodel.FavoritesViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FavoritesFragment @Inject constructor(
    private val favoritesRecyclerViewAdapter: FavoritesRecyclerViewAdapter
) : Fragment(R.layout.fragment_favorites) {

    private var fragmentBinding: FragmentFavoritesBinding? = null
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Favorites")
        }

        val binding = FragmentFavoritesBinding.bind(view)
        fragmentBinding = binding

        favoritesViewModel = ViewModelProvider(requireActivity())[FavoritesViewModel::class.java]

        fragmentBinding?.favoritesRecyclerView?.adapter = favoritesRecyclerViewAdapter
        fragmentBinding?.favoritesRecyclerView?.layoutManager =
            LinearLayoutManager(requireContext())

        favoriteListFromRoom()

    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.getFavoriteGamesList()
    }

    private fun favoriteListFromRoom() {
        favoritesViewModel.videoGamesFavoritesList.observe(viewLifecycleOwner) {

            if (favoritesViewModel.videoGamesFavoritesList.value?.size == 0) {
                fragmentBinding?.linearLayoutError?.visibility = View.VISIBLE
            } else {
                fragmentBinding?.linearLayoutError?.visibility = View.GONE
            }

            val favoritesFromRoom = it?.toList()
            favoritesRecyclerViewAdapter.favoriteVideoGamesList = favoritesFromRoom!!
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

}