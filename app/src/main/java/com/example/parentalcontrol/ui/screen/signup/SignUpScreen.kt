package com.example.parentalcontrol.ui.screen.signup

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentalcontrol.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel,
    onSignUpSuccess: () -> Unit,
    onGoToLogin: () -> Unit = {}
) {
    val purple = Color(0xFF6E63FF)
    val purpleDark = Color(0xFF3F2CE6)
    val purpleLight = Color(0xFFB36CFF)
    val labelGray = Color(0xFF7A7A7A)
    val strokeGray = Color(0xFF3A3A3A)
    val errorRed = Color(0xFFE53935)

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Field-level errors
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Success dialog
    var showSuccessDialog by remember { mutableStateOf(false) }

    val uiState = signUpViewModel.uiState

    // ===== Option B helpers (bring focused field into view) =====
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val nameBir = remember { BringIntoViewRequester() }
    val emailBir = remember { BringIntoViewRequester() }
    val passBir = remember { BringIntoViewRequester() }

    fun bringFieldIntoView(requester: BringIntoViewRequester) {
        scope.launch {
            // Tiny delay helps after IME opens so layout has updated sizes.
            delay(120)
            requester.bringIntoView()
        }
    }

    // Backend/API error dialog (kept)
    if (uiState.error != null) {
        AlertDialog(
            onDismissRequest = { signUpViewModel.clearError() },
            confirmButton = {
                Text(
                    text = "OK",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { signUpViewModel.clearError() }
                )
            },
            title = { Text("Create Account Failed") },
            text = { Text(uiState.error!!) }
        )
    }

    // Success dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                Text(
                    text = "OK",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            showSuccessDialog = false
                            onSignUpSuccess()
                        }
                )
            },
            title = { Text("Account Created") },
            text = { Text("Your account has been created successfully. You can now continue.") }
        )
    }

    fun isValidEmail(input: String): Boolean {
        val trimmed = input.trim()
        return trimmed.contains("@") && trimmed.contains(".") && !trimmed.contains(" ")
    }

    fun validate(): Boolean {
        val n = name.trim()
        val e = email.trim()
        val p = password

        nameError = when {
            n.isEmpty() -> "Name is required"
            n.length < 2 -> "Name must be at least 2 characters"
            else -> null
        }

        emailError = when {
            e.isEmpty() -> "Email is required"
            !isValidEmail(e) -> "Enter a valid email"
            else -> null
        }

        passwordError = when {
            p.isEmpty() -> "Password is required"
            p.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }

        return nameError == null && emailError == null && passwordError == null
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
                .imePadding() // ✅ pushes content above keyboard
                .verticalScroll(scrollState) // ✅ lets user scroll if needed
                .padding(horizontal = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(140.dp))

            Text(
                text = "Create new\nAccount",
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = purple,
                lineHeight = 38.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onGoToLogin() }
            ) {
                Text(
                    text = "Already Registered? ",
                    fontSize = 14.sp,
                    color = purple,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    color = purple,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.height(34.dp))

            // Name
            Text(
                text = "Please enter your name",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = labelGray
            )
            Spacer(Modifier.height(10.dp))

            RoundedField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                placeholder = "Your Name",
                strokeColor = strokeGray,
                isError = nameError != null,
                modifier = Modifier
                    .bringIntoViewRequester(nameBir)
                    .onFocusChanged { if (it.isFocused) bringFieldIntoView(nameBir) }
            )
            FieldError(text = nameError, color = errorRed)

            Spacer(Modifier.height(14.dp))

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
                onValueChange = {
                    email = it
                    emailError = null
                },
                placeholder = "you@email.com",
                strokeColor = strokeGray,
                isError = emailError != null,
                modifier = Modifier
                    .bringIntoViewRequester(emailBir)
                    .onFocusChanged { if (it.isFocused) bringFieldIntoView(emailBir) }
            )
            FieldError(text = emailError, color = errorRed)

            Spacer(Modifier.height(14.dp))

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
                onValueChange = {
                    password = it
                    passwordError = null
                },
                placeholder = "********",
                strokeColor = strokeGray,
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword },
                isError = passwordError != null,
                modifier = Modifier
                    .bringIntoViewRequester(passBir)
                    .onFocusChanged { if (it.isFocused) bringFieldIntoView(passBir) }
            )
            FieldError(text = passwordError, color = errorRed)

            Spacer(Modifier.height(40.dp))

            val canClick = !uiState.isLoading

            GradientPillButton(
                text = if (uiState.isLoading) "Creating..." else "Create Account",
                leftColor = purpleDark,
                rightColor = purpleLight,
                enabled = canClick,
                onClick = {
                    if (!canClick) return@GradientPillButton
                    if (!validate()) return@GradientPillButton

                    signUpViewModel.register(
                        name = name.trim(),
                        email = email.trim(),
                        password = password,
                        onSuccess = {
                            showSuccessDialog = true
                        }
                    )
                },
                modifier = Modifier
                    .width(220.dp)
                    .height(52.dp)
            )

            // Extra bottom padding so last field/button can scroll above IME comfortably
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FieldError(text: String?, color: Color) {
    if (text == null) return
    Spacer(Modifier.height(6.dp))
    Text(
        text = text,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
    )
}

@Composable
private fun RoundedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    strokeColor: Color,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        isError = isError,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF9A9A9A),
                fontSize = 16.sp
            )
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
            focusedBorderColor = if (isError) Color(0xFFE53935) else strokeColor,
            unfocusedBorderColor = if (isError) Color(0xFFE53935) else strokeColor,
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
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        isError = isError,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF9A9A9A),
                fontSize = 16.sp
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF404040),
            fontWeight = FontWeight.Medium
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onTogglePassword, modifier = Modifier.padding(end = 6.dp)) {
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
            focusedBorderColor = if (isError) Color(0xFFE53935) else strokeColor,
            unfocusedBorderColor = if (isError) Color(0xFFE53935) else strokeColor,
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
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(999.dp)
    val disabledOverlay = Color(0x66FFFFFF)

    Box(
        modifier = modifier
            .shadow(10.dp, shape)
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(leftColor, rightColor)))
            .background(if (enabled) Color.Transparent else disabledOverlay)
            .clickable(enabled = enabled) { onClick() },
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
