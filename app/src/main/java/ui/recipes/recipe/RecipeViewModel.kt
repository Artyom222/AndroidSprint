package ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.FAVORITES_KEY
import data.RecipesRepository
import data.SHARED_PREFS_NAME
import kotlinx.coroutines.launch
import model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
        val recipeImageUrl: String? = null,
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<RecipeState> = MutableLiveData()
    val liveData: LiveData<RecipeState> = _liveData
    private val repository = RecipesRepository(application.applicationContext)

    init {
        Log.i("!!!", "ViewModel initialized")
        _liveData.value = RecipeState(isFavorite = false)
    }

    fun loadRecipe(recipeId: Int) {
        val isFavorite = getFavorites().contains(recipeId.toString())
        val portionsCount = _liveData.value?.portionsCount ?: 1

        viewModelScope.launch {
            try {
                val recipe = repository.getRecipeById(recipeId)
                val recipeImageUrl = recipe?.imageUrl
                _liveData.postValue(
                    RecipeState(
                        recipe = recipe,
                        portionsCount = portionsCount,
                        isFavorite = isFavorite,
                        recipeImageUrl = recipeImageUrl,
                        errorMessage = null,
                    )
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки рецепта", e)
                _liveData.postValue(
                    RecipeState(
                        errorMessage = "Ошибка получения данных"
                    )
                )
            }
        }
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

    fun updatePortionsCount(portionsCount: Int) {
        val currentState = _liveData.value ?: return
        _liveData.value = currentState.copy(portionsCount = portionsCount)
    }

}