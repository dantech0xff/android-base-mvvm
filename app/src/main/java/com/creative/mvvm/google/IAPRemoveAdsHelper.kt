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
    private var productDetails: ProductDetails? = null

    init {
        init()
    }

    private fun init() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryProductDetailsParams =
                        QueryProductDetailsParams.newBuilder()
                            .setProductList(
                                listOf(
                                    QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId(COM_CREATIVE_MVVM_REMOVE_ADS)
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build()
                                )
                            )
                            .build()
                    billingClient.queryProductDetailsAsync(queryProductDetailsParams) { _billingResult,
                                                                                        productDetailsList ->
                        if (_billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            productDetailsList.forEach {
                                if (it.productId == COM_CREATIVE_MVVM_REMOVE_ADS) {
                                    productDetails = it
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
        if (productDetails == null) {
            XToast.show(activity, activity.getString(R.string.no_internet_error))
            init()
            return
        }

        productDetails?.let { product ->
            billingClient.launchBillingFlow(
                activity, BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(product)
                                .build()
                        )
                    ).build()
            )
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
                    if (purchase.products.contains(COM_CREATIVE_MVVM_REMOVE_ADS)) {
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
                if (purchase.products.contains(COM_CREATIVE_MVVM_REMOVE_ADS)) {
                    EventBus.getDefault().post(IAPEvent(IAPEvent.State.REMOVE_ADS_SUCCESS))
                    if (!purchase.isAcknowledged) {
                        billingClient.acknowledgePurchase(
                            AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken)
                                .build()
                        ) {}
                    }
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