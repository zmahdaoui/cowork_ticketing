package com.example.cowork.activities.storage


import android.content.Context
import com.example.cowork.activities.models.LoginResponse


class SharedPrefManager private constructor(private val mCtx: Context) {

    val token: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", "") != ""
        }

    val loginResponse: List<Any?>
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return listOf(
                sharedPreferences.getString("token",""),
                sharedPreferences.getInt("id",1),
                sharedPreferences.getString("first_name",""),
                sharedPreferences.getString("last_name",""),
                sharedPreferences.getString("email",""),
                sharedPreferences.getString("birthday",""),
                sharedPreferences.getString("client",""))
        }


    fun saveUser(loginresponse: LoginResponse) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token", loginresponse.result.token)
        editor.putInt("id", loginresponse.result.logger.id)
        editor.putString("first_name", loginresponse.result.logger.first_name)
        editor.putString("last_name", loginresponse.result.logger.last_name)
        editor.putString("email", loginresponse.result.logger.email)
        editor.putString("birthday", loginresponse.result.logger.birthday)
        editor.putString("client", loginresponse.result.logger.client)


        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }



    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}