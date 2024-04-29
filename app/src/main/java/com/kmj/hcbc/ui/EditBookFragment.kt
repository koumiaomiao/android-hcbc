package com.kmj.hcbc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kmj.hcbc.R
import com.kmj.hcbc.databinding.FragmentEditBookBinding
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.utils.Action
import com.kmj.hcbc.viewmodel.BookViewModel
import java.util.UUID

class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding
    private val viewModel by activityViewModels<BookViewModel>()
    private val book by lazy { arguments?.getParcelable<Book>(BOOK_KEY) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBookBinding.inflate(inflater, container, false)
        binding.book = book
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        initObserver()
    }

    private fun initObserver() {
        viewModel.latestBook.observe(viewLifecycleOwner) {
            it?.let {
                clearData()
                findNavController().popBackStack()
                viewModel.fetchAllBooks()
            }
        }

        viewModel.actionLiveData.observe(viewLifecycleOwner) {
            val message = when (it) {
                is Action.CreateDataError -> context?.getText(R.string.create_error_message)
                is Action.UpdateDataError -> context?.getText(R.string.update_error_message)
                else -> context?.getText(R.string.get_data_error_message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    private fun clearData() {
        viewModel.latestBook.value = null
        viewModel.actionLiveData.value = null
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
                viewModel.updateBook(it.id.orEmpty(), latestBook)
            } ?: run {
                viewModel.createBook(latestBook)
            }
        }
        binding.tooBar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val BOOK_KEY = "book"
    }
}
