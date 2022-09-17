package com.creative.mvvm.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.creative.mvvm.R
import com.creative.mvvm.databinding.NotesFragmentBinding
import com.creative.mvvm.di.component.FragmentComponent
import com.creative.mvvm.google.AdmobHelper
import com.creative.mvvm.ui.base.BaseFragment
import com.creative.mvvm.ui.launch.XLauncherViewModel
import com.creative.mvvm.ui.note.view.UpdateNoteFragmentArgs
import com.creative.mvvm.ui.view.XToolbar
import com.creative.mvvm.utils.XAnimationUtils
import javax.inject.Inject

class NoteFragment : BaseFragment<NotesFragmentBinding, NoteFragmentViewModel>()
    , XToolbar.ClickListener, NoteFragmentViewModel.NoteFragmentUiEvent {

    @Inject
    lateinit var admobHelper: AdmobHelper

    private val noteAdapter: NoteAdapter = NoteAdapter()

    private val activityViewModel by activityViewModels<XLauncherViewModel>()

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): NotesFragmentBinding {
        return NotesFragmentBinding.inflate(inflater, container, false)
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View, savedInstanceState: Bundle?) {

        viewBinding?.noteFragmentUiEvent = this@NoteFragment

        viewBinding?.xToolBar?.apply {
            setRightMenuButtonVisible(View.GONE)
            setTitleTextVisible(View.VISIBLE)
            setTitleText(R.string.todo)
            setToolbarClickListener(this@NoteFragment)
        }
        viewBinding?.apply {
            btnAddNotes.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.red_dot_animation
                )
            )
            notesRv.apply {
                adapter = noteAdapter
                layoutManager = LinearLayoutManager(activity)
            }
            XAnimationUtils.fadeInView(rootNotesContainer, 350)
        }

        noteAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_noteFragment_to_updateNoteFragment,
                UpdateNoteFragmentArgs(it.id).toBundle()
            )
        }
        viewModel.updateListNotes()
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.listNoteLiveData.observe(this) {
            viewBinding?.rootEmptyStateImage?.apply {
                if (it.isEmpty()) {
                    visibility = View.VISIBLE
                    Glide.with(this).load(R.mipmap.ic_launcher).into(viewBinding?.emptyStateImage!!)
                    XAnimationUtils.fadeInView(this)
                } else {
                    visibility = View.GONE
                    admobHelper.showInterstitial(requireActivity())
                }
            }
            noteAdapter.listDiffer.submitList(it)
        }
    }

    override fun onDrawerClick() {
        activityViewModel.openDrawer()
    }

    override fun onMenuRightClick() {}
    override fun addNewNoteClick() {
        findNavController().navigate(R.id.action_noteFragment_to_updateNoteFragment)
    }
}