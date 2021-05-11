

package com.zap.marvygroup.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider

import java.util.zip.Inflater


/**
 * A simple [Fragment] subclass.
 */
class LogoutFragment : Fragment() {
    lateinit var nav_view : NavigationView
    val token:String? = null
    val preferenceProvider: PreferenceProvider?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_logout,container,false)
        //nav_view = view.findViewById(R.id.nav_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



}
