package ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import data.RECIPES_API
import model.Category
import ru.example.androidsprint.R
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

        val imageView =  viewHolder.binding.ivCategory
        val imageUrl = category.imageUrl
        Glide.with(imageView.context)
            .load("${RECIPES_API}images/$imageUrl")
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(imageView)

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