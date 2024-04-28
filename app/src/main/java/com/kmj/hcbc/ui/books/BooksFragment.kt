package com.kmj.hcbc.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kmj.hcbc.R
import com.kmj.hcbc.databinding.FragmentBooksBinding
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private val viewModel by activityViewModels<BookViewModel>()
    private val adapter: BookAdapter by lazy {
        BookAdapter(books) {

        }
    }

    private val books by lazy {
        listOf(
            Book("1", "A Short History of Nearly Everything", "Bill Bryson", "1997", "0517149257"),
            Book("2", "A Short History of Nearly Everything", "Bill Bryson", "1997", "0517149257")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bookList.adapter = adapter
        initEvents()
        initObserver()
    }

    private fun initEvents() {
        binding.addBookFab.setOnClickListener {
            findNavController().navigate(R.id.action_booksFragment_to_addBookFragment)
        }
    }

    private fun initObserver() {
        viewModel.createdBook.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.fetchAllBooks()
            }
        }
        viewModel.booksLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it.orEmpty())
        }
        viewModel.actionLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), context?.getText(R.string.error_message), LENGTH_LONG)
                .show()
        }
    }
}
