package com.example.movistarapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setupShortcut(
            R.id.plan_shortcut,
            R.drawable.plans,
            getString(R.string.plansText),
            getString(R.string.optionsForYouText),
            "https://www.movistar.com.ve/Particulares/planes-movistar-plus.html"
        )
        setupShortcut(
            R.id.contact_shortcut,
            R.drawable.contact_us,
            getString(R.string.contactUsText),
            getString(R.string.managementChannelsText),
            "https://www.movistar.com.ve/Particulares/Autogestion.html"
        )
        setupShortcut(
            R.id.store_shortcut,
            R.drawable.store,
            getString(R.string.storeText),
            getString(R.string.buyLineText),
            "https://tienda.movistar.com.ve/linea-nueva"
        )
        setupShortcut(
            R.id.club_shortcut,
            R.drawable.movistar_club,
            getString(R.string.movistarClubText),
            getString(R.string.benefitsText),
            "https://www.movistar.com.ve/Particulares/Antesala_club_movistar.html"
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
            if (isInternetAvailable(this)) {
                try {
                    val url = Uri.parse(stringUrl)
                    val intent = Intent(Intent.ACTION_VIEW, url)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "No se encontró ningún navegador web", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: SecurityException) {
                    Toast.makeText(
                        this,
                        "Permisos insuficientes para abrir la URL",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Ocurrió un error al intentar abrir la URL",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show()
            }
        }
        val title: TextView = cardView.findViewById(R.id.title)
        val subtitle: TextView = cardView.findViewById(R.id.subtitle)
        shortcutButton.setImageResource(imageResource)
        title.text = titleText
        subtitle.text = subtitleText
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}