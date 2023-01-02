package dev.redfox.moneymanager.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.*
import dev.refox.moneymanager.R
import dev.refox.moneymanager.MainActivity
import dev.refox.moneymanager.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setStatusBarColor(ContextCompat.getColor(baseContext, R.color.yellow))
        firebaseAuth = FirebaseAuth.getInstance()


        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

        binding.btnSignUp.setOnClickListener {
            email = binding.etEmail.text.toString()
            val name = binding.etName.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            val sharedPref = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
            val editor2 = sharedPref.edit()
            editor2.putBoolean("hasLoggedIn", true)
            editor2.apply()


            if (email.isNotEmpty() && name.isNotEmpty() && confirmPass.isNotEmpty() && pass.equals(
                    confirmPass
                ) && pass.isNotEmpty()
            ) {

                firebaseAuth.createUserWithEmailAndPassword(email, confirmPass)
                    .addOnSuccessListener {

//                        binding.etEmail.text?.clear()
//                        binding.etName.text?.clear()

                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                        startMainActivity()

                    }.addOnFailureListener {
                    if (it is FirebaseAuthInvalidUserException) {
                        Toast.makeText(
                            this,
                            "Email cannot be used to create account",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (it is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "Email Already Exists", Toast.LENGTH_SHORT).show()
                    } else if (pass != confirmPass) {
                        Toast.makeText(this, "Check Password", Toast.LENGTH_SHORT).show()
                    } else if (it is FirebaseAuthWeakPasswordException) {
                        Toast.makeText(this, "Weak Password", Toast.LENGTH_SHORT).show()
                    } else if (it is FirebaseAuthEmailException) {
                        Toast.makeText(this, "Incorrect Email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
                    }
                    it.printStackTrace()
                }

            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    fun EncodeString(string: String): String? {
        return string.replace(".", ",")
    }

    fun DecodeString(string: String): String? {
        return string.replace(",", ".")
    }
}