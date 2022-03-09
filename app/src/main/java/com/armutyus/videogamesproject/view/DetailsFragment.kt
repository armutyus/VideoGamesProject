package com.armutyus.videogamesproject.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.databinding.DetailsFragmentBinding
import com.armutyus.videogamesproject.viewmodel.DetailsViewModel

class DetailsFragment : Fragment(R.layout.details_fragment) {

    private var fragmentBinding: DetailsFragmentBinding? = null
    private lateinit var viewModel: DetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        val binding = DetailsFragmentBinding.bind(view)
        fragmentBinding = binding
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }


}