package ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        val recipe: Recipe? = null,
        val portion: Int = 1,
        val isFavorite: Boolean = false,
    )

    private val _liveData: MutableLiveData<RecipeState> = MutableLiveData()
    val liveData: LiveData<RecipeState> = _liveData

    init {
        Log.i("!!!", "ViewModel initialized")
        _liveData.value = RecipeState(isFavorite = false)
    }

}