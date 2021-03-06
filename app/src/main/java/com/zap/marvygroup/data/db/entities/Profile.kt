package com.zap.marvygroup.data.db.entities

data class Profile(
    var aadhar: String? = null,
    var ac_holder: String? = null,
    var ac_no: String? = null,
    var address: String? = null,
    var bank_branch_add: String? = null,
    var bank_name: String? = null,
    var basic: String? = null,
    var bonus_add: String? = null,
    var city: String? = null,
    var company_city: String? = null,
    var company_contact: String? = null,
    var company_id: String? = null,
    var company_name: String? = null,
    var company_state: String? = null ?: "Not Available",
    var conveyance: String? = null,
    var da: String? = null,
    var date_of_birth: String? = null,
    var date_of_joining: String? = null,
    var dedesi: String? = null,
    var dedpf: String? = null,
    var dedtds: String? = null,
    var deduction: String? = null,
    var deduction1: String? = null,
    var department: String? = null,
    var designation: String? = null,
    var dol: String? = null,
    var driver: String? = null,
    var education: String? = null,
    var email: String? = null,
    var emp_code: String? = null,
    var emp_name: String? = null,
    var emp_type: String? = null,
    var epf_uan: String? = null,
    var esic: String? = null,
    var esic_clinic: String? = null,
    var fatherhusband: String? = null,
    var grade_all: String? = null,
    var gratualty: String? = null,
    var helper: String? = null,
    var hra: String? = null,
    var id: String? = null,
    var ifsc: String? = null,
    var loan_installment: String? = null,
    var loc_name: String? = null,
    var lta: String? = null,
    var medical: String? = null,
    var medical_rebm: String? = null,
    var other_deduction1: String? = null,
    var other_deduction2: String? = null,
    var otheradd1: String? = null,
    var otheradd2: String? = null,
    var pan: String? = null,
    var payment_mode: String? = null,
    var pension: String? = null,
    var permanent_address: String? = null,
    var pf: String? = null,
    var pf_withdran: String? = null,
    var pf_withdrawn_date: String? = null,
    var phone: String? = null,
    var photo: String? = null,
    var pin: String? = null,
    var probation: String? = null,
    var qualification: String? = null,
    var registration_date: String? = null,
    var reset_password_token: String? = null,
    var resigned_date: String? = null,
    var sex: String? = null,
    var splallowance: String? = null,
    var statusech: String? = null,
    var ta: String? = null,
    var uniform: String? = null,
    var washing: String? = null
)
