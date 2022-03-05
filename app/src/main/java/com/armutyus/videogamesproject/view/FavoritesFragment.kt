package com.armutyus.videogamesproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.armutyus.videogamesproject.databinding.FragmentFavoritesBinding
import com.armutyus.videogamesproject.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var fragmentBinding: FragmentFavoritesBinding? = null
    private val binding get() = fragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favoritesViewModel =
            ViewModelProvider(this)[FavoritesViewModel::class.java]
        fragmentBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textFavorites
        favoritesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

}