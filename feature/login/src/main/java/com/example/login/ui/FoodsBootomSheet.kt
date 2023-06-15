package com.example.login.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.component.FoodsBackground
import com.example.designsystem.component.FoodsGradientBackground
import com.example.designsystem.theme.GradientColors
import com.example.designsystem.theme.LocalGradientColors
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodsBottomSheet(
    coroutineScope: CoroutineScope,
    sheetScaffoldState: BottomSheetScaffoldState,
    loginViewModel: LoginViewModel,
    onSignUpSuccess: (user: User) -> Unit,
) {
    FoodsBackground {
        FoodsGradientBackground(
            modifier = Modifier.alpha(0.6f), gradientColors = GradientColors(
                top = MaterialTheme.colorScheme.background,
                bottom = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            )
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(sheetPeekHeight + 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val color = MaterialTheme.colorScheme.primary
                        Canvas(
                            modifier = Modifier
                                .size(width = 35.dp, height = 4.dp)
                                .background(MaterialTheme.colorScheme.primary),
                            onDraw = {
                                drawLine(
                                    color = color,
                                    start = Offset.Zero,
                                    end = Offset.Infinite,
                                    cap = StrokeCap.Round
                                )
                            })
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Sign Up",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                SignUpContent(loginViewModel, sheetScaffoldState, coroutineScope, onSignUpSuccess)
            }
        }
    }
}