package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.*
import com.rms.sampleapp.utils.getTerminalId

class TransactionListViewModel(application: Application) : BaseViewModel(application) {

    val transactionList = MutableLiveData<ModelTransaction>()

    fun fetchTransactionsList(terminal: Terminal) {
        isShowLoader.value = true
        RmsClient.setActiveTerminal(terminal.getTerminalId())
        RmsClient.getTransactionsList(object : RmsApiCallback<ModelTransaction> {
            override fun error(exception: RmsApiException) {
                isShowLoader.value = false
                snackbarMessage.value = exception.message
            }

            override fun success(data: ModelTransaction) {
                isShowLoader.value = false
                transactionList.value = data
            }
        })
    }


    fun searchTransactions(terminalId: String, transactionStatus: String, transactionType: String) {
        isShowLoader.value = true
        RmsClient.searchTransactions(terminalId = terminalId,
            transactionStatus = transactionStatus,
            transactionType = transactionType,
            callback = object : RmsApiCallback<ModelTransaction> {
                override fun error(exception: RmsApiException) {
                    isShowLoader.value = false
                    snackbarMessage.value = exception.message
                }

                override fun success(data: ModelTransaction) {
                    isShowLoader.value = false
                    transactionList.value = data
                }
            })
    }

    fun searchTransactionById(transactionId: String) {
        if (transactionId.isEmpty()) return
        isShowLoader.value = true
        RmsClient.searchTransactionById(transactionId,
            object : RmsApiCallback<Transaction> {
                override fun error(exception: RmsApiException) {
                    isShowLoader.value = false
                    snackbarMessage.value = exception.message
                }

                override fun success(data: Transaction) {
                    isShowLoader.value = false
                    val transactionsList = listOf(data)
                    val embeddedTransactions = EmbeddedTransactions(transactionsList)

                    transactionList.value = ModelTransaction(
                        embeddedTransactions,
                        OuterLink(
                            Url("", null), Url("", null),
                            Url("", null), Url("", null),
                            Url("", null)
                        )
                    )
                }
            })
    }

}