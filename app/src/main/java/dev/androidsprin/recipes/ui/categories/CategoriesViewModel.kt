package dev.androidsprin.recipes.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.androidsprin.recipes.data.RecipesRepository
import dev.androidsprin.recipes.model.Category
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    data class CategoriesState(
        val categories: List<Category> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val liveData: LiveData<CategoriesState> = _liveData

    init {
        Log.i("!!!", "CategoriesViewModel initialized")
    }

    fun loadCategories() {

        viewModelScope.launch {
            try {
                var categories = recipesRepository.getCategoriesFromCache()
                Log.i("!!!", "Загрузка категорий из кэша ${categories.map { it.title }.toString()}")
                _liveData.postValue(
                    CategoriesState(
                        categories = categories,
                        errorMessage = null,
                    )
                )
                categories = recipesRepository.getCategories()
                recipesRepository.saveCategoriesToCache(categories)
                Log.i("!!!", "Обновление категорий в кэше ${categories.map { it.title }.toString()}")
                _liveData.postValue(
                    CategoriesState(
                        categories = categories,
                        errorMessage = null,
                    )
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
                _liveData.postValue(
                    CategoriesState(
                        errorMessage = "Ошибка получения данных"
                    )
                )
            }
        }
    }
}