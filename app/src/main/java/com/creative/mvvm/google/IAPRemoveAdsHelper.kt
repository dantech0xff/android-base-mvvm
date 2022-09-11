package com.creative.mvvm.google

import android.app.Activity
import android.app.Application
import android.text.TextUtils
import com.android.billingclient.api.*
import com.creative.mvvm.R
import com.creative.mvvm.event.IAPEvent
import com.creative.mvvm.utils.XLog
import com.creative.mvvm.utils.XToast
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IAPRemoveAdsHelper @Inject constructor(private val context: Application) : PurchasesUpdatedListener {

    private val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(context).setListener(this).enablePendingPurchases().build()
    }
    private var skuRemoveAdsDetail: SkuDetails? = null

    init {
        init()
    }

    private val isBillingReady: Boolean
        get() = skuRemoveAdsDetail != null && billingClient.isReady

    private fun init() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val params = SkuDetailsParams.newBuilder()
                    val skuList = ArrayList<String>()
                    skuList.add(COM_CREATIVE_MVVM_REMOVE_ADS)
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
                    billingClient.querySkuDetailsAsync(params.build()) { _billingResult, skuDetailsList ->
                        if (_billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null) {
                                for (skuDetail in skuDetailsList) {
                                    if (skuDetail != null && !TextUtils.isEmpty(skuDetail.sku)) {
                                        if (skuDetail.sku == COM_CREATIVE_MVVM_REMOVE_ADS) {
                                            skuRemoveAdsDetail = skuDetail
                                            XLog.d("skuRemoveAdsDetail " + skuRemoveAdsDetail.toString())
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }

    fun makePurchaseRemoveAds(activity: Activity) {
        try {
            if (isBillingReady) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuRemoveAdsDetail!!)
                    .build()
                billingClient.launchBillingFlow(activity, flowParams)
            } else {
                XToast.show(activity, activity.getString(R.string.no_internet_error))
                init()
            }
        } catch (e: Exception) {
            XLog.e(e)
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            EventBus.getDefault().post(IAPEvent(IAPEvent.State.CANCEL))
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            if (purchases != null) {
                for (purchase in purchases) {
                    if (purchase.sku == COM_CREATIVE_MVVM_REMOVE_ADS) {
                        EventBus.getDefault().post(IAPEvent(IAPEvent.State.REMOVE_ADS_SUCCESS))
                    }
                }
            } else {
                EventBus.getDefault().post(IAPEvent(IAPEvent.State.REMOVE_ADS_SUCCESS))
            }
        } else {
            EventBus.getDefault().post(IAPEvent(IAPEvent.State.ERROR))
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        try {
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (purchase.sku == COM_CREATIVE_MVVM_REMOVE_ADS && !purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) {

                    }
                }
                if (purchase.sku == COM_CREATIVE_MVVM_REMOVE_ADS) {
                    EventBus.getDefault().post(IAPEvent(IAPEvent.State.REMOVE_ADS_SUCCESS))
                }
            }
        } catch (e: Exception) {
            XLog.e(e)
            EventBus.getDefault().post(IAPEvent(IAPEvent.State.ERROR))
        }
    }

    companion object {
        const val COM_CREATIVE_MVVM_REMOVE_ADS = "com.creative.mvvm.remove_ads"
    }
}