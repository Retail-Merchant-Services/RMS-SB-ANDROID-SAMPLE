package com.rms.sampleapp.viewmodels

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rms.sampleapp.utils.SingleLiveEvent

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val resources by lazy { application.resources }

    val isShowLoader = MutableLiveData<Boolean>()

    val snackbarMessage = SingleLiveEvent<String>()
    val toastMessage = SingleLiveEvent<String>()

    val snackbarResId = SingleLiveEvent<@StringRes Int>()
    val toastResId = SingleLiveEvent<@StringRes Int>()

    val backAction = SingleLiveEvent<Boolean>()

    protected fun getContext(): Context = getApplication()

    protected fun goBack() {
        backAction.value = true
    }
}