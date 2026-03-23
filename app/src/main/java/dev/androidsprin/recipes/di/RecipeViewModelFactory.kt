package dev.androidsprin.recipes.di

import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.ui.categories.CategoriesViewModel
import dev.androidsprin.recipes.ui.recipes.recipe.RecipeViewModel
import dev.androidsprin.recipes.ui.recipes.recipe_list.RecipesListViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
): Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}