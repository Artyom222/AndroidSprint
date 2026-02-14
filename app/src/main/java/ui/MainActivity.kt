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
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.ActivityMainBinding
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

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

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpsURLConnection
            connection.connect()

            Log.i("!!!", "Выполняется запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseMessage}")
            val categories = Json.decodeFromString<List<Category>>("${connection.inputStream.bufferedReader().readText()}")
            categories.forEach { category ->
                Log.i("!!!", "Категория: ${category.title}, id: ${category.id}")
            }
        }
        thread.start()

    }
}