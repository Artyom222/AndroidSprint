package ui.recipes.recipe

import androidx.lifecycle.ViewModel
import model.Recipe

class RecipeViewModel() : ViewModel() {
    data class RecipeState(
        val recipe: Recipe? = null,
        val portion: Int = 1,
        val isFavorite: Boolean = false,
    )
}