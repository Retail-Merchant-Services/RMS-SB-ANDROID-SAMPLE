package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rms.sampleapp.utils.SingleLiveEvent
import com.rms.sampleapp.utils.getTerminalId
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.exceptions.RmsApiError
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.Terminal

class TerminalDetailsViewModel(application: Application) : BaseViewModel(application) {

    val terminalDetails = MutableLiveData<Terminal>()
    val viewPayScreen = SingleLiveEvent<Terminal>()
    val viewTransactionListScreen = SingleLiveEvent<Terminal>()
    val onReportSuccess = SingleLiveEvent<Boolean>()

    fun fetchTerminalDetails(terminal: Terminal) {
        if (terminalDetails.value != null) return
        isShowLoader.value = true
        RmsClient.setActiveTerminal(terminal.getTerminalId())
        RmsClient.getTerminalDetails(object : RmsApiCallback<Terminal> {
            override fun success(data: Terminal) {
                isShowLoader.value = false
                terminalDetails.value = data
            }

            override fun error(exception: RmsApiException) {
                isShowLoader.value = false
                snackbarMessage.value = exception.message
            }

        })
    }

    fun onTransactionsClicked() {
        terminalDetails.value?.let { terminal ->
            viewTransactionListScreen.value = terminal
        }
    }

    fun onReportsClicked(terminalId: String, reportType: String) {
        isShowLoader.value = true
        RmsClient.setActiveTerminal(terminalId)
        RmsClient.requestReportByType(reportType, object : RmsApiCallback<Void> {
            override fun error(exception: RmsApiException) {
                isShowLoader.value = false
                if (exception.apiError.toString() == RmsApiError.ERROR_NO_CONTENT.toString()) {
                    onReportSuccess.value = true
                } else {
                    onReportSuccess.value = false
                    snackbarMessage.value = exception.message
                }
            }

            override fun success(data: Void) {
                isShowLoader.value = false
                onReportSuccess.value = true
            }

        })
    }

}