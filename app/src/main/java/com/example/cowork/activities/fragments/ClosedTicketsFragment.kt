package com.example.cowork.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cowork.R
import com.example.cowork.activities.adapters.TicketAdapter
import com.example.cowork.activities.api.RetrofitClient
import com.example.cowork.activities.models.TicketResponse
import com.example.cowork.activities.models.Tickets
import com.example.cowork.activities.storage.SharedPrefManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class ClosedTicketsFragment : Fragment(), View.OnClickListener  {
    lateinit var tickets : MutableList<Tickets>//List<Tickets>
    lateinit var recyclerView: RecyclerView
    private val myCompositeDisposable= CompositeDisposable()
    lateinit var adapter: TicketAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_closed_tickets,  container, false)!!


        recyclerView = view.findViewById(R.id.closed_tickets)
        adapter = TicketAdapter(listOf(),this)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter

        val role = SharedPrefManager.getInstance(activity!!.applicationContext).loginResponse[6] as String
        val id = SharedPrefManager.getInstance(activity!!.applicationContext).loginResponse[1] as Int
        if(role == "false"){
            loadData(id)
        }else{
            loadData2(id)
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        val role = SharedPrefManager.getInstance(activity!!.applicationContext).loginResponse[6] as String
        val id = SharedPrefManager.getInstance(activity!!.applicationContext).loginResponse[1] as Int
        if(role == "false"){
            loadData(id)
        }else{
            loadData2(id)
        }

    }


    private fun loadData(id: Int) {
        myCompositeDisposable.add(
            RetrofitClient.instance.getProClosedTicket(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse))
    }

    private fun loadData2(id: Int) {
        myCompositeDisposable.add(
            RetrofitClient.instance.getClientClosedTicket(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse2))
    }

    private fun handleResponse(tickets: TicketResponse){
        this.tickets = tickets.result.toMutableList()
        adapter.tickets = this.tickets
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun handleResponse2(tickets: TicketResponse){
        this.tickets = tickets.result.toMutableList()
        adapter.tickets = this.tickets
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onClick(p0: View?) {
    }

    override fun onStop() {
        super.onStop()
        myCompositeDisposable.dispose()
    }
}
