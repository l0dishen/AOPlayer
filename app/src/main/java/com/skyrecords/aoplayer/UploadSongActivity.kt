package com.skyrecords.aoplayer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.skyrecords.aoplayer.databinding.ActivityUploadSongBinding

class UploadSongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadSongBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var selectedSongUri: Uri? = null

    private val REQUEST_CODE_PICK_AUDIO = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MainActivity.currentTheme[MainActivity.themeIndex])
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentTheme[MainActivity.themeIndex])
        binding = ActivityUploadSongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutUpload,
            binding.toolbarUpload,
            R.string.open,
            R.string.close
        )
        binding.drawerLayoutUpload.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Upload Song"
        setSupportActionBar(binding.toolbarUpload)


        binding.navigationViewUpload.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_upload -> startActivity(Intent(this, UploadSongActivity::class.java))
                R.id.navSettings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.navAbout -> startActivity(Intent(this, AboutActivity::class.java))
                R.id.navExit -> finishAffinity()
            }
            true
        }

        binding.progressBar.visibility = View.GONE

        binding.chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_AUDIO)
        }

        binding.uploadButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val artist = binding.artistEditText.text.toString().trim()

            if (selectedSongUri != null && title.isNotEmpty() && artist.isNotEmpty()) {
                uploadSongToFirebase(title, artist, selectedSongUri!!)
            } else {
                Toast.makeText(this, "Please fill all fields and select a song", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_AUDIO && resultCode == Activity.RESULT_OK) {
            selectedSongUri = data?.data
            Toast.makeText(this, "Song Selected!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadSongToFirebase(title: String, artist: String, fileUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("songs/${System.currentTimeMillis()}.mp3")

        binding.progressBar.visibility = View.VISIBLE

        val uploadTask = storageRef.putFile(fileUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception ?: Exception("Upload failed")
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            binding.progressBar.visibility = View.GONE

            if (task.isSuccessful) {
                val downloadUri = task.result
                saveSongToFirestore(title, artist, downloadUri.toString())
            } else {
                Toast.makeText(this, "Upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveSongToFirestore(title: String, artist: String, url: String) {
        val db = FirebaseFirestore.getInstance()
        val songData = hashMapOf(
            "title" to title,
            "artist" to artist,
            "url" to url
        )

        db.collection("Songs")
            .add(songData)
            .addOnSuccessListener {
                Toast.makeText(this, "Song uploaded successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving song info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
