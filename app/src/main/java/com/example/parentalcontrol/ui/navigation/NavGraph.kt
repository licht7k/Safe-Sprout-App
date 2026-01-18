package com.example.parentalcontrol.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parentalcontrol.data.local.TokenStore
import com.example.parentalcontrol.data.remote.network.ApiClient
import com.example.parentalcontrol.data.repository.AuthRepository
import com.example.parentalcontrol.ui.screen.getstarted.GetStartedScreen
import com.example.parentalcontrol.ui.screen.home.HomeScreen
import com.example.parentalcontrol.ui.screen.login.LoginScreen
import com.example.parentalcontrol.ui.screen.login.LoginViewModel
import com.example.parentalcontrol.ui.screen.login.LoginViewModelFactory
import com.example.parentalcontrol.ui.screen.signup.SignUpScreen
import com.example.parentalcontrol.ui.screen.signup.SignUpViewModel
import com.example.parentalcontrol.ui.screen.signup.SignUpViewModelFactory

@Composable
fun AppNavGraph(startDestination: String = Routes.GET_STARTED) {
    val navController = rememberNavController()
    val bg = Color(0xFFF6F6F6)

    // ✅ Create once, reuse for Login + SignUp
    val context = LocalContext.current

    val tokenStore = remember { TokenStore(context) }

    val repo = remember(tokenStore) {
        AuthRepository(
            api = ApiClient.authApi,
            tokenStore = tokenStore
        )
    }

    Scaffold(
        containerColor = bg,
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Routes.GET_STARTED) {
                GetStartedScreen(
                    onGetStarted = { navController.navigate(Routes.LOGIN) }
                )
            }

            composable(Routes.LOGIN) {
                val viewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(repo)
                )

                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onCreateAccount = { navController.navigate(Routes.SIGN_UP) },
                    onForgotPassword = { }
                )
            }

            composable(Routes.SIGN_UP) {
                val vm: SignUpViewModel = viewModel(
                    factory = SignUpViewModelFactory(repo) // ✅ use repo here
                )

                SignUpScreen(
                    signUpViewModel = vm,
                    onSignUpSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.SIGN_UP) { inclusive = true }
                        }
                    },
                    onGoToLogin = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
