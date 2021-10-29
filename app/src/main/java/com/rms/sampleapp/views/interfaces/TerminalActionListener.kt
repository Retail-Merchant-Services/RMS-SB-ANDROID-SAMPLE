package com.rms.sampleapp.views.interfaces

import com.kachyng.rmssdk.repository.model.Terminal

interface TerminalActionListener {

    fun onViewClicked(terminal: Terminal)
    fun onDetailClicked(terminal: Terminal)
}