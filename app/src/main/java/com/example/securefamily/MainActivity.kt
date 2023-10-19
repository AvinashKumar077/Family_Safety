package com.example.securefamily
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class MainActivity : AppCompatActivity() {

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS,
    )
    val permissionCode = 78



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askForPermission()

        var bottomBar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottomBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.nav_guard -> {
                    inflateFragment(GuardFragment.newInstance())
                }
                R.id.nav_home -> {
                    inflateFragment(HomeFragment.newInstance())
                }
                R.id.nav_dashboard -> {
                    inflateFragment(MapsFragment())
                }
                R.id.nav_profile -> {
                    inflateFragment(ProfileFragment.newInstance())
                }
            }
            true
        }
        bottomBar.selectedItemId = R.id.nav_home

        val auth = FirebaseAuth.getInstance().currentUser
        val name = auth?.displayName.toString()
        val mail = auth?.email.toString()
        val phoneNumber = auth?.phoneNumber.toString()
        val imageUrl = auth?.photoUrl.toString()

        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            "name" to name,
            "mail" to mail,
            "phoneNumber" to phoneNumber,
            "imageUrl" to imageUrl,
        )
    db.collection("users").document(mail).set(user)
        .addOnSuccessListener {  }
        .addOnFailureListener {  }
// Add a new document with a generated ID


    }

    private fun askForPermission() {

        ActivityCompat.requestPermissions(this,permissions,permissionCode)
    }

    private fun inflateFragment(newInstance:Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,newInstance)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == permissionCode){
            if(allPermissionGranted()){

            }else{

            }
        }
    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionGranted(): Boolean {
        for(item in permissions){
            if(ContextCompat.checkSelfPermission(this,item) != PackageManager.PERMISSION_GRANTED ){
                return false
            }
        }
        return true
    }
}