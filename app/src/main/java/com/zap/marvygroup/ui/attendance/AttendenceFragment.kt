package com.zap.marvygroup.ui.attendance


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zap.marvygroup.HomeActivity
import com.zap.marvygroup.R
import com.zap.marvygroup.data.db.entities.Attendance
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.databinding.FragmentAttendenceBinding
import com.zap.marvygroup.util.SharedLocationViewModel
import com.zap.marvygroup.util.Utility
import com.zap.marvygroup.util.toast
import kotlinx.android.synthetic.main.fragment_attendence.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class AttendenceFragment : Fragment(), AttendanceListener,AttendanceOutListener, KodeinAware {
    override val kodein by kodein()
    private lateinit var locationViewModel: SharedLocationViewModel
    private var prefs: PreferenceProvider? = null
    var token: String? = null
    var employeeCode: String? = null
    private var currentDate:String? = null
    lateinit var attendanceViewModel: AttendanceViewModel
    var address: String? = null
    var savedDate:String?=null
    lateinit var text_address:TextView
    lateinit var text_date:TextView
    lateinit var text_address_out:TextView
    lateinit var text_date_out:TextView
    lateinit var btn_attendence:LinearLayout
    lateinit var btn_salary:LinearLayout
    private val factory: AttendanceViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding: FragmentAttendenceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendence, container, false)
        attendanceViewModel = ViewModelProvider(this, factory).get(AttendanceViewModel::class.java)
        binding.viewModel = attendanceViewModel
        binding.lifecycleOwner = this
        text_address = binding.textAddress
        text_date = binding.textDate
        text_address_out = binding.textAddressOut
        text_date_out = binding.textDateOut
        attendanceViewModel.attendanceListener = this
        attendanceViewModel.attendanceOutListener = this
        prefs = PreferenceProvider(context!!.applicationContext)
        token = prefs!!.getUserToken()
        if (prefs!!.getPastDate()!=null){
            savedDate = prefs!!.getPastDate()
        }

        employeeCode = prefs!!.getEmployeeCode()
        getCurrentDate()
        btn_attendence = binding.btnAttendence
        btn_salary = binding.btnSalary
        locationViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedLocationViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        if (locationViewModel.latValue != 0.0) {
          //  Log.e("latitude", "" + locationViewModel.latValue)
           // Log.e("longitude", "" + locationViewModel.longValue)
            attendanceViewModel.lattitude = locationViewModel.latValue
            attendanceViewModel.longitude = locationViewModel.longValue
            attendanceViewModel.token = token
            attendanceViewModel.employee_code = employeeCode
            attendanceViewModel.name = prefs?.getUserName()
            try {
                val geocoder: Geocoder
                val addresses: List<Address>
                geocoder = Geocoder(activity, Locale.getDefault())
                addresses = geocoder.getFromLocation(
                    locationViewModel.latValue,
                    locationViewModel.longValue,
                    1
                )
                address = addresses[0].getAddressLine(0)

              //  Log.e("Address", address)
            } catch (e: NullPointerException) {
                activity?.toast(e.message!!)
            } catch (e: IOException) {
                activity?.toast(e.message!!)
            }
        }
        text_address.setText(prefs?.getAddress())
        text_date.setText(prefs?.getPastDate())
        text_address_out.setText(prefs?.getOutAddress())
        text_date_out.setText(prefs?.getOutDate())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = current.format(formatter)
            attendanceViewModel.currentTime = formatted
         //   current_time.text = formatted
        } else {
           // current_time.text = ""
            attendanceViewModel.currentTime = ""
        }
//        if (savedDate == currentDate){
//            //btn_salary.isClickable = true
//            btn_salary.setOnClickListener {
//                attendanceViewModel.onAttendanceOutButtonClick()
//            }
//        }
//        else if (savedDate != currentDate){
//           // btn_attendence.isClickable = true
//            btn_attendence.setOnClickListener {
//                attendanceViewModel.onAttendanceInButtonClick()
//            }
//        }
        Log.e("current date",currentDate)
        btn_salary.setOnClickListener {
            attendanceViewModel.onAttendanceOutButtonClick()

        }

        btn_attendence.setOnClickListener {
            attendanceViewModel.onAttendanceInButtonClick()
        }


        return binding.root
    }

    private fun goBackToHome() {
       val intent = Intent(activity,HomeActivity::class.java)
        startActivity(intent)
    }

    private fun getCurrentDate() {
        val dateString: String = DateFormat.format("MM/dd/yyyy", Date(Date().getTime())).toString()
        val sp: SharedPreferences = context!!.getSharedPreferences(
            "com.zap.marvygroup",
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString("dateAndTime", dateString)
        editor.commit()
        currentDate = dateString
        Log.e("dateString", dateString)
    }


    override fun onStarted() {

    }

    override fun onSuccess(userAddress: com.zap.marvygroup.ui.attendance.Address) {
        if (userAddress.address.isNullOrEmpty()){
//            text_address.visibility = View.VISIBLE
//            text_address.setText(prefs?.getAddress())
//            text_date.setText(prefs?.getPastDate())
//            text_date.setTextColor(resources.getColor(R.color.white))
//            text_address.setTextColor(resources.getColor(R.color.white))
        }else{
            prefs?.saveDate(userAddress.marked_on)
            prefs?.saveAddress(userAddress.address)
            text_address.visibility = View.VISIBLE
            text_address.setText(userAddress.address)
            text_date.setText(userAddress.marked_on)
            text_date.setTextColor(resources.getColor(R.color.white))
            text_address.setTextColor(resources.getColor(R.color.white))

        }
    }

    override fun onFailure(message: String?) {
//          activity?.toast(message!!)
//        try {
//            activity?.toast(address!!)
//
//        }catch (e: NullPointerException){
//
//        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onStartedOut() {

    }

    override fun onSuccessOut(address: com.zap.marvygroup.ui.attendance.Address) {
        text_address_out.setText(address.address)
        text_date_out.setText(address.marked_on)
        prefs?.saveOutAddress(address.address)
        prefs?.saveOutDate(address.marked_on)
    }

    override fun onFailureOut(message: String?) {

    }


}
