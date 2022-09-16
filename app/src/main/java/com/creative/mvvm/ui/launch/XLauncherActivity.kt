package com.creative.mvvm.ui.launch

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.creative.mvvm.R
import com.creative.mvvm.data.app.AppRepo
import com.creative.mvvm.databinding.ActivityLauncherBinding
import com.creative.mvvm.di.component.ActivityComponent
import com.creative.mvvm.event.IAPEvent
import com.creative.mvvm.google.AdmobHelper
import com.creative.mvvm.google.RemoteConfigHelper
import com.creative.mvvm.ui.base.BaseActivity
import com.creative.mvvm.utils.Utils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class XLauncherActivity : BaseActivity<ActivityLauncherBinding, XLauncherViewModel>() {

    @Inject
    lateinit var admobHelper: AdmobHelper

    @Inject
    lateinit var appRepo: AppRepo

    private val navController by lazy { findNavController(R.id.nav_host_container) }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        Utils.cancelAllNotification(this)

        viewBinding?.apply {
            navView.setupWithNavController(navController)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.openDrawerLiveData.observe(this) {
            if (it) {
                viewBinding?.drawerLayout?.openDrawer(GravityCompat.START, true)
            } else {
                viewBinding?.drawerLayout?.closeDrawer(GravityCompat.START)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        RemoteConfigHelper.fetch()
    }

    override fun onResume() {
        super.onResume()
        admobHelper.loadInterstitial()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onIAPEvent(event: IAPEvent) {
        if (event.state == IAPEvent.State.REMOVE_ADS_SUCCESS) {
            appRepo.setRemoveAds(true)
        }
    }

    override fun provideViewBinding(): ActivityLauncherBinding {
        return ActivityLauncherBinding.inflate(layoutInflater)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}