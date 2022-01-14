package com.rms.sampleapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsApiCallback
import com.kachyng.rmssdk.constants.CurrencyCode
import com.kachyng.rmssdk.exceptions.RmsApiException
import com.kachyng.rmssdk.repository.model.Terminal
import com.kachyng.rmssdk.repository.model.Transaction
import com.rms.sampleapp.R
import com.rms.sampleapp.utils.SingleLiveEvent
import com.rms.sampleapp.utils.getTerminalId
import com.rms.sampleapp.utils.getTransactionId

class PayViewModel(application: Application) : BaseViewModel(application) {

    private val transactionOngoing = MutableLiveData<Boolean>()

    private val transactionListInfo = MutableLiveData<List<Transaction>>()

    val onCancelTransactionSuccess = MutableLiveData<Boolean>()

    val scrollListToBottom = SingleLiveEvent<Boolean>()

    private var terminal: Terminal? = null

    fun updateTerminal(terminal: Terminal) {
        this.terminal = terminal
        // To Verify 11- ERROR_TERMINAL_NOT_FOUND
//        RmsClient.setActiveTerminal("ef5fba7f-4198-4599-9e9b-4aa1b059123f")
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
                override fun success(data: Transaction?) {
                    transactionOngoing.value = false
                    data?.let {
                        val list = arrayListOf<Transaction>()
                        list.addAll(transactionListInfo.value ?: emptyList())
                        list.add(data)
                        transactionListInfo.value = list
                        scrollListToBottom.value = true
                    }

                }

                override fun error(exception: RmsApiException) {
                    transactionOngoing.value = false
                    snackbarMessage.value = exception.apiError
                }
            })
    }

    fun checkTransactionStatus(transaction: Transaction) {
        isShowLoader.value = true
        // To Verify 14- ERROR_TRANSACTION_NOT_FOUND
//        RmsClient.checkStatus("ef5fba7f-4198-4599-9e9b-4aa1b059123f",

            // To Verify 12- ERROR_BAD_REQUEST
//        RmsClient.checkStatus("ef5fba7f-4198",

        // To Verify 13- ERROR_TERMINAL_NOT_FOUND
//        RmsClient.setActiveTerminal("ef5fba7f-4198-4599-9e9b-4aa1b059123f")

        // To Verify 15 B- ERROR_BAD_REQUEST
//        RmsClient.setActiveTerminal(transaction.terminalId)

        RmsClient.checkStatus(transaction.getTransactionId(),
            object : RmsApiCallback<Transaction> {
                override fun success(data: Transaction?) {
                    isShowLoader.value = false
                    data?.let {
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
                }

                override fun error(exception: RmsApiException) {
                    isShowLoader.value = false
                    snackbarMessage.value = exception.apiError
                }
            })
    }

    fun onPayClick(amountText: String, saleType: String) {
        val amount = amountText.toDoubleOrNull()
        when {
            amount == null -> toastResId.value = R.string.error_invalid_input
            amount < 1.0 -> toastResId.value = R.string.error_invalid_amount
            else -> makeTransaction((amount * 100).toInt(), saleType)
        }
    }

    fun onCashBackPayClick(amountText: String, cashBackAmountText: String, saleType: String) {
        val amount = amountText.toDoubleOrNull()
        val cashBackAmount = cashBackAmountText.toDoubleOrNull()
        when {
            amount == null -> toastResId.value = R.string.error_invalid_input
            amount < 1.0 -> toastResId.value = R.string.error_invalid_amount
            cashBackAmount == null -> toastResId.value = R.string.error_invalid_cashback_input
            cashBackAmount < 1.0 -> toastResId.value = R.string.error_invalid_amount
            amount < cashBackAmount -> toastResId.value = R.string.error_cashback_amount_greater
            else -> makeTransaction(
                (amount * 100).toInt(),
                saleType,
                (cashBackAmount * 100).toInt()
            )
        }
    }

    fun cancelTransaction(transaction: Transaction) {
        isShowLoader.value = true
        // To Verify 15 A- ERROR_BAD_REQUEST
//        RmsClient.setActiveTerminal("ef5fba7f-4198")

        // To Verify 16- ERROR_TERMINAL_NOT_FOUND
//        RmsClient.setActiveTerminal("ef5fba7f-4198-4599-9e9b-4aa1b059182d")

        RmsClient.setActiveTerminal(transaction.terminalId)

        // To Verify 17 - ERROR_TRANSACTION_NOT_FOUND
//        RmsClient.cancelTransaction("ef5fba7f-4198-4599-9e9b-4aa1b059182d",

        RmsClient.cancelTransaction(transaction.getTransactionId(),
            object : RmsApiCallback<Void> {
                override fun error(exception: RmsApiException) {
                    isShowLoader.value = false
                    snackbarMessage.value = exception.apiError
                }

                override fun success(data: Void?) {
                    isShowLoader.value = false
                    onCancelTransactionSuccess.value = true
                }
            })
    }


    fun getTransactionOngoing(): LiveData<Boolean> = transactionOngoing
    fun getTransactionListDetails(): LiveData<List<Transaction>> = transactionListInfo

}