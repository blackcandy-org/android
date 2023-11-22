package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R

@Composable
fun PlayerActions(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_repeat_24),
            contentDescription = stringResource(R.string.repeat_mode),
        )

        Icon(
            painter = painterResource(R.drawable.baseline_favorite_border_24),
            contentDescription = stringResource(R.string.unfavorited),
        )

        Icon(
            painter = painterResource(R.drawable.baseline_format_list_bulleted_24),
            contentDescription = stringResource(R.string.playlist),
        )
    }
}
