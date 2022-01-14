package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.Terminal
import com.rms.sampleapp.utils.SingleLiveEvent
import com.rms.sampleapp.utils.getTerminalId

class TerminalDetailsViewModel(application: Application) : BaseViewModel(application) {

    val terminalDetails = MutableLiveData<Terminal>()
    val viewPayScreen = SingleLiveEvent<Terminal>()
    val viewTransactionListScreen = SingleLiveEvent<Terminal>()
    val onReportSuccess = SingleLiveEvent<Boolean>()

    fun fetchTerminalDetails(terminal: Terminal) {
        if (terminalDetails.value != null) return
        isShowLoader.value = true
        //To verify ERROR_INVALID_TID / ERROR_BAD_REQUEST
//        RmsClient.setActiveTerminal("dwerwer34fc")

        //To verify ERROR_TERMINAL_NOT_FOUND / ERROR_BAD_REQUEST
//        RmsClient.setActiveTerminal("ef5fba7f-4198-4599-9e9b-4aa1b059182d")

        //To verify 6 - ERROR_INVALID_TOKEN
//        RmsClient.setTokens("accessToken", "refreshToken")

        RmsClient.setActiveTerminal(terminal.getTerminalId())
        RmsClient.getTerminalDetails(object : RmsApiCallback<Terminal> {
            override fun success(data: Terminal?) {
                isShowLoader.value = false
                data?.let {
                    terminalDetails.value = data
                }
            }

            override fun error(exception: RmsApiException) {
                isShowLoader.value = false
                snackbarMessage.value = exception.apiError
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

        // To verify ERROR_TERMINAL_NOT_FOUND
//        RmsClient.setActiveTerminal("ef5fba7f-4198-4599-9e9b-4aa1b059182d")
        RmsClient.setActiveTerminal(terminalId)

        // To verify ERROR_INVALID_REPORT_TYPE
//        RmsClient.requestReportByType("reportType", object : RmsApiCallback<Void> {

        RmsClient.requestReportByType(reportType, object : RmsApiCallback<Void> {
            override fun error(exception: RmsApiException) {

                isShowLoader.value = false
                snackbarMessage.value = exception.apiError
            }

            override fun success(data: Void?) {
                isShowLoader.value = false
                onReportSuccess.value = true
            }

        })
    }

}