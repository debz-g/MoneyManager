package dev.refox.moneymanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import dev.refox.moneymanager.adapters.EmployeeAdapter
import dev.refox.moneymanager.adapters.HistoryAdapter
import dev.refox.moneymanager.databinding.ActivityHistoryBinding
import dev.refox.moneymanager.model.HistoryModel
import dev.refox.moneymanager.model.UserModel
import kotlin.properties.Delegates

private lateinit var binding: ActivityHistoryBinding
private lateinit var databaseReference: DatabaseReference
private lateinit var database: DatabaseReference
private lateinit var historyAdapter: HistoryAdapter
private lateinit var historyList: ArrayList<HistoryModel>
private lateinit var historyData: HistoryModel


class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val email: String= bundle?.get("email").toString()

        val firebaseDatabase: FirebaseDatabase

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference =
            firebaseDatabase.getReference("employees").child(email.toString())


        binding.rvHistoryList.layoutManager = LinearLayoutManager(this)
        binding.rvHistoryList.setHasFixedSize(true)

        database = FirebaseDatabase.getInstance().getReference("employees").child(email).child("History")

        historyList = arrayListOf<HistoryModel>()


            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    historyList.clear()
                    if (snapshot.exists()) {
                            for (historySnap in snapshot.children){
                                historyData = historySnap.getValue(HistoryModel::class.java)!!
                                historyList.add(historyData)
                            }

                        historyAdapter = HistoryAdapter(historyList)
                        binding.rvHistoryList.adapter = historyAdapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

}