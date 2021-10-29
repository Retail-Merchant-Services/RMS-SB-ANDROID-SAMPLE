package com.rms.sampleapp.views.fragments

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rms.sampleapp.R
import com.rms.sampleapp.viewmodels.TerminalListViewModel
import com.rms.sampleapp.views.adapters.TerminalListApiAdapter
import com.rms.sampleapp.views.interfaces.TerminalActionListener
import com.kachyng.rmssdk.repository.model.Terminal
import kotlinx.android.synthetic.main.fragment_terminals_list.*

class TerminalsListFragment : BaseFragment<TerminalListViewModel>() {

    override fun provideBaseViewModel() =
        ViewModelProvider(this).get(TerminalListViewModel::class.java)

    private val terminalAdapter by lazy {
        TerminalListApiAdapter(object : TerminalActionListener {
            override fun onViewClicked(terminal: Terminal) {
                viewModel.openPayScreen(terminal)
            }

            override fun onDetailClicked(terminal: Terminal) {
                viewModel.viewTerminal(terminal)
            }
        })
    }

    override fun getLayoutResource() = R.layout.fragment_terminals_list

    override fun init() {
        rvTerminals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = terminalAdapter
        }
    }

    override fun registerObservables() {
        viewModel.terminalList.observe(viewLifecycleOwner) {
            it?._embedded?.terminals?.let { terminalList ->
                terminalAdapter.updateData(terminalList)
            }
        }
        viewModel.viewTerminalDetails.observe(viewLifecycleOwner) {
            it?.let { terminalId ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, TerminalDetailsFragment.newInstance(terminalId))
                    .addToBackStack(TerminalDetailsFragment::class.simpleName)
                    .commit()
            }
        }
        viewModel.viewPayScreen.observe(viewLifecycleOwner) {
            it?.let { terminal ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, PayFragment.newInstance(terminal))
                    .addToBackStack(PayFragment::class.simpleName)
                    .commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "List of terminals"
    }
}