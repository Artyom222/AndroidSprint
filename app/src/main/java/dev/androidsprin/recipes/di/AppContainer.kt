package dev.androidsprin.recipes.di

import android.content.Context
import androidx.room.Room
import dev.androidsprin.recipes.data.AppDatabase
import dev.androidsprin.recipes.data.RECIPES_API
import dev.androidsprin.recipes.data.RecipeApiService
import dev.androidsprin.recipes.data.RecipesRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(RECIPES_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        name = "database-category"
    ).fallbackToDestructiveMigration()
        .build()

    private val recipeDao = db.recipeDao()
    private val categoryDao = db.categoryDao()
    private val recipeApiService = retrofit.create(RecipeApiService::class.java)
    private val ioDispatcher = Dispatchers.IO

    val repository = RecipesRepository(
        recipesDao = recipeDao,
        categoriesDao = categoryDao,
        recipeApiService = recipeApiService,
        ioDispatcher = ioDispatcher,
    )

    val categoriesViewModelFactory = CategoriesViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
}