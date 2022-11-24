package com.example.sns_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.homeitem.view.*
import kotlinx.coroutines.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private var param2: String? = null
    private val usersCollectionRef = db.collection("users")
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    val database = Firebase.database
    val friendsRef = database.getReference("friends")
    var friendList : ArrayList<String> = arrayListOf()
    val adapter=HomeAdapter(this)


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
        // Inflate the layout for this fragment
        var view = LayoutInflater.from(activity).inflate(R.layout.activity_home,container,false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        view.recyclerview_home.adapter = adapter
        view.recyclerview_home.layoutManager = LinearLayoutManager(activity)
        (view.recyclerview_home.layoutManager as LinearLayoutManager).setReverseLayout(true)
        (view.recyclerview_home.layoutManager as LinearLayoutManager).setStackFromEnd(true)

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000L) //안나온다면 성능차이때문이니 이 값을 조정하기 바람
            firestore?.collection("images")?.orderBy("timestamp")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    val PostDatas = ArrayList<PostData>()
                    for (snapshot in querySnapshot!!.documents) {
                        val postData = snapshot.toObject(PostData::class.java)
                        if (postData != null && adapter.friendList.contains(postData.userId)) {
                            PostDatas.add(postData)
                        }
                    }
                    adapter.updateList(PostDatas)
                }
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

