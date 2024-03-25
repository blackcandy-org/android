package org.blackcandy.android.utils

import android.app.Activity
import android.view.View
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.google.android.material.snackbar.Snackbar
import org.blackcandy.android.R
import org.blackcandy.android.models.AlertMessage

class SnackbarUtil {
    companion object {
        @Composable
        fun ShowSnackbar(
            message: AlertMessage,
            state: SnackbarHostState,
            onShown: () -> Unit,
        ) {
            val snackbarText =
                when (message) {
                    is AlertMessage.String -> message.value
                    is AlertMessage.StringResource -> stringResource(message.value)
                } ?: return

            LaunchedEffect(state) {
                state.showSnackbar(snackbarText)
                onShown()
            }
        }

        fun showSnackbar(
            activity: Activity,
            message: AlertMessage,
            onShown: () -> Unit,
        ) {
            val rootView = activity.findViewById<View>(R.id.main_layout)

            val snackbarText =
                when (message) {
                    is AlertMessage.String -> message.value
                    is AlertMessage.StringResource -> rootView.context.getString(message.value)
                } ?: return

            Snackbar.make(rootView, snackbarText, Snackbar.LENGTH_SHORT).addCallback(
                object : Snackbar.Callback() {
                    override fun onShown(sb: Snackbar?) {
                        super.onShown(sb)
                        onShown()
                    }
                },
            ).show()
        }
    }
}
