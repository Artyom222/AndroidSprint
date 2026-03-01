package ui.categories

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.RecipesRepository
import model.Category
import ru.example.androidsprint.R
import java.util.concurrent.Executors

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesState(
        val image: Drawable? = null,
        val title: String? = null,
        val categories: List<Category> = emptyList(),
        val errorMessage: String? = null,
    )

    private val _liveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val liveData: LiveData<CategoriesState> = _liveData
    private val threadPool = Executors.newFixedThreadPool(10)
    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "CategoriesViewModel initialized")
    }

    fun loadCategories() {
        val image = loadImageFromRes("bcg_categories")
        val title = getApplication<Application>().getString(R.string.title_ingredients)
        threadPool.execute {
            try {
                val categories = repository.getCategories()

                runOnUiThread {
                    _liveData.value = CategoriesState(
                        image = image,
                        title = title,
                        categories = categories,
                        errorMessage = null,
                    )
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
                _liveData.value = CategoriesState(
                    errorMessage = "Ошибка получения данных"
                )
            }
        }
    }

    private fun loadImageFromRes(imageName: String): Drawable? {
        return try {
            val context = getApplication<Application>().applicationContext
            val resId = context.resources.getIdentifier(
                imageName,
                "drawable",
                context.packageName
            )
            ContextCompat.getDrawable(context, resId)
        } catch (e: Exception) {
            Log.e("!!!", "Image not found: $imageName", e)
            null
        }
    }

    private fun runOnUiThread(action: () -> Unit) {
        android.os.Handler(android.os.Looper.getMainLooper()).post(action)
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}