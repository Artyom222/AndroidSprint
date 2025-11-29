package ru.example.androidsprint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.androidsprint.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): IngredientsAdapter.ViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]
        viewHolder.binding.tvIngredient.text = ingredient.description.uppercase()
        viewHolder.binding.tvQuantity.text =
            "${ingredient.quantity} ${ingredient.unitOfMeasure.uppercase()}"
    }

    override fun getItemCount(): Int = dataSet.size

}