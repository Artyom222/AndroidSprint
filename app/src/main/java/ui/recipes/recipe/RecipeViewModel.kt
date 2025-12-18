package ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.ARG_RECIPE
import data.FAVORITES_KEY
import data.SHARED_PREFS_NAME
import data.STUB
import model.Recipe
import kotlin.collections.remove

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
    )

    private val _liveData: MutableLiveData<RecipeState> = MutableLiveData()
    val liveData: LiveData<RecipeState> = _liveData

    init {
        Log.i("!!!", "ViewModel initialized")
        _liveData.value = RecipeState(isFavorite = false)
    }

    fun loadRecipe(recipeId: Int) {

        val recipe = STUB.getRecipeById(recipeId)
        val isFavorite = getFavorites().contains(recipeId.toString())

        _liveData.value = RecipeState(
            recipe = recipe,
            portionsCount = 1,
            isFavorite = isFavorite
        )
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            SHARED_PREFS_NAME, Context.MODE_PRIVATE
        )
        val savedSet = sharedPrefs?.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(savedSet)
    }

    private fun saveFavorites(favoriteIds: Set<String>) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            SHARED_PREFS_NAME, Context.MODE_PRIVATE
        ) ?: return
        with(sharedPrefs.edit()) {
            putStringSet(FAVORITES_KEY, favoriteIds)
            apply()
        }
    }

    private fun updateFavoriteInStorage(favoriteState: Boolean) {
        val recipe = _liveData.value?.recipe ?: return
        val favorites = getFavorites()

        if (favoriteState) {
            favorites.add(recipe.id.toString())
        } else {
            favorites.remove(recipe.id.toString())
        }
        saveFavorites(favorites)
    }

    fun onFavoritesClicked() {
        val favoriteState = _liveData.value ?: return
        val newFavoriteState = !favoriteState.isFavorite
        _liveData.value = favoriteState.copy(isFavorite = newFavoriteState)
        updateFavoriteInStorage(newFavoriteState)
    }
}