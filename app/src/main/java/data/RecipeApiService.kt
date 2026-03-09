package data

import model.Category
import model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(
        @Path("id") id: Int
    ): List<Recipe>

    @GET("recipes")
    suspend fun getRecipesByIds(
        @Query("ids") ids: Set<Int>
    ): List<Recipe>

    @GET("recipe/{id}")
    suspend fun getRecipeById(
        @Path("id") id: Int
    ): Recipe
}