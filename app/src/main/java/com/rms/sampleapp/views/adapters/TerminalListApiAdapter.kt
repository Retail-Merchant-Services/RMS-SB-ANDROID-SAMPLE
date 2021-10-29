package com.rms.sampleapp.views.adapters

import android.content.res.ColorStateList
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rms.sampleapp.R
import com.rms.sampleapp.views.interfaces.TerminalActionListener
import com.kachyng.rmssdk.constants.TerminalStatus
import com.kachyng.rmssdk.repository.model.Terminal

/**
 * Adapter class for terminals list.
 */
class TerminalListApiAdapter(private val listener: TerminalActionListener) :
    RecyclerView.Adapter<TerminalListApiAdapter.TerminalViewHolder>() {

    /**
     * List of [com.sdk.rms.model.TerminalResource] for adapter reference.
     */
    private val arrayList = arrayListOf<Terminal>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerminalViewHolder {
        //Create viewholder for the album
        return TerminalViewHolder(parent)
    }

    override fun getItemCount() = arrayList.size //Count of terminals

    override fun onBindViewHolder(holder: TerminalViewHolder, position: Int) {
        //Bind the terminal into viewholder
        holder.bind(arrayList[position])

        //Add click listeners
        holder.btView.setOnClickListener {
            listener.onViewClicked(arrayList[position])
        }


        //Add click listeners
        holder.itemView.setOnClickListener {
            listener.onDetailClicked(arrayList[position])
        }
    }

    /**
     * Updates the terminal list.
     *
     * @param items New list of [TerminalResource].
     */
    fun updateData(items: List<Terminal>) {
        //Clear old terminals
        arrayList.clear()
        //Add new terminals
        arrayList.addAll(items)
        //Notify the adapter for the change
        notifyDataSetChanged()
    }

    /**
     * View holder for adapter.
     */
    class TerminalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Title view for the terminal list view item.
         */
        private val tvManufacturer: TextView = itemView.findViewById(R.id.tvManufacturer)
        private val tvTerminalName: TextView = itemView.findViewById(R.id.tvTerminalName)
        private val tvPos: TextView = itemView.findViewById(R.id.tvPos)
        private val tvTerminalStatus: TextView = itemView.findViewById(R.id.tvTerminalStatus)
        val btView: TextView = itemView.findViewById(R.id.btView)

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_terminal_list_item, parent, false
            )
        )

        /**
         * Binds the terminal data to list view item.
         *
         * @param item [TerminalResource] containing terminal data.
         */
        fun bind(item: Terminal) {
            //Set title of the album
            tvManufacturer.text = Html.fromHtml(
                "<b>Manufacturer: </b>" + item.manufacturer,
                Html.FROM_HTML_MODE_LEGACY
            )
            tvTerminalName.text = Html.fromHtml(
                "<b>Terminal Name: </b>" + item.terminalName,
                Html.FROM_HTML_MODE_LEGACY
            )
            tvPos.text = Html.fromHtml(
                "<b>Pos: </b>" + item.pos,
                Html.FROM_HTML_MODE_LEGACY
            )
            tvTerminalStatus.text = Html.fromHtml(
                "<b>Status: </b>" + item.terminalStatus,
                Html.FROM_HTML_MODE_LEGACY
            )



            TextViewCompat.setCompoundDrawableTintList(
                tvTerminalStatus,
                ColorStateList.valueOf(
                    itemView.resources.getColor(
                        when (item.terminalStatus) {
                            TerminalStatus.OFFLINE -> R.color.colorOffline
                            TerminalStatus.READY -> R.color.colorOnline
                            TerminalStatus.PROCESSING -> R.color.purple_500
                        }, null
                    )
                )
            )
        }
    }
}