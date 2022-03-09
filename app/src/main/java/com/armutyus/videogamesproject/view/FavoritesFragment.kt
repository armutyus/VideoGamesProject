package com.armutyus.videogamesproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.databinding.FragmentFavoritesBinding
import com.armutyus.videogamesproject.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var fragmentBinding: FragmentFavoritesBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favoritesViewModel =
            ViewModelProvider(this)[FavoritesViewModel::class.java]

        val binding = FragmentFavoritesBinding.bind(view)
        fragmentBinding = binding

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

}