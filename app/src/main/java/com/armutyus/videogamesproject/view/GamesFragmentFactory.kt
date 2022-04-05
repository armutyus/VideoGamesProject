package com.armutyus.videogamesproject.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.armutyus.videogamesproject.adapter.FavoritesRecyclerViewAdapter
import com.armutyus.videogamesproject.adapter.HomeRecyclerViewAdapter
import com.armutyus.videogamesproject.adapter.ViewPagerAdapter
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class GamesFragmentFactory @Inject constructor(
    private val homeRecyclerViewAdapter: HomeRecyclerViewAdapter,
    private val viewPagerAdapter: ViewPagerAdapter,
    private val favoritesRecyclerViewAdapter: FavoritesRecyclerViewAdapter,
    private val glide: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){

            HomeFragment::class.java.name -> HomeFragment(viewPagerAdapter, homeRecyclerViewAdapter)
            FavoritesFragment::class.java.name -> FavoritesFragment(favoritesRecyclerViewAdapter)
            DetailsFragment::class.java.name -> DetailsFragment(glide)

            else -> super.instantiate(classLoader, className)
        }
    }

}