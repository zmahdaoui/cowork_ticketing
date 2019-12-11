package com.example.cowork.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import android.widget.TableLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.cowork.R
import com.example.cowork.activities.adapters.ViewPagerAdapter
import com.example.cowork.activities.api.RetrofitClient
import com.example.cowork.activities.fragments.*
import com.example.cowork.activities.storage.SharedPrefManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_client.*

class ClientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        RetrofitClient.AUTH = SharedPrefManager.getInstance(applicationContext).loginResponse[0] as String

        val profileToolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(profileToolbar)

        val firstName =  SharedPrefManager.getInstance(applicationContext).loginResponse[2] as String
        val lastName = SharedPrefManager.getInstance(applicationContext).loginResponse[3] as String
        supportActionBar?.title = "$firstName  $lastName"


        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_profile,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.action_logout -> {
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
