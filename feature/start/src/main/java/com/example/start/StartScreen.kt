package com.example.start

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.FoodsBackground
import com.example.designsystem.theme.FoodsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StartScreen(
    lottieAnimations:List<String> = defaultLottieAnimations,
    onSignUpClick:() -> Unit,
    onBeginClick: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val pagerState = rememberPagerState(pageCount = { lottieAnimations.size })
            Spacer(modifier = Modifier.height(25.dp))
            FoodieText()
            Lotties(pagerState = pagerState, modifier = Modifier.padding(top = 20.dp))
            Spacer(modifier = Modifier.height(30.dp))
            MeetMeal()
            Spacer(modifier = Modifier.height(16.dp))
            IndicatorRow(pagerState,lottieAnimations,onBeginClick)
            SignUp(onSignUpClick = onSignUpClick)
        }
    }
}




@Preview
@Composable
fun StartScreenPreview(){
    FoodsBackground {
        FoodsTheme {
            StartScreen(
                onBeginClick = {},
                onSignUpClick = {}
            )
        }
    }
}