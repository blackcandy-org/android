package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                .padding(bottom = dimensionResource(R.dimen.padding_small)),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlayerInfo()
            PlayerControl(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.padding_medium)),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PlayerActions(
            modifier =
                Modifier
                    .padding(top = dimensionResource(R.dimen.padding_medium))
                    .padding(horizontal = dimensionResource(R.dimen.padding_small)),
        )
    }
}
