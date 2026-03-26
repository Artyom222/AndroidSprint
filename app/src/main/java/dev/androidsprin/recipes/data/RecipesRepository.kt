package dev.androidsprin.recipes.data

import android.util.Log
import dev.androidsprin.recipes.model.CategoriesDao
import dev.androidsprin.recipes.model.Category
import dev.androidsprin.recipes.model.Recipe
import dev.androidsprin.recipes.model.RecipesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getCategories(): List<Category> {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getCategories()
            } catch (e: IOException) {
                Log.i("!!!", e.toString())
                emptyList()
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByCategoryId(categoryId)
            } catch (e: IOException) {
                Log.i("!!!", e.toString())
                emptyList()
            }
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(ioDispatcher) {
            categoriesDao.getAll()
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        return withContext(ioDispatcher) {
            categoriesDao.insertAll(*categories.toTypedArray())
        }
    }

    suspend fun getRecipesFromCache(categoryId: Int): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getRecipesByCategoryId(categoryId)
        }
    }

    suspend fun getRecipeFromCache(recipeId: Int): Recipe {
        return withContext(ioDispatcher) {
            recipesDao.getRecipeById(recipeId)
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        return withContext(ioDispatcher) {
            recipesDao.insertAll(*recipes.toTypedArray())
        }
    }

    suspend fun getFavoritesRecipesFromCache(): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getFavoriteRecipes()
        }
    }

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) {
        return withContext(ioDispatcher) {
            recipesDao.updateFavoriteStatus(recipeId, isFavorite)
        }
    }

}