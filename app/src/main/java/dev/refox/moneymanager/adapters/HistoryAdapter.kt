package dev.refox.moneymanager.adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.refox.moneymanager.R
import dev.refox.moneymanager.model.HistoryModel

class HistoryAdapter(private var historyList: ArrayList<HistoryModel>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHistory = historyList[position]
        holder.desc.text = currentHistory.ExpenseDescription
        holder.amt.text = currentHistory.ExpenseAmount.toString()
        holder.date.text = currentHistory.SpendDate
        holder.time.text = currentHistory.SpendTime
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val desc: TextView = itemView.findViewById(R.id.tvExpenseDesc)
        val amt: TextView = itemView.findViewById(R.id.tvExpenseAmt)
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val time: TextView = itemView.findViewById(R.id.tvTime)
    }

}

