package ui.recipes.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.RecipesRepository
import model.Category
import model.Recipe
import java.util.concurrent.Executors

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipesListStates(
        val imageUrl: String? = null,
        val titleCategory: String? = null,
        val recipes: List<Recipe> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<RecipesListStates> = MutableLiveData()
    val liveData: LiveData<RecipesListStates> = _liveData
    private val threadPool = Executors.newFixedThreadPool(10)
    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "RecipesListViewModel initialized")
    }

    fun loadRecipes(arguments: Category) {
        threadPool.execute {
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

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}