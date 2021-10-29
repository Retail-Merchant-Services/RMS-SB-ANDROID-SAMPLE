package com.rms.sampleapp.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.kachyng.rmssdk.repository.model.Terminal
import com.kachyng.rmssdk.repository.model.Transaction

fun EditText.hideKeyboard() {
    clearFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Terminal.getTerminalId(): String {
    return _links.self.href.split("/").last()
}

fun Transaction.getTransactionId(): String {
    return _links.self.href.split("/").last()
}