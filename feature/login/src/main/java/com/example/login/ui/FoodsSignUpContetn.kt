package com.example.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.component.FoodsOutlinedTextField
import com.example.designsystem.theme.LocalTintTheme
import com.example.model.remoteModel.User
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
    val (hasError, setHasError) = remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(TextFieldValue("")) }

    var password by remember { mutableStateOf(TextFieldValue("")) }

    var email by remember { mutableStateOf(TextFieldValue("")) }

    var passwordVisualTransformation by remember {
        mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
    }

    FoodsOutlinedTextField(
        username,
        onValueChange = { username = it },
        hasError = hasError,
        leadingIcon = Icons.Default.AccountBox,
        labelText = "Username",
        placeholderText = "xxx"
    )

    FoodsOutlinedTextField(
        value = email,
        onValueChange = {
            email = it
            setHasError(false)
        },
        hasError = hasError,
        leadingIcon = Icons.Default.Email,
        labelText = "email",
        placeholderText = "ylc@swu.edu"
    )

    FoodsOutlinedTextField(
        value = password,
        leadingIcon = Icons.Default.Lock,
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
        hasError = hasError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        labelText = "Password",
        placeholderText = "xxxxxx",
        onValueChange = {
            password = it
            setHasError(false)
        },
        visualTransformation = passwordVisualTransformation,
    )

    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    FoodsOutlinedTextField(
        value = confirmPassword,
        leadingIcon = Icons.Default.Lock,
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
        hasError = hasError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        labelText = "Password",
        onValueChange = {
            confirmPassword = it
            setHasError(false)
        },
        visualTransformation = passwordVisualTransformation,
        placeholderText = "xxxxxx"
    )

    TextButton(
        onClick = {
            viewModel.register(
                username.text,
                email.text,
                password.text,
                confirmPassword.text,
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
                    setHasError(true)
                }
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "Never Hungry Again!",
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelMedium
        )
    }
}