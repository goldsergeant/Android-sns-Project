package com.example.sns_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.transition.Scene
import com.example.sns_project.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.home->{
                    var HomeFragment = Home()
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, HomeFragment).commit()
                }
                R.id.search->{
                    var SearchFragment = SearchFriend()
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, SearchFragment).commit()
                }
                R.id.post->{
                    startActivity(Intent(this,Post::class.java))
                }
                R.id.friend->{
                    var FriendFragment = ListFriend()
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, FriendFragment).commit()
                }
                R.id.profile->{
                    var ProfileFragment = Profile()
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, ProfileFragment).commit()
                }
            }
            true
        }


        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

    }

}