package site.ylan.application.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import site.ylan.application.ui.theme.ErrorRed
import site.ylan.application.ui.theme.InputFieldBackground
import site.ylan.application.ui.theme.MutedTextDark
import site.ylan.application.ui.theme.PrimaryBlack
import site.ylan.application.ui.theme.TextDark

@Composable
fun PillTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(50.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    color = MutedTextDark,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            shape = RoundedCornerShape(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputFieldBackground,
                unfocusedContainerColor = InputFieldBackground,
                disabledContainerColor = InputFieldBackground.copy(alpha = 0.8f),
                errorContainerColor = InputFieldBackground,
                focusedBorderColor = PrimaryBlack,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = ErrorRed,
                focusedTextColor = TextDark,
                unfocusedTextColor = TextDark,
                focusedPlaceholderColor = MutedTextDark,
                unfocusedPlaceholderColor = MutedTextDark,
                focusedLeadingIconColor = TextDark,
                unfocusedLeadingIconColor = TextDark,
                focusedTrailingIconColor = TextDark,
                unfocusedTrailingIconColor = TextDark
            )
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
