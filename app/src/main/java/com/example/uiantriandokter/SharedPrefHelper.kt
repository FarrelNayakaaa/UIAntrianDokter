package com.example.uiantriandokter

import android.content.Context
import android.content.SharedPreferences

object SharedPrefHelper {

    private const val PREF_NAME = "UserPref"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LAST_NAME = "last_name"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private const val KEY_BIRTHDATE = "birthdate"
    private const val KEY_PHONE = "phone"
    private const val KEY_GENDER = "gender"

    // ✅ Simpan semua data user
    fun saveUser(
        context: Context,
        firstName: String,
        lastName: String,
        email: String,
        birthDate: String,
        phone: String,
        gender: String,
        password: String
    ) {
        val editor = getPref(context).edit()
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.putString(KEY_LAST_NAME, lastName)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_BIRTHDATE, birthDate)
        editor.putString(KEY_PHONE, phone)
        editor.putString(KEY_GENDER, gender)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    // ✅ Getter masing-masing
    fun getFirstName(context: Context): String? = getPref(context).getString(KEY_FIRST_NAME, null)
    fun getLastName(context: Context): String? = getPref(context).getString(KEY_LAST_NAME, null)
    fun getEmail(context: Context): String? = getPref(context).getString(KEY_EMAIL, null)
    fun getPassword(context: Context): String? = getPref(context).getString(KEY_PASSWORD, null)
    fun getBirthDate(context: Context): String? = getPref(context).getString(KEY_BIRTHDATE, null)
    fun getPhone(context: Context): String? = getPref(context).getString(KEY_PHONE, null)
    fun getGender(context: Context): String? = getPref(context).getString(KEY_GENDER, null)

    // ✅ Getter gabungan (dipakai di HomeFragment)
    fun getUserData(context: Context): Map<String, String?> {
        return mapOf(
            "firstName" to getFirstName(context),
            "lastName" to getLastName(context),
            "email" to getEmail(context),
            "birthDate" to getBirthDate(context),
            "phone" to getPhone(context),
            "gender" to getGender(context)
        )
    }

    // ✅ Logout / hapus data
    fun clearUser(context: Context) {
        getPref(context).edit().clear().apply()
    }

    private fun getPref(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}
