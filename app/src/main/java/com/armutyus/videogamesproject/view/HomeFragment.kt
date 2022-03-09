package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.databinding.FragmentHomeBinding
import com.armutyus.videogamesproject.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        val binding = FragmentHomeBinding.bind(view)
        _binding = binding

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}