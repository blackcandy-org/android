package org.blackcandy.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import org.blackcandy.android.compose.login.LoginScreen

class LoginActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Mdc3Theme {
                LoginScreen()
            }
        }
    }
}
