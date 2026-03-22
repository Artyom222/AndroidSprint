package dev.androidsprin.recipes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.androidsprin.recipes.model.CategoriesDao
import dev.androidsprin.recipes.model.Category
import dev.androidsprin.recipes.model.Recipe
import dev.androidsprin.recipes.model.RecipesDao

@Database(entities = [Category::class, Recipe::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoriesDao
    abstract fun recipeDao(): RecipesDao
}