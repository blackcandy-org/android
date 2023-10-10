package org.blackcandy.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import kotlinx.coroutines.launch
import org.blackcandy.android.compose.login.LoginScreen
import org.blackcandy.android.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModel()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initServerAddress()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUserFlow.collect {
                    if (it != null) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                        startActivity(intent)
                    }
                }
            }
        }

        setContent {
            Mdc3Theme {
                LoginScreen(viewModel = viewModel)
            }
        }
    }
}
