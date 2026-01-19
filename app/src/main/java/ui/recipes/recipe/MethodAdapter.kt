package ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.androidsprint.databinding.ItemMethodBinding

class MethodAdapter(var dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMethodBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method: String = dataSet[position]
        viewHolder.binding.tvMethod.text = "${position + 1}. $method"
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateData(newMethods: List<String>) {
        this.dataSet = newMethods
        notifyDataSetChanged()
    }

}