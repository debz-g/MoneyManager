package dev.refox.moneymanager

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import dev.refox.moneymanager.adapters.EmployeeAdapter
import dev.refox.moneymanager.databinding.ActivityMainBinding
import dev.refox.moneymanager.model.UserModel

private lateinit var binding: ActivityMainBinding
private lateinit var empList: ArrayList<UserModel>
private lateinit var database: DatabaseReference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setStatusBarColor(this.getResources().getColor(R.color.material_blue))

        binding.rvEmployeesList.layoutManager = LinearLayoutManager(this)
        binding.rvEmployeesList.setHasFixedSize(true)
        binding.tvNoUsers.visibility = View.VISIBLE
        binding.rvEmployeesList.visibility = View.INVISIBLE

        database = FirebaseDatabase.getInstance().getReference("employees")


        empList = arrayListOf<UserModel>()
        val employeeAdapter = EmployeeAdapter(empList)

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if(snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(UserModel::class.java)
                        empList.add(empData!!)
                    }
                    binding.tvNoUsers.visibility = View.GONE
                    binding.rvEmployeesList.visibility = View.VISIBLE
                    binding.rvEmployeesList.adapter = employeeAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        employeeAdapter.onItemClick = {
            val dialogBinding = layoutInflater.inflate(R.layout.edit_dialog_layout, null)
            val dialog = Dialog(this)

            dialog.setContentView(dialogBinding)
            dialog.setCancelable(true)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val btnEnter = dialogBinding.findViewById<Button>(R.id.btnEnter)
            val etTotalAmt = dialogBinding.findViewById<TextInputEditText>(R.id.tietAmount)

            var email: String = it.Email.toString()

            btnEnter.setOnClickListener {
                database.child(email).child("Total").setValue(etTotalAmt.text.toString().toInt())
                database.child(email).child("Left").setValue(etTotalAmt.text.toString().toInt())
                dialog.dismiss()
            }
        }
    }




}