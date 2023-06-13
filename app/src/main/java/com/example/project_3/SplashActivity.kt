package com.example.project_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.postDelayed({
            nextActivity()
        }, 1000)
    }

    private fun nextActivity() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val usersCollection = FirebaseFirestore.getInstance().collection("Users")
            val userDocument = usersCollection.document(userId)

            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.getString("isAdmin") != null) {
                    // User is an admin
                    startActivity(Intent(applicationContext, AdminActivity::class.java))
                    finish()
                } else if (documentSnapshot.getString("isUser") != null) {
                    // User is a regular user
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    // User document does not have admin or user access level
                    handleInvalidAccessLevel()
                }
            }.addOnFailureListener { exception ->
                // Failed to retrieve user document
                handleInvalidAccessLevel()
            }
        }
    }

    private fun handleInvalidAccessLevel() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }
}