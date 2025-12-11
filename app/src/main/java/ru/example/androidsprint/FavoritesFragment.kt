package ru.example.androidsprint

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.example.androidsprint.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

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
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = activity?.getSharedPreferences(
            SHARED_PREFS_NAME, Context.MODE_PRIVATE
        )
        val savedSet = sharedPrefs?.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(savedSet)
    }

    private fun initRecycler() {
        val favoriteRecipeIds = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteRecipeIds).toList()
        if (favoriteRecipes.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvRecipes.visibility = View.GONE
            return
        }
        val recipesAdapter = RecipesListAdapter(favoriteRecipes)
        binding.rvRecipes.adapter = recipesAdapter
        recipesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = Bundle().apply {
            putParcelable(ARG_RECIPE, recipe)
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