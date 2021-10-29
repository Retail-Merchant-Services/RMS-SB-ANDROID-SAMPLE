package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rms.sampleapp.utils.SingleLiveEvent
import com.rms.sampleapp.utils.getTerminalId
import com.rms.sampleapp.utils.getTransactionId
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.constants.CurrencyCode
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.Terminal
import com.kachyng.rmssdk.repository.model.Transaction
import com.rms.sampleapp.R

class PayViewModel(application: Application) : BaseViewModel(application) {

//    private var transactionJob: Job? = null

    private val transactionOngoing = MutableLiveData<Boolean>()

    private val transactionInfo = MutableLiveData<Transaction>()

    private val transactionListInfo = MutableLiveData<List<Transaction>>()

    val scrollListToBottom = SingleLiveEvent<Boolean>()

    private var terminal: Terminal? = null

    fun updateTerminal(terminal: Terminal) {
        this.terminal = terminal
        RmsClient.setActiveTerminal(terminal.getTerminalId())
    }

    private fun makeTransaction(amount: Int, saleType: String, cashBackAmount: Int = 0) {
        transactionOngoing.value = true
        RmsClient.createTransaction(
            amount,
            CurrencyCode.POUND,
            saleType,
            cashBackAmount,
            object : RmsApiCallback<Transaction> {
                override fun success(data: Transaction) {
                    transactionOngoing.value = false
//                    transactionInfo.value = data

                    val list = arrayListOf<Transaction>()
                    list.addAll(transactionListInfo.value ?: emptyList())
                    list.add(data)
                    transactionListInfo.value = list
                    scrollListToBottom.value = true
                }

                override fun error(exception: RmsApiException) {
                    transactionOngoing.value = false
                    snackbarMessage.value = exception.message
                }
            })
    }

//    fun checkTransactionStatus() {
//        val transaction = transactionInfo.value
//        if (transaction == null) {
//            toastResId.value = R.string.no_transaction_made_yet
//            return
//        }
//        checkTransactionStatus(transaction)
//    }

    fun checkTransactionStatus(transaction: Transaction) {
        isShowLoader.value = true
        RmsClient.checkStatus(transaction.getTransactionId(),
            object : RmsApiCallback<Transaction> {
                override fun success(data: Transaction) {
                    isShowLoader.value = false
//                    transactionInfo.value = data

                    val list = arrayListOf<Transaction>()
                    transactionListInfo.value?.forEach { item ->
                        if (item.getTransactionId() == data.getTransactionId()) {
                            list.add(data)
                        } else {
                            list.add(item)
                        }
                    }
                    transactionListInfo.value = list
                }

                override fun error(exception: RmsApiException) {
                    isShowLoader.value = false
                    snackbarMessage.value = exception.message
                }
            })
    }

    fun onPayClick(amountText: String, saleType: String) {
        val amount = amountText.toIntOrNull()
        when {
            amount == null -> toastResId.value = R.string.error_invalid_input
            amount < 100 -> toastResId.value = R.string.error_invalid_amount
            else -> makeTransaction(amount, saleType)
        }
    }

    fun onCashBackPayClick(amountText: String, cashBackAmountText: String, saleType: String) {
        val amount = amountText.toIntOrNull()
        val cashBackAmount = cashBackAmountText.toIntOrNull()
        when {
            amount == null -> toastResId.value = R.string.error_invalid_input
            cashBackAmount == null -> toastResId.value = R.string.error_invalid_cashback_input
            amount < 100 -> toastResId.value = R.string.error_invalid_amount
            cashBackAmount < 100 -> toastResId.value = R.string.error_invalid_amount
            amount < cashBackAmount -> toastResId.value = R.string.error_cashback_amount_greater
            else -> makeTransaction(amount, saleType, cashBackAmount)
        }
    }

//    fun onCancelClick() {
//        if (transactionJob?.isActive == true) {
//            transactionJob?.cancel()
//            transactionOngoing.value = false
//            snackbarResId.value = R.string.transaction_cancelled
//        }
//    }

    fun getTransactionOngoing(): LiveData<Boolean> = transactionOngoing
    fun getTransactionListDetails(): LiveData<List<Transaction>> = transactionListInfo

}