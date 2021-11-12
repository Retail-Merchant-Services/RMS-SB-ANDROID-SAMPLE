package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.ModelTerminal
import com.kachyng.rmssdk.repository.model.Terminal
import com.rms.sampleapp.utils.SingleLiveEvent

class TerminalListViewModel(application: Application) : BaseViewModel(application) {

    val terminalList = MutableLiveData<ModelTerminal>()
    val viewTerminalDetails = SingleLiveEvent<Terminal>()
    val searchTransaction = SingleLiveEvent<Boolean>()
    val viewPayScreen = SingleLiveEvent<Terminal>()

    init {
        fetchTerminalsList()
    }

    private fun fetchTerminalsList() {
        isShowLoader.value = true
        RmsClient.getTerminalsList(object : RmsApiCallback<ModelTerminal> {
            override fun success(data: ModelTerminal?) {
                isShowLoader.value = false
                data?.let {
                    terminalList.value = data
                }
            }

            override fun error(exception: RmsApiException) {
                isShowLoader.value = false
                snackbarMessage.value = exception.apiError.message
            }
        })
    }

    fun viewTerminal(terminal: Terminal) {
        viewTerminalDetails.value = terminal
    }

    fun searchTransaction() {
        searchTransaction.value = true
    }

    fun openPayScreen(terminal: Terminal) {
        viewPayScreen.value = terminal
    }

}