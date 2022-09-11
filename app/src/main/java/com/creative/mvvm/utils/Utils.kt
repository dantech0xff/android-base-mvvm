package com.creative.mvvm.utils

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import com.creative.mvvm.R
import khangtran.preferenceshelper.PrefHelper
import java.io.File
import java.io.FileOutputStream

object Utils {
    val REQUIRED_PERMISSIONS_FOR_PICK = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun delFile(fileUri: Uri) {
        try {
            val path = if (fileUri.path != null) fileUri.path else ""
            val f = File(path!!)
            if (f.isFile && f.exists()) {
                f.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun cancelAllNotification(context: Context) {
        val nMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancelAll()
    }

    fun getPickImageIntent(ctx: Context): Intent {

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }

        ctx.let {
            intentList = addIntentsToList(it, intentList, pickIntent)
        }

        return pickIntent
    }

    @JvmStatic
    private fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    fun downloadUrl(context: Context):String {
        return "https://play.google.com/store/apps/details?id=" + context.packageName
    }

    fun shareScreenShoot(rootView: View, outPath: String, runnablePreShoot: Runnable? = null, runnablePostShoot: Runnable? = null) {
        val outFile = File(outPath)
        val out = FileOutputStream(outPath)
        rootView.apply {
            runnablePreShoot?.run()
        }.drawToBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out).apply {
            runnablePostShoot?.run()
        }
        out.flush()
        out.close()

        rootView.context.startActivity(
            Intent.createChooser(
                ShareCompat.IntentBuilder(rootView.context)
                    .setType("*/*")
                    .setStream(
                        FileProvider.getUriForFile(
                            rootView.context,
                            "com.creative.mvvm.FILE_PROVIDER", outFile
                        )
                    )
                    .setText(
                        rootView.context.getString(R.string.share_todo) + downloadUrl(
                            rootView.context
                        )
                    )
                    .intent, "Share for fun!"
            )
        )
    }
}