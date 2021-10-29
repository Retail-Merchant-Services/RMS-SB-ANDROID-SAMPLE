package com.rms.sampleapp.views.adapters

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rms.sampleapp.R
import com.rms.sampleapp.views.interfaces.TransactionActionListener
import com.kachyng.rmssdk.repository.model.Transaction

/**
 * Adapter class for transaction list.
 */
class TransactionsListApiAdapter(private val listener: TransactionActionListener) :
    RecyclerView.Adapter<TransactionsListApiAdapter.TerminalViewHolder>() {

    /**
     * List of [com.sdk.rms.model.TerminalResource] for adapter reference.
     */
    private val arrayList = arrayListOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerminalViewHolder {
        //Create viewholder for the album
        return TerminalViewHolder(parent)
    }

    override fun getItemCount() = arrayList.size //Count of terminals

    override fun onBindViewHolder(holder: TerminalViewHolder, position: Int) {
        //Bind the terminal into viewholder
        holder.bind(arrayList[position])

        //Add click listeners
        holder.btCancel.setOnClickListener {
            Log.e("UrlTransaction", "onBindViewHolder: " + arrayList[position]._links.self.href)
            listener.onCancelTransaction(arrayList[position], position)
        }

    }

    /**
     * Updates the transaction list.
     *
     * @param items New list of [TerminalResource].
     */
    fun updateData(items: List<Transaction>) {
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
         * Title view for the transaction list view item.
         */
        private val tvTransactionAmount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
        private val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
        private val tvTransactionStage: TextView = itemView.findViewById(R.id.tvTransactionStage)
        private val tvTransactionStatus: TextView = itemView.findViewById(R.id.tvTransactionStatus)
        private val tvTransactionType: TextView = itemView.findViewById(R.id.tvTransactionType)
        private val tvCashBackAmount: TextView = itemView.findViewById(R.id.tvCashBackAmount)
        val btCancel: Button = itemView.findViewById(R.id.btCancel)

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_transaction_list, parent, false
            )
        )

        /**
         * Binds the transaction data to list view item.
         *
         * @param item [TerminalResource] containing transaction data.
         */
        fun bind(item: Transaction) {

            with(item) {
                //Set title of the album
                tvTransactionAmount.text = Html.fromHtml(
                    "<b>Transaction Amount: </b>${amount / 100} $currencyCode",
                    Html.FROM_HTML_MODE_LEGACY
                )

                tvTransactionId.text = Html.fromHtml(
                    "<b>Transaction Id: </b>$transactionRefId",
                    Html.FROM_HTML_MODE_LEGACY
                )

                tvTransactionStage.text = Html.fromHtml(
                    "<b>Transaction Stage: </b>$transactionStage",
                    Html.FROM_HTML_MODE_LEGACY
                )

                tvTransactionStatus.text = Html.fromHtml(
                    "<b>Transaction Status: </b>$transactionStatus",
                    Html.FROM_HTML_MODE_LEGACY
                )

                tvTransactionType.text = Html.fromHtml(
                    "<b>Transaction Type: </b>$transactionType",
                    Html.FROM_HTML_MODE_LEGACY
                )

                if (0 != amountCashback) {
                    tvCashBackAmount.visibility = View.VISIBLE
                    tvCashBackAmount.text = Html.fromHtml(
                        "<b>Cashback Amount: </b>${(amountCashback.toDouble() / 100)} $currencyCode",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    tvCashBackAmount.visibility = View.GONE
                }
            }

        }
    }
}