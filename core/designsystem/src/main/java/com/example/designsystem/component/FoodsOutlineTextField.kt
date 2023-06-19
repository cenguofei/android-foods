package com.example.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.LocalTintTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodsOutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    leadingIcon: ImageVector? = null,
    trailingImageVector: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    hasError: Boolean = false,
    labelText: String? = null,
    placeholderText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    backgroundEnabled: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = TextFieldDefaults.outlinedShape,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        shape = shape,
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            if (trailingImageVector != null) {
                Icon(
                    imageVector = trailingImageVector,
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            } else {
                trailingIcon?.invoke()
            }
        },
        maxLines = 1,
        isError = hasError,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        keyboardActions = keyboardActions,
        label = { labelText?.let { Text(text = it) } },
        placeholder = { placeholderText?.let { Text(text = it) } },
        interactionSource = interactionSource,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = MaterialTheme.colorScheme.error,
            containerColor = if (!backgroundEnabled) {
                MaterialTheme.colorScheme.background
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            },
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}