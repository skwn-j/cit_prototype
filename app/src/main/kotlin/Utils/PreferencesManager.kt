package Utils

import Common.ConstVariables
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log


object PreferencesManager {
    val TAG = this.javaClass.simpleName

    fun saveIntegerPreferencesco(context: Context, key: String, value: Int) {
        Log.d(TAG, "##### savePreferences #####")
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun loadIntegerSharedPreferences(context: Context, key: String): Int {
        Log.d(TAG, "##### laodIntegerSharedPreferences #####")

        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        return pref.getInt(key, ConstVariables.PREF_AGENT_TYPE_NONE)
    }

    fun clearPreference(context: Context) {
        Log.d(TAG, "##### clearPreference #####")
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }
}