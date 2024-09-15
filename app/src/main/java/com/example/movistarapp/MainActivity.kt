package com.example.movistarapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movistarapp.databinding.ActivityMainBinding
import com.example.movistarapp.databinding.ShortcutBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    private lateinit var binding: ActivityMainBinding

    private var users: List<ResponseDTO> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        handleWindowInsets()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fetchUsers()
        setupShortcuts()
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchUsers() {
        apiService = RetrofitInstance.getInstance().create(ApiService::class.java)
        thread {
            try {
                val response = apiService.getUsers().execute()
                if (response.isSuccessful && response.body() != null) {
                    users = response.body()!!

                    runOnUiThread {
                        val mobileNumbers = users.map { it.mobilNumber }
                        setSpinnerAdapter(mobileNumbers)
                    }
                } else {
                    runOnUiThread {
                        showToast("Error: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showToast("Exception: ${e.message}")
                }
            }
        }
    }

    private fun setSpinnerAdapter(mobileNumbers: List<String>) {
        val spinnerNumber = binding.spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mobileNumbers)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerNumber.adapter = adapter
        spinnerNumber.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long){
                val selectedNumber = mobileNumbers[position]

                users.find { it.mobilNumber == selectedNumber }?.let {
                    binding.name.text = getString(R.string.greeting, it.name)
                    binding.numberPhone.text = it.mobilNumber
                    binding.platform.text = it.platform.toLocalizedString(this@MainActivity)
                    binding.cutOffDate.text = getString(R.string.cutOffDateText, it.cutOffDate)
                    binding.balance.balanceAmount.text = it.balance.toPlainString()
                    binding.data.amount.text=it.data.toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

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