package dev.pegasus.mediastoreapi.ui.general.ui.activities.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import dev.pegasus.mediastoreapi.R

/**
 * @Author: SOHAIB AHMED
 * @Date: 27-12-2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

abstract class BaseActivity<T : ViewBinding>(private val bindingFactory: (LayoutInflater) -> T) : AppCompatActivity() {

    protected val binding: T by lazy { bindingFactory(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullScreenDisplay()
        onCreated()
    }

    private fun enableFullScreenDisplay() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    abstract fun onCreated()
}