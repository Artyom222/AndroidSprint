package ui.recipes.favorites

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipesRepository
import kotlinx.coroutines.launch
import model.Recipe
import ru.example.androidsprint.R

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    data class FavoritesState(
        val image: Drawable? = null,
        val title: String? = null,
        val favoriteRecipes: List<Recipe> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<FavoritesState> = MutableLiveData()
    val liveData: LiveData<FavoritesState> = _liveData
    private val repository = RecipesRepository(application.applicationContext)

    init {
        Log.i("!!!", "FavoritesViewModel initialized")
    }

    fun loadFavorites() {
        val image = ContextCompat.getDrawable(
            getApplication<Application>().applicationContext,
            R.drawable.bcg_categories)
        val title = getApplication<Application>().getString(R.string.title_favorites)

        viewModelScope.launch {
            try {
                val favoriteRecipes = repository.getFavoritesRecipesFromCache()
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
}