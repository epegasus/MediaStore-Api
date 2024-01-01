package dev.pegasus.mediastoreapi.ui.general.ui.fragments.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

open class BasePermissionFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var alertDialog: AlertDialog? = null
    private var callback: ((Boolean) -> Unit)? = null
    private var permissionName: String? = null
    private var permissionArray = arrayOf<String>()
    private var isPermissionDialogShowing = false

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        val values: Collection<Boolean> = it.values
        val contains = values.contains(true)
        callback?.invoke(contains)
    }

    private var settingLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        permissionName?.let { permissionLauncher.launch(permissionArray) }
    }

    fun checkStoragePermission(): Boolean {
        context?.let {
            val permissionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            return (ContextCompat.checkSelfPermission(it, permissionName) == PackageManager.PERMISSION_GRANTED)
        } ?: return false
    }

    fun askStoragePermission(permissionGranted: (Boolean) -> Unit) {
        this.callback = permissionGranted
        context?.let {
            sharedPreferences = it.getSharedPreferences("permission_preferences", Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()

            val permissionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            permissionArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            this.permissionName = permissionName
            if (ContextCompat.checkSelfPermission(it, permissionName) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted.invoke(true)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permissionName))
                    showPermissionDialog()
                else {
                    if (sharedPreferences.getBoolean(permissionName, true)) {
                        editor.putBoolean(permissionName, false)
                        editor.apply()
                        permissionLauncher.launch(permissionArray)
                    } else {
                        showSettingDialog()
                    }
                }
            }
        }
    }

    @Synchronized
    private fun showPermissionDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Storage Permission Required")
        builder.setMessage("Will you grant us storage permission?")
        builder.setPositiveButton("Grant") { _, _ -> permissionLauncher.launch(permissionArray) }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        alertDialog = builder.show()
    }

    private fun showSettingDialog() {
        val builder = context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Permission required")
                .setMessage("Allow permission from 'Setting' to proceed")
                .setCancelable(false)
                .setPositiveButton("Setting") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    openSettingPage()
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
        }

        if (!(context as Activity).isFinishing)
            builder?.show()
    }

    private fun openSettingPage() {
        context?.let {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", it.packageName, null)
            intent.data = uri
            settingLauncher.launch(intent)
        }
    }

    override fun onPause() {
        isPermissionDialogShowing = alertDialog?.isShowing ?: false
        alertDialog?.dismiss()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionDialogShowing) {
            alertDialog?.show()
        }
    }
}