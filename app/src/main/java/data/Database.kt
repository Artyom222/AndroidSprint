package data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import model.CategoriesDao
import model.Category
import model.Recipe
import model.RecipesDao

@Database(entities = [Category::class, Recipe::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoriesDao
    abstract fun recipeDao(): RecipesDao
}