package com.example.app_28_journal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.app_28_journal.databinding.ActivityJournalListBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class JournalList : AppCompatActivity() {

    private lateinit var binding : ActivityJournalListBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    //var db = FirebaseFirestore.getInstance()
    var db = Firebase.firestore
    var collectionRef = db.collection("Journal")
    lateinit var adapter : JournalRecyclerAdapter
    lateinit var journalList : MutableList<Journal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_list)

        auth = Firebase.auth
        user = auth.currentUser!!

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        journalList = arrayListOf<Journal>()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addBtn -> if(user != null && auth != null){
                var intent = Intent(this, NewJournalActivity::class.java)
                startActivity(intent)
            }
            R.id.signOut -> if(user != null && auth != null){
                auth.signOut()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAGY", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("TAGY", "onRestart")
    }

    override fun onPause() {
        super.onPause()
        journalList.clear()
        Log.d("TAGY", "onPause")
    }

    override fun onStart() {
        super.onStart()

        collectionRef.whereEqualTo("userId", user.uid)
            .get().addOnSuccessListener {
                if(!it.isEmpty){

                    for(doc in it){
                        var journal = Journal(
                            doc.data["title"].toString(),
                            doc.data["desc"].toString(),
                            doc.data["imageUrl"].toString(),
                            doc.data["userId"].toString(),
                            doc.data["timeAdded"] as Timestamp,
                            doc.data["userName"].toString()
                        )

                        journalList.add(journal)
                    }

                    adapter = JournalRecyclerAdapter(this , journalList)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }else{
                    binding.noPosts.visibility = View.VISIBLE
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }
    }
}