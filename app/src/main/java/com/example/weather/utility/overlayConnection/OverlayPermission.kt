package com.example.weather.utility.overlayConnection


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.weathear.R

object OverlayPermission {

    const val PERMISSION_ID = 55

    fun checkOverlayPermission(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun requestOverlayPermission(fragment: Fragment) {
        val alertDialogBuilder = AlertDialog.Builder(fragment.requireContext())
        alertDialogBuilder.setTitle(fragment.resources.getString(R.string.delete_alaret))
            .setMessage(fragment.resources.getString(R.string.alaret_permission))
            .setPositiveButton(fragment.resources.getString(R.string.yes)) { dialog, _ ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + fragment.requireActivity().packageName)
                )
                Log.e("WeatherOverlayPermission", " going to permission settings" )

                // request permission via start activity for result
                fragment.startActivityForResult(intent, PERMISSION_ID)
                // It will call onActivityResult Function After you press Yes/No and go Back after giving permission
                Log.e("WeatherOverlayPermission", " go Back after giving permission" )

                dialog.dismiss()
            }
            .setNegativeButton(fragment.resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun handleOverlayPermissionResult(
        requestCode: Int,
        grantResults: IntArray,
        permissionCallback: () -> Unit
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, invoke the callback
                permissionCallback.invoke()
            } else {
                Log.e("WeatherOverlay","Permission denied")
            }
        }
    }

}
