package dev.androidsprin.recipes.di

import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.ui.recipes.recipe_list.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
): Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}