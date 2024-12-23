package com.example.universe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.universe.databinding.ActivityMainBinding
import com.example.universe.model.SharedViewModel
import com.example.universe.model.User
import com.example.universe.ui.post.NewPostActivity
import com.example.universe.utils.USER_NODE
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.fabCenter.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            startActivity(intent)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)

        // Fetch user data once the activity is created
        fetchUserDataOnce()
    }

    private fun fetchUserDataOnce() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userId = currentUser.uid
            FirebaseFirestore.getInstance().collection(USER_NODE).document(userId).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        user?.let {
                            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit().apply {
                                putString("profile_image_url", user.image)
                                putString("user_name", user.name)
                                putString("user_email", user.email)
                                putString("user_username", user.username)
                                putString("user_bio", user.bio)
                                putString("user_gender", user.gender)
                                putString("user_dob", user.dob)
                                putString("user_city", user.city)
                                apply()
                            }
                        }
                    }.addOnFailureListener {
                        // Handle error (log or show a message)
                    }
        }
    }

}
