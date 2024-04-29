package com.kmj.hcbc.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kmj.hcbc.R
import com.kmj.hcbc.databinding.FragmentBooksBinding
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.utils.Action
import com.kmj.hcbc.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private val viewModel by activityViewModels<BookViewModel>()
    private val adapter: BookAdapter by lazy {
        BookAdapter(
            clickCallback = {
                navigateToEditFragment(it)
            },
            longClickCallback = {
                showConfirmDeleteDialog(it)
            })
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
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.findBookById(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initObserver() {
        viewModel.booksLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it.orEmpty())
        }
        viewModel.latestBook.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.fetchAllBooks()
            }
        }
        viewModel.foundBook.observe(viewLifecycleOwner) {
            it?.let {
                adapter.setData(listOf(it))
            }
        }
        viewModel.actionLiveData.observe(viewLifecycleOwner) {
            val message = when (it) {
                is Action.FetchDataError -> it.message.orEmpty()
                else -> context?.getText(R.string.error_message)
            }
            Toast.makeText(requireContext(), message, LENGTH_LONG).show()
        }
    }

    private fun navigateToEditFragment(it: Book?) {
        findNavController().navigate(
            R.id.action_booksFragment_to_addBookFragment,
            bundleOf(BOOK_KEY to it)
        )
    }

    private fun showConfirmDeleteDialog(it: Book?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_title))
            .setMessage(resources.getString(R.string.dialog_support_title))
            .setPositiveButton(resources.getString(R.string.dialog_accept)) { dialog, which ->
                viewModel.deleteBookById(it?.id.orEmpty())
            }
            .show()
    }

    companion object {
        private const val BOOK_KEY = "book"

        private val books by lazy {
            listOf(
                Book(
                    "1",
                    "A Short History of Nearly Everything",
                    "Bill Bryson",
                    "1997",
                    "0517149257"
                ),
                Book(
                    "2",
                    "The Hitchhiker's Guide to the Galaxy",
                    "Douglas Adams",
                    "1997",
                    "0517149257"
                )
            )
        }
    }
}
