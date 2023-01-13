package dev.refox.moneymanager.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import dev.refox.moneymanager.HistoryActivity
import dev.refox.moneymanager.R
import dev.refox.moneymanager.model.UserModel

class EmployeeAdapter(val context: Context, private var empList: ArrayList<UserModel>): RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    var onItemClick : ((UserModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_item_layout, parent, false )
        return ViewHolder(itemView)
    }

    fun setFilteredList(empList: ArrayList<UserModel>){
        this.empList = empList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = empList[position]
        holder.name.text = currentEmp.Name
        holder.total.text = "Total Amount: ₹" + currentEmp.Total.toString()
        holder.left.text = "Amount Left: ₹" + currentEmp.Left.toString()
        holder.email.text = DecodeString(currentEmp.Email)
        holder.outlet.text = currentEmp.Outlet
        Picasso.get().load(currentEmp.imageUrl).into(holder.imageView)

        holder.btnEdit.setOnClickListener {
            onItemClick?.invoke(currentEmp)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, HistoryActivity::class.java)
            intent.putExtra("email", empList[position].Email)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return empList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val total: TextView = itemView.findViewById(R.id.tvTotalAmt)
        val left: TextView = itemView.findViewById(R.id.tvLeftAmt)
        val btnEdit: ImageView = itemView.findViewById(R.id.btnEdit)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val imageView: CircleImageView = itemView.findViewById(R.id.profilePicImage)
        val outlet: TextView = itemView.findViewById(R.id.tvOutlet)
    }

}

fun DecodeString(string: String?): String? {
    return string?.replace(",", ".")
}