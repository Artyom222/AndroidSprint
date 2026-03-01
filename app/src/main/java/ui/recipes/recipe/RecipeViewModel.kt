package ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.FAVORITES_KEY
import data.RecipesRepository
import data.SHARED_PREFS_NAME
import model.Recipe
import java.util.concurrent.Executors

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
        val recipeImage: Drawable? = null,
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<RecipeState> = MutableLiveData()
    val liveData: LiveData<RecipeState> = _liveData
    private val threadPool = Executors.newFixedThreadPool(10)
    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "ViewModel initialized")
        _liveData.value = RecipeState(isFavorite = false)
    }

    fun loadRecipe(recipeId: Int) {
        val isFavorite = getFavorites().contains(recipeId.toString())
        val portionsCount = _liveData.value?.portionsCount ?: 1

        threadPool.execute {
            try {
                val recipe = repository.getRecipeById(recipeId)
                val recipeImage = loadImageFromAssets(recipe?.imageUrl ?: return@execute)

                runOnUiThread {
                    _liveData.value = RecipeState(
                        recipe = recipe,
                        portionsCount = portionsCount,
                        isFavorite = isFavorite,
                        recipeImage = recipeImage,
                        errorMessage = null,
                    )
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки рецепта", e)
                _liveData.value = RecipeState(
                    errorMessage = "Ошибка получения данных"
                )
            }
        }
    }

    private fun loadImageFromAssets(imageUrl: String): Drawable? {
        return try {
            val inputStream = getApplication<Application>().assets.open(imageUrl)
            Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            Log.e("!!!", "Image not found: $imageUrl", e)
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

    private fun runOnUiThread(action: () -> Unit) {
        android.os.Handler(android.os.Looper.getMainLooper()).post(action)
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}