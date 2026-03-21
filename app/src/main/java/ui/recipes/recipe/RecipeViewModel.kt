package ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipesRepository
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

        val portionsCount = _liveData.value?.portionsCount ?: 1
        viewModelScope.launch {
            try {
                val recipe = repository.getRecipeFromCache(recipeId)
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
            repository.updateFavoriteStatus(recipeId, newFavoriteState)
        }
    }

    fun updatePortionsCount(portionsCount: Int) {
        val currentState = _liveData.value ?: return
        _liveData.value = currentState.copy(portionsCount = portionsCount)
    }
}