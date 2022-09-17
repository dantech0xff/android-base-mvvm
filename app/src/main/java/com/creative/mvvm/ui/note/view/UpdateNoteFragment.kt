package com.creative.mvvm.ui.note.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.creative.mvvm.R
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.databinding.UpdateNoteFragmentBinding
import com.creative.mvvm.di.CacheDirectory
import com.creative.mvvm.di.FileDirectory
import com.creative.mvvm.di.component.FragmentComponent
import com.creative.mvvm.ui.base.BaseFragment
import com.creative.mvvm.ui.view.XToolbar
import com.creative.mvvm.utils.Utils
import com.creative.mvvm.utils.XAnimationUtils
import com.creative.mvvm.utils.XLog
import com.creative.mvvm.utils.XToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import org.json.JSONArray
import org.json.JSONException
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class UpdateNoteFragment : BaseFragment<UpdateNoteFragmentBinding, UpdateNoteFragmentViewModel>(),
    XToolbar.ClickListener, PhotoListAdapter.PhotoListAdapterListener, UpdateNoteFragmentViewModel.UpdateNoteFragmentUiEvent {

    @Inject
    @CacheDirectory
    lateinit var cacheRootPath: File

    @Inject
    @FileDirectory
    lateinit var fileRootPath: File

    private val args: UpdateNoteFragmentArgs by navArgs()
    private var textChanged: Boolean = false
    private var updatingNote: Note = Note()

    private val photoListAdapter by lazy { PhotoListAdapter(this@UpdateNoteFragment) }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.noteLiveData.observe(this) {
            updatingNote = it
            showNoteDetails(updatingNote)
        }

        viewModel.exitNoteLiveData.observe(this) {
            if(it) {
                findNavController().popBackStack()
            }
        }

        viewModel.noteUpdatedLiveData.observe(this) {
            XToast.show(requireContext(), getString(R.string.update_success))
            this@UpdateNoteFragment.apply {
                textChanged = false
                viewBinding?.fabSaveNote?.setImageResource(R.drawable.round_send_to_mobile_24)
                closeKeyboardAndClearFocus()
            }
            updatingNote.id = it
        }
    }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        viewBinding?.uiEvent = this@UpdateNoteFragment
        viewBinding?.wrapNoteContent?.uiEvent = this@UpdateNoteFragment

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    doUpdateCurrentNote(exitAfterUpdateDone = true)
                }
            })

        viewBinding?.xToolBar?.apply {
            setRightMenuButtonResource(R.drawable.round_delete_white_24)
            setDrawerButtonResource(R.drawable.baseline_arrow_back_white_24)
            setTitleText(R.string.todo)
            setToolbarClickListener(this@UpdateNoteFragment)
        }

        enableEditMode()

        viewBinding?.apply {
            wrapNoteContent.apply {
                recyclerViewPhotos.visibility = View.GONE
                recyclerViewPhotos.adapter = photoListAdapter
                recyclerViewPhotos.layoutManager = GridLayoutManager(requireContext(),1).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
            }


            fabSaveNote.apply {
                visibility = View.VISIBLE
            }

            updatingNote.id = args.id
            if (updatingNote.id  >= 0) {
                viewModel.getTodoNote(updatingNote.id)
                fabSaveNote.apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.round_send_to_mobile_24)
                }
            } else {
                showNoteDetails(updatingNote)
                fabSaveNote.visibility = View.INVISIBLE
            }
        }

        viewBinding?.apply {
            XAnimationUtils.fadeInView(
                updateNoteContainer,
                500)
        }
    }

    private fun pickNotePhotos() {
        try {
            Dexter.withContext(requireContext())
                .withPermissions(*Utils.REQUIRED_PERMISSIONS_FOR_PICK.toTypedArray()).withListener(
                    object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                startActivityForResult(
                                    Utils.getPickImageIntent(requireContext()),
                                    ADD_NOTE_PHOTO_REQUEST_CODE
                                )
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            list: MutableList<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
        } catch (e: Exception) {
            XLog.e(e)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            ADD_NOTE_PHOTO_REQUEST_CODE -> {
                data?.data?.let {
                    val savePath = Uri.fromFile(
                        File(
                            fileRootPath.toString()
                                    + "/note_" + updatingNote.id + "_" + System.currentTimeMillis()
                        )
                    )
                    CropImage.activity(it)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setOutputCompressQuality(99)
                        .setOutputUri(savePath)
                        .setAspectRatio(1, 1)
                        .setCropMenuCropButtonTitle(getString(R.string.crop))
                        .setAllowFlipping(false)
                        .start(requireActivity(), this)
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == FragmentActivity.RESULT_OK) {
                    CropImage.getActivityResult(data)?.uri?.toString()?.let {
                        deleteNotePhoto()
                        updatingNote.photoPath = JSONArray().put(it).toString()
                        doUpdateNote(false)
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    XToast.show(requireContext(), getString(R.string.str_error))
                }
            }
        }
    }

    private fun doShareScreenshot() {
        try {
            val outPath =
                File(cacheRootPath.toString() + "/screenshot_" + System.currentTimeMillis() + ".jpeg")
            val out = FileOutputStream(outPath)
            requireView().apply {
                viewBinding?.apply {
                    xToolBar.apply {
                        setDrawerButtonVisible(View.INVISIBLE)
                        setRightMenuButtonVisible(View.INVISIBLE)
                    }
                    fabSaveNote.visibility = View.INVISIBLE
                    layoutWaterMark.visibility = View.VISIBLE
                }
            }.drawToBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out).apply {
                viewBinding?.apply {
                    xToolBar.apply {
                        setDrawerButtonVisible(View.VISIBLE)
                        setRightMenuButtonVisible(View.VISIBLE)
                    }
                    fabSaveNote.visibility = View.VISIBLE
                    layoutWaterMark.visibility = View.GONE
                }
            }
            out.flush()
            out.close()

            startActivity(
                Intent.createChooser(
                    ShareCompat.IntentBuilder(requireActivity())
                        .setType("*/*")
                        .setStream(
                            FileProvider.getUriForFile(
                                requireActivity(),
                                "com.creative.mvvm.FILE_PROVIDER", outPath
                            )
                        )
                        .setText(
                            getString(R.string.app_name) + "\n" + Utils.downloadUrl(
                                requireContext()
                            )
                        )
                        .intent, "Share for fun!"
                )
            )
        } catch (e: Exception) {
            XLog.e(e)
        }
    }

    private fun doUpdateNote(exit: Boolean) {
        val updateTime = Calendar.getInstance().time
        if (updatingNote.id >= 0) {
            viewModel.updateNote(
                Note(
                    updatingNote.id,
                    viewBinding?.let { it.wrapNoteContent.titleEditText.text.toString().trim() } ?: "",
                    viewBinding?.let { it.wrapNoteContent.noteEditText.text.toString().trim() } ?: "",
                    date_updated = updateTime,
                    photoPath = updatingNote.photoPath
                ), exit
            )
        } else {
            viewModel.insertNote(
                Note(
                    title = viewBinding?.let { it.wrapNoteContent.titleEditText.text.toString().trim() } ?: "",
                    description = viewBinding?.let { it.wrapNoteContent.noteEditText.text.toString().trim() } ?: "",
                    date_updated = updateTime,
                    photoPath = updatingNote.photoPath
                ), exit
            )
        }
        viewBinding?.apply {
            wrapNoteContent
                .noteUpdateTime
                .text = SimpleDateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                .format(updateTime)
        }
    }

    private fun noteHasContent(): Boolean {
        return (viewBinding?.let {
            it.wrapNoteContent.titleEditText.text.toString()
                .trim().length + it.wrapNoteContent.noteEditText.text.toString().trim().length
        } ?: 0) > 0
    }

    private fun doUpdateCurrentNote(exitAfterUpdateDone: Boolean) {
        if (!textChanged) {
            findNavController().popBackStack()
        } else if (!exitAfterUpdateDone) {
            doUpdateNote(false)
        } else {
            AlertDialog.Builder(requireContext())
                .apply {
                    setTitle(getString(R.string.todo))
                    setMessage(getString(R.string.update_before_exit))
                    setPositiveButton(android.R.string.yes) { _, _ ->
                        doUpdateNote(true)
                    }
                    setNegativeButton(android.R.string.no) { _, _ ->
                        findNavController().popBackStack()
                    }
                }.show()
        }
    }

    private fun enableEditMode() {
        viewBinding?.wrapNoteContent?.apply {
            titleEditText.apply {
                isFocusable = true
                isFocusableInTouchMode = true
            }
            noteEditText.apply {
                isFocusable = true
                isFocusableInTouchMode = true
            }
        }
    }

    private fun deleteNoteAndExit() {
        AlertDialog.Builder(requireContext())
            .apply {
                setTitle(getString(R.string.todo))
                setMessage(getString(R.string.delete_note_and_exit))
                setPositiveButton(android.R.string.yes) { _, _ ->
                    if (updatingNote.id  >= 0) {
                        viewModel.deleteNote(updatingNote.id)
                    } else {
                        findNavController().popBackStack()
                    }
                }
                setNegativeButton(android.R.string.no) { _, _ ->
                }
            }.show()
    }

    private fun showNoteDetails(note: Note) {
        viewBinding?.wrapNoteContent?.apply {
            titleEditText.apply {
                setText(note.title)
                doOnTextChanged { text, _, _, _ ->
                    textChanged = true

                    viewBinding?.apply {
                        fabSaveNote.setImageResource(R.drawable.ic_check_white_24dp)
                    }

                    if (text.toString().trim().isEmpty()
                        && noteEditText.text.toString().trim().isEmpty()
                    ) {
                        viewBinding?.apply {
                            fabSaveNote.visibility = View.INVISIBLE
                        }
                    } else {
                        viewBinding?.apply {
                            fabSaveNote.visibility = View.VISIBLE
                        }
                    }
                }
            }

            noteEditText.apply {
                setText(note.description)
                doOnTextChanged { text, _, _, _ ->
                    textChanged = true
                    viewBinding?.apply {
                        fabSaveNote.setImageResource(R.drawable.ic_check_white_24dp)
                    }
                    if (text.toString().trim().isEmpty()
                        && titleEditText.text.toString().trim().isEmpty()
                    ) {
                        viewBinding?.apply {
                            fabSaveNote.visibility = View.INVISIBLE
                        }
                    } else {
                        viewBinding?.apply {
                            fabSaveNote.visibility = View.VISIBLE
                        }
                    }
                }
            }

            note.date_updated?.let {
                noteUpdateTime.text = SimpleDateFormat
                    .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                    .format(it)
            }

            if (note.title.isEmpty() && note.description.isEmpty()) {
                if (titleEditText.requestFocus()) {
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(titleEditText, InputMethodManager.SHOW_IMPLICIT)
                }
            }

            showListPhoto(note.photoPath)
        }
    }

    private fun showListPhoto(listPhoto: String) {
        viewBinding?.wrapNoteContent?.apply {
            if (listPhoto.isNotEmpty()) {
                try {
                    val listOfPhoto = mutableListOf<String>()
                    JSONArray(listPhoto).let {
                        for (i in 0 until it.length()) {
                            listOfPhoto.add(it[i] as String)
                        }
                    }
                    recyclerViewPhotos.visibility = View.VISIBLE
                    buttonAddPhotos.visibility = View.GONE
                    photoListAdapter.submitList(listOfPhoto)
                } catch (e: JSONException) {
                    XLog.e(e)
                    recyclerViewPhotos.visibility = View.GONE
                    buttonAddPhotos.visibility = View.VISIBLE
                }
            } else {
                recyclerViewPhotos.visibility = View.GONE
                buttonAddPhotos.visibility = View.VISIBLE
            }
        }
    }

    private fun closeKeyboardAndClearFocus() {
        requireActivity().currentFocus?.apply {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)
                ?.hideSoftInputFromWindow(windowToken, 0)
            clearFocus()
        }
    }

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): UpdateNoteFragmentBinding {
        return UpdateNoteFragmentBinding.inflate(inflater, container, false)
    }

    override fun onDrawerClick() {
        doUpdateCurrentNote(exitAfterUpdateDone = true)
    }

    override fun onMenuRightClick() {
        deleteNoteAndExit()
    }


    companion object {
        const val ADD_NOTE_PHOTO_REQUEST_CODE = 10001
    }

    override fun onDeleteItemClick(uri: String) {
        deleteNotePhoto()
        doUpdateNote(false)
    }

    private fun deleteNotePhoto() {
        if (updatingNote.photoPath.isNotEmpty()) {
            Utils.delFile(Uri.parse(updatingNote.photoPath))
        }
        updatingNote.photoPath = ""
    }

    override fun addPhotoClick() {
        pickNotePhotos()
    }

    override fun fabSaveNoteClick() {
        if (!textChanged && noteHasContent()) {
            doShareScreenshot()
        } else {
            doUpdateCurrentNote(exitAfterUpdateDone = false)
        }
    }
}