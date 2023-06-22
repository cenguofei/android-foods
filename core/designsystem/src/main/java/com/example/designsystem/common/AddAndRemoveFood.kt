package com.example.designsystem.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.LocalTintTheme
import com.example.model.remoteModel.Food

@Composable
fun AddAndRemoveFood(
    modifier: Modifier = Modifier,
    num:Int,
    onRemove:() -> Unit,
    onAdd:() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(end = 4.dp, bottom = 4.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = null,
            tint = LocalTintTheme.current.iconTint,
            modifier = Modifier
                .size(36.dp)
                .padding(8.dp)
                .clickable(onClick = onRemove)
        )
        Text(text = num.toString())
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = LocalTintTheme.current.iconTint,
            modifier = Modifier
                .size(36.dp)
                .padding(8.dp)
                .clickable(onClick = onAdd)
        )
    }
}