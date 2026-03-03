package data

import model.Category
import model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(
        @Path("id") id: Int
    ): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryByCategoryId(
        @Path("id") id: Int
    ): Call<Category>

    @GET("recipes")
    fun getRecipesByIds(
        @Query("ids") ids: Set<Int>
    ): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipeById(
        @Path("id") id: Int
    ): Call<Recipe>
}