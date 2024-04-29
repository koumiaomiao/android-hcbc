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
                if (newText.isNullOrEmpty()) {
                    adapter.setData(viewModel.booksLiveData.value.orEmpty())
                }
                return false
            }
        })
        binding.refresh.setOnRefreshListener {
            viewModel.fetchAllBooks()
        }
    }

    private fun initObserver() {
        viewModel.booksLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it.orEmpty())
        }
        viewModel.foundBook.observe(viewLifecycleOwner) {
            val data = if (null != it) listOf(it) else emptyList()
            adapter.setData(data)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.refresh.isRefreshing = it
        }
        viewModel.actionLiveData.observe(viewLifecycleOwner) {
            val message = when (it) {
                is Action.FetchDataError -> context?.getText(R.string.get_data_error_message)
                is Action.DeleteDataError -> context?.getText(R.string.delete_error_message)
                is Action.SearchDataError -> context?.getText(R.string.search_error_message)
                else -> context?.getText(R.string.general_error_message)
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
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_title))
            .setMessage(resources.getString(R.string.dialog_support_title))
            .setNegativeButton(resources.getString(R.string.dialog_cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.dialog_accept)) { dialog, which ->
                viewModel.deleteBookById(it?.id.orEmpty())
            }
        dialog.show()
    }

    companion object {
        private const val BOOK_KEY = "book"
    }
}
