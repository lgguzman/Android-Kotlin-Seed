package com.lgguzman.example.kotlinseed.ui.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lgguzman.example.kotlinseed.R
import com.lgguzman.example.kotlinseed.databinding.FragmentOnboardingBinding

class OnboardingFragment: Fragment(){
    val text =  arrayOf(R.string.activity_onboarding_1,R.string.activity_onboarding_2,R.string.activity_onboarding_3)
    var position: Int=0;

    fun setIndex(value: Int){
        position = value
    }

    lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_onboarding, container, false)
        binding.data =OnboardingData(getText(text[position]))
        return binding.root;
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    data class OnboardingData(val text:CharSequence)
}