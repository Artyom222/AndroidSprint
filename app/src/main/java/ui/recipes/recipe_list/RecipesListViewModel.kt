package ui.recipes.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipesRepository
import kotlinx.coroutines.launch
import model.Category
import model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipesListStates(
        val imageUrl: String? = null,
        val titleCategory: String? = null,
        val recipes: List<Recipe> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<RecipesListStates> = MutableLiveData()
    val liveData: LiveData<RecipesListStates> = _liveData
    private val repository = RecipesRepository(application.applicationContext)

    init {
        Log.i("!!!", "RecipesListViewModel initialized")
    }

    fun loadRecipes(arguments: Category) {
        viewModelScope.launch {
            try {
                var recipes = repository.getRecipesFromCache().filter(choiceRightRecipes(arguments))
                Log.i("!!!", "Загрузка списка рецептов из кэша ${recipes.map { it.title }.toString()}")
                _liveData.postValue(
                    RecipesListStates(
                        imageUrl = arguments.imageUrl,
                        titleCategory = arguments.title,
                        recipes = recipes,
                        errorMessage = null,
                    )
                )
                recipes = repository.getRecipesByCategoryId(arguments.id)
                repository.saveRecipesToCache(recipes)
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

    private fun choiceRightRecipes(arguments: Category): (Recipe) -> Boolean {
        return when (arguments.id) {
            0 -> { recipe -> recipe.id in 0..99 }
            1 -> { recipe -> recipe.id in 100..199 }
            2 -> { recipe -> recipe.id in 200..299 }
            3 -> { recipe -> recipe.id in 300..399 }
            4 -> { recipe -> recipe.id in 400..499 }
            else -> { recipe -> recipe.id in 500..599 }
        }
    }
}