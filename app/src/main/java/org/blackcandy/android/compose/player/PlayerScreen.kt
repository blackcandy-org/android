package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import org.blackcandy.android.R

@Composable
fun PlayerScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
    ) {
        PlayerInfo()
    }
}
