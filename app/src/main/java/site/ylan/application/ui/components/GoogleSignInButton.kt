package site.ylan.application.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import site.ylan.application.ui.theme.InputFieldBackground
import site.ylan.application.ui.theme.TextDark

@Composable
fun GoogleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val strokeWidth = width * 0.22f
        val center = Offset(width / 2f, height / 2f)
        val radius = (width - strokeWidth) / 2f

        // Blue arc & right bar
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = 0f,
            sweepAngle = -45f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        drawRect(
            color = Color(0xFF4285F4),
            topLeft = Offset(center.x - strokeWidth * 0.1f, center.y - strokeWidth / 2f),
            size = Size(radius + strokeWidth * 0.1f, strokeWidth)
        )

        // Red top arc
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = -185f,
            sweepAngle = 140f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // Yellow left arc
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = -225f,
            sweepAngle = 70f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // Green bottom arc
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 45f,
            sweepAngle = 110f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Continue with Google",
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(50.dp)),
        enabled = enabled,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = InputFieldBackground,
            contentColor = TextDark,
            disabledContainerColor = InputFieldBackground.copy(alpha = 0.6f),
            disabledContentColor = TextDark.copy(alpha = 0.5f)
        )
    ) {
        GoogleIcon(modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
