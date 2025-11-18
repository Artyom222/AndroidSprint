package ru.example.androidsprint

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import ru.example.androidsprint.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding: FragmentRecipesListBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must not be null")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

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
        categoryId = arguments?.getInt(ARG_CATEGORY_ID)
        categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        categoryImageUrl = arguments?.getString(ARG_CATEGORY_IMAGE_URL)

        val drawable =
            Drawable.createFromStream(
                requireContext().assets.open(categoryImageUrl.toString()),
                null
            )
        binding.ivRecipe.setImageDrawable(drawable)
        binding.tvTitleRecipe.text = categoryName
        initRecycler(categoryId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(categoryId: Int?) {
        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
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
            replace(R.id.mainContainer, RecipeFragment::class.java, bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

}