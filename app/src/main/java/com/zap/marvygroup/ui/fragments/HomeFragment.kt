package com.zap.marvygroup.ui.fragments
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.ui.documents_type.Documents
import com.zap.marvygroup.util.SharedImageViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_nav_drawer.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    lateinit var nav_view : NavigationView
    private lateinit var sharedImageViewModel: SharedImageViewModel
    private lateinit var preferenceProvider: PreferenceProvider
   override fun onCreateView(
       inflater: LayoutInflater,
       container: ViewGroup?,
       savedInstanceState: Bundle?
   ):View?{
       val view = inflater.inflate(R.layout.fragment_home, container, false)
       return view
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        change_image.visibility = View.GONE
        preferenceProvider = PreferenceProvider(requireContext())
        sharedImageViewModel = this?.run {
            ViewModelProviders.of(this)[SharedImageViewModel::class.java]
        }
        btn_show_attendance.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_attendenceFragment)
        }
        btn_view_salary.setOnClickListener {
           findNavController().navigate(R.id.action_nav_home_to_salaryDetail)
        }
//        btn_view_salary.setOnClickListener {
//            val intent = Intent(context, SalaryActivity::class.java)
//            startActivity(intent)
//        }

        btn_upload_documents.setOnClickListener {
            val intent = Intent(context, Documents::class.java)
            startActivity(intent)
        }
        btn_form16_documents.setOnClickListener {
            val uri= Uri.parse("http://online.marvygroup.com/marvy_payroll/employee/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        if (preferenceProvider.getEmployeeCode()!= null){
            employee_code.setText(preferenceProvider.getEmployeeCode())
        }
        if (preferenceProvider.getUserName()!= null){
            headername.setText(preferenceProvider.getUserName())
        }
        if (preferenceProvider.getUserImage()!= null){
            Glide.with(this).load(preferenceProvider.getUserImage()).into(profile_image)
        }else{
            Glide.with(this).load(sharedImageViewModel.photo).into(profile_image)
        }
    }

}
