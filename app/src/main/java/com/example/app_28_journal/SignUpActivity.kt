package com.example.app_28_journal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.app_28_journal.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        auth = Firebase.auth

        binding.signUp.setOnClickListener(){
            createUser()
        }


    }

    private fun createUser() {

        val email = binding.newEmail.text.toString()
        val password = binding.newPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAGY", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val uid = auth.currentUser?.uid.toString()
                    Log.d("TAGY", "uid = $uid")

                    auth.currentUser?.updateProfile(userProfileChangeRequest {
                        UserProfileChangeRequest.Builder().setDisplayName(binding.username.toString()).build() })
                    val nm = auth.currentUser?.displayName.toString()
                    Log.d("TAGY", "uid = $nm")
                    updateUI(email, password)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGY", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    private fun updateUI(email : String, password : String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                Log.d("TAGY", "LoginUserWithEmail:success")
                val journal = JournalUser.instance!!
                journal.userId = auth.currentUser?.uid
                journal.userName = auth.currentUser?.displayName

                val intent = Intent(this, JournalList::class.java)
                startActivity(intent)
            }else{
                Log.d("TAGY", "LoginUserWithEmail:fail")
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        TODO("Not yet implemented")
    }

}