package dev.androidsprin.recipes.ui.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import dev.androidsprin.recipes.RecipesApplication
import dev.androidsprin.recipes.di.AppContainer
import dev.androidsprin.recipes.model.Category
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding: FragmentListCategoriesBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")
    lateinit var viewModel: CategoriesViewModel
    private lateinit var categoriesAdapter: CategoriesListAdapter
    private var categoriesList: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.categoriesViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCategories()
        initUI()
        setOnCategoriesClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        categoriesAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = categoriesAdapter
        val image = ContextCompat.getDrawable(
            context,
            R.drawable.bcg_categories
        )
        binding.ivCategories.setImageDrawable(image)

        val title = context?.getString(R.string.title_ingredients)
        binding.tvTitle.text = title

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            Log.i("!!!", "state change")

            categoriesList = state.categories
            categoriesAdapter.updateData(categoriesList)

            if (state.errorMessage != null) {
                val text = state.errorMessage
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(context?.applicationContext, text, duration).show()
            }
        }
    }

    private fun setOnCategoriesClickListener() {
        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = categoriesList.find { it.id == categoryId } ?:
        throw IllegalArgumentException("Category with id $categoryId not found")

        val action = CategoriesListFragmentDirections.
            actionCategoriesListFragmentToRecipesListFragment(category)
        findNavController().navigate(action)
    }
}