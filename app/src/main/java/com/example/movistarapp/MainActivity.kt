package com.example.movistarapp

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setupShortcut(R.id.plan_shortcut, R.drawable.plans, getString(R.string.plansText), getString(R.string.optionsForYouText))
        setupShortcut(R.id.contact_shortcut, R.drawable.contact_us, getString(R.string.contactUsText), getString(R.string.managementChannelsText))
        setupShortcut(R.id.store_shortcut, R.drawable.store, getString(R.string.storeText), getString(R.string.buyLineText))
        setupShortcut(R.id.club_shortcut, R.drawable.movistar_club, getString(R.string.movistarClubText), getString(R.string.benefitsText))

    }

    fun setupShortcut(cardViewId: Int, imageResource: Int, titleText: String, subtitleText: String) {
        val cardView: CardView = findViewById(cardViewId)
        val shortcutImage: ImageView = cardView.findViewById(R.id.imageView)
        val title: TextView = cardView.findViewById(R.id.title)
        val subtitle: TextView = cardView.findViewById(R.id.subtitle)

        shortcutImage.setImageResource(imageResource)
        title.text = titleText
        subtitle.text = subtitleText
    }

}