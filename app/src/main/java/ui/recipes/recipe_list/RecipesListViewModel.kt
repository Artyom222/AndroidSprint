package ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.ARG_CATEGORY_ID
import data.ARG_CATEGORY_IMAGE_URL
import data.ARG_CATEGORY_NAME
import data.STUB
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

    fun loadRecipes(arguments: Bundle?) {
        val categoryId = arguments?.getInt(ARG_CATEGORY_ID)
        val categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        val categoryImageUrl = arguments?.getString(ARG_CATEGORY_IMAGE_URL) ?: return
        val drawable = loadImageFromAssets(categoryImageUrl)

        _liveData.value = RecipesListStates(
            imageCategory = drawable,
            titleCategory = categoryName,
            recipes = STUB.getRecipesByCategoryId(categoryId)
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