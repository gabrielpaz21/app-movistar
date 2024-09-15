package com.example.movistarapp

import android.content.Context

enum class Platform {
    PREPAID,
    POSTPAID
}

fun Platform.toLocalizedString(context: Context): String {
    return when (this) {
        Platform.PREPAID -> context.getString(R.string.platformPrepaid)
        Platform.POSTPAID -> context.getString(R.string.platformPostpaid)
    }
}
