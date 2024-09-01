package com.weiyou.attractions.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.weiyou.attractions.R
import java.util.Locale

object LanguageUtil {

    fun syncLanguage(context: Context, dbLanguage: String) {
        val resources = context.resources
        val currentLocale = resources.configuration.locales.get(0)
        var currentDeviceLanguage: String? = null

        if (currentLocale.language == "zh") {
            currentDeviceLanguage = if (currentLocale.country == "CN") "zh-cn" else "zh-tw"
        } else if (resources.getStringArray(R.array.language_values)
                .contains(currentLocale.language)
        ) {
            currentDeviceLanguage = currentLocale.language
        } else {
            currentDeviceLanguage =
                "zh-tw"  // Default to Traditional Chinese if the language is not supported
        }

        if (dbLanguage != currentDeviceLanguage) {
            setLocale(context, dbLanguage)
            (context as? AppCompatActivity)?.recreate()  // Recreate the activity to apply new language settings
        }
    }

    private fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}