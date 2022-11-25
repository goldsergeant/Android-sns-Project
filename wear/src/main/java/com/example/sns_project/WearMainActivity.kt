package com.example.sns_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sns_project.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WearMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        var HomeFragment = WearHome()
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, HomeFragment).commit()


        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, WearLoginActivity::class.java)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(this, WearMenu::class.java)
        )
        finish()
    }
}