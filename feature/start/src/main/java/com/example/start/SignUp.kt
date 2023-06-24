package com.example.start

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SignUp(
    modifier: Modifier = Modifier,
    onSignUpClick:() -> Unit
) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.no_account),
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontWeight = FontWeight.Light
        )
        Text(
            text = stringResource(id = R.string.sign_up),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Light,
            modifier = Modifier.clickable(onClick = onSignUpClick)
        )
    }
}