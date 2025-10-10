package ru.example.androidsprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.example.androidsprint.databinding.FragmentListCategoriesBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding: FragmentListCategoriesBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

}