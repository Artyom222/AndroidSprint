package ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.RecipesRepository
import model.Category
import model.Recipe
import java.util.concurrent.Executors

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipesListStates(
        val imageCategory: Drawable? = null,
        val titleCategory: String? = null,
        val recipes: List<Recipe> = emptyList(),
    )

    private val _liveData: MutableLiveData<RecipesListStates> = MutableLiveData()
    val liveData: LiveData<RecipesListStates> = _liveData
    private val threadPool = Executors.newFixedThreadPool(10)
    val repository = RecipesRepository()

    init {
        Log.i("!!!", "RecipesListViewModel initialized")
    }

    fun loadRecipes(arguments: Category) {
        val drawable = loadImageFromAssets(arguments.imageUrl)
        threadPool.execute {
            try {
                val recipes = repository.getRecipesByCategoryId(arguments.id)

                runOnUiThread {
                    _liveData.value = RecipesListStates(
                        imageCategory = drawable,
                        titleCategory = arguments.title,
                        recipes = recipes
                    )
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки рецептов", e)
                val text = "Ошибка получения данных"
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(getApplication<Application>().applicationContext, text, duration).show()
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

    private fun runOnUiThread(action: () -> Unit) {
        android.os.Handler(android.os.Looper.getMainLooper()).post(action)
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}