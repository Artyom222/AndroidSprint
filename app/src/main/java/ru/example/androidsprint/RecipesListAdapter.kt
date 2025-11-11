package ru.example.androidsprint

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.androidsprint.databinding.ItemRecipeBinding

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]
        viewHolder.binding.tvTitleRecipe.text = recipe.title
        val drawable = try {
            Drawable.createFromStream(
                viewHolder.binding.root.context.assets.open(recipe.imageUrl.toString()),
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found")
            null
        }
        viewHolder.binding.ivRecipe.setImageDrawable(drawable)

        viewHolder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }

    }

    override fun getItemCount(): Int = dataSet.size

}