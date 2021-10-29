package com.rms.sampleapp.views.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.ViewModelProvider
import com.rms.sampleapp.R
import com.rms.sampleapp.utils.getTerminalId
import com.rms.sampleapp.viewmodels.TerminalDetailsViewModel
import com.kachyng.rmssdk.constants.ReportType
import com.kachyng.rmssdk.constants.TerminalStatus
import com.kachyng.rmssdk.repository.model.Terminal
import kotlinx.android.synthetic.main.fragment_terminal_details.*

class TerminalDetailsFragment : BaseFragment<TerminalDetailsViewModel>() {
    companion object {

        private const val EXTRA_TERMINAL_DETAILS = "extra_terminal_details"

        fun newInstance(terminalDetails: Terminal): TerminalDetailsFragment {
            val fragment = TerminalDetailsFragment()

            val bundle = Bundle()
            bundle.putParcelable(EXTRA_TERMINAL_DETAILS, terminalDetails)
            fragment.arguments = bundle

            return fragment
        }
    }

    private var terminalId = ""
    private var reportType=""

    override fun provideBaseViewModel() =
        ViewModelProvider(this).get(TerminalDetailsViewModel::class.java)

    override fun getLayoutResource() = R.layout.fragment_terminal_details

    override fun init() {
        arguments?.getParcelable<Terminal>(EXTRA_TERMINAL_DETAILS)?.let { terminalDetails ->
            viewModel.fetchTerminalDetails(terminalDetails)
            terminalId = terminalDetails.getTerminalId()
        }

        //Click listener
        btListTransaction.setOnClickListener {
            viewModel.onTransactionsClicked()
        }

        btReportsXBAL.setOnClickListener {
            reportType=ReportType.XBAL
            viewModel.onReportsClicked(terminalId, ReportType.XBAL)
        }

        btReportsZBAL.setOnClickListener {
            reportType=ReportType.ZBAL
            viewModel.onReportsClicked(terminalId, ReportType.ZBAL)
        }

        btReportsEOD.setOnClickListener {
            reportType=ReportType.EOD
            viewModel.onReportsClicked(terminalId, ReportType.EOD)
        }
    }

    override fun registerObservables() {
        viewModel.terminalDetails.observe(viewLifecycleOwner) {
            it?.let { terminalDetails ->

                tvManufacturer.text = Html.fromHtml(
                    "<b>Manufacturer: </b>" + terminalDetails.manufacturer,
                    Html.FROM_HTML_MODE_LEGACY
                )
                tvTerminalName.text = Html.fromHtml(
                    "<b>Terminal Name: </b>" + terminalDetails.terminalName,
                    Html.FROM_HTML_MODE_LEGACY
                )
                tvPos.text = Html.fromHtml(
                    "<b>Pos: </b>" + terminalDetails.pos,
                    Html.FROM_HTML_MODE_LEGACY
                )
                tvTerminalStatus.text = Html.fromHtml(
                    "<b>Status: </b>" + terminalDetails.terminalStatus,
                    Html.FROM_HTML_MODE_LEGACY
                )

                TextViewCompat.setCompoundDrawableTintList(
                    tvTerminalStatus,
                    ColorStateList.valueOf(
                        requireContext().resources.getColor(
                            when (terminalDetails.terminalStatus) {
                                TerminalStatus.OFFLINE -> R.color.colorOffline
                                TerminalStatus.READY -> R.color.colorOnline
                                TerminalStatus.PROCESSING -> R.color.purple_500
                            }, null
                        )
                    )
                )
            }
        }
        viewModel.viewPayScreen.observe(viewLifecycleOwner) {
            it?.let { terminal ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, PayFragment.newInstance(terminal))
                    .addToBackStack(TerminalDetailsFragment::class.simpleName)
                    .commit()
            }
        }

        viewModel.viewTransactionListScreen.observe(viewLifecycleOwner) {
            it?.let { terminal ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, TransactionsListFragment.newInstance(terminal))
                    .addToBackStack(TerminalDetailsFragment::class.simpleName)
                    .commit()
            }
        }

        viewModel.onReportSuccess.observe(viewLifecycleOwner) {
            it?.let { isReportSuccess ->
                if (isReportSuccess){
                    (reportType+getString(R.string.st_reports_message)).also { tvReportsMessage.text = it }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "Terminal Details"
    }
}