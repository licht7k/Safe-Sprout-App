package com.example.parentalcontrol.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentalcontrol.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCreateAccount: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    viewModel: LoginViewModel,
) {
    val purple = Color(0xFF6E63FF)
    val purpleDark = Color(0xFF3F2CE6)
    val purpleLight = Color(0xFFB36CFF)
    val labelGray = Color(0xFF7A7A7A)
    val strokeGray = Color(0xFF3A3A3A)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val uiState = viewModel.uiState

    // ===== Keyboard-safe helpers =====
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val emailBir = remember { BringIntoViewRequester() }
    val passBir = remember { BringIntoViewRequester() }

    fun bringIntoView(requester: BringIntoViewRequester) {
        scope.launch {
            delay(120)
            requester.bringIntoView()
        }
    }

    // Error dialog
    if (uiState.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            confirmButton = {
                Text(
                    text = "OK",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { viewModel.clearError() }
                )
            },
            title = { Text("Login Failed") },
            text = { Text(uiState.error!!) }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ===== Background blobs =====
        Image(
            painter = painterResource(R.drawable.blob_top_left),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(290.dp)
                .offset(x = (-80).dp, y = (-65).dp)
        )

        Image(
            painter = painterResource(R.drawable.blob_bottom_right),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(340.dp)
                .offset(x = -(20.dp), y = 110.dp)
        )

        // ===== Main content =====
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()              // ✅ push content above keyboard
                .verticalScroll(scrollState)
                .padding(horizontal = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(150.dp))

            Text(
                text = "Login",
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = purple
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Sign in to Continue",
                fontSize = 14.sp,
                color = purple,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(50.dp))

            // Email
            Text(
                text = "Please enter Email",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = labelGray
            )
            Spacer(Modifier.height(10.dp))

            RoundedField(
                value = email,
                onValueChange = { email = it },
                placeholder = "you@email.com",
                strokeColor = strokeGray,
                modifier = Modifier
                    .bringIntoViewRequester(emailBir)
                    .onFocusChanged {
                        if (it.isFocused) bringIntoView(emailBir)
                    }
            )

            Spacer(Modifier.height(18.dp))

            // Password
            Text(
                text = "Please enter Password",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = labelGray
            )
            Spacer(Modifier.height(10.dp))

            RoundedPasswordField(
                value = password,
                onValueChange = { password = it },
                placeholder = "********",
                strokeColor = strokeGray,
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword },
                modifier = Modifier
                    .bringIntoViewRequester(passBir)
                    .onFocusChanged {
                        if (it.isFocused) bringIntoView(passBir)
                    }
            )

            Spacer(Modifier.height(12.dp))

            // Forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Forgot Password?",
                    fontSize = 14.sp,
                    color = purple,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable { onForgotPassword() }
                )
            }

            Spacer(Modifier.height(42.dp))

            GradientPillButton(
                text = "LOGIN",
                leftColor = purpleDark,
                rightColor = purpleLight,
                onClick = {
                    viewModel.login(
                        email = email,
                        password = password,
                        onSuccess = onLoginSuccess
                    )
                },
                modifier = Modifier
                    .width(220.dp)
                    .height(52.dp)
            )

            Spacer(Modifier.height(18.dp))

            // Create account
            Row(
                modifier = Modifier.clickable { onCreateAccount() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don’t have an account? ",
                    color = Color(0xFF444444),
                    fontSize = 14.sp
                )
                Text(
                    text = "Create one",
                    color = purple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ---------- Components ---------- */

@Composable
private fun RoundedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    strokeColor: Color,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(placeholder, color = Color(0xFF9A9A9A), fontSize = 16.sp)
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF404040),
            fontWeight = FontWeight.Medium
        ),
        shape = RoundedCornerShape(999.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = strokeColor,
            unfocusedBorderColor = strokeColor,
            cursorColor = strokeColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
private fun RoundedPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    strokeColor: Color,
    showPassword: Boolean,
    onTogglePassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(placeholder, color = Color(0xFF9A9A9A), fontSize = 16.sp)
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF404040),
            fontWeight = FontWeight.Medium
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onTogglePassword) {
                Text(
                    text = if (showPassword) "Hide" else "Show",
                    color = Color(0xFF6E63FF),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        shape = RoundedCornerShape(999.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = strokeColor,
            unfocusedBorderColor = strokeColor,
            cursorColor = strokeColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
private fun GradientPillButton(
    text: String,
    leftColor: Color,
    rightColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(999.dp)

    Box(
        modifier = modifier
            .shadow(10.dp, shape)
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(leftColor, rightColor)))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp
        )
    }
}
