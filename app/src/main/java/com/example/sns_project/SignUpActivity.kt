package com.example.sns_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sns_project.databinding.ActivitySignupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class SignUpActivity:AppCompatActivity(){
    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("users")
    val database=Firebase.database
    val friendsRef=database.getReference("friends")
    private lateinit var binding:ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val birthPicker=binding.dpSpinner
        val c=Calendar.getInstance()
        c.add(Calendar.YEAR,-100)
        birthPicker.minDate=c.timeInMillis
        val c2=Calendar.getInstance()
        birthPicker.maxDate=c2.timeInMillis

        binding.signupButton.setOnClickListener {
            val passwordText=binding.editTextTextPassword.text.toString()
            val passwordTextConfirm=binding.editTextTextPassword2.text.toString()
            if(passwordText==passwordTextConfirm){
                val email=binding.editTextTextEmailAddress.text.toString()
                addUser()
                Firebase.auth.createUserWithEmailAndPassword(email,passwordText)
                doLogin(email,passwordText)
            }
            else if(passwordText != passwordTextConfirm) {
                Toast.makeText(this, "password incorrect", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUser() {
        val name = binding.editTextTextPersonName.text.toString()
        val year = binding.dpSpinner.year
        val month = binding.dpSpinner.month+1
        val day = binding.dpSpinner.dayOfMonth
        if (name.isEmpty()) {
            Snackbar.make(binding.root, "Input name!", Snackbar.LENGTH_SHORT).show()
            return
        }

        val userMap = hashMapOf(
            "name" to name,
            "year" to year,
            "month" to month,
            "day" to day
        )
        val email=binding.editTextTextEmailAddress.text.toString()
        usersCollectionRef.document(email).set(userMap)
    }

}
