package org.blackcandy.android.compose.login

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.blackcandy.android.R

enum class LoginForm(@StringRes val title: Int) {
    Connection(R.string.connection_title),
    Authentication(R.string.authentication_title),
}

@Composable
fun LoginScreen(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentForm = LoginForm.valueOf(
        backStackEntry?.destination?.route ?: LoginForm.Connection.name,
    )

    Scaffold(
        topBar = {
            LoginScreenAppBar(
                currentForm = currentForm,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginForm.Connection.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = LoginForm.Connection.name) {
                LoginConnectionForm(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    onConnectButtonClicked = { navController.navigate(LoginForm.Authentication.name) },
                )
            }

            composable(route = LoginForm.Authentication.name) {
                LoginAuthenticationForm(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenAppBar(
    currentForm: LoginForm,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(currentForm.title)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_description),
                    )
                }
            }
        },
    )
}
