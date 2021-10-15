package com.smartzone.myapp.ui.main.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.smartzone.diva_wear.BuildConfig
import com.smartzone.myapp.MyApp
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.SettingsFragmentBinding
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.ui.change_password.ChangePasswordActivity
import com.smartzone.myapp.ui.contact_us.ContactUsActivity
import com.smartzone.myapp.ui.login.LoginActivity
import com.smartzone.myapp.ui.main.MainActivity
import com.smartzone.myapp.ui.profile.ProfileActivity
import com.smartzone.myapp.utilis.AppUtils
import com.smartzone.myapp.utilis.BASE_URL_IMAGE
import com.smartzone.myapp.utilis.LocaleUtils
import com.smartzone.myapp.utilis.SavePrefs

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() =
            SettingsFragment()
    }

    lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //LocaleUtils.updateConfig(requireActivity().applicationContext)
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fillData()

        if (BuildConfig.FLAVOR == "horizon") {
            binding.languageLinear.visibility = View.VISIBLE
            binding.languageView.visibility = View.VISIBLE
            binding.offers.visibility = View.VISIBLE
        }
        if (BuildConfig.FLAVOR == "roseRoman")
            binding.offers.visibility = View.VISIBLE

        binding.notification.setOnClickListener {
            (activity as MainActivity).openNotification()
        }

        binding.changePassword.setOnClickListener {
            startActivity(ChangePasswordActivity.getIntent(requireContext()))
        }


        handleClick()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun fillData() {
        val user = SavePrefs(binding.getRoot().context, User::class.java).load()
        user?.let { userData ->
            binding.name.text = userData.name
            binding.tvPhone.text = userData.mobile
            userData.image?.let { path ->
                val url = BASE_URL_IMAGE + path
                AppUtils.loadGlideImage(
                    binding.getRoot().context,
                    url,
                    R.drawable.com_facebook_profile_picture_blank_square,
                    binding.profile
                )
            }
        }
    }

    private fun handleClick() {
        binding.contactus.setOnClickListener {
            activity?.let {
                startActivity(ContactUsActivity.getIntent(it))
            }

        }

        binding.contentProfile.setOnClickListener {
            startActivityForResult(ProfileActivity.getIntent(binding.getRoot().context), 1)
        }

        binding.favourite.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_setting_to_navigation_favourite)
        }
        binding.languageLinear.setOnClickListener {

        }

        binding.offers.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_setting_to_navigation_offers)
        }

        binding.logout.setOnClickListener {
            activity?.let {
                MyApp.getApp().appPreferencesHelper.setLogin(false)
                SavePrefs(it, User::class.java).clear()
            }
            startActivity(LoginActivity.getIntent(binding.getRoot().context).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
            activity?.finishAffinity()
        }

        binding.pref = SavePrefs(requireActivity().applicationContext, User::class.java)
        binding.txtChangeLang.setOnClickListener {
            LocaleUtils.changeLanguage(requireContext())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            fillData()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}