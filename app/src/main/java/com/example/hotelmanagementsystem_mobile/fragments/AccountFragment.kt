package com.example.hotelmanagementsystem_mobile.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.CheckOutHistoryActivity
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.activities.SplashScreen
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingHistory
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.Categories
import com.example.hotelmanagementsystem_mobile.activities.user_profile.ChangeUserPassword
import com.example.hotelmanagementsystem_mobile.activities.user_profile.EditUserProfile
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mUserDetail : User
    private lateinit var name : TextView
    private lateinit var ID: TextView
    private lateinit var email : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        FirestoreClass().loadUserData(Homepage(), this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Account"
        name = requireView().findViewById<TextView>(R.id.textViewUserName)
        ID = requireView().findViewById<TextView>(R.id.textViewID)
        email = requireView().findViewById<TextView>(R.id.textViewEmail)

        buttonLogout.setOnClickListener {
            activity?.let {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(it, SplashScreen::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                it.finish()
            }
        }

        book.setOnClickListener {
            activity?.let {
                val intent = Intent(it, BookingHistory::class.java)
                intent.putExtra(Constants.USERS, mUserDetail)
                it.startActivity(intent)
            }
        }

        check_out_history.setOnClickListener {
            activity?.let {
                val intent = Intent(it, CheckOutHistoryActivity::class.java)
                intent.putExtra(Constants.USERS, mUserDetail)
                startActivity(intent)
            }
        }

        EditProfile.setOnClickListener {
            activity?.let {
                val intent = Intent(it, EditUserProfile::class.java)
                it.startActivity(intent)
            }
        }

        ChangePassword.setOnClickListener{
            activity?.let {
                val intent = Intent(it, ChangeUserPassword::class.java)
                it.startActivity(intent)
            }
        }
    }

    //write the user information getting from firebase to related field
    fun getUserDetails(user: User) {
        mUserDetail = user
        //TODO: Remove later
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_profile_user_image)

        name.setText(mUserDetail.name.toString())
        ID.setText(mUserDetail.passportNumber.toString())
        email.setText(mUserDetail.email.toString())
        Log.i("AccountFragment", mUserDetail.toString())
    }

    //when user navigate back from the edit profile page, update the information in the account page
    override fun onStart(){
        super.onStart()
        Log.i("Account Fragment", "onStart")
        FirestoreClass().loadUserData(Homepage(), this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}