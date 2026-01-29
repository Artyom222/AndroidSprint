package ui.categories

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.STUB
import model.Category
import ru.example.androidsprint.R

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesState(
        val image: Drawable? = null,
        val title: String? = null,
        val categories: List<Category> = emptyList(),
    )

    private val _liveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val liveData: LiveData<CategoriesState> = _liveData

    init {
        Log.i("!!!", "CategoriesViewModel initialized")
    }

    fun loadCategories() {
        val image = loadImageFromRes("bcg_categories")
        val title = getApplication<Application>().getString(R.string.title_ingredients)
        val categories = STUB.getCategories()

        _liveData.value = CategoriesState(
            image = image,
            title = title,
            categories = categories,
        )
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

}