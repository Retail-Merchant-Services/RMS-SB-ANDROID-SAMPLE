package com.rms.sampleapp.views.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.rms.sampleapp.R

class TransparentProgressDialog(context: Context) :
    Dialog(context, R.style.TransparentProgressDialog) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.attributes?.gravity = Gravity.CENTER_HORIZONTAL
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)

        val layout = FrameLayout(context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val progressBar = ProgressBar(context)
        layout.addView(progressBar, params)
//        layout.addView(ProgressBar(context), params)
        addContentView(layout, params)
    }
}