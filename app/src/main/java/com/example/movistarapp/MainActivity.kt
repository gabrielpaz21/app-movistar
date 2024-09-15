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
import com.example.movistarapp.databinding.ActivityMainBinding
import com.example.movistarapp.databinding.ShortcutBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        handleWindowInsets()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setupShortcuts()
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupShortcuts() {
        setupShortcut(
            binding.planShortcut,
            R.drawable.plans,
            getString(R.string.plansText),
            getString(R.string.optionsForYouText),
            UrlConstants.PLAN_URL
        )
        setupShortcut(
            binding.contactShortcut,
            R.drawable.contact_us,
            getString(R.string.contactUsText),
            getString(R.string.managementChannelsText),
            UrlConstants.CONTACT_URL
        )
        setupShortcut(
            binding.storeShortcut,
            R.drawable.store,
            getString(R.string.storeText),
            getString(R.string.buyLineText),
            UrlConstants.STORE_URL
        )
        setupShortcut(
            binding.clubShortcut,
            R.drawable.movistar_club,
            getString(R.string.movistarClubText),
            getString(R.string.benefitsText),
            UrlConstants.CLUB_URL
        )
    }

    private fun setupShortcut(
        shortcutBinding: ShortcutBinding,
        imageResource: Int,
        titleText: String,
        subtitleText: String,
        stringUrl: String
    ) {
        shortcutBinding.imageButton.setImageResource(imageResource)
        shortcutBinding.title.text = titleText
        shortcutBinding.subtitle.text = subtitleText
        shortcutBinding.imageButton.setOnClickListener {
            handleShortcutClick(stringUrl)
        }
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