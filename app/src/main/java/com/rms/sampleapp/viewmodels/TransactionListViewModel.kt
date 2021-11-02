package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.ModelTransactionsList
import com.kachyng.rmssdk.repository.model.Terminal
import com.rms.sampleapp.utils.getTerminalId

class TransactionListViewModel(application: Application) : BaseViewModel(application) {

    val transactionList = MutableLiveData<ModelTransactionsList>()

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