package com.example.shppprojects.presentation.ui.fragments.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shppprojects.presentation.ui.fragments.contacts.ContactsFragment
import com.example.shppprojects.presentation.ui.fragments.userprofile.UserProfile
import com.example.shppprojects.presentation.utils.Constants

class ViewPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = Constants.FRAGMENT_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (Fragments.values()[position]) {
            Fragments.USER_PROFILE -> UserProfile()
            Fragments.CONTACTS -> ContactsFragment()
        }
    }

    enum class Fragments {
        USER_PROFILE,
        CONTACTS
    }

}