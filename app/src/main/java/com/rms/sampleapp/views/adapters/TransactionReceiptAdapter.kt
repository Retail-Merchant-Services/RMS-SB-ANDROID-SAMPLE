package com.rms.sampleapp.views.adapters

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rms.sampleapp.R
import com.rms.sampleapp.views.interfaces.TransactionActionListener
import com.kachyng.rmssdk.repository.model.Transaction

/**
 * Adapter class for terminals list.
 */
class TransactionReceiptAdapter(private val listener: TransactionActionListener) :
    RecyclerView.Adapter<TransactionReceiptAdapter.TransactionViewHolder>() {

    /**
     * List of [com.kachyng.rmssdk.repository.model.Transaction] for adapter reference.
     */
    private val arrayList = arrayListOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        //Create viewholder for the album
        return TransactionViewHolder(parent)
    }

    override fun getItemCount() = arrayList.size //Count of terminals

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        //Bind the terminal into viewholder
        holder.bind(arrayList[position])

        //Add click listeners
        holder.btCheckStatus.setOnClickListener {
            listener.onRefreshClicked(arrayList[position])
        }
    }

    /**
     * Updates the terminal list.
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
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Title view for the terminal list view item.
         */
        private val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
        private val tvTransactionAmount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
        private val tvTransactionStage: TextView = itemView.findViewById(R.id.tvTransactionStage)
        private val tvTransactionStatus: TextView = itemView.findViewById(R.id.tvTransactionStatus)
        private val tvTransactionType: TextView = itemView.findViewById(R.id.tvTransactionType)
        private val tvCashBackAmount: TextView = itemView.findViewById(R.id.tvCashBackAmount)
        val btCheckStatus: TextView = itemView.findViewById(R.id.btCheckStatus)

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_transaction_receipt, parent, false
            )
        )

        /**
         * Binds the terminal data to list view item.
         *
         * @param item [TerminalResource] containing terminal data.
         */
        fun bind(item: Transaction) {
            with(item) {
                //Set title of the album
                tvTransactionAmount.text = Html.fromHtml(
                    "<b>Transaction Amount: </b>${(amount.toDouble() / 100)} $currencyCode",
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

                if (null != transactionStatus) {
                    tvTransactionStatus.visibility = View.VISIBLE
                    tvTransactionStatus.text = Html.fromHtml(
                        "<b>Transaction Status: </b>$transactionStatus",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    tvTransactionStatus.visibility = View.GONE
                }


                tvTransactionType.text = Html.fromHtml(
                    "<b>Transaction Type: </b>$transactionType",
                    Html.FROM_HTML_MODE_LEGACY
                )

                if (0 != item.amountCashback) {
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
