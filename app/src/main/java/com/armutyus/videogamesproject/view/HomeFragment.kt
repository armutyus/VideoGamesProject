package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.adapter.HomeRecyclerViewAdapter
import com.armutyus.videogamesproject.adapter.ViewPagerAdapter
import com.armutyus.videogamesproject.databinding.FragmentHomeBinding
import com.armutyus.videogamesproject.util.Status
import com.armutyus.videogamesproject.viewmodel.HomeViewModel
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val viewPagerAdapter: ViewPagerAdapter,
    private val homeRecyclerViewAdapter: HomeRecyclerViewAdapter
): Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val binding = FragmentHomeBinding.bind(view)
        _binding = binding

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        _binding?.viewPager?.adapter = viewPagerAdapter
        _binding?.circleIndicator?.setViewPager(_binding?.viewPager)

        _binding?.homeRecyclerView?.adapter = homeRecyclerViewAdapter
        _binding?.homeRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        observeLiveData()

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.makeGamesResponse()
    }

    private fun observeLiveData() {
        homeViewModel.gamesResponseList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    val videoGamesHomeViewPager = it.data?.results?.toList()?.subList(0,3)
                    viewPagerAdapter.videoGamesList = videoGamesHomeViewPager!!
                    val videoGamesHome = it.data.results.toList()
                    homeRecyclerViewAdapter.videoGamesList = videoGamesHome
                    _binding?.viewPager?.visibility = View.VISIBLE
                    _binding?.circleIndicator?.visibility = View.VISIBLE
                    _binding?.homeRecyclerView?.visibility = View.VISIBLE
                    _binding?.linearLayoutSearchError?.visibility = View.GONE
                    _binding?.linearLayoutLoading?.visibility = View.GONE

                    /*homeViewModel.insertGames(Games(

                    ))*/

                }

                Status.ERROR -> {
                    _binding?.viewPager?.visibility = View.GONE
                    _binding?.circleIndicator?.visibility = View.GONE
                    _binding?.homeRecyclerView?.visibility = View.GONE
                    _binding?.linearLayoutSearchError?.visibility = View.VISIBLE
                    _binding?.linearLayoutLoading?.visibility = View.GONE

                }

                Status.LOADING -> {
                    _binding?.viewPager?.visibility = View.GONE
                    _binding?.circleIndicator?.visibility = View.GONE
                    _binding?.homeRecyclerView?.visibility = View.GONE
                    _binding?.linearLayoutSearchError?.visibility = View.GONE
                    _binding?.linearLayoutLoading?.visibility = View.VISIBLE
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()

        val menuInflater = activity?.menuInflater
        menuInflater?.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}