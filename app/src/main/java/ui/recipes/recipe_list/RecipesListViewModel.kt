package ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.STUB
import model.Category
import model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipesListStates(
        val imageCategory: Drawable? = null,
        val titleCategory: String? = null,
        val recipes: List<Recipe> = emptyList(),
    )

    private val _liveData: MutableLiveData<RecipesListStates> = MutableLiveData()
    val liveData: LiveData<RecipesListStates> = _liveData

    init {
        Log.i("!!!", "RecipesListViewModel initialized")
    }

    fun loadRecipes(arguments: Category) {
        val drawable = loadImageFromAssets(arguments.imageUrl)

        _liveData.value = RecipesListStates(
            imageCategory = drawable,
            titleCategory = arguments.title,
            recipes = STUB.getRecipesByCategoryId(arguments.id)
        )
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
}