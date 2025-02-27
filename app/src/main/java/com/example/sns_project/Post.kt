package com.example.sns_project

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.transition.Scene
import com.example.sns_project.databinding.ActivityLoginBinding
import com.example.sns_project.databinding.ActivityPostBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class Post : AppCompatActivity() {

    lateinit var storage: FirebaseStorage
    lateinit var binding: ActivityPostBinding

    var PICK_IMAGE_FROM_ALBUM = 0
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    lateinit var storagePermission: ActivityResultLauncher<String>
    lateinit var storagePermission_T: ActivityResultLauncher<Array<String>>

    lateinit var gallaryLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Firebase.auth.currentUser ?: finish()

        storage = Firebase.storage
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermission_T =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    if (it.all { permission -> permission.value == true }) {
                        setViews()
                    } else {
                        Toast.makeText(baseContext, "외부저장소 권한을 승인해주십시오.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

            storagePermission_T.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        }
        else {
            storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted->
                if(isGranted) {
                    setViews()
                } else {
                    Toast.makeText(baseContext,"외부저장소 권한을 승인해주십시오.",Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        gallaryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri->
            binding.postPhoto.setImageURI(uri)
            photoUri = uri
        }


    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val binding = ActivityPostBinding.inflate(layoutInflater)
        if(requestCode == PICK_IMAGE_FROM_ALBUM){
            if(resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                binding.postPhoto.setImageURI(photoUri)
            }else{
                finish()
            }
        }
    }*/

    fun setViews() {
        binding.postPhotobutton.setOnClickListener {
            /*var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)*/*/
            gallaryLauncher.launch("image/*")
        }

        binding.postUploadbutton.setOnClickListener {
            contentUpload(photoUri!!)
        }
    }

    fun contentUpload(photoUri: Uri){

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri)?.continueWithTask{ task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var PostData = PostData()

            PostData.uid = auth?.currentUser?.uid
            PostData.userId = auth?.currentUser?.email

            PostData.posttext = binding.postEdittext.text.toString()
            PostData.imageUrl = uri.toString()

            PostData.timestamp = System.currentTimeMillis()

            firestore?.collection("images")?.document()?.set(PostData)

            setResult(Activity.RESULT_OK)
            finish()

        }


        /*storageRef?.putFile(photoUri)?.addOnSuccessListener {
            //Toast.makeText(this,"upload success",Toast.LENGTH_LONG).show()
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var PostData = PostData()

                PostData.uid = auth?.currentUser?.uid
                PostData.userId = auth?.currentUser?.email

                PostData.posttext = binding.postEdittext.text.toString()
                PostData.imageUrl = uri.toString()

                PostData.timestamp = System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(PostData)

                setResult(Activity.RESULT_OK)
                finish()
            }
        }*/

    }
}
