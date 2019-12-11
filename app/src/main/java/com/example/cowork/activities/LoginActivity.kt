package com.example.cowork.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cowork.R
import com.example.cowork.activities.api.RetrofitClient
import com.example.cowork.activities.models.*
import com.example.cowork.activities.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        login.setOnClickListener {
            //récupération des différents champs de saisie
            val email = email_login.text.toString().trim()
            val password = password_login.text.toString().trim()

            //vérification si les champs ne sont pas vides
            if(email.isEmpty()){
                email_login.error = "Email required"
                email_login.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                password_login.error = "Password required"
                password_login.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.Login(LoginRequest(email, password))
                .enqueue(object  : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.code() == 200) {
                            if ( response.body()?.status == 1){
                                SharedPrefManager.getInstance(applicationContext).saveUser(
                                    LoginResponse(
                                        response.body()?.status!!,
                                        Result(response.body()?.result?.logger!!,response.body()?.result?.token!!)
                                    )
                                )

                                val role = SharedPrefManager.getInstance(applicationContext).loginResponse[6] as String
                                when (role) {
                                    "true" -> {
                                        val intent = Intent(applicationContext,ClientActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                        finish()
                                    }
                                    "false" -> {
                                        val intent = Intent(applicationContext,ProfessionalActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }else{
                                Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }


                })

        }

    }
}
