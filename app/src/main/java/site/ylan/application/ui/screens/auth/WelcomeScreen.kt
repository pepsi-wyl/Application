package site.ylan.application.ui.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.ylan.application.R
import site.ylan.application.ui.components.PillButton
import site.ylan.application.ui.theme.ApplicationTheme
import site.ylan.application.ui.theme.PrimaryBlack
import site.ylan.application.ui.theme.SoftVioletBackground
import site.ylan.application.ui.theme.TextWhite

@Composable
fun WelcomeScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToTaskManager: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftVioletBackground,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // Background Soft White Cloud Decorations
            SoftCloudDecoration(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = 20.dp)
                    .size(width = 180.dp, height = 90.dp),
                color = Color(0xFFFFFFFF).copy(alpha = 0.35f),
            )

            SoftCloudDecoration(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-30).dp, y = 100.dp)
                    .size(width = 150.dp, height = 75.dp),
                color = Color(0xFFFFFFFF).copy(alpha = 0.25f),
            )

            SoftCloudDecoration(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 50.dp, y = (-70).dp)
                    .size(width = 200.dp, height = 100.dp),
                color = Color(0xFFFFFFFF).copy(alpha = 0.3f),
            )

            // Character Illustration (Anchored neatly at bottom-left)
            Image(
                painter = painterResource(id = R.drawable.illustration_welcome),
                contentDescription = "Welcome character illustration",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(0.55f)
                    .height(200.dp),
                contentScale = ContentScale.Fit,
            )

            // Foreground Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Header Title & Subtitle Section
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                ) {
                    Text(
                        text = "Welcome To",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            lineHeight = 42.sp,
                        ),
                        color = TextWhite,
                    )
                    Text(
                        text = "Shh!",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 44.sp,
                            lineHeight = 50.sp,
                        ),
                        color = TextWhite,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "A Hub Where Whispers Echo Loudest",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = PrimaryBlack,
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Action Section (Positioned cleanly in lower portion above bottom illustration)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 190.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    PillButton(
                        text = "Sign up",
                        onClick = onNavigateToSignUp,
                        containerColor = PrimaryBlack,
                        contentColor = TextWhite,
                    )

                    // "Already have an account? Login" link
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onNavigateToLogin)
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Already have an account? ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                            ),
                            color = TextWhite.copy(alpha = 0.9f),
                        )
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = PrimaryBlack,
                        )
                    }

                    // Secondary navigation link
                    TextButton(
                        onClick = onNavigateToTaskManager,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Explore Task Manager",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = TextWhite.copy(alpha = 0.95f),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "Explore Task Manager",
                                tint = TextWhite.copy(alpha = 0.95f),
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SoftCloudDecoration(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFFFFFF).copy(alpha = 0.35f),
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Base rounded pill
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.08f, h * 0.45f),
            size = Size(w * 0.84f, h * 0.5f),
            cornerRadius = CornerRadius(h * 0.25f, h * 0.25f),
        )
        // Left circle puff
        drawCircle(
            color = color,
            radius = h * 0.35f,
            center = Offset(w * 0.28f, h * 0.52f),
        )
        // Main center circle puff
        drawCircle(
            color = color,
            radius = h * 0.45f,
            center = Offset(w * 0.52f, h * 0.42f),
        )
        // Right circle puff
        drawCircle(
            color = color,
            radius = h * 0.32f,
            center = Offset(w * 0.74f, h * 0.58f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    ApplicationTheme {
        WelcomeScreen(
            onNavigateToSignUp = {},
            onNavigateToLogin = {},
        )
    }
}
