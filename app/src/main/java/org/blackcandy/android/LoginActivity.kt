package org.blackcandy.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.google.accompanist.themeadapter.material3.Mdc3Theme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Mdc3Theme {
                Button(onClick = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }) {
                    Text(text = "Hi")
                }
            }
        }
    }
}
