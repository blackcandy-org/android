package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import org.blackcandy.android.R

@Composable
fun MiniPlayer() {
    Row(
        modifier = Modifier.height(dimensionResource(R.dimen.mini_player_height)),
    ) {
        Text(text = "MiniPlayer")
    }
}
