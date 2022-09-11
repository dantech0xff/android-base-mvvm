package com.creative.mvvm.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.creative.mvvm.MvvmApp
import com.creative.mvvm.di.component.ActivityComponent
import com.creative.mvvm.di.component.DaggerActivityComponent
import com.creative.mvvm.di.module.ActivityModule
import com.creative.mvvm.utils.XToast
import javax.inject.Inject

abstract class BaseActivity<VB: ViewBinding, VM: BaseViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: VM
    protected var viewBinding: VB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildActivityComponent())

        super.onCreate(savedInstanceState)

        viewBinding = provideViewBinding().apply {
            setContentView(root)
        }

        setupObservers()

        setupView(savedInstanceState)
    }

    private fun buildActivityComponent() = DaggerActivityComponent.builder()
        .applicationComponent((application as MvvmApp).applicationComponent)
        .activityModule(ActivityModule(this))
        .build()

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
        XToast.show(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    protected abstract fun provideViewBinding(): VB

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)

    protected abstract fun setupView(savedInstanceState: Bundle?)
}