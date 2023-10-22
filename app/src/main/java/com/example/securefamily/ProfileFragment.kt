package com.example.securefamily

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user_name  = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val user_email = FirebaseAuth.getInstance().currentUser?.email.toString()
        val user_phone = FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()

        getView()?.findViewById<TextView>(R.id.user_name)?.text = user_name
        getView()?.findViewById<TextView>(R.id.profile_name)?.text = "Name: $user_name"
        getView()?.findViewById<TextView>(R.id.profile_email)?.text = "Email id: $user_email"
        getView()?.findViewById<TextView>(R.id.profile_phone)?.text = "Phone Number: $user_phone"

    }

    companion object {

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}