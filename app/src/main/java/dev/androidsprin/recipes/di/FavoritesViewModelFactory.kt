package dev.androidsprin.recipes.di

import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.ui.categories.CategoriesViewModel
import dev.androidsprin.recipes.ui.recipes.favorites.FavoritesViewModel
import dev.androidsprin.recipes.ui.recipes.recipe.RecipeViewModel
import dev.androidsprin.recipes.ui.recipes.recipe_list.RecipesListViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
): Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}