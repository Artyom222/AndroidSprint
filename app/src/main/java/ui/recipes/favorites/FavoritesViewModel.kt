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
import data.RecipesRepository
import data.SHARED_PREFS_NAME
import model.Recipe
import ru.example.androidsprint.R
import java.util.concurrent.Executors

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    data class FavoritesState(
        val image: Drawable? = null,
        val title: String? = null,
        val favoriteRecipes: List<Recipe> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<FavoritesState> = MutableLiveData()
    val liveData: LiveData<FavoritesState> = _liveData
    private val threadPool = Executors.newFixedThreadPool(10)
    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "FavoritesViewModel initialized")
    }

    fun loadFavorites() {
        val image = ContextCompat.getDrawable(
            getApplication<Application>().applicationContext,
            R.drawable.bcg_categories)
        val title = getApplication<Application>().getString(R.string.title_favorites)
        val favoriteRecipeIds = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()

        threadPool.execute {
            try {
                val favoriteRecipes = repository.getRecipesByIds(favoriteRecipeIds)
                _liveData.postValue(
                    FavoritesState(
                        image = image,
                        title = title,
                        favoriteRecipes = favoriteRecipes,
                        errorMessage = null,
                    )
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки рецептов", e)
                _liveData.postValue(
                    FavoritesState(
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

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}