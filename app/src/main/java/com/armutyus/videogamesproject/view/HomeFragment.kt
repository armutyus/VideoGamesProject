package com.armutyus.videogamesproject.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.adapter.HomeRecyclerViewAdapter
import com.armutyus.videogamesproject.adapter.ViewPagerAdapter
import com.armutyus.videogamesproject.databinding.FragmentHomeBinding
import com.armutyus.videogamesproject.roomdb.Games
import com.armutyus.videogamesproject.util.Constants.gameItem
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
    private var gameID = 0

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

        if (gameItem != null) {
            listsFromRoom()
        } else {
            observeLiveData()
        }

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.makeGamesResponse()

    }

    private fun observeLiveData() {
        homeViewModel.gamesResponseList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {

                    val videoGamesList = it.data?.results?.toList()
                    var i = 0
                    while (i < videoGamesList!!.size) {

                        gameID = videoGamesList[i].id

                        homeViewModel.gameDetailResponse(gameID)

                        homeViewModel.gamesDetails.observe(viewLifecycleOwner) { gameDetailsResponse ->
                            when (gameDetailsResponse.status) {

                                Status.SUCCESS -> {

                                    val videoGamesImage =
                                        gameDetailsResponse.data!!.background_image
                                    val videoGamesName = gameDetailsResponse.data.name
                                    val videoGamesID = gameDetailsResponse.data.id
                                    val videoGamesReleased = gameDetailsResponse.data.released
                                    val videoGamesRating = gameDetailsResponse.data.rating
                                    val videoGamesDescription = gameDetailsResponse.data.description
                                    val videoGamesMetacritic = gameDetailsResponse.data.metacritic

                                    homeViewModel.insertGames(
                                        Games(
                                            videoGamesImage,
                                            videoGamesName,
                                            videoGamesRating,
                                            videoGamesReleased,
                                            videoGamesMetacritic,
                                            videoGamesDescription,
                                            false,
                                            videoGamesID
                                        )
                                    )

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
                        }

                        i += 1

                    }

                    homeViewModel.getGamesList()
                    listsFromRoom()


                    _binding?.viewPager?.visibility = View.VISIBLE
                    _binding?.circleIndicator?.visibility = View.VISIBLE
                    _binding?.homeRecyclerView?.visibility = View.VISIBLE
                    _binding?.linearLayoutSearchError?.visibility = View.GONE
                    _binding?.linearLayoutLoading?.visibility = View.GONE

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

        }

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

            homeViewModel.searchGamesFromRoomList.observe(viewLifecycleOwner) {
                val searchList = it?.toList()
                homeRecyclerViewAdapter.videoGamesList = searchList!!

                if (searchList.isEmpty()) {
                    _binding?.linearLayoutSearchError?.visibility = View.VISIBLE
                    _binding?.linearLayoutLoading?.visibility = View.GONE
                } else {
                    _binding?.linearLayoutSearchError?.visibility = View.GONE
                }

            }

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

    private fun listsFromRoom() {

        homeViewModel.videoGamesList.observe(viewLifecycleOwner) {
            val videoGamesFromRoom = it?.toList()
            val checkSize = homeViewModel.gamesResponseList.value!!.data!!.results.size
            println("listsFromRoomSize:" + videoGamesFromRoom?.size)
            println("listCheckSize:$checkSize")

            if (videoGamesFromRoom!!.size == checkSize) {
                viewPagerAdapter.videoGamesList = videoGamesFromRoom.subList(0, 3)
                homeRecyclerViewAdapter.videoGamesList = videoGamesFromRoom.subList(3, checkSize)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}