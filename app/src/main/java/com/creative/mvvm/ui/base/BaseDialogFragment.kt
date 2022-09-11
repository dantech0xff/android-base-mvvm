package com.creative.mvvm.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.creative.mvvm.MvvmApp
import com.creative.mvvm.di.component.DaggerDialogFragmentComponent
import com.creative.mvvm.di.component.DialogFragmentComponent
import com.creative.mvvm.di.module.DialogFragmentModule
import com.creative.mvvm.utils.XToast
import javax.inject.Inject

abstract class BaseDialogFragment<VB: ViewBinding, VM: BaseViewModel> : DialogFragment() {

    @Inject
    lateinit var viewModel: VM
    protected var viewBinding: VB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        viewBinding = provideViewBinding(inflater, container)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view, savedInstanceState)
    }


    protected open fun setupObservers() {
        viewModel.messageString.observe(this) {
            it.run {
                showMess(this)
            }
        }
        viewModel.messageStringId.observe(this) {
            it.run {
                showMess(getString(it))
            }
        }
    }

    open fun showMess(msg: String) {
        XToast.show(requireContext(), msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun buildFragmentComponent(): DialogFragmentComponent =
        DaggerDialogFragmentComponent.builder().applicationComponent((requireContext().applicationContext as MvvmApp).applicationComponent)
            .dialogFragmentModule(DialogFragmentModule(this))
            .build()

    protected abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun injectDependencies(buildFragmentComponent: DialogFragmentComponent)

    protected abstract fun setupView(view: View, savedInstanceState: Bundle?)
}