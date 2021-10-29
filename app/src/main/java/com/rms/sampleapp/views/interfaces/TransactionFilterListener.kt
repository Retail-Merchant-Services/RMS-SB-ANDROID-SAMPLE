package com.rms.sampleapp.views.interfaces

interface TransactionFilterListener {
    fun onTransactionStatusListener(status:String)
    fun onTransactionTypeListener(type:String)

}