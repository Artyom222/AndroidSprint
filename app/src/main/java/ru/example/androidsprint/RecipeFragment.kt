package ru.example.androidsprint

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.example.androidsprint.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding: FragmentRecipeBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_RECIPE) as? Recipe
        }
        initUI(recipe ?: return)
        initRecyclers(recipe ?: return)
        binding.ibFavorite.setImageResource(R.drawable.ic_favourite)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI(recipe: Recipe) {
        binding.tvNameRecipe.text = recipe.title
        val drawable = try {
            Drawable.createFromStream(
                binding.root.context.assets.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found")
            null
        }
        binding.ivRecipe.setImageDrawable(drawable)

        var isFavorite = false
        binding.ibFavorite.setOnClickListener {
            isFavorite = !isFavorite
            val drawable = if (isFavorite) {
                R.drawable.ic_heart
            } else {
                R.drawable.ic_favourite
            }
            binding.ibFavorite.setImageResource(drawable)
        }
    }

    private fun initRecyclers(recipe: Recipe) {
        binding.tvCountPortions.text = "1"
        val ingredientAdapter =
            IngredientsAdapter(recipe.ingredients)
        binding.rvIngredients.adapter = ingredientAdapter

        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvMethod.adapter = methodAdapter

        binding.sbPortions.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    ingredientAdapter.updateIngredients(progress)
                    binding.tvCountPortions.text = progress.toString()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            }
        )
        setupDividers()
    }

    private fun setupDividers() {
        val ingredientsDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        ingredientsDivider.dividerColor =
            ContextCompat.getColor(requireContext(), R.color.color_divider)
        ingredientsDivider.dividerThickness =
            resources.getDimensionPixelSize(R.dimen.divider_thickness)
        ingredientsDivider.isLastItemDecorated = false
        binding.rvIngredients.addItemDecoration(ingredientsDivider)

        val methodDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        methodDivider.dividerColor = ContextCompat.getColor(requireContext(), R.color.color_divider)
        methodDivider.dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
        methodDivider.isLastItemDecorated = false
        binding.rvMethod.addItemDecoration(methodDivider)
    }
}