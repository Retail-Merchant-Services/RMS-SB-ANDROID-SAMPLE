package com.rms.sampleapp.views.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kachyng.rmssdk.constants.TransactionType
import com.kachyng.rmssdk.repository.model.Terminal
import com.kachyng.rmssdk.repository.model.Transaction
import com.rms.sampleapp.R
import com.rms.sampleapp.utils.hideKeyboard
import com.rms.sampleapp.viewmodels.PayViewModel
import com.rms.sampleapp.views.adapters.TransactionReceiptAdapter
import com.rms.sampleapp.views.interfaces.TransactionActionListener
import kotlinx.android.synthetic.main.fragment_pay.*

class PayFragment : BaseFragment<PayViewModel>() {

    companion object {

        private const val EXTRA_TERMINAL = "extra_terminal"

        fun newInstance(terminal: Terminal): PayFragment {
            val fragment = PayFragment()

            val bundle = Bundle()
            bundle.putParcelable(EXTRA_TERMINAL, terminal)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun provideBaseViewModel() = ViewModelProvider(this).get(PayViewModel::class.java)

    override fun getLayoutResource() = R.layout.fragment_pay

    private val receiptAdapter by lazy {
        TransactionReceiptAdapter(object : TransactionActionListener {
            override fun onCancelTransaction(transaction: Transaction, position: Int) {
                viewModel.cancelTransaction(transaction)
            }

            override fun onRefreshClicked(transaction: Transaction) {
                viewModel.checkTransactionStatus(transaction)
            }
        })
    }

    override fun init() {
        arguments?.getParcelable<Terminal>(EXTRA_TERMINAL)?.let { data ->
            viewModel.updateTerminal(data)
        }

        rvReceipts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = receiptAdapter
        }

        btPaySale.setOnClickListener {
            if (etEnterAmount.length() != 0) {
                viewModel.onPayClick(
                    etEnterAmount.cleanDoubleValue.toString(),
                    TransactionType.SALE
                )
            } else {
                showToastMessage(getString(R.string.error_invalid_input), true)
            }

        }

        btRefund.setOnClickListener {
            if (etEnterAmount.length() != 0) {
                viewModel.onPayClick(
                    etEnterAmount.cleanDoubleValue.toString(),
                    TransactionType.REFUND
                )
            } else {
                showToastMessage(getString(R.string.error_invalid_input), true)
            }
        }

        btCashback.setOnClickListener {
            when {
                etEnterAmount.length() == 0 -> showToastMessage(
                    getString(R.string.error_invalid_input),
                    true
                )
                etEnterCashBack.length() == 0 -> showToastMessage(
                    getString(R.string.error_invalid_amount),
                    true
                )
                else -> {
                    viewModel.onCashBackPayClick(
                        etEnterAmount.cleanDoubleValue.toString(),
                        etEnterCashBack.cleanDoubleValue.toString(),
                        TransactionType.SALE,
                    )
                }
            }

        }


//        btCancel.setOnClickListener {
//            viewModel.onCancelClick()
//        }
//        btCheckStatus.setOnClickListener {
//            viewModel.checkTransactionStatus()
//        }
    }

    override fun registerObservables() {
        viewModel.getTransactionOngoing().observe(this) {
            when (it) {
                true -> {
                    btPaySale.visibility = View.GONE
                    btRefund.visibility = View.GONE
                    btCashback.visibility = View.GONE
                    btInProgress.visibility = View.VISIBLE
                    etEnterAmount.hideKeyboard()
//                    btCancel.visibility = View.VISIBLE
                }
                else -> {
                    btPaySale.visibility = View.VISIBLE
                    btRefund.visibility = View.VISIBLE
                    btCashback.visibility = View.VISIBLE
                    btInProgress.visibility = View.GONE
//                    btCancel.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.getTransactionListDetails().observe(viewLifecycleOwner) {
            it?.let { data ->
                receiptAdapter.updateData(data)
            }
        }

        viewModel.scrollListToBottom.observe(viewLifecycleOwner) {
            if (it == true) {
                rvReceipts.smoothScrollToPosition(rvReceipts.adapter?.itemCount ?: 0 - 1)
            }
        }


        viewModel.onCancelTransactionSuccess.observe(viewLifecycleOwner) {
            it?.let { isCancelTransactionSuccess ->
                if (isCancelTransactionSuccess) {
                    showSnackbarMessage(getString(R.string.st_cancellation_success_message))
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "Create Transaction"
    }
}