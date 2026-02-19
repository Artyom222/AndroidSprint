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
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.ActivityMainBinding
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

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
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpsURLConnection
            connection.connect()

            Log.i("!!!", "Выполняется запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseMessage}")
            val categories = Json.decodeFromString<List<Category>>(
                connection.inputStream.bufferedReader().readText()
            )

            categories.forEach { category ->
                Log.i("!!!", "Категория: ${category.title}, id: ${category.id}")
            }

            connection.disconnect()

            val categoryIds = categories.map { it.id }
            categoryIds.forEach { id ->
                threadPool.execute {
                    val url = URL("https://recipes.androidsprint.ru/api/category/$id/recipes")
                    val connection = url.openConnection() as HttpsURLConnection
                    connection.connect()
                    Log.i("!!!", "Выполняется запрос на потоке: ${Thread.currentThread().name}")
                    val recipesByCategoryId = Json.decodeFromString<List<Recipe>>(
                        connection.inputStream.bufferedReader().readText()
                    )
                    Log.i("!!!", "Id категории: $id")
                    recipesByCategoryId.forEach { recipe ->
                        Log.i("!!!", "Рецепт: ${recipe.title}")
                    }
                    connection.disconnect()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}