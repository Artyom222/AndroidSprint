package dev.androidsprin.recipes.ui.recipes.favorites

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.model.Recipe
import kotlinx.coroutines.launch
import ru.example.androidsprint.R

class FavoritesViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    data class FavoritesState(
        val favoriteRecipes: List<Recipe> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<FavoritesState> = MutableLiveData()
    val liveData: LiveData<FavoritesState> = _liveData

    init {
        Log.i("!!!", "FavoritesViewModel initialized")
    }

    fun loadFavorites() {

        viewModelScope.launch {
            try {
                val favoriteRecipes = recipesRepository.getFavoritesRecipesFromCache()
                _liveData.postValue(
                    FavoritesState(
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