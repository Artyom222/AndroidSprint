package dev.androidsprin.recipes.ui.recipes.favorites

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.androidsprin.recipes.RecipesApplication
import dev.androidsprin.recipes.ui.recipes.recipe_list.RecipesListAdapter
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.FragmentFavoritesBinding
import kotlin.getValue

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")
    lateinit var viewModel: FavoritesViewModel
    private lateinit var recipesAdapter: RecipesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.favoritesViewModelFactory.create()
    }

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
        val image = ContextCompat.getDrawable(
            context,
            R.drawable.bcg_categories)
        val title = context?.getString(R.string.title_favorites)
        binding.ivFavoriteRecipes.setImageDrawable(image)
        binding.tvFavoriteRecipes.text = title

        viewModel.liveData.observe(viewLifecycleOwner) { state ->

            recipesAdapter.updateData(state.favoriteRecipes)
            if (state.favoriteRecipes.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.rvRecipes.visibility = View.GONE
            }

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
        val action = FavoritesFragmentDirections.
            actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}