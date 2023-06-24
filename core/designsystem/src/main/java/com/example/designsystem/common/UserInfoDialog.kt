package com.example.designsystem.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.FoodsOutlinedTextField

@Composable
fun UserInfoDialog(
    onDismiss: () -> Unit,
    onCommitOrder: () -> Unit,
    hasError: Boolean,
    address: TextFieldValue,
    onAddressChanged: (TextFieldValue) -> Unit,
    phone: TextFieldValue,
    onPhoneChanged: (TextFieldValue) -> Unit,
) {
    AlertDialog(
        title = { Text(text = "收货信息", style = MaterialTheme.typography.titleSmall) },
        text = {
            Column {
                FoodsOutlinedTextField(
                    value = address,
                    onValueChange = onAddressChanged,
                    hasError = hasError,
                    leadingIcon = Icons.Default.PinDrop,
                    labelText = "address",
                    placeholderText = "西南大学"
                )
                FoodsOutlinedTextField(
                    value = phone,
                    onValueChange = onPhoneChanged,
                    hasError = hasError,
                    leadingIcon = Icons.Default.Call,
                    labelText = "phone number",
                    placeholderText = "199****3240"
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onCommitOrder,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "完成")
            }
        },
        onDismissRequest = onDismiss,
    )
}