package com.example.criminalintent.fragments

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.databinding.CustomShowPhotoFragmentBinding
import com.example.criminalintent.utils.getScaledBitmap

class PhotoDisplayFragment : DialogFragment(){
    private val TAG = "PhotoDisplayFragment_TAG"
    private val args: PhotoDisplayFragmentArgs by navArgs()
    private lateinit var binding : CustomShowPhotoFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val inflater = layoutInflater
        binding = CustomShowPhotoFragmentBinding.inflate(inflater)

        dialog.setContentView(binding.root)

        val path = "${args.pathDir}/${args.photoName}"

        binding.ivPhoto.apply {
            doOnLayout {
                setImageBitmap(
                    getPhoto(path,this)
                )
            }
        }

        return dialog
    }

    private fun getPhoto(path: String, imageView: ImageView) : Bitmap{
        return getScaledBitmap(
            path, //path to file in the internal memory
            imageView.width,
            imageView.height
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}