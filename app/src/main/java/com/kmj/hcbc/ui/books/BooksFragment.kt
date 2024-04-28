package com.kmj.hcbc.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kmj.hcbc.databinding.FragmentBooksBinding
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private val viewModel by viewModels<BookViewModel>()
    private val adapter: BookAdapter by lazy {
        BookAdapter(books) {

        }
    }

    private val books by lazy {
        listOf(
            Book("1", "A Short History of Nearly Everything", "Bill Bryson", "1997", "0517149257"),
            Book("2", "A Short History of Nearly Everything", "Bill Bryson", "1997", "0517149257"))
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
        viewModel.booksLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it.orEmpty())
        }
    }
}
