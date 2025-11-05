package ru.example.androidsprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.example.androidsprint.databinding.FragmentRecipesListBinding

class RecipesListFragment() : Fragment() {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}