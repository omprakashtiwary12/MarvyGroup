package com.zap.marvygroup

import android.app.Application
import android.content.Context
import com.zap.marvygroup.data.db.AppDatabase
import com.zap.marvygroup.data.network.MyApi
import com.zap.marvygroup.data.network.NetworkConnectionInterceptor
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.monitorinternet.MonitorInternet
import com.zap.marvygroup.ui.apply.ApplyViewModelFactory
import com.zap.marvygroup.ui.documents_type.DocumentTypeFactory
import com.zap.marvygroup.ui.documents_type.DocumentViewTypeFactory
import com.zap.marvygroup.ui.attendance.AttendanceViewModelFactory
import com.zap.marvygroup.ui.auth.AuthViewModelFactory
import com.zap.marvygroup.ui.contactus.ContactViewModelFactory
import com.zap.marvygroup.ui.documents.DocumentsViewModelFactory
import com.zap.marvygroup.ui.profile.ProfileViewModelFactory
import com.zap.marvygroup.ui.profile_image.ChangeProfileViewModelFactory
import com.zap.marvygroup.ui.reset.ResetViewModelFactory
import com.zap.marvygroup.ui.salary.SalaryViewModelFactory
import com.zap.marvygroup.ui.uploaded_documents.UploadViewModelFactory
import com.zap.marvygroup.ui.uploaded_pdf.PdfViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

private val mInstance: MarvyGroupApplication? = null
class MarvyGroupApplication : Application(),KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MarvyGroupApplication))
        bind()from singleton { NetworkConnectionInterceptor(instance()) }
        bind()from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { UserRepository(instance(), instance(), instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { ApplyViewModelFactory(instance()) }
        bind() from provider { ResetViewModelFactory(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from provider { SalaryViewModelFactory(instance()) }
        bind() from provider { ContactViewModelFactory(instance()) }
        bind() from provider { DocumentsViewModelFactory(instance()) }
        bind() from provider { AttendanceViewModelFactory(instance()) }
        bind() from provider { ChangeProfileViewModelFactory(instance()) }
        bind()from provider { UploadViewModelFactory(instance()) }
        bind()from provider { PdfViewModelFactory(instance()) }
        bind()from provider { DocumentTypeFactory(instance()) }
        bind()from provider {DocumentViewTypeFactory(instance())}
    }


    @Synchronized
    fun getInstance(): MarvyGroupApplication {
        return mInstance!!.getInstance()
    }

}