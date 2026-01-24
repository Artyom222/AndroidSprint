package ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.FAVORITES_KEY
import data.SHARED_PREFS_NAME
import data.STUB
import model.Recipe
import ru.example.androidsprint.R

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    data class FavoritesState(
        val image: Drawable? = null,
        val title: String? = null,
        val favoriteRecipes: List<Recipe>,
    )

    private val _liveData: MutableLiveData<FavoritesState> = MutableLiveData()
    val liveData: LiveData<FavoritesState> = _liveData

    init {
        Log.i("!!!", "FavoritesViewModel initialized")
    }

    fun loadFavorites() {
        val image = loadImageFromRes("bcg_categories")
        val title = getApplication<Application>().getString(R.string.title_favorites)
        val favoriteRecipeIds = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteRecipeIds).toList()

        _liveData.value = FavoritesState(
            image = image,
            title = title,
            favoriteRecipes = favoriteRecipes,
        )
    }

    private fun loadImageFromRes(imageName: String): Drawable? {
        return try {
            val context = getApplication<Application>().applicationContext
            val resId = context.resources.getIdentifier(
                imageName,
                "drawable",
                context.packageName
            )
            ContextCompat.getDrawable(context, resId)
        } catch (e: Exception) {
            Log.e("!!!", "Image not found: $imageName", e)
            null
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            SHARED_PREFS_NAME, Context.MODE_PRIVATE
        )
        val savedSet = sharedPrefs?.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(savedSet)
    }


}