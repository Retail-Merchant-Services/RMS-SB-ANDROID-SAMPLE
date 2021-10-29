package com.rms.sampleapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rms.sampleapp.R
import com.rms.sampleapp.views.interfaces.TransactionFilterListener
import kotlinx.android.synthetic.main.layout_transaction_status_list.view.*

/**
 * Adapter class for terminals list.
 */
class TransactionStageListAdapter(var listener: TransactionFilterListener) :
    RecyclerView.Adapter<TransactionStageListAdapter.TerminalViewHolder>() {

    /**
     * Contains possible values from Transaction status.
     */
    private var arrayList = mutableListOf<String>(
        "SALE", "REFUND"
    )

    /**
     * Store the selected item position
     */
    private var mPosition=-1


    /**
     * List of [com.sdk.rms.model.TerminalResource] for adapter reference.
     */

    fun updateData(position:Int){
        mPosition=position
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerminalViewHolder {
        //Create viewholder for the album
        return TerminalViewHolder(parent)
    }

    override fun getItemCount() = arrayList.size //Count of terminals

    override fun onBindViewHolder(holder: TerminalViewHolder, position: Int) {
        //Bind the terminal into viewholder
        holder.bind(arrayList[position])

        //Store the selected value
        holder.itemView.tvTransactionName.isChecked = mPosition==position

        //Add listener
        holder.itemView.tvTransactionName.setOnClickListener {
            mPosition=holder.adapterPosition
            listener.onTransactionTypeListener(arrayList[position])
            notifyDataSetChanged()
        }


    }


    /**
     * View holder for adapter.
     */
    class TerminalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Title view for the terminal list view item.
         */
        private val tvTransactionName: TextView = itemView.findViewById(R.id.tvTransactionName)

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_transaction_status_list, parent, false
            )
        )

        /**
         * Binds the terminal data to list view item.
         *
         * @param item [TerminalResource] containing terminal data.
         */
        fun bind(item: String) {
            tvTransactionName.text=item

        }
    }
}