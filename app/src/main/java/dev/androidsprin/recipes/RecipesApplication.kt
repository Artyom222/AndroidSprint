package dev.androidsprin.recipes

import android.app.Application
import dev.androidsprin.recipes.di.AppContainer

class RecipesApplication: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}