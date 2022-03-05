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

class DetailsFragment : Fragment() {

    private var fragmentBinding: DetailsFragmentBinding? = null
    private val binding get() = fragmentBinding!!

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        fragmentBinding = DetailsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


}