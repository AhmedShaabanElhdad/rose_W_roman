package com.smartzone.myapp.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.smartzone.diva_wear.R
import com.smartzone.myapp.data.pojo.City
import com.smartzone.myapp.data.pojo.User
import com.smartzone.diva_wear.databinding.ActivityProfileBinding
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.ui.dailogs.CityDialog
import com.smartzone.myapp.utilis.AppUtils
import com.smartzone.myapp.utilis.BASE_URL_IMAGE
import com.smartzone.myapp.utilis.SavePrefs

import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    lateinit var binding: ActivityProfileBinding
    val viewModel: ProfileViewModel by viewModel()
    var selectCity: City? = null
    var idCity: String? = null
    var nameCity: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        fillData()
        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.notification.setOnClickListener {
            openNotification()
        }

        SavePrefs(MyApp.getApp(), User::class.java).load()?.apply {
            binding.name.setText(this.name)
            binding.etPhone.setText(this.mobile)
            idCity = this.city_id
//            nameCity = this.cityname
        }


        observerUser()
        viewModel.getCity()


        binding.etCity.setOnClickListener {
            binding.etCity.setOnClickListener {
                viewModel.cities.value?.let {
                    CityDialog(this).show(it, false) {
                        selectCity = it
                        binding.etCity.setText(selectCity?.name)
                    }
                } ?: run {
                    viewModel.getCity()
                }
            }
        }
        binding.save.setOnClickListener {
           saveEdit()
        }
        binding.capture.setOnClickListener {
            filesPermissions()
        }
    }

    private fun saveEdit() {
        val user= SavePrefs(this, User::class.java).load()
        if (validateInput()) {
            viewModel.editProfile(
                binding.etPhone.text.toString(),
                binding.name.text.toString(),
                selectCity!!.id,
                user!!.id
            )
        }
    }

    private fun fillData() {
        val user = SavePrefs(this, User::class.java).load()
        user?.let { user ->
            binding.name.setText(user.name)
            binding.etPhone.setText(user.mobile)
            idCity = user.city_id
            user.image?.let { path ->
                val url = BASE_URL_IMAGE + path
                AppUtils.loadGlideImage(
                    binding.getRoot().context,
                    url,
                    R.drawable.com_facebook_profile_picture_blank_square,
                    binding.profile
                )
            }
            //binding.et.setText()
        }
    }

    fun observerUser() {
        viewModel.user.observe(this, Observer {
            SavePrefs(this, User::class.java).save(it)
            finish()
        })
        viewModel.cities.observe(this, Observer {
            if (idCity != null && !idCity.isNullOrEmpty()) {
                for (item in it) {
                    if (item.id == idCity) {
                        binding.etCity.setText(item.name)
                        selectCity = item
                        idCity = item.id
                        break
                    }
                }
            }

        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_profile
    }


    private fun validateInput(): Boolean {
        if (binding.name.text.isBlank()) {
            binding.name.error = getString(R.string.field_required)
            return false
        }
        if (binding.etPhone.text.isBlank()) {
            binding.etPhone.error = getString(R.string.field_required)
            return false
        }
        if (selectCity == null) {
            Toast.makeText(this, getString(R.string.mustSelectCity), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private fun filesPermissions() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val rationale =
            "" + "من فضلك قم بتوفير بعض الصلاحيات لنتمكن من توفير الخدمة "
        val options: Permissions.Options = Permissions.Options()
            .setRationaleDialogTitle("")
            .setSettingsDialogTitle("هام")
        Permissions.check(
            this,
            permissions,
            rationale,
            options,
            object : PermissionHandler() {
                override fun onGranted() {
                    uploadImage()
                }

                override fun onDenied(
                    context: Context?,
                    deniedPermissions: ArrayList<String?>?
                ) {
                    // permission denied, block the feature.
                    filesPermissions()
                }
            })
    }

    private fun uploadImage() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            .toolbarFolderTitle("Folder") // folder selection title
            .toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(resources.getColor(R.color.ef_white)) // Toolbar 'up' arrow color
            .includeVideo(false) // Show video on image picker
            .single() // single mode
            .showCamera(true) // show camera or not (true by default)
            .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
            .enableLog(true) // disabling log
            .start() // start image picker activity with request code
    }

    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TTTTTT", "onActivityResult: ")
        Log.d(
            "TTTTTT",
            "onActivityResult: " + ImagePicker.shouldHandle(requestCode, resultCode, data)
        )
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single ssplash_bg only
            val image = ImagePicker.getFirstImageOrNull(data)
            val file = File(image.path)
            Glide.with(this)
                .load(Uri.fromFile(file)).apply(
                    RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                        .placeholder(R.drawable.default_image)
                ).into(binding.profile)
            viewModel.uploadImage(file)
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, ProfileActivity::class.java)
    }
}