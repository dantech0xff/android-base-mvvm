package com.creative.mvvm.ui.note

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.databinding.ItemTodoNoteBinding
import java.text.DateFormat
import java.text.SimpleDateFormat

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NotesVH>() {

    inner class NotesVH(val viewBinding: ItemTodoNoteBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(todoNote: Note) {
            viewBinding.apply {
                itemNotesTitle.text = todoNote.title
                itemNotesDesc.text = todoNote.description
                todoNote.date_updated?.let {
                    itemNotesUpdateTime.text =
                        SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                            .format(it)
                }
                todoNote.getListPhoto().let { listPhoto->
                    if(listPhoto.size > 0) {
                        imageView.visibility = View.VISIBLE
                        Glide.with(imageView).load(Uri.parse(listPhoto[0])).into(imageView)
                    }else{
                        imageView.visibility = View.GONE
                    }
                }

                // on item click
                root.setOnClickListener {
                    onItemClickListener?.let { it(todoNote) }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val listDiffer = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesVH {

        return NotesVH(
            ItemTodoNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: NotesVH, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    // on item click listener
    private var onItemClickListener: ((Note) -> Unit)? = null
    fun setOnItemClickListener(listener: (Note) -> Unit) {
        onItemClickListener = listener
    }
}