package com.example.sns_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.transition.Scene
import com.example.sns_project.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var scene_friendList: ListFriend
    private lateinit var scene_post: Post
    private lateinit var scene_home: Home
    private lateinit var scene_search: SearchFriend


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabBar=binding.tabbar
        scene_friendList=ListFriend()
        scene_post=Post()
        scene_home=Home()
        scene_search=SearchFriend()
        supportFragmentManager.beginTransaction().add(R.id.framelayout,scene_home).commit()

        tabBar.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0->{
                        replaceView(scene_home)
                    }
                    1->{
                        replaceView(scene_search)
                    }
                    2->{
                        replaceView(scene_post)
                    }
                    3->{
                        replaceView(scene_friendList)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

    }
    private fun replaceView(tab:Fragment){
        var selectedFragment:Fragment?=null
        selectedFragment=tab
        selectedFragment.let{
            supportFragmentManager.beginTransaction().replace(R.id.framelayout,it).commit()
        }
    }
}