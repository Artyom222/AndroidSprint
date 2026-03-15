package data

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Category
import model.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RecipesRepository(context: Context) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(RECIPES_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        name = "database-category"
    ).build()

    private val categoryDao = db.categoryDao()

    private val service = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            try {
                service.getCategories()
            } catch (e: IOException) {
                Log.i("!!!", e.toString())
                emptyList()
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipesByCategoryId(categoryId)
            } catch (e: IOException) {
                Log.i("!!!", e.toString())
                emptyList()
            }
        }
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipeById(id)
            } catch (e: IOException) {
                Log.i("!!!", e.toString())
                null
            }
        }
    }

    suspend fun getRecipesByIds(recipesIds: Set<Int>): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipesByIds(recipesIds)
            } catch (e: IOException) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryDao.getAll()
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        return withContext(Dispatchers.IO) {
            categoryDao.insertAll(*categories.toTypedArray())
        }
    }
}