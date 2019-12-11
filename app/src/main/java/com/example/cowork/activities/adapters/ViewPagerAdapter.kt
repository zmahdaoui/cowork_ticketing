package com.example.cowork.activities.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.cowork.activities.fragments.*

class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return NewTicketsFragment()
            }
            1-> {
                return InProgressTicketsFragment()
            }
            2 -> {
                return AwaitingTicketsFragment()
            }
            3 -> {
                return LateTicketsFragment()
            }
            4 -> {
                return ClosedTicketsFragment()
            }
            5 -> {
                return ResolvedFragment()
            }
            else -> {
                return NewTicketsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 6
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {
                return "Nouveaux"
            }
            1-> {
                return "En rogressions"
            }
            2 -> {
                return "En attentes"
            }
            3 -> {
                return "En retards"
            }
            4 -> {
                return "Fermés"
            }
            5 -> {
                return "Résolus"
            }
            else -> {
                return "Nouveau ticket"
            }
        }
    }

}