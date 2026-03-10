package ui.categories

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipesRepository
import kotlinx.coroutines.launch
import model.Category
import ru.example.androidsprint.R

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesState(
        val image: Drawable? = null,
        val title: String? = null,
        val categories: List<Category> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val liveData: LiveData<CategoriesState> = _liveData
    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "CategoriesViewModel initialized")
    }

    fun loadCategories() {
        val image = ContextCompat.getDrawable(
            getApplication<Application>().applicationContext,
            R.drawable.bcg_categories)
        val title = getApplication<Application>().getString(R.string.title_ingredients)
        viewModelScope.launch {
            try {
                val categories = repository.getCategories()
                _liveData.postValue(
                    CategoriesState(
                        image = image,
                        title = title,
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