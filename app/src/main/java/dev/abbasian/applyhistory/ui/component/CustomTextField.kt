package dev.abbasian.applyhistory.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import dev.abbasian.applyhistory.core.extension.UiText
import dev.abbasian.applyhistory.core.extension.isNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    placeholder: String,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    modifier: Modifier = Modifier,
    errorMessage: UiText? = null,
    isError: Boolean = false,
    isVisible: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    maxLine: Int = 1,
) {
    val context = LocalContext.current
    val colorBorder = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

    Column {
        OutlinedTextField(
            value = if (keyboardType == KeyboardType.Phone || keyboardType == KeyboardType.Number) {
                if (isNumber(text)) text else ""
            } else text,
            onValueChange = {
                if (keyboardType == KeyboardType.Phone || keyboardType == KeyboardType.Number) {
                    if (isNumber(it)) onValueChange(it)
                } else onValueChange(it)
            },
            label = { Text(text = placeholder) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            maxLines = maxLine,
            isError = isError,
            visualTransformation = if (keyboardType == KeyboardType.Password) {
                if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = colorBorder,
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            modifier = modifier
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage.asString(context),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = modifier
            )
        }
    }
}