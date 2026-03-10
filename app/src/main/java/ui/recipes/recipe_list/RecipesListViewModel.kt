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
    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "RecipesListViewModel initialized")
    }

    fun loadRecipes(arguments: Category) {
        viewModelScope.launch {
            try {
                val recipes = repository.getRecipesByCategoryId(arguments.id)
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