package org.blackcandy.android.utils

import android.app.Activity
import android.view.View
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.google.android.material.snackbar.Snackbar
import org.blackcandy.android.R
import org.blackcandy.shared.models.AlertMessage

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
                    is AlertMessage.LocalizedString -> stringResource(getLocalizedString(message.value))
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
                    is AlertMessage.LocalizedString -> rootView.context.getString(getLocalizedString(message.value))
                } ?: return

            Snackbar
                .make(rootView, snackbarText, Snackbar.LENGTH_SHORT)
                .addCallback(
                    object : Snackbar.Callback() {
                        override fun onShown(sb: Snackbar?) {
                            super.onShown(sb)
                            onShown()
                        }
                    },
                ).show()
        }

        fun getLocalizedString(definedMessage: AlertMessage.DefinedMessages): Int =
            when (definedMessage) {
                AlertMessage.DefinedMessages.UNSUPPORTED_SERVER -> R.string.unsupported_server
                AlertMessage.DefinedMessages.INVALID_SERVER_ADDRESS -> R.string.invalid_server_address
                AlertMessage.DefinedMessages.ADDED_TO_PLAYLIST -> R.string.added_to_playlist
            }
    }
}
