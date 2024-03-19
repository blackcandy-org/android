package org.blackcandy.android.compose.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.blackcandy.android.R
import org.blackcandy.android.models.Song
import sh.calvin.reorderable.ReorderableItemScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPlaylistItem(
    song: Song,
    isCurrent: Boolean,
    scope: ReorderableItemScope,
    onSweepToDismiss: (Int) -> Unit,
    onClicked: (Int) -> Unit,
    onMoveStarted: (Int) -> Unit,
    onMoveEnded: (Int) -> Unit,
) {
    val dismissState =
        rememberSwipeToDismissBoxState(
            confirmValueChange = {
                if (it == SwipeToDismissBoxValue.EndToStart) {
                    onSweepToDismiss(song.id)
                }

                it == SwipeToDismissBoxValue.EndToStart
            },
            positionalThreshold = { it * 0.5f },
        )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.error)
                        .padding(horizontal = dimensionResource(R.dimen.padding_small)),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.onError,
                )
            }
        },
    ) {
        ListItem(
            modifier =
                Modifier
                    .clickable(
                        onClick = { onClicked(song.id) },
                    ),
            headlineContent = {
                Text(
                    text = song.name,
                    color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Unspecified,
                    fontWeight = if (isCurrent) FontWeight.Bold else null,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            supportingContent = {
                Text(
                    text = song.artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            trailingContent = {
                Icon(
                    modifier =
                        with(scope) {
                            Modifier.draggableHandle(
                                onDragStarted = {
                                    onMoveStarted(song.id)
                                },
                                onDragStopped = {
                                    onMoveEnded(song.id)
                                },
                            )
                        },
                    painter = painterResource(R.drawable.baseline_drag_handle_24),
                    contentDescription = stringResource(R.string.drag_handle),
                )
            },
        )
    }

    HorizontalDivider()
}
