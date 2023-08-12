package com.example.app_28_journal

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.app_28_journal.databinding.ActivityNewJournalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class NewJournalActivity : AppCompatActivity() {

    lateinit var binding : ActivityNewJournalBinding
    var currentUserId : String = ""
    var currentUserName : String = ""

    lateinit var auth : FirebaseAuth
    lateinit var user : FirebaseUser
    var db = FirebaseFirestore.getInstance()
    var collectionReference = db.collection("Journal")
    lateinit var storageReference: StorageReference
    lateinit var imageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_journal)

        storageReference = FirebaseStorage.getInstance().reference
        auth = Firebase.auth
        user = auth.currentUser!!

        binding.apply {
            progressBar.visibility = View.INVISIBLE

            if(JournalUser.instance != null){
                currentUserId = auth.currentUser?.uid.toString()
                currentUserName = auth.currentUser?.displayName.toString()
            }

            photoIcon.setOnClickListener{
                var intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                startActivityForResult(intent, 1)
            }

            saveBtn.setOnClickListener{
                SaveJournal()
            }
        }
    }

    private fun SaveJournal() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            var title = editText.text.toString().trim()
            var body = bodyEdit.text.toString().trim()
            
            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body) && imageUri != null){
                val path : StorageReference = storageReference.child("journal_images").child("my_image"+com.google.firebase.Timestamp.now().seconds)
                var timeStamp = com.google.firebase.Timestamp(java.util.Date())
                path.putFile(imageUri).addOnSuccessListener {
                    path.downloadUrl.addOnSuccessListener {
                        val imageUri : Uri = it
                        var journal = Journal(
                            title,body,imageUri.toString(), currentUserId, timeStamp, currentUserName
                        )

                        //collectionReference.document(currentUserId)

                        collectionReference.add(journal).addOnSuccessListener {
                            progressBar.visibility = View.INVISIBLE
                            Log.d("TAGY", "SaveJournal:success")
                            var intent = Intent(this@NewJournalActivity, JournalList::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            Log.d("TAGY", "SaveJournal:fail")
                        }
                    }
                }.addOnFailureListener {
                    progressBar.visibility = View.INVISIBLE
                }
            }else{
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data != null){
                imageUri = data.data!!
                binding.mainImage.setImageURI(imageUri)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        user = auth.currentUser!!
    }

    override fun onStop() {
        super.onStop()
        if(auth != null){

        }
    }
}