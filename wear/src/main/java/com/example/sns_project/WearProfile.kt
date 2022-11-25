package com.example.sns_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sns_project.databinding.ActivityProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WearProfile : Fragment(){
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= ActivityProfileBinding.inflate(layoutInflater)
        binding.signoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(
                Intent(context,WearLoginActivity::class.java)
            )
        }
        // Inflate the layout for this fragment
        val db: FirebaseFirestore = Firebase.firestore
        val usersCollectionRef=db.collection("users")
        val useremail= Firebase.auth.currentUser?.email
        if (useremail != null) {
            usersCollectionRef.document(useremail).get().addOnSuccessListener {
                binding.name.text= it["name"].toString()
                binding.email.text=useremail
                binding.birth.text=it["year"].toString()+"-"+it["month"].toString()+"-"+it["day"].toString()
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WearProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}