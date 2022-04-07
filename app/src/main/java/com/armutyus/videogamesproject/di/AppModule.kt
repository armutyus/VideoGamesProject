package com.armutyus.videogamesproject.di

import android.content.Context
import androidx.room.Room
import com.armutyus.videogamesproject.R
import com.armutyus.videogamesproject.api.VideoGamesAPI
import com.armutyus.videogamesproject.repo.VideoGamesRepo
import com.armutyus.videogamesproject.repo.VideoGamesRepoInterface
import com.armutyus.videogamesproject.roomdb.GamesDB
import com.armutyus.videogamesproject.roomdb.GamesDao
import com.armutyus.videogamesproject.util.Constants.BASE_URL
import com.armutyus.videogamesproject.view.GamesFragmentFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, GamesDB::class.java, "GamesDB"
    ).build()

    @Singleton
    @Provides
    fun injectDao(database: GamesDB) = database.gamesDao()

    @Singleton
    @Provides
    fun injectRetrofitApi(): VideoGamesAPI {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VideoGamesAPI::class.java)
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface GamesFragmentFactoryEntryPoint {
        fun getFragmentFactory(): GamesFragmentFactory
    }

    @Singleton
    @Provides
    fun injectRepo(gamesDao: GamesDao, videoGamesAPI: VideoGamesAPI) =
        VideoGamesRepo(gamesDao, videoGamesAPI) as VideoGamesRepoInterface

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide
        .with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )


}