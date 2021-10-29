package com.rms.sampleapp.views.activities

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.rms.sampleapp.viewmodels.BaseViewModel

abstract class BaseViewModelActivity<BVM : BaseViewModel> : BaseActivity() {

    val viewModel by lazy { provideBaseViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTransition()
        setContentView(getLayoutResource())
        init()
        baseObservables()
        registerObservables()
    }

    private fun baseObservables() {
        viewModel.isShowLoader.observe(this, Observer {
            when (it) {
                true -> showProgressDialog()
                false -> hideProgressDialog()
            }
        })
        viewModel.snackbarMessage.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                showSnackbarMessage(it, isLong = true)
            }
        })
        viewModel.toastMessage.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                showToastMessage(it, isLong = true)
            }
        })
        viewModel.snackbarResId.observe(this, Observer {
            if (it != null) {
                showSnackbarMessage(getString(it), isLong = true)
            }
        })
        viewModel.toastResId.observe(this, Observer {
            if (it != null) {
                showToastMessage(getString(it), isLong = true)
            }
        })
        viewModel.backAction.observe(this, Observer {
            if (it == true) {
                onBackPressed()
            }
        })
    }

    open fun applyTransition() {

    }

    @LayoutRes
    abstract fun getLayoutResource(): Int

    abstract fun init()

    abstract fun provideBaseViewModel(): BVM

    abstract fun registerObservables()
}