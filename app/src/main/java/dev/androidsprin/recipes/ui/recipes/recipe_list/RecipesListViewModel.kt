package dev.androidsprin.recipes.ui.recipes.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.model.Category
import dev.androidsprin.recipes.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    data class RecipesListStates(
        val imageUrl: String? = null,
        val titleCategory: String? = null,
        val recipes: List<Recipe> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<RecipesListStates> = MutableLiveData()
    val liveData: LiveData<RecipesListStates> = _liveData

    init {
        Log.i("!!!", "RecipesListViewModel initialized")
    }

    fun loadRecipes(arguments: Category) {
        viewModelScope.launch {
            try {
                var recipes = recipesRepository.getRecipesFromCache(arguments.id)
                Log.i("!!!", "Загрузка списка рецептов из кэша ${recipes.map { it.title }.toString()}")
                _liveData.postValue(
                    RecipesListStates(
                        imageUrl = arguments.imageUrl,
                        titleCategory = arguments.title,
                        recipes = recipes,
                        errorMessage = null,
                    )
                )
                recipes = recipesRepository.getRecipesByCategoryId(arguments.id)
                recipesRepository.saveRecipesToCache(recipes.map { it.copy(categoryId = arguments.id) })
                _liveData.postValue(
                    RecipesListStates(
                        imageUrl = arguments.imageUrl,
                        titleCategory = arguments.title,
                        recipes = recipes,
                        errorMessage = null,
                    )
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки рецептов", e)
                _liveData.postValue(
                    RecipesListStates(
                        errorMessage = "Ошибка получения данных"
                    )
                )
            }
        }
    }
}