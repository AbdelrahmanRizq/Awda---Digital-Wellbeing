package com.awda.app.presentation.biometric

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.awda.app.common.Constants
import com.awda.app.common.getAppName

/**
 * Created by Abdelrahman Rizq
 */

class BiometricAuthActivity : AppCompatActivity() {

    var pName: String? = null

    companion object {
        var callbacks: Callbacks? = null
        fun start(context: Context, pName: String) {
            val intent = Intent(context, BiometricAuthActivity::class.java)
            intent.putExtra(Constants.SECURE_APP, pName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(intent)
        }
    }

    interface Callbacks {
        fun onAuthenticationSucceeded()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pName = intent.getStringExtra(Constants.SECURE_APP)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
    }

    override fun onResume() {
        super.onResume()
        showBiometricPrompt(pName)
    }

    override fun onPause() {
        super.onPause()
        finishAffinity()
    }

    private fun showBiometricPrompt(pName: String?) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    callbacks?.onAuthenticationSucceeded()
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        this@BiometricAuthActivity,
                        "Authentication failed, try again",
                        Toast.LENGTH_SHORT
                    ).show()
                    showBiometricPrompt(pName)
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock ${if (pName == null) "App" else getAppName(pName)}")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
