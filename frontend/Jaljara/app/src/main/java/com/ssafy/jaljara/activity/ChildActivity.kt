package com.ssafy.jaljara.activity

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.ssafy.jaljara.manager.SleepManager.registerForSleepUpdates
import com.ssafy.jaljara.ui.screen.child.ChildApp
import com.ssafy.jaljara.ui.theme.JaljaraTheme

class ChildActivity: ComponentActivity() {

    companion object {
        private const val TAG = "ChildActivity"
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arrayOf(Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS,
        )

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted.
                    Log.d(TAG, "Permission granted: ACTIVITY_RECOGNITION")
                    registerForSleepUpdates(
                        applicationContext,
                        // Callback functions
//                        ::registerForSleepUpdatesOnSuccess,
//                        ::registerForSleepUpdatesOnFailure
                    )
                } else {
                    // Permission declined.
                    Log.d(TAG, "Permission declined: ACTIVITY_RECOGNITION")

                    // Inform user of unavailable features.
                    AlertDialog.Builder(this)
                        .setMessage("수면 감지는 신체 활동 정보 권한을 허용 해야만 가능해요. 권한을 실행시켜주세요.")
                        .setTitle("권한 거부됨")
                        .create()
                        .show()
                }
            }

        // Check permissions and request them if not granted
        when {
            checkSelfPermission(
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted.
                Log.d(TAG, "Permission granted: ACTIVITY_RECOGNITION")
                registerForSleepUpdates(
                    applicationContext,
                    // Callback functions
//                    ::registerForSleepUpdatesOnSuccess,
//                    ::registerForSleepUpdatesOnFailure
                )
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION) -> {
                // Show rationale for required permission. Must include a no/cancel option.
                Log.d(TAG, "Permission will be requested after informing user of rationale (Activity Recognition).")
                val alertDialog: AlertDialog? = this.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setMessage("신체 활동 정보 권한이 필요해요.")
                        setTitle("권한 필요")
                        setPositiveButton("ok") { _, _ ->
                            // User clicked OK button - request permissions
                            Log.d(TAG, "Permission will be requested (Activity Recognition).")
                            requestPermissionLauncher.launch(
                                Manifest.permission.ACTIVITY_RECOGNITION
                            )
                        }
                        setNegativeButton("cancel") { _, _ ->
                            // User cancelled the dialog
                            Log.d(TAG, "Permission request cancelled by user (Activity Recognition).")
                        }
                    }
                    // Create the AlertDialog
                    builder.create()
                }
                alertDialog?.show()
            }
            else -> {
                // Request permission
                Log.d(TAG, "Permission will be requested, rationale not required (Activity Recognition).")
                requestPermissionLauncher.launch(
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            }
        }

        setContent {
            JaljaraTheme {
                ChildApp()
            }
        }
    }
}