package com.rms.sampleapp.views.interfaces

import com.kachyng.rmssdk.repository.model.Transaction

interface TransactionActionListener {
    fun onCancelTransaction(transaction: Transaction, position: Int)
    fun onRefreshClicked(transaction: Transaction)

}