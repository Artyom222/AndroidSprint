package dev.androidsprin.recipes.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Serializable
@Parcelize
@Entity
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo val categoryId: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val ingredients: List<Ingredient>,
    @ColumnInfo val method: List<String>,
    @ColumnInfo val imageUrl: String,
    @ColumnInfo val isFavorite: Boolean = false,
) : Parcelable