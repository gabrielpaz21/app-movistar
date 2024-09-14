package com.example.movistarapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
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
        handleWindowInsets()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setupShortcuts()
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupShortcuts() {
        setupShortcut(
            R.id.plan_shortcut,
            R.drawable.plans,
            getString(R.string.plansText),
            getString(R.string.optionsForYouText),
            UrlConstants.PLAN_URL
        )
        setupShortcut(
            R.id.contact_shortcut,
            R.drawable.contact_us,
            getString(R.string.contactUsText),
            getString(R.string.managementChannelsText),
            UrlConstants.CONTACT_URL
        )
        setupShortcut(
            R.id.store_shortcut,
            R.drawable.store,
            getString(R.string.storeText),
            getString(R.string.buyLineText),
            UrlConstants.STORE_URL
        )
        setupShortcut(
            R.id.club_shortcut,
            R.drawable.movistar_club,
            getString(R.string.movistarClubText),
            getString(R.string.benefitsText),
            UrlConstants.CLUB_URL
        )
    }

    private fun setupShortcut(
        cardViewId: Int,
        imageResource: Int,
        titleText: String,
        subtitleText: String,
        stringUrl: String
    ) {
        val cardView: CardView = findViewById(cardViewId)
        val shortcutButton: ImageButton = cardView.findViewById(R.id.imageView)
        shortcutButton.setOnClickListener {
            handleShortcutClick(stringUrl)
        }
        val title: TextView = cardView.findViewById(R.id.title)
        val subtitle: TextView = cardView.findViewById(R.id.subtitle)
        shortcutButton.setImageResource(imageResource)
        title.text = titleText
        subtitle.text = subtitleText
    }

    private fun handleShortcutClick(stringUrl: String) {
        if (NetworkUtils.isInternetAvailable(this)) {
            try {
                val url = Uri.parse(stringUrl)
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            } catch (e: Exception) {
                handleShortcutError(e)
            }
        } else {
            showToast(getString(R.string.noInternetConnection))
        }
    }

    private fun handleShortcutError(e: Exception) {
        when (e) {
            is ActivityNotFoundException -> showToast(getString(R.string.noBrowserFound))
            is SecurityException -> showToast(getString(R.string.insufficientPermissions))
            else -> showToast(getString(R.string.errorOpeningUrl))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}