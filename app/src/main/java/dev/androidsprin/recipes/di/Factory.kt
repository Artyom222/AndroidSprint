package dev.androidsprin.recipes.di

import dev.androidsprin.recipes.ui.recipes.recipe_list.RecipesListViewModel

interface Factory<T> {
    fun create(): T
}