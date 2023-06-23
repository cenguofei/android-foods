package com.example.designsystem.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionsRow(
    onSelectAll:() -> Unit = {},
    onEdit:() -> Unit = {},
    title:String = ""
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        val contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else
            MaterialTheme.colorScheme.onSurface

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = contentColor
        )
        Row(verticalAlignment = Alignment.Bottom) {
            TextButton(onClick = onSelectAll) {
                Text(
                    text = "全选",
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(onClick = onEdit) {
                Text(
                    text = "编辑",
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}
