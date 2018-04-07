package com.lgguzman.example.kotlinseed.ui.activities.onboarding

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.ViewGroup
import com.lgguzman.example.kotlinseed.R
import com.lgguzman.example.kotlinseed.databinding.ActivityOnboardingBinding
import com.lgguzman.example.kotlinseed.services.UserService
import com.lgguzman.example.kotlinseed.ui.activities.BaseActivity
import com.lgguzman.example.kotlinseed.ui.activities.main.MainActivity
import com.lgguzman.example.kotlinseed.ui.fragments.OnboardingFragment
import kotlinx.android.synthetic.main.activity_onboarding.*

/**
 * Created by lgguzman on 27/03/18.
 */
class OnboardingActivity: BaseActivity(){
    private var NUM_PAGES = 3
    lateinit var binding : ActivityOnboardingBinding
    lateinit var pagerAdapter: ScreenSlidePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        pagerAdapter = ScreenSlidePagerAdapter(NUM_PAGES,supportFragmentManager)
        pager.setAdapter(pagerAdapter)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0->{binding.pagerindex.setImageDrawable(resources.getDrawable(R.drawable.group_7))}
                    1->{binding.pagerindex.setImageDrawable(resources.getDrawable(R.drawable.group_501))}
                    2->{binding.pagerindex.setImageDrawable(resources.getDrawable(R.drawable.group_503))}
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        binding.login.setOnClickListener({
            _ ->  UserService.login().subscribe ({startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))},{})})
        binding.register.setOnClickListener ({
            _ -> UserService.login().subscribe ({startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))},{})})
    }



    class ScreenSlidePagerAdapter(val NUM_PAGES: Int, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        var registeredFragments = SparseArray<Fragment>()

        override fun getCount(): Int {
            return NUM_PAGES
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun getItem(position: Int): Fragment {
            return OnboardingFragment().apply {
                setIndex(position)
            }

        }

        fun getRegisteredFragment(position: Int): Fragment {
            return registeredFragments.get(position)
        }

    }
}