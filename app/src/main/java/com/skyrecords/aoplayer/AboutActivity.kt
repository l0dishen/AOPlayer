package com.skyrecords.aoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skyrecords.aoplayer.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "About"
        binding.aboutText.text = aboutText()
    }
    private fun aboutText(): String{
        return "SkyRecords is an independent record label based in Moorhead, Minnesota, founded by three passionate young music lovers. \n" +
                "    Driven by their shared vision to uplift emerging artists, they created SkyRecords to provide a platform that nurtures creativity, \n" +
                "    supports innovation, and promotes musical expression across genres.\n" +
                "\n" +
                "    The Ao Player is SkyRecords' very own online streaming platform, developed by the in-house SkyRecords software team. \n" +
                "    This high-quality media player offers users fast and free access to our ever-growing library of music online. \n" +
                "    We are committed to constantly improving your listening experience — feel free to send us your feedback at \n" +
                "    technical@skyrecords.com."
    }
}