package com.armutyus.videogamesproject.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.armutyus.videogamesproject.adapter.HomeRecyclerViewAdapter
import com.armutyus.videogamesproject.adapter.ViewPagerAdapter
import javax.inject.Inject

class GamesFragmentFactory @Inject constructor(
    private val homeRecyclerViewAdapter: HomeRecyclerViewAdapter,
    private val viewPagerAdapter: ViewPagerAdapter
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){

            HomeFragment::class.java.name -> HomeFragment(viewPagerAdapter, homeRecyclerViewAdapter)
            FavoritesFragment::class.java.name -> FavoritesFragment()
            DetailsFragment::class.java.name -> DetailsFragment()

            else -> super.instantiate(classLoader, className)
        }
    }

}