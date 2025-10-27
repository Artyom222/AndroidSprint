package ru.example.androidsprint

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.example.androidsprint.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

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
    }

    override fun getItemCount() = dataSet.size
}