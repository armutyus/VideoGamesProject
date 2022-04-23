package com.armutyus.videogamesproject.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.armutyus.videogamesproject.util.Status
import com.armutyus.videogamesproject.viewmodel.HomeViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import me.relex.circleindicator.CircleIndicator3
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val viewPagerAdapter: ViewPagerAdapter,
    private val homeRecyclerViewAdapter: HomeRecyclerViewAdapter
) : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private var gameID = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Home")
        }

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

        if (sharedPreferences.getBoolean("first_time", true)) {
            homeViewModel.makeGamesResponse()
            observeLiveData()
        } else {

            listsFromRoom()
        }

    }

    override fun onResume() {
        super.onResume()
        listsFromRoom()
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
                                    val videoGamesDescription =
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            Html.fromHtml(
                                                gameDetailsResponse.data.description,
                                                Html.FROM_HTML_OPTION_USE_CSS_COLORS
                                            ).toString()
                                        } else {
                                            Html.fromHtml(gameDetailsResponse.data.description)
                                                .toString()
                                        }
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
                                    _binding?.linearLayoutSearchError?.visibility = View.VISIBLE
                                    _binding?.linearLayoutLoading?.visibility = View.GONE

                                }

                                Status.LOADING -> {
                                    _binding?.linearLayoutLoading?.visibility = View.VISIBLE
                                }

                            }

                        }

                        i += 1

                    }

                    _binding?.viewPager?.visibility = View.VISIBLE
                    _binding?.circleIndicator?.visibility = View.VISIBLE
                    _binding?.homeRecyclerView?.visibility = View.VISIBLE
                    _binding?.linearLayoutLoading?.visibility = View.GONE

                }

                Status.ERROR -> {
                    _binding?.linearLayoutSearchError?.visibility = View.VISIBLE
                    _binding?.linearLayoutLoading?.visibility = View.GONE

                }

                Status.LOADING -> {
                    _binding?.linearLayoutLoading?.visibility = View.VISIBLE
                }

            }

            with(sharedPreferences.edit()) {
                putBoolean("first_time", false)
                apply()
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

        homeViewModel.getGamesList()

        homeViewModel.videoGamesList.observe(viewLifecycleOwner) {
            val videoGamesFromRoom = it?.toList()
            val checkSize = homeViewModel.videoGamesList.value!!.size

            if (videoGamesFromRoom!!.size == checkSize) {
                viewPagerAdapter.videoGamesList = videoGamesFromRoom.subList(0, 3)
                homeRecyclerViewAdapter.videoGamesList =
                    videoGamesFromRoom.subList(3, videoGamesFromRoom.size)

                _binding?.linearLayoutLoading?.visibility = View.GONE
                _binding?.viewPager?.visibility = View.VISIBLE
                _binding?.circleIndicator?.visibility = View.VISIBLE
                _binding?.homeRecyclerView?.visibility = View.VISIBLE

            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}