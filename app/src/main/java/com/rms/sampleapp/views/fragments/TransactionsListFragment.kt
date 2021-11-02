package com.rms.sampleapp.views.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kachyng.rmssdk.repository.model.Terminal
import com.kachyng.rmssdk.repository.model.Transaction
import com.rms.sampleapp.R
import com.rms.sampleapp.utils.getTerminalId
import com.rms.sampleapp.viewmodels.TransactionListViewModel
import com.rms.sampleapp.views.adapters.TransactionStageListAdapter
import com.rms.sampleapp.views.adapters.TransactionStatusListAdapter
import com.rms.sampleapp.views.adapters.TransactionsListApiAdapter
import com.rms.sampleapp.views.interfaces.TransactionActionListener
import com.rms.sampleapp.views.interfaces.TransactionFilterListener
import kotlinx.android.synthetic.main.filter_list_view.*
import kotlinx.android.synthetic.main.fragment_search_transaction.*
import kotlinx.android.synthetic.main.fragment_transactions_list.*
import kotlinx.android.synthetic.main.fragment_transactions_list.etSearch
import kotlinx.android.synthetic.main.fragment_transactions_list.rvTerminals
import kotlinx.android.synthetic.main.fragment_transactions_list.tvFilter
import kotlinx.android.synthetic.main.fragment_transactions_list.tvNoData

class TransactionsListFragment : BaseFragment<TransactionListViewModel>() {

    companion object {
        private const val EXTRA_TERMINAL_DETAILS = "extra_terminal_details"

        fun newInstance(terminalDetails: Terminal): TransactionsListFragment {
            val fragment = TransactionsListFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_TERMINAL_DETAILS, terminalDetails)
            fragment.arguments = bundle

            return fragment
        }
    }

    private var isFilterVisible = false
    private var transactionStatus = ""
    private var transactionType = ""
    private var terminalId = ""
    private var terminal: Terminal? = null

    override fun provideBaseViewModel() =
        ViewModelProvider(this).get(TransactionListViewModel::class.java)

    private val transactionAdapter by lazy {
        TransactionsListApiAdapter(object : TransactionActionListener {
            override fun onCancelTransaction(transaction: Transaction, position: Int) {

            }

            override fun onRefreshClicked(transaction: Transaction) {

            }

        })
    }

    private val transactionStatusAdapter by lazy {
        TransactionStatusListAdapter(object : TransactionFilterListener {
            override fun onTransactionStatusListener(status: String) {
                transactionStatus = status
            }

            override fun onTransactionTypeListener(type: String) {

            }

        })
    }

    private val transactionStageListAdapter by lazy {
        TransactionStageListAdapter(object : TransactionFilterListener {
            override fun onTransactionStatusListener(status: String) {

            }

            override fun onTransactionTypeListener(type: String) {
                transactionType = type
            }

        })
    }


    override fun getLayoutResource() = R.layout.fragment_transactions_list

    override fun init() {
        //Get arguments
        arguments?.getParcelable<Terminal>(EXTRA_TERMINAL_DETAILS)?.let { terminalDetails ->
            viewModel.fetchTransactionsList(terminalDetails)
            terminal = terminalDetails
            terminalId = terminalDetails.getTerminalId()

        }

        //Set search action
        etSearch.requestFocus()
        etSearch.isFocusableInTouchMode = true
        showSoftKeyboard()

        etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //Api call
                viewModel.searchTransactionById(
                    etSearch.text.toString().trim()
                )
                return@OnEditorActionListener true
            }
            false
        })


        //Set click listener for search
        btnSearch.setOnClickListener {
            clFilterContentView.visibility = View.GONE
            tvFilter.text = getString(R.string.st_filter)
            viewModel.searchTransactions(terminalId, transactionStatus, transactionType)

        }

        //Set click listener for reset
        btnReset.setOnClickListener {
            clFilterContentView.visibility = View.GONE
            tvFilter.text = getString(R.string.st_filter)

            //Reset filters
            transactionStatusAdapter.updateData(-1)
            transactionStageListAdapter.updateData(-1)
            viewModel.fetchTransactionsList(terminal!!)

        }

        //Set click listener for filter
        tvFilter.setOnClickListener {
            if (isFilterVisible) {
                tvFilter.text = getString(R.string.st_filter)
                clFilterContentView.visibility = View.GONE
                isFilterVisible = false
            } else {
                tvFilter.text = getString(R.string.st_close)
                clFilterContentView.visibility = View.VISIBLE
                isFilterVisible = true
            }
        }


        //Transaction list adapter
        rvTerminals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = transactionAdapter
        }

        //Transaction status filter adapter
        rvTransactionStatus.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = transactionStatusAdapter
        }

        //Transaction stage filter adapter
        rvTransactionStage.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = transactionStageListAdapter
        }

    }

    override fun registerObservables() {
        viewModel.transactionList.observe(viewLifecycleOwner) {
            if (it._embedded != null) {
                it?._embedded?.transactions?.let { terminalList ->
                    transactionAdapter.updateData(terminalList)
                }
                rvTerminals.visibility = View.VISIBLE
                tvNoData.visibility = View.GONE
            } else {
                rvTerminals.visibility = View.GONE
                tvNoData.visibility = View.VISIBLE
            }

        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "List of transactions"
    }
}