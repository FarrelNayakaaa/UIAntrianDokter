package com.example.uiantriandokter

import android.content.Context
import android.os.Bundle
import org.json.JSONObject

object QueueHelper {
    private const val PREF_NAME = "queue_data"
    private const val KEY_QUEUE = "current_queue"
    private const val KEY_ESTIMATION = "estimation_minutes"
    private const val KEY_PATIENT_NAME = "patient_name"
    private const val KEY_HAS_QUEUE = "has_queue"
    private const val KEY_PATIENT_BUNDLE = "patient_bundle_json"

    fun getCurrentQueue(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_QUEUE, 0)
    }

    fun getEstimation(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_ESTIMATION, 0)
    }

    fun increaseQueue(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val queue = prefs.getInt(KEY_QUEUE, 0) + 1
        val estimation = queue * 35 // estimasi 35 menit per pasien
        prefs.edit()
            .putInt(KEY_QUEUE, queue)
            .putInt(KEY_ESTIMATION, estimation)
            .apply()
    }

    fun decreaseQueue(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentQueue = prefs.getInt(KEY_QUEUE, 0)
        val newQueue = if (currentQueue > 0) currentQueue - 1 else 0
        val estimation = newQueue * 35

        prefs.edit()
            .putInt(KEY_QUEUE, newQueue)
            .putInt(KEY_ESTIMATION, estimation)
            .apply()
    }

    fun resetQueue(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    // ---------- Data Pasien ----------
    fun savePatientName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_PATIENT_NAME, name)
            .putBoolean(KEY_HAS_QUEUE, true)
            .apply()
    }

    fun getPatientName(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PATIENT_NAME, null)
    }

    fun hasQueue(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_HAS_QUEUE, false)
    }

    fun clearPatientData(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_PATIENT_NAME)
            .putBoolean(KEY_HAS_QUEUE, false)
            .apply()
    }

    // ---------- Simpan & ambil bundle pasien ----------
    fun savePatientBundle(context: Context, bundle: Bundle) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = JSONObject()
        for (key in bundle.keySet()) {
            val value = bundle.getString(key, "")
            json.put(key, value)
        }
        prefs.edit()
            .putString(KEY_PATIENT_BUNDLE, json.toString())
            .putBoolean(KEY_HAS_QUEUE, true)
            .apply()
    }

    fun getPatientBundle(context: Context): Bundle? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_PATIENT_BUNDLE, null) ?: return null
        val json = JSONObject(raw)
        val bundle = Bundle()
        val keys = json.keys()
        while (keys.hasNext()) {
            val k = keys.next()
            bundle.putString(k, json.optString(k, ""))
        }
        return bundle
    }

    fun clearPatientBundle(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_PATIENT_BUNDLE)
            .putBoolean(KEY_HAS_QUEUE, false)
            .apply()
    }
}
