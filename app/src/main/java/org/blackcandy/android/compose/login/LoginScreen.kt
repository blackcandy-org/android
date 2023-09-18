package org.blackcandy.android.compose.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.blackcandy.android.R
import org.blackcandy.android.models.AlertMessage
import org.blackcandy.android.viewmodels.LoginRoute
import org.blackcandy.android.viewmodels.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: LoginViewModel = koinViewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val uiState by viewModel.uiState.collectAsState()

    val currentRoute = LoginRoute.valueOf(
        backStackEntry?.destination?.route ?: LoginRoute.Connection.name,
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LoginScreenAppBar(
                currentRoute = currentRoute,
                canNavigateBack = currentRoute != LoginRoute.Connection,
                navigateUp = {
                    navController.previousBackStackEntry?.destination?.route?.let {
                        val previousRoute = LoginRoute.valueOf(it)
                        viewModel.updateLoginRoute(previousRoute)
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginRoute.Connection.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = LoginRoute.Connection.name) {
                LoginConnectionForm(
                    serverAddress = uiState.serverAddress,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    onConnectButtonClicked = {
                        keyboardController?.hide()
                        viewModel.checkSystemInfo()
                    },
                    onServerAddressChanged = { viewModel.updateServerAddress(it) },
                )
            }

            composable(route = LoginRoute.Authentication.name) {
                LoginAuthenticationForm(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                )
            }
        }

        uiState.alertMessage?.let { alertMessage ->
            val snackbarText = when (alertMessage) {
                is AlertMessage.String -> alertMessage.value
                is AlertMessage.StringResource -> stringResource(alertMessage.value)
            }

            scope.launch {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }

        if (uiState.loginRoute != currentRoute) {
            navController.navigate(uiState.loginRoute.name)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenAppBar(
    currentRoute: LoginRoute,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(currentRoute.title)) },
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
