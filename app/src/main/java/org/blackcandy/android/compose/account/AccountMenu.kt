package org.blackcandy.android.compose.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.models.MenuItem

@Composable
fun AccountMenu(menuItems: List<MenuItem>) {
    Column {
        menuItems.forEach {
            AccountMenuItem(
                title = stringResource(id = it.titleResourceId),
                iconResourceId = it.iconResourceId,
                action = it.action,
            )
        }
    }
}

@Composable
fun AccountMenuItem(
    title: String,
    iconResourceId: Int,
    action: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable { action() },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = { Text(title) },
        leadingContent = {
            Icon(
                painterResource(id = iconResourceId),
                contentDescription = title,
            )
        },
    )
}
