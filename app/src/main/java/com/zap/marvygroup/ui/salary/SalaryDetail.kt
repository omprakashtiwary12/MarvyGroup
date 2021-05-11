package com.zap.marvygroup.ui.salary

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.ui.ShowSalaryActivity
import com.zap.marvygroup.util.Utility
import com.zap.marvygroup.util.toast
import kotlinx.android.synthetic.main.fragment_salary_detail.*
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class SalaryDetail : Fragment(), KodeinAware {
    private val TAG = "Response"
    override val kodein by kodein()
    private lateinit var viewModel: SalaryViewModel
    private val factory: SalaryViewModelFactory by instance()
    private var prefs: PreferenceProvider? = null

    private var month: ArrayList<String>? = null
    private var listOfYears: ArrayList<String>? = null
    private var adapter = null
    var monthVal: String? = null
    var year: String? = null
    var token: String? = null
    var employeeCode: String? = null
    // private lateinit var monthYear: MonthYear
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_salary_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myContext: Context
        prefs = PreferenceProvider(context!!.applicationContext)
         token = prefs!!.getUserToken()
         employeeCode = prefs!!.getEmployeeCode()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = current.format(formatter)
            current_time.text = formatted
        } else {
            current_time.text = ""
        }


        getMonthBySalaryDetail(employeeCode, token)
        month_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val monthItem = parent?.getItemAtPosition(position).toString()
                if(!monthItem.isNullOrEmpty() ){
                    monthVal = monthItem
                   // Toast.makeText(activity, monthItem, Toast.LENGTH_SHORT).show()
                }

            }
        }
        year_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val yearItem = parent?.getItemAtPosition(position).toString()
                if(yearItem != null){
                    year = yearItem
                   // Toast.makeText(activity, yearItem, Toast.LENGTH_SHORT).show()
                }

            }

        }
        btn_show_salary.setOnClickListener {
            if (itemSelected()) {
                callSalaryDetailsApi()

            }
        }

    }

    private fun itemSelected(): Boolean {

        return true
    }

    private fun callSalaryDetailsApi() {
          if (!monthVal.isNullOrEmpty() && !year.isNullOrEmpty()){
              fetchPdf(monthVal!!,year!!)
          }
    }

    private fun fetchPdf(monthVal: String, year: String) {
        AndroidNetworking.post(" http://online.marvygroup.com/marvy_payroll/apiv2/salary/get")
            .addBodyParameter("employee_code", employeeCode)
            .addBodyParameter("access_token", token)
            .addBodyParameter("month",monthVal)
            .addBodyParameter("year",year)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                   try {
                    val jsonObject = JSONObject(response.toString())
                       Log.e(TAG,jsonObject.toString())
                    if (!jsonObject.getString("link").isNullOrEmpty()){
                        val link = jsonObject.getString("link")
                        Log.e("completeUrlLink",link)
                        if (!link.isNullOrEmpty()){
                            var bundle = bundleOf("urlval" to link)
                            val intent = Intent(activity, ShowSalaryActivity::class.java)
                            intent.putExtra("key_url",link)
                            Log.e("link",link)
                            startActivity(intent)
//                            view?.findNavController()
//                                ?.navigate(R.id.action_salaryDetail_to_sailarySheet,bundle)


//                            val browserIntent =  Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                                startActivity(browserIntent);

                        }else{
                            activity?.toast("Salary Not found")
                        }
                    }
                   }catch (e:NullPointerException){
                       e.printStackTrace()
                       activity?.toast("Salary Not found")
                   }


                }

                override fun onError(anError: ANError?) {
                    activity?.toast("Salary Not found")
                }

            })
    }

    private fun getMonthBySalaryDetail(employeeCode: String?, token: String?) {
        AndroidNetworking.post("http://online.marvygroup.com/marvy_payroll/apiv2/salary")
            .addBodyParameter("employee_code", employeeCode)
            .addBodyParameter("access_token", token)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject = JSONObject(response.toString())
                    Log.e("json object",jsonObject.toString())
                    val message: Boolean = jsonObject.getBoolean("isSuccessful")
                    val monthArray = jsonObject.getJSONArray("months")
                    Log.e("listOfMonth",monthArray.toString())
                    val list = Array(monthArray.length()) {
                        monthArray.getString(it).toString()
                    }
                    try{
                        if (!list.isNullOrEmpty()){
                            val arrayAdapter =
                                ArrayAdapter(activity!!.baseContext, R.layout.spinner_item,list)
                            month_spinner.adapter = arrayAdapter
                            arrayAdapter.notifyDataSetChanged()
                        }else{

                        }
                        val yearArray = jsonObject.getJSONArray("years")
                        val yearsList = Array(yearArray.length()) {
                            yearArray.getString(it)
                        }
                        if (!list.isNullOrEmpty()){
                            year_spinner.adapter =
                                ArrayAdapter(activity!!.baseContext, R.layout.spinner_item, yearsList)
                        }
                    }catch (e:java.lang.NullPointerException){

                    }



                }

                override fun onError(anError: ANError?) {
                    Utility.showToast(activity!!,"Please Logout from other device",false)
                }

            })


    }

}
