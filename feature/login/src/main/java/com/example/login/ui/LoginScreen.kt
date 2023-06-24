package com.example.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.component.FoodsOutlinedTextField
import com.example.designsystem.theme.LocalBackgroundTheme
import com.example.designsystem.theme.LocalTintTheme
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val sheetPeekHeight = 60.dp

@Composable
fun LoginScreenRoute(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    onSuccess: (user: User) -> Unit,
    onError: (msg: String) -> Unit,
    isRegister: Boolean
) {
    LoginScreen(
        coroutineScope = coroutineScope,
        loginViewModel = loginViewModel,
        onSuccess = onSuccess,
        onError = onError,
        isRegister = isRegister
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun LoginScreen(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    onSuccess: (user: User) -> Unit,
    onError: (msg: String) -> Unit,
    isRegister: Boolean
) {
    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    val onOpenDrawer = {
        coroutineScope.launch {
            sheetScaffoldState.bottomSheetState.expand()
        }
    }

    if(isRegister) {
        onOpenDrawer()
    }
    BottomSheetScaffold(
        sheetContent = {
            FoodsBottomSheet(
                coroutineScope,
                sheetScaffoldState,
                loginViewModel,
                onSignUpSuccess = onSuccess,
            )
        },
        modifier = Modifier,
        scaffoldState = sheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetPeekHeight = sheetPeekHeight,
        sheetElevation = 16.dp,
        backgroundColor = LocalBackgroundTheme.current.color,
        contentColor = LocalContentColor.current
    ) {
        //TextFields
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var hasError by remember { mutableStateOf(false) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(60.dp)) }
            item {
                Text(
                    text = buildAnnotatedString {
                        pushStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        append("WaraCh")
                        append("\uD83D\uDE0B")
                        append("w")
                    },
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 30.dp)
                )
            }
            item {
                Text(
                    text = "delivering dangerously delicious, Let's start by Sign In!",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                )
            }
            item {
                FoodsOutlinedTextField(
                    value = email,
                    leadingIcon = Icons.Default.AccountBox,
                    hasError = hasError,
                    labelText = "Email/Username",
                    placeholderText = "lyc@swu.edu",
                    onValueChange = {
                        hasError = false
                        email = it
                    },
                )
            }
            item {
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
                        imeAction = ImeAction.Done
                    ),
                    labelText = "Password",
                    placeholderText = "123456",
                    onValueChange = {
                        hasError = false
                        password = it
                    },
                    visualTransformation = passwordVisualTransformation,
                )
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            item {
//                var loading by remember { mutableStateOf(false) }
//                var logIn by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        if (email.text.isEmpty() or password.text.isEmpty()) {
                            hasError = true
                        } else {
//                            loading = true
                            loginViewModel.login(
                                email.text,
                                password.text,
                                onSuccess = {
//                                    loading = true
                                    hasError = false
//                                    logIn = true
//                                    coroutineScope.launch {
//                                        delay(5)
//                                        Log.v("navigation_test","LoginScreen onSuccess.invoke(it)")
//                                    }
                                    onSuccess.invoke(it)
                                },
                                onError = { msg ->
                                    onError(msg)
                                    hasError = true
//                                    loading = false
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
//                    if (loading) {
//                        if (!logIn) {
//                            HorizontalDottedProgressBar()
//                        }
//                    } else {
                    Text(text = "Log In", style = MaterialTheme.typography.labelMedium)
//                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .background(Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.Center).padding(bottom = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .weight(1f)
                        )

                        Text(
                            text = buildAnnotatedString {
                                pushStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface))
                                append("no account?")
                                pop()
                                pushStyle(style = SpanStyle(color = Color.Blue))
                                append("register")
                            },
                            modifier = Modifier
                                .clickable(onClick = { onOpenDrawer() })
                                .padding(horizontal = 4.dp)
                        )

                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}
