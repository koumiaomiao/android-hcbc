package com.kmj.hcbc.ui.addbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kmj.hcbc.databinding.FragmentAddBookBinding
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.viewmodel.BookViewModel
import java.util.UUID

class AddBookFragment : Fragment() {

    private lateinit var binding: FragmentAddBookBinding
    private val viewmodel by activityViewModels<BookViewModel>()
    private val book by lazy { arguments?.getParcelable<Book>("book") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBookBinding.inflate(inflater, container, false)
        binding.book = book
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
    }

    private fun initEvents() {
        binding.saveBookFab.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val author = binding.authorEditText.text.toString()
            val publishYear = binding.publishYearEditText.text.toString()
            val isbn = binding.isbnEditText.text.toString()
            val id = book?.id ?: UUID.randomUUID().toString()
            val latestBook = Book(id, title, author, publishYear, isbn)
            book?.let {
                viewmodel.updateBook(latestBook)
            } ?: run {
                viewmodel.createBook(latestBook)
            }
            findNavController().popBackStack()
        }
        binding.tooBar.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
