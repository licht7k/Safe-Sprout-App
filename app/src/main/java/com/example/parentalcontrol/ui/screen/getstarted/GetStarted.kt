package com.example.parentalcontrol.ui.screen.getstarted

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentalcontrol.R
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer




@Composable
fun GetStartedScreen(onGetStarted: () -> Unit) {
    val purpleLeft = Color(0xFF3F2CE6)
    val purpleRight = Color(0xFFB36CFF)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val w = maxWidth
        val h = maxHeight

        // --- LOGO (top-left, small) ---
        Image(
            painter = painterResource(R.drawable.safesprout_logo),
            contentDescription = "SafeSprout",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-50).dp, y = (-20).dp)   // ⬅ move left
                .height(180.dp)
        )

        // --- BEAR
        Image(
            painter = painterResource(R.drawable.bear),
            contentDescription = "Bear",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(w * 1.75f)                 // BIGGER than before
                .offset(
                    x = -(w * 0.30f),            // more cropped on the left
                    y = h * - 0.01f                // slightly higher
                )
                .graphicsLayer {
                    scaleX = 1.5f                // THIS is what really enlarges it
                    scaleY = 1.5f
                    rotationZ = -12f             // rotate here (more reliable)
                }
                .rotate(57f)                    // subtle tilt like reference
        )

        // --- HEART (top-right) ---
        Image(
            painter = painterResource(R.drawable.heart),
            contentDescription = "Heart",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(w * 0.80f)
                .offset(x = (w * 0.20f), y = h * 0.01f)
        )

        // --- TEXT + BUTTON (bottom-left) ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 40.dp, end = 40.dp, bottom = 100.dp)
        ) {
            Text(
                text = "Sign Up\nand  Protect  your\nChild Today!",
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 42.sp,
                color = Color.Black
            )

            Spacer(Modifier.height(18.dp))

            // Button matches reference proportions + arrow pill on right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(10.dp, RoundedCornerShape(999.dp))
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        Brush.horizontalGradient(listOf(purpleLeft, purpleRight))
                    )
                    .clickable { onGetStarted() }
                    .padding(start = 85.dp, end = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GET STARTED",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    letterSpacing = 3.sp
                )

                Spacer(Modifier.weight(1f))


            }
        }
    }
}
