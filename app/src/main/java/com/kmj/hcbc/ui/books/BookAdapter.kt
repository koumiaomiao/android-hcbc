package com.kmj.hcbc.ui.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmj.hcbc.databinding.ItemBookBinding
import com.kmj.hcbc.model.Book

class BookAdapter(
    var items: List<Book> = emptyList(),
    val clickCallback: (book: Book?) -> Unit = {},
    val longClickCallback: (book: Book?) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemBookViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        (viewHolder as? ItemBookViewHolder)?.binding?.let {
            it.book = item
            it.root.setOnClickListener {
                clickCallback(item)
            }
            it.root.setOnLongClickListener {
                longClickCallback(item)
                true
            }
        }
    }

    class ItemBookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(books: List<Book>) {
        items = books
        notifyDataSetChanged()
    }
}
