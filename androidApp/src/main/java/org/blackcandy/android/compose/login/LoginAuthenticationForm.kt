package org.blackcandy.android.compose.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import org.blackcandy.android.R

@Composable
fun LoginAuthenticationForm(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onLoginButtonClicked: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_small),
            ),
        modifier = modifier,
    ) {
        val submitEnabled = email.isNotEmpty() && password.isNotEmpty()
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = email,
            label = { Text(text = stringResource(R.string.email)) },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
            onValueChange = onEmailChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = password,
            label = { Text(text = stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                        if (submitEnabled) {
                            onLoginButtonClicked()
                        }
                    },
                ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = submitEnabled,
            onClick = { onLoginButtonClicked() },
        ) {
            Text(text = stringResource(R.string.login))
        }
    }
}

@Preview
@Composable
fun LoginAuthenticationFormPreview() {
    LoginAuthenticationForm(
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
        email = "",
        password = "",
        onEmailChanged = {},
        onPasswordChanged = {},
        onLoginButtonClicked = {},
    )
}
