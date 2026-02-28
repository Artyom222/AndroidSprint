package ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import kotlinx.serialization.json.Json
import model.Category
import model.Recipe
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        binding.btnCategories.setOnClickListener() {
            findNavController(R.id.mainContainer).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavourites.setOnClickListener() {
            findNavController(R.id.mainContainer).navigate(R.id.favoritesFragment)
        }

        threadPool.execute {
            try {
                val request = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw Exception("Unexpected code $response")
                    Log.i("!!!", "Выполняется запрос на потоке: ${Thread.currentThread().name}")

                    val categories = Json.decodeFromString<List<Category>>(
                        response.body?.string()
                            ?: throw IllegalStateException("Response body is null")
                    )
                    categories.forEach { category ->
                        Log.i("!!!", "Категория: ${category.title}, id: ${category.id}")
                    }
                    val categoryIds = categories.map { it.id }
                    categoryIds.forEach { id ->
                        threadPool.execute {
                            try {
                                val request = Request.Builder()
                                    .url("https://recipes.androidsprint.ru/api/category/$id/recipes")
                                    .build()
                                client.newCall(request).execute().use { response ->
                                    if (!response.isSuccessful) throw Exception("Unexpected code $response")
                                    Log.i("!!!", "Выполняется запрос на потоке: ${Thread.currentThread().name}")
                                    val recipesByCategoryId = Json.decodeFromString<List<Recipe>>(
                                        response.body?.string()
                                            ?: throw IllegalStateException("Response body is null")
                                    )
                                    Log.i("!!!", "Id категории: $id")
                                    recipesByCategoryId.forEach { recipe ->
                                        Log.i("!!!", "Рецепт: ${recipe.title}")
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("!!!", "Ошибка загрузки рецептов категории $id", e)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}