package com.example.sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sns_project.databinding.ActivitySearchfriendBinding
import com.google.apphosting.datastore.testing.DatastoreTestTrace.FirestoreV1Action.GetDocument
import com.google.firebase.auth.ktx.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFriend.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFriend : Fragment() {
    // TODO: Rename and change types of parameters
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
        val binding=ActivitySearchfriendBinding.inflate(layoutInflater)
        val db: FirebaseFirestore = Firebase.firestore
        val database=Firebase.database
        val friendsRef=database.getReference("friends")
        val usersCollectionRef=db.collection("users")
        var username=""
        var userEmail=""
        binding.findUserButton.setOnClickListener{
            if(binding.userEmailText.text.toString()!="")
                userEmail=binding.userEmailText.text.toString()
            if(userEmail!="") {
                usersCollectionRef.document(userEmail).get().addOnSuccessListener {
                    if (it["name"] != null) {
                        username = it["name"] as String
                    }
                    if (username != "") {
                        binding.addFriendText.text = username.toString()
                    } else {
                        Toast.makeText(activity, "user not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.addFriendButton.setOnClickListener {
            if(binding.addFriendText.text!=""){
                val friendMap = hashMapOf(
                    "email" to userEmail
                )
                Firebase.auth.currentUser?.let { it1 -> friendsRef.child(it1.uid).child(username).setValue(friendMap) }
            }
            binding.addFriendText.text=""
            Toast.makeText(activity,username+" is your friend!",Toast.LENGTH_SHORT).show()
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFriend.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFriend().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}