package com.example.app_28_journal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.app_28_journal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.newAccBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener{
            loginWithEmailPass(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim()
            )
        }

        auth = Firebase.auth

    }

    private fun loginWithEmailPass(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                JournalUser.instance!!.userId = auth.currentUser?.uid
                JournalUser.instance!!.userName = auth.currentUser?.displayName

                goToJournalList()
            }else{
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

    }

    private fun goToJournalList() {
        val intent = Intent(this, JournalList::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            goToJournalList()
        }
    }
}