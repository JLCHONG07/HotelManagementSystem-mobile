package com.example.hotelmanagementsystem_mobile.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.*
import com.example.hotelmanagementsystem_mobile.activities.admin.AdminCheckInDetailsActivity
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.Categories
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import kotlinx.android.synthetic.main.fragment_admin_home.*
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mUserDetail : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        FirestoreClass().loadUserData(AdminHomepage(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("AdminHomeFragment", "OnCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("AdminHomeFragment", "OnViewCreated")
        (activity as AppCompatActivity).supportActionBar?.title = "Home"

        btnAdminCheckin.setOnClickListener {
            activity?.let {
                val intent = Intent(it, AdminCheckInDetailsActivity::class.java)
                startActivity(intent)
            }
        }

        btnAdminFacilities.setOnClickListener {
            activity?.let {
                val intent = Intent(it, AdminFacilitiesBooking::class.java)
                it.startActivity(intent)
            }
        }
        btnAdminVoucher.setOnClickListener{
            activity?.let {
                val intent = Intent(it, EVouchers::class.java)
                it.startActivity(intent)
            }

        }
    }

    fun updateUserDetails(user: User) {
        mUserDetail = user
        //TODO: Remove later
        Log.i("AdminHomeFragment", mUserDetail.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}