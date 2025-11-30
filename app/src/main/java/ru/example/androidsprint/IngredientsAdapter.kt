package ru.example.androidsprint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.androidsprint.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

        private var quantityPortions = 1

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
        val result = ingredient.quantity.toDouble() * quantityPortions
        val formattedQuantity = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            "%.1f".format(result)
        }
        viewHolder.binding.tvQuantity.text =
            "$formattedQuantity ${ingredient.unitOfMeasure.uppercase()}"
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateIngredients(progress: Int) {
        quantityPortions = progress
        notifyDataSetChanged()
    }

}