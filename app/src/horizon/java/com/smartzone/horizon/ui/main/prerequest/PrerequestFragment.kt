package com.smartzone.horizon.ui.main.prerequest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.FragmentPrerequestBinding
import com.smartzone.myapp.MyApp
import com.smartzone.myapp.data.pojo.User
import com.smartzone.myapp.ui.base.BaseFragment
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.SavePrefs
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PrerequestFragment : BaseFragment<FragmentPrerequestBinding>() {

    lateinit var binding: FragmentPrerequestBinding
    val viewModel: PrerequestViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=getViewDataBinding()
        observeSetting()


        SavePrefs(MyApp.getApp(), User::class.java).load()?.apply {
            binding.name.setText(this.name)
            binding.etPhone.setText(this.mobile)
        }

        binding.sendBtn.setOnClickListener {
            if (validateInput()) {
               viewModel.sendPrescription(binding.name.text.toString(),binding.etPhone.text.toString(),binding.message.text.toString())
            }
        }

        binding.sendUploadBtn.setOnClickListener {
            //viewModel.uploadImage()
            uploadImage()
        }
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
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single ssplash_bg only
            val image = ImagePicker.getFirstImageOrNull(data)
            val file = File(image.path)
//            Glide.with(this)
//                .load(Uri.fromFile(file)).apply(
//                    RequestOptions()
//                        .centerCrop()
//                        .dontAnimate()
//                        .dontTransform()
//                        .placeholder(R.drawable.default_image)
//                ).into(binding.profile)
            viewModel.uploadImage(file)
        }
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
        if (binding.message.text.isBlank()) {
            binding.message.error = getString(R.string.field_required)
            return false
        }

        return true
    }


    fun observeSetting(){
        viewModel.success.observe(this, Observer {
            if (it){
                Toast.makeText(requireContext(),getString(R.string.yourRequest),Toast.LENGTH_LONG).show()
            }
        })
        viewModel.pathImage.observe(this, Observer {
                Toast.makeText(requireContext(),getString(R.string.yourRequest),Toast.LENGTH_LONG).show()
        })
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_prerequest
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object{
        fun getIntent(context: Context)=Intent(context, PrerequestFragment::class.java)
    }
}