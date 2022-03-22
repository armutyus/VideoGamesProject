package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.adapter.HomeRecyclerViewAdapter
import com.armutyus.videogamesproject.adapter.ViewPagerAdapter
import com.armutyus.videogamesproject.databinding.FragmentHomeBinding
import com.armutyus.videogamesproject.model.VideoGames
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Status
import com.armutyus.videogamesproject.viewmodel.HomeViewModel
import me.relex.circleindicator.CircleIndicator3
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val viewPagerAdapter: ViewPagerAdapter,
    private val homeRecyclerViewAdapter: HomeRecyclerViewAdapter
) : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val binding = FragmentHomeBinding.bind(view)
        _binding = binding

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        _binding?.viewPager?.adapter = viewPagerAdapter
        val indicator3 = view.findViewById<CircleIndicator3>(R.id.circleIndicator)
        indicator3?.setViewPager(_binding?.viewPager)

        viewPagerAdapter.registerAdapterDataObserver(indicator3.adapterDataObserver)

        _binding?.homeRecyclerView?.adapter = homeRecyclerViewAdapter
        _binding?.homeRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        observeLiveData()

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.makeGamesResponse()
        homeViewModel.getGamesList()
    }

    private fun observeLiveData() {
        homeViewModel.gamesResponseList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    val videoGamesList = it.data?.results?.toList()
                    _binding?.viewPager?.visibility = View.VISIBLE
                    _binding?.circleIndicator?.visibility = View.VISIBLE
                    _binding?.homeRecyclerView?.visibility = View.VISIBLE
                    _binding?.linearLayoutSearchError?.visibility = View.GONE
                    _binding?.linearLayoutLoading?.visibility = View.GONE

                    storeInRoom(videoGamesList!!)
                    listsFromRoom()

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
        return true
    }

    override fun onQueryTextChange(searchString: String?): Boolean {
        if (searchString?.length!! >= 3) {

            _binding?.viewPager?.visibility = View.GONE
            _binding?.circleIndicator?.visibility = View.GONE
            _binding?.homeRecyclerView?.visibility = View.VISIBLE
            _binding?.linearLayoutSearchError?.visibility = View.GONE
            _binding?.linearLayoutLoading?.visibility = View.GONE
            searchDatabase(searchString)

            homeViewModel.searchGamesFromRoomList.observe(viewLifecycleOwner, Observer {
                val searchList = it?.toList()
                homeRecyclerViewAdapter.videoGamesList = searchList!!
            })

        } else {

            _binding?.viewPager?.visibility = View.VISIBLE
            _binding?.circleIndicator?.visibility = View.VISIBLE
            _binding?.homeRecyclerView?.visibility = View.VISIBLE
            _binding?.linearLayoutSearchError?.visibility = View.GONE
            _binding?.linearLayoutLoading?.visibility = View.GONE
            listsFromRoom()
            return false
        }

        return true
    }

    private fun searchDatabase(searchString: String) {
        val searchQuery = "%$searchString%"
        homeViewModel.searchGamesList(searchQuery)
    }

    private fun storeInRoom(list: List<VideoGames>) {
        var i = 0
        while (i < list.size) {

            val image = list[i].background_image
            val name = list[i].name
            val rating = list[i].rating
            val released = list[i].released
            val metacritic = list[i].metacritic
            val description = ""
            val favorite = false
            val id = list[i].id

            homeViewModel.insertGames(
                Games(image, name, rating, released, metacritic, description, favorite, id)
            )

            i += 1

        }

    }

    private fun listsFromRoom() {

        homeViewModel.videoGamesList.observe(viewLifecycleOwner, Observer {
            val videoGamesFromRoom = it?.toList()
            homeRecyclerViewAdapter.videoGamesList = videoGamesFromRoom?.subList(3,it.size)!!
            viewPagerAdapter.videoGamesList = videoGamesFromRoom.subList(0,3)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}