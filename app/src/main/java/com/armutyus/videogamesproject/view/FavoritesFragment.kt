package com.armutyus.videogamesproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.adapter.FavoritesRecyclerViewAdapter
import com.armutyus.videogamesproject.databinding.FragmentFavoritesBinding
import com.armutyus.videogamesproject.viewmodel.FavoritesViewModel
import javax.inject.Inject

class FavoritesFragment @Inject constructor(
    private val favoritesRecyclerViewAdapter: FavoritesRecyclerViewAdapter
) : Fragment(R.layout.fragment_favorites) {

    private var fragmentBinding: FragmentFavoritesBinding? = null
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoritesBinding.bind(view)
        fragmentBinding = binding

        favoritesViewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

        fragmentBinding?.favoritesRecyclerView?.adapter = favoritesRecyclerViewAdapter
        fragmentBinding?.favoritesRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        favoriteListFromRoom()

    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.getFavoriteGamesList()
    }

    private fun favoriteListFromRoom() {
        favoritesViewModel.videoGamesFavoritesList.observe(viewLifecycleOwner) {
            val favoritesFromRoom = it?.toList()

            if (favoritesFromRoom == null) {

                fragmentBinding?.linearLayoutError?.visibility = View.VISIBLE

            } else {
                favoritesRecyclerViewAdapter.favoriteVideoGamesList = favoritesFromRoom
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

}