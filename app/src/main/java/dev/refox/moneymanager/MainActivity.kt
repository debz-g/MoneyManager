package dev.refox.moneymanager

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import dev.refox.moneymanager.adapters.EmployeeAdapter
import dev.refox.moneymanager.databinding.ActivityMainBinding
import dev.refox.moneymanager.model.UserModel
import java.util.*
import kotlin.collections.ArrayList

private lateinit var binding: ActivityMainBinding
private lateinit var empList: ArrayList<UserModel>
private lateinit var database: DatabaseReference
private lateinit var employeeAdapter: EmployeeAdapter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setStatusBarColor(this.getResources().getColor(R.color.material_blue))

        var total = 0
        var left = 0

        binding.rvEmployeesList.layoutManager = LinearLayoutManager(this)
        binding.rvEmployeesList.setHasFixedSize(true)
        binding.tvNoUsers.visibility = View.VISIBLE
        binding.rvEmployeesList.visibility = View.INVISIBLE

        database = FirebaseDatabase.getInstance().getReference("employees")

        empList = arrayListOf<UserModel>()
        employeeAdapter = EmployeeAdapter(empList)

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

        binding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
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


                database.child(email).child("Left").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val leftAmt = snapshot.getValue(Int::class.java)
                            left = leftAmt!!
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

                total = total + etTotalAmt.text.toString().toInt()
                left = left + etTotalAmt.text.toString().toInt()

                database.child(email).child("Total").setValue(total)
                database.child(email).child("Left").setValue(left)
                dialog.dismiss()
            }
        }
    }

    private fun filterList(query: String?) {
        if(query!=null){
            val filteredList = ArrayList<UserModel>()
            for(i in empList){
                if(i.Name?.toLowerCase(Locale.ROOT)!!.contains(query)){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()){
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                employeeAdapter.setFilteredList(filteredList)
            }
        }
    }


}