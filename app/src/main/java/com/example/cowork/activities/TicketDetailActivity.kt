package com.example.cowork.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.cowork.R
import com.example.cowork.activities.api.RetrofitClient
import com.example.cowork.activities.models.TicketUpdatedResponse
import com.example.cowork.activities.models.Tickets
import com.example.cowork.activities.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_ticket_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketDetailActivity : AppCompatActivity() {
    lateinit var option: Spinner
    lateinit var option_res : String
    lateinit var options: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        val profileToolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(profileToolbar)
        supportActionBar?.title = "Détail ticket"

        val bundle = intent.getBundleExtra("bundle")
        val ticket = bundle!!.getParcelable<Tickets>("ticket")

        option = findViewById<Spinner>(R.id.status_option)
        var close = findViewById<Button>(R.id.close)
        var resolve = findViewById<Button>(R.id.resolve)
        var save = findViewById<Button>(R.id.save)
        val role = SharedPrefManager.getInstance(applicationContext).loginResponse[6] as String

        if(ticket.open == "true"){
            if( ticket.status == "new" ){
                ownerName.visibility = View.GONE
                if(role == "false"){
                    options = arrayOf("Nouveau ticket", "Ticket en progression", "Ticket en attente")
                    option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
                    option.setSelection(0)
                }else{
                    val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_constraint)
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    val biasedValue = 0.5f
                    constraintSet.setHorizontalBias(R.id.close, biasedValue)
                    constraintSet.applyTo(constraintLayout)
                    resolve.visibility = View.GONE
                    save.visibility = View.GONE
                    option.visibility = View.GONE
                    labelStatus.text = "Status du ticket : Nouveau ticket"
                }
            }else if ( ticket.status == "in progress" ){
                ownerName.text = "Nom du propriétaire : -"+ticket.owner_name!!.trim()
                if(role == "false"){
                    options = arrayOf("Ticket en progression", "Ticket en attente")
                    option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
                    option.setSelection(0)
                }else{
                    val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_constraint)
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    val biasedValue = 0.5f
                    constraintSet.setHorizontalBias(R.id.close, biasedValue)
                    constraintSet.applyTo(constraintLayout)
                    resolve.visibility = View.GONE
                    save.visibility = View.GONE
                    option.visibility = View.GONE
                    labelStatus.text = "Status du ticket : Ticket en progression"
                }
            }else if ( ticket.status == "waiting" ){
                ownerName.text = "Nom du propriétaire : -"+ticket.owner_name!!.trim()
                if(role == "false"){
                    options = arrayOf("Ticket en progression", "Ticket en attente")
                    option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
                    option.setSelection(1)
                }else{
                    val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_constraint)
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    val biasedValue = 0.5f
                    constraintSet.setHorizontalBias(R.id.close, biasedValue)
                    constraintSet.applyTo(constraintLayout)
                    resolve.visibility = View.GONE
                    save.visibility = View.GONE
                    option.visibility = View.GONE
                    labelStatus.text = "Status du ticket : Ticket en attente"
                }
            }
        }else{
            resolve.visibility = View.GONE
            save.visibility = View.GONE
            close.visibility = View.GONE
            ownerName.text = "Nom du propriétaire : -"+ticket.owner_name!!.trim()
            option.visibility = View.GONE
            if( ticket.status == "new" ){
                ownerName.visibility = View.GONE
                labelStatus.text = "Status du ticket : Nouveau ticket"
            }else if ( ticket.status == "in progress" ){
                labelStatus.text = "Status du ticket : Ticket en progression"
            }else if ( ticket.status == "waiting" ) {
                labelStatus.text = "Status du ticket : Ticket en attente"
            }
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                option_res = options.get(position)
            }

        }

        Tname.text = "Nom du ticket : -"+ticket.name.trim()
        Tdate.text = "Date de création : -"+ticket.date_creation?.trim()
        transmitterName.text = "Nom de l'emetteur : -"+ticket.user_name.trim()
        TType.text = "Type du ticket : -"+ticket.type.trim()
        MaterialId.text = "Matériel : -"+ticket.material_id.trim()
        TLocation.text = "Site du matériel : -"+ticket.location.trim()
        TDescription.text = "Déscription du ticket : -"+ticket.description.trim()

        close.setOnClickListener{
            ticket.open = "false"
            RetrofitClient.instance.updateTicket(ticket)
                .enqueue(object : Callback<TicketUpdatedResponse>{
                    override fun onFailure(call: Call<TicketUpdatedResponse>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(
                        call: Call<TicketUpdatedResponse>,
                        response: Response<TicketUpdatedResponse>
                    ) {
                        if (response.code() == 403) {
                            val intent = Intent(applicationContext,LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }else if(response.body()?.status==1){
                            Toast.makeText(applicationContext, "Ticket fermé", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext,ClientActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }

                    }
                })
        }

        resolve.setOnClickListener {
            ticket.resolved = "true"
            ticket.open = "false"
            var id = SharedPrefManager.getInstance(applicationContext).loginResponse[1] as Int
            ticket.id_owner = id
            var firstName = SharedPrefManager.getInstance(applicationContext).loginResponse[2] as String
            var lastName = SharedPrefManager.getInstance(applicationContext).loginResponse[3] as String
            ticket.owner_name = "$firstName $lastName"
            RetrofitClient.instance.updateTicket(ticket)
                .enqueue(object : Callback<TicketUpdatedResponse>{
                    override fun onFailure(call: Call<TicketUpdatedResponse>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(
                        call: Call<TicketUpdatedResponse>,
                        response: Response<TicketUpdatedResponse>
                    ) {
                        if (response.code() == 403) {
                            val intent = Intent(applicationContext,LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }else if(response.body()?.status==1){
                            Toast.makeText(applicationContext, "Ticket résolu", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext,ClientActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }

                    }
                })
        }

        save.setOnClickListener {
            var id = SharedPrefManager.getInstance(applicationContext).loginResponse[1] as Int
            ticket.id_owner = id
            var firstName = SharedPrefManager.getInstance(applicationContext).loginResponse[2] as String
            var lastName = SharedPrefManager.getInstance(applicationContext).loginResponse[3] as String
            ticket.owner_name = firstName+" "+lastName
            if(option_res == "Nouveau ticket" ){
                ticket.status = "new"
            }else if(option_res == "Ticket en progression" ){
                ticket.status = "in progress"
            }else if(option_res == "Ticket en attente" ){
                ticket.status = "waiting"
            }

            RetrofitClient.instance.updateTicket(ticket)
                .enqueue(object : Callback<TicketUpdatedResponse>{
                    override fun onFailure(call: Call<TicketUpdatedResponse>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(
                        call: Call<TicketUpdatedResponse>,
                        response: Response<TicketUpdatedResponse>
                    ) {
                        if (response.code() == 403) {
                            val intent = Intent(applicationContext,LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }else if(response.body()?.status==1){
                            Toast.makeText(applicationContext, "Ticket résolu", Toast.LENGTH_LONG).show()
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

    override fun onBackPressed() {
        val intent = Intent(applicationContext,ClientActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
