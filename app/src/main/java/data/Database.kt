package data

import androidx.room.Database
import androidx.room.RoomDatabase
import model.CategoriesDao
import model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoriesDao
}