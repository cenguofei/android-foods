package com.example.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.theme.LocalTintTheme
import com.example.network.remote.remoteModel.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignUpContent(
    viewModel: LoginViewModel,
    sheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    onSuccess: (user: User) -> Unit,
) {
    val hasError = remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }

    var passwordVisualTransformation by remember {
        mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
    }
    val passwordInteractionState = remember { MutableInteractionSource() }
    val emailInteractionState = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountBox,
                tint = LocalTintTheme.current.iconTint,
                contentDescription = null
            )
        },
        maxLines = 1,
        isError = hasError.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        label = { Text(text = "Username") },
        placeholder = { Text(text = "xxx") },
        interactionSource = emailInteractionState,
    )

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                tint = LocalTintTheme.current.iconTint,
                contentDescription = null
            )
        },
        maxLines = 1,
        isError = hasError.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        label = { Text(text = "Email") },
        placeholder = { Text(text = "lyc@swu.edu") },
        interactionSource = emailInteractionState,
    )

    OutlinedTextField(
        value = password,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                tint = LocalTintTheme.current.iconTint,
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (passwordVisualTransformation != VisualTransformation.None) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                tint = LocalTintTheme.current.iconTint,
                modifier = Modifier.clickable(onClick = {
                    passwordVisualTransformation =
                        if (passwordVisualTransformation != VisualTransformation.None) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                }),
                contentDescription = null
            )
        },
        maxLines = 1,
        isError = hasError.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        label = { Text(text = "Password") },
        placeholder = { Text(text = "123456") },
        onValueChange = {
            password = it
        },
        interactionSource = passwordInteractionState,
        visualTransformation = passwordVisualTransformation,
    )

    var confirmPassword by remember { mutableStateOf("") }
    OutlinedTextField(
        value = confirmPassword,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                tint = LocalTintTheme.current.iconTint,
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (passwordVisualTransformation != VisualTransformation.None) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                tint = LocalTintTheme.current.iconTint,
                modifier = Modifier.clickable(onClick = {
                    passwordVisualTransformation =
                        if (passwordVisualTransformation != VisualTransformation.None) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                }),
                contentDescription = null
            )
        },
        maxLines = 1,
        isError = hasError.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        label = { Text(text = "Password") },
        onValueChange = {
            confirmPassword = it
        },
        interactionSource = passwordInteractionState,
        visualTransformation = passwordVisualTransformation,
    )

    TextButton(
        onClick = {
            viewModel.register(
                username,
                email,
                password,
                confirmPassword,
                onError = { msg ->
                    coroutineScope.launch {
                        sheetScaffoldState.snackbarHostState.showSnackbar(
                            msg,
                            "error",
                            SnackbarDuration.Long
                        )
                    }
                },
                onSuccess = onSuccess,
                onInputError = {
                    hasError.value = true
                }
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )

    ) {
        Text(
            text = "Never Hungry Again!",
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelMedium
        )
    }
}