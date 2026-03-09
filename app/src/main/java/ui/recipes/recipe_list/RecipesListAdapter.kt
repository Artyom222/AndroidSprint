package ui.recipes.recipe_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import data.RECIPES_API
import model.Recipe
import ru.example.androidsprint.R
import ru.example.androidsprint.databinding.ItemRecipeBinding

class RecipesListAdapter(private var dataSet: List<Recipe>) :
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

        val imageView = viewHolder.binding.ivRecipe
        val imageUrl = recipe.imageUrl
        Glide.with(imageView.context)
            .load("${RECIPES_API}images/$imageUrl")
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(imageView)

        viewHolder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateData(newRecipes: List<Recipe>) {
        this.dataSet = newRecipes
        notifyDataSetChanged()
    }
}