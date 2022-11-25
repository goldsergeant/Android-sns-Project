package com.example.sns_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sns_project.databinding.ActivityMenuBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WearMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.homeButton.setOnClickListener {
            startActivity(
                Intent(this, WearMainActivity::class.java)
            )
        }

        binding.profileButton.setOnClickListener{
            startActivity(
                Intent(this, WearProfileActivity::class.java)
            )
        }

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, WearLoginActivity::class.java)
            )
            finish()
        }
    }
}