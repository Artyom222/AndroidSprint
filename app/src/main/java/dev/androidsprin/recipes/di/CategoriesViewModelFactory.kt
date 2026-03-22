package dev.androidsprin.recipes.di

import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.ui.categories.CategoriesViewModel

class CategoriesViewModelFactory(
    private val recipesRepository: RecipesRepository,
): Factory<CategoriesViewModel> {
    override fun create(): CategoriesViewModel {
        return CategoriesViewModel(recipesRepository)
    }


}