package ui.recipes.recipe_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.example.androidsprint.databinding.FragmentRecipesListBinding
import kotlin.getValue

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding: FragmentRecipesListBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must not be null")
    private val viewModel: RecipesListViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesListAdapter
    private val arg: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = arg.category
        viewModel.loadRecipes(category)

        initUI()
        setOnRecipeClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        recipesAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = recipesAdapter

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            Log.i("!!!", "state change")
            binding.ivRecipe.setImageDrawable(state.imageCategory)
            binding.tvTitleRecipe.text = state.titleCategory
            recipesAdapter.updateData(state.recipes)

            if (state.errorMessage != null) {
                val text = state.errorMessage
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(context?.applicationContext, text, duration).show()
            }
        }
    }

    private fun setOnRecipeClickListener() {
        recipesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = RecipesListFragmentDirections.
            actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}