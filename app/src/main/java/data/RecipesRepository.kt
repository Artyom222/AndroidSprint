package data

import android.util.Log
import model.Category
import model.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RecipesRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(RecipeApiService::class.java)

    fun getCategories(): List<Category>  {
        return try {
            val call = service.getCategories()
            val response = call.execute()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        }catch (e: IOException) {
            Log.i("!!!", e.toString())
            emptyList()
        }
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        return try {
            val call = service.getRecipesByCategoryId(categoryId)
            val response = call.execute()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            Log.i("!!!", e.toString())
            emptyList()
        }
    }

    fun getRecipeById(id: Int): Recipe? {
        return try {
            val call = service.getRecipeById(id)
            val response = call.execute()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: IOException) {
            Log.i("!!!", e.toString())
            null
        }
    }

    fun getRecipesByIds(recipesIds: Set<Int>): List<Recipe> {
        return try {
            val call = service.getRecipesByIds(recipesIds)
            val response = call.execute()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}