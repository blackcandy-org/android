package org.blackcandy.android.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.blackcandy.android.R

@Composable
fun CustomErrorScreen(
    errorDescription: String,
    onRetry: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_warning_24),
            contentDescription = null,
            modifier =
                Modifier
                    .size(dimensionResource(R.dimen.icon_size_large))
                    .padding(bottom = dimensionResource(R.dimen.padding_small)),
        )

        Text(
            text = errorDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_small)),
        )

        Row {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }

            TextButton(onClick = onLogout) {
                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
