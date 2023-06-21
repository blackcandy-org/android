package org.blackcandy.android.compose.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.blackcandy.android.R

@Composable
fun LoginConnectionForm(
    modifier: Modifier = Modifier,
    onConnectButtonClicked: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_small),
        ),
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.black_candy_logo),
            contentDescription = stringResource(R.string.logo_description),
            modifier = Modifier
                .width(dimensionResource(R.dimen.logo_width))
                .padding(bottom = dimensionResource(R.dimen.padding_medium)),
        )

        OutlinedTextField(
            value = "",
            label = { Text(text = stringResource(R.string.server_address)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onConnectButtonClicked()
            },
        ) {
            Text(text = stringResource(R.string.connect))
        }
    }
}

@Preview
@Composable
fun LoginConnectionFormPreview() {
    LoginConnectionForm(
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
        onConnectButtonClicked = {},
    )
}
