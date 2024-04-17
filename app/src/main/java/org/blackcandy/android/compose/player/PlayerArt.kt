package org.blackcandy.android.compose.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import org.blackcandy.android.R

@Composable
fun PlayerArt(
    modifier: Modifier = Modifier,
    imageURL: String?,
    size: Dp,
) {
    AsyncImage(
        model = imageURL,
        contentDescription = stringResource(R.string.album_cover),
        modifier =
            modifier
                .width(size)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)))
                .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
        contentScale = ContentScale.FillWidth,
    )
}
