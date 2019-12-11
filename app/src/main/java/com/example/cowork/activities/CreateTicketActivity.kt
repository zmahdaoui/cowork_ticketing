package com.example.cowork.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.cowork.R
import com.example.cowork.activities.api.RetrofitClient
import com.example.cowork.activities.models.MaterialResponse
import com.example.cowork.activities.models.TicketCreatedResponse
import com.example.cowork.activities.models.Tickets
import com.example.cowork.activities.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_create_ticket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTicketActivity : AppCompatActivity() {
    lateinit var option: Spinner
    lateinit var option_res : String
    lateinit var option2 : Spinner
    lateinit var option_res2 : String
    lateinit var option3 : Spinner
    lateinit var option_res3 : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ticket)

        val profileToolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(profileToolbar)
        supportActionBar?.title = "Créer un ticket"

        option = findViewById<Spinner>(R.id.type_option)
        val options = arrayOf("Matériel", "Logiciel")
        option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                option_res = options.get(position)
            }

        }


        option2 = findViewById<Spinner>(R.id.location_option)
        val options2 = arrayOf("Bastille", "République", "Odéon","Place d'Italie","Ternes","Beaubourg")
        var options3 = mutableListOf<String>()
        option2.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options2)
        option3 = findViewById<Spinner>(R.id.techmat_option)
        option2.setSelection(0)
        option2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                option_res2 = options2.get(position)
                if(option_res2.isNullOrBlank()){
                    option_res2 = "Bastille"
                }
                Log.i("MATERIAL", option_res2)
                RetrofitClient.instance.getTechMat(option_res2)
                    .enqueue(object : Callback<MaterialResponse>{
                        override fun onFailure(call: Call<MaterialResponse>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<MaterialResponse>,
                            response: Response<MaterialResponse>
                        ) {
                            if (response.code() == 403) {
                                val intent = Intent(applicationContext,LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                            }
                            if(response.body()?.status==1){
                                options3.clear()
                                //var optionsList = mutableListOf<String>()
                                var filteredPrinters = response.body()?.result!!.filterNot { it.type.equals("laptop")}
                                var filteredLaptops = response.body()?.result!!.filterNot { it.type.equals("printer")}
                                var sortedPrinter = filteredPrinters.sortedWith(compareBy({ it.number }))
                                var sortedLaptops = filteredLaptops.sortedWith(compareBy({ it.number }))
                                for(mat in sortedLaptops){
                                    options3.add(mat.type+" "+mat.number)
                                }
                                for(mat in sortedPrinter){
                                    options3.add(mat.type+" "+mat.number)
                                }
                                option3.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,options3/*.toTypedArray()*/)
                                option3.setSelection(0)
                                option3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                        option_res3 = options3.get(position)
                                        Log.i("MATERIAL", option_res3)
                                    }

                                }
                            }
                        }

                    })
            }

        }

        create_ticket.setOnClickListener {
            Log.i("MATERIAL", "HNAAA A ZBIIIII")
            val ticketName = ticket_name.text.toString().trim()
            val description = ticket_description.text.toString().trim()

            if(ticketName.isEmpty()){
                ticket_name.error = "Non du tickeckt obligatoire"
                ticket_name.requestFocus()
                return@setOnClickListener
            }

            if(description.isEmpty()){
                ticket_description.error = "une description est obligatoire"
                ticket_description.requestFocus()
                return@setOnClickListener
            }

            val id = SharedPrefManager.getInstance(applicationContext).loginResponse[1] as Int
            val firstName =  SharedPrefManager.getInstance(applicationContext).loginResponse[2] as String
            val lastName = SharedPrefManager.getInstance(applicationContext).loginResponse[3] as String
            val name = firstName+" "+lastName
            RetrofitClient.instance.createTicket(
                Tickets(
                    null,
                    ticketName,
                    null,
                    null,
                    id,
                    name,
                    null,
                    null,
                    option_res,
                    option_res3,
                    description,
                    option_res2,
                    null,
                    null,
                    null
                )).enqueue(object : Callback<TicketCreatedResponse> {
                override fun onFailure(call: Call<TicketCreatedResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<TicketCreatedResponse>, response: Response<TicketCreatedResponse>) {
                    if(response.body()?.status==1){
                        Toast.makeText(applicationContext, "Ticket crée", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext,ClientActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_profile,menu)
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext,ClientActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.action_logout -> {//déconnéxion
                SharedPrefManager.getInstance(applicationContext).clear()
                val intent = Intent(applicationContext,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
            R.id.create_action -> {
                val intent = Intent(applicationContext,CreateTicketActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
