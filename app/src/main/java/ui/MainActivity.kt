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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.ActivityMainBinding
import java.io.IOException

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

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val request = Request.Builder()
            .url("https://recipes.androidsprint.ru/api/category")
            .get()
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("!!!", "Ошибка запроса: ${e.message}")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.i("!!!", "Выполняется запрос на потоке: ${Thread.currentThread().name}")
                Log.i("!!!", "responseCode: ${response.code}")
                Log.i("!!!", "responseMessage: ${response.message}")

                val body = response.body?.string() ?: return
                val categories = Json.decodeFromString<List<Category>>(body)
                categories.forEach { category ->
                    Log.i("!!!", "Категория: ${category.title}, id: ${category.id}")
                }
            }
        })
    }
}