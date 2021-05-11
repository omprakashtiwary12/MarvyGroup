package com.zap.marvygroup.ui.contactus


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.zap.marvygroup.R
import com.zap.marvygroup.data.db.entities.ContactUser
import com.zap.marvygroup.databinding.FragmentContactUsBinding
import com.zap.marvygroup.util.hide
import com.zap.marvygroup.util.show
import kotlinx.android.synthetic.main.fragment_contact_us.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
class ContactUs : Fragment(),KodeinAware,ContactListener{
    override val kodein by kodein()
    private lateinit var viewModel: ContactViewModel
    private val factory: ContactViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentContactUsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_contact_us,container,false)
        viewModel = ViewModelProviders.of(this,factory).get(ContactViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.contactListener = this
        return binding.root
    }

    override fun onStarted() {
        contact_progress_bar.show()
    }

    override fun onSuccess(user: ContactUser) {
        contact_progress_bar.hide()
    }

    override fun onFailure(message: String) {
        contact_progress_bar.hide()
        et_message.error = message
    }


}
