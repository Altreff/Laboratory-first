package com.example.maybefirstalready

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class IntentsFragment : Fragment() {

    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            imageView.setImageURI(selectedImageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_intents, container, false)

        imageView = view.findViewById(R.id.imageView)
        val selectImageButton = view.findViewById<Button>(R.id.selectImageButton)
        val shareImageButton = view.findViewById<Button>(R.id.shareImageButton)

        selectImageButton.setOnClickListener {
            openGallery()
        }

        shareImageButton.setOnClickListener {
            if (selectedImageUri != null) {
                shareToInstagramStories(selectedImageUri!!)
            } else {
                Toast.makeText(requireContext(), "Сначала выберите изображение", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun shareToInstagramStories(imageUri: Uri) {
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(imageUri, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra("source_application", "1592444761470600")
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Instagram не установлен или не поддерживает эту функцию", Toast.LENGTH_SHORT).show()
        }
    }
}
