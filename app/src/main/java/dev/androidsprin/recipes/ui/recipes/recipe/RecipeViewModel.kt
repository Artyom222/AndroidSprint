package dev.androidsprin.recipes.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.model.Recipe
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
        val recipeImageUrl: String? = null,
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<RecipeState> = MutableLiveData()
    val liveData: LiveData<RecipeState> = _liveData

    init {
        Log.i("!!!", "ViewModel initialized")
        _liveData.value = RecipeState(isFavorite = false)
    }

    fun loadRecipe(recipeId: Int) {

        val portionsCount = _liveData.value?.portionsCount ?: 1
        viewModelScope.launch {
            try {
                val recipe = recipesRepository.getRecipeFromCache(recipeId)
                val isFavorite = recipe.isFavorite
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

    fun onFavoritesClicked() {
        val favoriteState = _liveData.value ?: return
        val newFavoriteState = !favoriteState.isFavorite
        val recipeId = favoriteState.recipe?.id ?: return
        _liveData.value = favoriteState.copy(isFavorite = newFavoriteState)
        viewModelScope.launch {
            recipesRepository.updateFavoriteStatus(recipeId, newFavoriteState)
        }
    }

    fun updatePortionsCount(portionsCount: Int) {
        val currentState = _liveData.value ?: return
        _liveData.value = currentState.copy(portionsCount = portionsCount)
    }
}