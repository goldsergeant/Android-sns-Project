package com.example.sns_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.wearhomeitem.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WearHome : Fragment() {
    private var param1: String? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private var param2: String? = null
    private val usersCollectionRef = db.collection("users")
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    val database = Firebase.database
    val friendsRef = database.getReference("friends")
    var friendList : ArrayList<String> = arrayListOf()
    val adapter=WearHomeAdapter(this)

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
                    val PostDatas = ArrayList<WearPostData>()
                    var Uids: ArrayList<String> = arrayListOf()
                    for (snapshot in querySnapshot!!.documents) {
                        val postData = snapshot.toObject(WearPostData::class.java)
                        if (postData != null&&(adapter.friendList.contains(postData.userId)||postData.userId==Firebase.auth.currentUser?.email)) {
                            PostDatas.add(postData)
                            Uids.add(snapshot.id)
                        }
                    }
                    adapter.updateList(PostDatas,Uids)
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
            WearHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}