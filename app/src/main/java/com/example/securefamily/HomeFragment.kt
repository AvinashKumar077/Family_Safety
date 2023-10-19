package com.example.securefamily

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securefamily.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
   private val listsContacts : ArrayList<ContactModel> = ArrayList()
   lateinit var inviteAdapter: InviteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMembers = listOf<MemberModel>(
            MemberModel(
                "Lokesh",
                "9th buildind, 2nd floor, maldiv road, manali 9th buildind, 2nd floor",
                "90%",
                "220"
            ),
            MemberModel(
                "Kedia",
                "10th buildind, 3rd floor, maldiv road, manali 10th buildind, 3rd floor",
                "80%",
                "210"
            ),
            MemberModel(
                "D4D5",
                "11th buildind, 4th floor, maldiv road, manali 11th buildind, 4th floor",
                "70%",
                "200"
            ),
            MemberModel(
                "Ramesh",
                "12th buildind, 5th floor, maldiv road, manali 12th buildind, 5th floor",
                "60%",
                "190"
            ),
        )

        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter





        Log.d("fetchContact", "fetchContact: start hogya ${listsContacts.size}")
        inviteAdapter = InviteAdapter(listsContacts)
        fetchDatabaseContacts()
        Log.d("fetchContact", "fetchContact: end hogya")

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("fetchContact", "fetchContact: coroutine start")

            insertDatabaseContacts(fetchContact())

            Log.d("fetchContact", "fetchContact: coroutine end ${listsContacts.size}")
        }


        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inviteRecycler.adapter = inviteAdapter

        val threeDots = requireView().findViewById<ImageView>(R.id.three_dots)
        threeDots.setOnClickListener{
            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
        }
    }

    private fun fetchDatabaseContacts(){
        val database = MyFamilyDatabase.getDatabase(requireContext())
         database.contactDao().getAllContacts().observe(viewLifecycleOwner){
             Log.d("fetchContact","fetchContacts")
             listsContacts.clear()
             listsContacts.addAll(it)
             inviteAdapter.notifyDataSetChanged()
         }
    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {

        val database = MyFamilyDatabase.getDatabase(requireContext())

        database.contactDao().insertAll(listContacts)

    }


    private fun fetchContact(): ArrayList<ContactModel> {
        Log.d("fetchContact", "fetchContact: start ")

        val cr = requireActivity().contentResolver
        val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val listContacts: ArrayList<ContactModel> = ArrayList()

        cursor?.use { // Use cursor extension to auto-close the cursor
            if (cursor.count > 0) {
                val idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val hasPhoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                while (cursor.moveToNext()) {
                    val id = cursor.getString(idColumnIndex)
                    val name = cursor.getString(nameColumnIndex)
                    val hasPhoneNumber = cursor.getInt(hasPhoneNumberColumnIndex)

                    if (hasPhoneNumber > 0) {
                        val pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )

                        pCur?.use {
                            if (pCur.count > 0) {
                                val phoneNumColumnIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                while (pCur.moveToNext()) {
                                    val phoneNum = pCur.getString(phoneNumColumnIndex)
                                    listContacts.add(ContactModel(name, phoneNum))
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.d("fetchContact", "fetchContact: end")
        return listContacts
    }


    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}