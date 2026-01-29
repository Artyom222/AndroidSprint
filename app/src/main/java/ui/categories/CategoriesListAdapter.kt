package ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import model.Category
import model.Recipe
import ru.example.androidsprint.databinding.ItemCategoryBinding

class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category: Category = dataSet[position]
        viewHolder.binding.tvTitleCategory.text = category.title
        viewHolder.binding.tvDescriptionCategory.text = category.description

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.binding.root.context.assets.open(category.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found")
            null
        }
        viewHolder.binding.ivCategory.setImageDrawable(drawable)

        viewHolder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newCategories: List<Category>){
        this.dataSet = newCategories
        notifyDataSetChanged()
    }
}