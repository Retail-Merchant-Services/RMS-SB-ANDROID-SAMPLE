package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rms.sampleapp.utils.SingleLiveEvent
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.Terminal
import com.kachyng.rmssdk.repository.model.ModelTerminalList

class TerminalListViewModel(application: Application) : BaseViewModel(application) {

    val terminalList = MutableLiveData<ModelTerminalList>()
    val viewTerminalDetails = SingleLiveEvent<Terminal>()
    val searchTransaction = SingleLiveEvent<Boolean>()
    val viewPayScreen = SingleLiveEvent<Terminal>()

    init {
        fetchTerminalsList()
    }

    private fun fetchTerminalsList() {
        isShowLoader.value = true
        RmsClient.getTerminalsList(object : RmsApiCallback<ModelTerminalList> {
            override fun success(data: ModelTerminalList) {
                isShowLoader.value = false
                terminalList.value = data
            }

            override fun error(exception: RmsApiException) {
                isShowLoader.value = false
                snackbarMessage.value = exception.message
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