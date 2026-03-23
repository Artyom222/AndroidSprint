package dev.androidsprin.recipes.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE categoryId = :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipe WHERE Id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): Recipe

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Query("UPDATE recipe SET isFavorite =:isFavorite WHERE id =:recipeId")
    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg recipe: Recipe)
}