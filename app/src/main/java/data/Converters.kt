package data

import androidx.room.TypeConverter
import model.Ingredient
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class Converters {
    @TypeConverter
    fun fromIngredientsList(value: List<Ingredient>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIngredientsList(value: String): List<Ingredient> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromMethodList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toMethodList(value: String): List<String> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}