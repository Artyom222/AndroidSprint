package ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import data.ARG_RECIPE
import data.ARG_RECIPE_ID
import data.STUB
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.FragmentFavoritesBinding
import ui.recipes.recipe.RecipeFragment
import ui.recipes.recipe_list.RecipesListAdapter
import kotlin.getValue

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadFavorites()
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
            binding.ivFavoriteRecipes.setImageDrawable(state.image)
            binding.tvFavoriteRecipes.text = state.title
            recipesAdapter.updateData(state.favoriteRecipes)

            if (state.favoriteRecipes.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.rvRecipes.visibility = View.GONE
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
        val bundle = Bundle().apply {
            putInt(ARG_RECIPE_ID, recipeId)
        }
        parentFragmentManager.commit {
            replace<RecipeFragment>(
                R.id.mainContainer,
                args = bundle
            )
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

}