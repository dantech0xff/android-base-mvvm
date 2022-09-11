package com.creative.mvvm.ui.note.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creative.mvvm.databinding.LayoutNotePhotoBinding

class PhotoListAdapter (val photoListAdapterListener: PhotoListAdapterListener? = null) : ListAdapter<String, PhotoListAdapter.PhotoVH> (diffCallback) {
    companion object {
        private val diffCallback: DiffUtil.ItemCallback<String> = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PhotoVH(val viewBinding: LayoutNotePhotoBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(uri: String) {
            viewBinding.apply {
                Glide.with(imageView).load(Uri.parse(uri)).into(imageView)
                buttonDelete.setOnClickListener {
                    photoListAdapterListener?.onDeleteItemClick(uri)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        return PhotoVH(
            LayoutNotePhotoBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        holder.bind(getItem(position))
    }

    interface PhotoListAdapterListener {
        fun onDeleteItemClick(uri: String)
    }
}