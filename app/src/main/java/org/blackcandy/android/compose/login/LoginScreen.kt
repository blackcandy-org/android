package org.blackcandy.android.compose.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.blackcandy.android.R

enum class LoginForm {
    Connection,
    Authentication,
}

@Composable
fun LoginScreen(navController: NavHostController = rememberNavController()) {
    Scaffold() { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginForm.Connection.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = LoginForm.Connection.name) {
                LoginConnectionForm(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    onConnectButtonClicked = { navController.navigate(LoginForm.Authentication.name) },
                )
            }

            composable(route = LoginForm.Authentication.name) {
                LoginAuthenticationForm(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                )
            }
        }
    }
}
