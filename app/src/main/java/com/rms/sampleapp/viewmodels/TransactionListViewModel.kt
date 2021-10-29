package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rms.sampleapp.utils.SingleLiveEvent
import com.rms.sampleapp.utils.getTerminalId
import com.rms.sampleapp.utils.getTransactionId
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.exceptions.RmsApiError
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.ModelTransactionsList
import com.kachyng.rmssdk.repository.model.Terminal
import com.kachyng.rmssdk.repository.model.Transaction

class TransactionListViewModel(application: Application) : BaseViewModel(application) {

    val transactionList = MutableLiveData<ModelTransactionsList>()
    val onCancelTransactionSuccess = SingleLiveEvent<Boolean>()

    fun fetchTransactionsList(terminal: Terminal) {
        isShowLoader.value = true
        RmsClient.setActiveTerminal(terminal.getTerminalId())
        RmsClient.getTransactionsList(object : RmsApiCallback<ModelTransactionsList> {
            override fun error(exception: RmsApiException) {
                isShowLoader.value = false
                snackbarMessage.value = exception.message
            }

            override fun success(data: ModelTransactionsList) {
                isShowLoader.value = false
                transactionList.value = data
            }
        })
    }

    fun cancelTransaction(transaction: Transaction) {
        isShowLoader.value = true
        RmsClient.setActiveTerminal(transaction.terminalId)
        RmsClient.cancelTransaction(transaction.getTransactionId(),
            object : RmsApiCallback<Void> {
                override fun error(exception: RmsApiException) {
                    isShowLoader.value = false
                    if (exception.apiError.toString() == RmsApiError.ERROR_NO_CONTENT.toString()) {
                        onCancelTransactionSuccess.value = true
                    } else {
                        onCancelTransactionSuccess.value = false
                        snackbarMessage.value = exception.message
                    }
                }

                override fun success(data: Void) {
                    isShowLoader.value = false
                    onCancelTransactionSuccess.value = true
                }
            })
    }

    fun searchTransaction(terminalId: String, transactionStatus: String, transactionType: String) {
        isShowLoader.value = true
        RmsClient.searchTransactions(terminalId = terminalId,
            transactionStatus = transactionStatus,
            transactionType = transactionType,
            callback = object : RmsApiCallback<ModelTransactionsList> {
                override fun error(exception: RmsApiException) {
                    isShowLoader.value = false
                    snackbarMessage.value = exception.message
                }

                override fun success(data: ModelTransactionsList) {
                    isShowLoader.value = false
                    transactionList.value = data
                }
            })
    }

}