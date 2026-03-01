package ui.categories

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import model.Category
import ru.example.androidsprint.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding: FragmentListCategoriesBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")
    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesListAdapter
    private var categoriesList: List<Category> = emptyList()

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
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            Log.i("!!!", "state change")

            binding.tvTitle.text = state.title
            binding.ivCategories.setImageDrawable(state.image)
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