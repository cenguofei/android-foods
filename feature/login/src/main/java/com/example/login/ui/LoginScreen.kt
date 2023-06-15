package com.example.login.ui

import android.util.Log
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.theme.LocalTintTheme
import com.example.network.remote.remoteModel.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val sheetPeekHeight = 60.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    onSuccess: (user: User) -> Unit,
    onError:(msg:String) -> Unit
) {
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val onOpenDrawer = {
        coroutineScope.launch {
            sheetScaffoldState.bottomSheetState.expand()
        }
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
        sheetShape = CutCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetPeekHeight = sheetPeekHeight
    ) {
        //TextFields
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var hasError by remember { mutableStateOf(false) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
        }
        val passwordInteractionState = remember { MutableInteractionSource() }
        val emailInteractionState = remember { MutableInteractionSource() }

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
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
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
                OutlinedTextField(
                    value = email,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            tint = LocalTintTheme.current.iconTint,
                            contentDescription = null
                        )
                    },
                    maxLines = 1,
                    isError = hasError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text(text = "Email/Username") },
                    placeholder = { Text(text = "lyc@swu.edu") },
                    onValueChange = {
                        hasError = false
                        email = it
                    },
                    interactionSource = emailInteractionState,
                )
            }
            item {
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
                    isError = hasError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "123456") },
                    onValueChange = {
                        hasError = false
                        password = it
                    },
                    interactionSource = passwordInteractionState,
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
                Box(modifier = Modifier.padding(vertical = 16.dp)) {
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                    Text(
                        text = buildAnnotatedString {
                            pushStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface))
                            append("no account?")
                            pop()
                            pushStyle(style = SpanStyle(color = Color.Blue))
                            append("register")
                        },
                        color = Color.LightGray,
                        modifier = Modifier
                            .clickable(onClick = { onOpenDrawer() })
                            .align(Alignment.Center)
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
