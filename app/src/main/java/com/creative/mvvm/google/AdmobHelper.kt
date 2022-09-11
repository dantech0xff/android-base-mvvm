package com.creative.mvvm.google

import android.app.Activity
import android.app.Application
import com.creative.mvvm.data.app.AppRepo
import com.creative.mvvm.utils.XLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdmobHelper @Inject constructor(private val appContext: Application, private val appRepo: AppRepo) {

    companion object {
        const val LIMIT = 3
        const val DELAY_NEXT_SHOW_AD = 60
    }

    private var interstitialAd: InterstitialAd? = null
    private var countReload: Int = 0
    private var lastTimeShowAd: Long = -1

    fun loadInterstitial() {
        if (countReload >= LIMIT || appRepo.appRemoveAds()) {
            return
        }

        countReload += 1
        interstitialAd = null
        val startLoad = System.currentTimeMillis()
        InterstitialAd.load(
            appContext,
            RemoteConfigHelper.getInterstitialId(),

            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    AndroidSchedulers.mainThread().scheduleDirect({
                        loadInterstitial()
                    }, 1000, TimeUnit.MILLISECONDS)
                    XLog.d("AdmobHelper#onAdFailedToLoad $adError")
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@AdmobHelper.interstitialAd = interstitialAd
                    this@AdmobHelper.countReload = 0
                    XLog.d("AdmobHelper#onAdLoad $interstitialAd ${System.currentTimeMillis() - startLoad}")
                }
            }
        )
    }

    fun showInterstitial(activity: Activity) {
        if ((System.currentTimeMillis() - lastTimeShowAd) / 1000 > DELAY_NEXT_SHOW_AD) {
            interstitialAd?.apply {
                fullScreenContentCallback = object :
                    FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        this@AdmobHelper.loadInterstitial()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {}
                }
                show(activity)
                lastTimeShowAd = System.currentTimeMillis()
            }
            interstitialAd = null
        }
    }
}