package com.example.universe.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.universe.databinding.FragmentProfileBinding
import com.example.universe.ui.login.SignUpActivity
import com.example.universe.model.SharedViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Load user data from SharedPreferences
        loadUserData()

        // Clear data on "Edit Profile" button click
        binding.editProfileBtn.setOnClickListener {

            val intent = Intent(activity, SignUpActivity::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
        }

        return root
    }

    private fun loadUserData() {
        val (cachedImageUrl, cachedName, cachedEmail) = getUserData()

        // Load user profile image if available
        if (!cachedImageUrl.isNullOrEmpty()) {
            Picasso.get().load(cachedImageUrl).into(binding.circleProfileImage)
        }

        // Load user name if available
        binding.nameTvProfile.text = cachedName ?: "Unknown Name"

        // Load user email if available
        binding.profileEmailTV.text = cachedEmail ?: "Unknown Email"
    }

    private fun getUserData(): Triple<String?, String?, String?> {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val imageUrl = sharedPreferences.getString("profile_image_url", null)
        val name = sharedPreferences.getString("user_name", null)
        val email = sharedPreferences.getString("user_email", null)
        return Triple(imageUrl, name, email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
