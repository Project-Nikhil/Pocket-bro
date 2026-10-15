package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentGold
import com.example.ui.theme.TextCream

/**
 * Custom artistic hero illustration matching the reference mockup:
 * Caring brother looking over the warm evening city sunset with a cute white kitten on his shoulder.
 */
@Composable
fun BrotherHeroIllustration(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "star_twinkle")
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Warm twilight dusk sky gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF160E0B),
                    Color(0xFF2E1912),
                    Color(0xFF5A2A1A),
                    Color(0xFF8A4325),
                    Color(0xFFD6783B),
                    Color(0xFFF3AB6B)
                )
            ),
            size = Size(w, h)
        )

        // 2. City skyline silhouettes & sunset glow in distance
        val sunCenter = Offset(w * 0.72f, h * 0.58f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFF0D0).copy(alpha = 0.85f), Color(0x00FF8833)),
                center = sunCenter,
                radius = w * 0.35f
            ),
            radius = w * 0.35f,
            center = sunCenter
        )

        // Distant city buildings
        drawRoundRect(
            color = Color(0xFF381C14).copy(alpha = 0.65f),
            topLeft = Offset(w * 0.05f, h * 0.48f),
            size = Size(w * 0.16f, h * 0.45f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = Color(0xFF2E1710).copy(alpha = 0.75f),
            topLeft = Offset(w * 0.68f, h * 0.44f),
            size = Size(w * 0.22f, h * 0.50f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = Color(0xFF24120C).copy(alpha = 0.85f),
            topLeft = Offset(w * 0.24f, h * 0.50f),
            size = Size(w * 0.18f, h * 0.42f),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // City lights dots
        drawCircle(Color(0xFFFFEEAA).copy(alpha = twinkle * 0.8f), 3f, Offset(w * 0.12f, h * 0.54f))
        drawCircle(Color(0xFFFFDD88).copy(alpha = twinkle * 0.7f), 3.5f, Offset(w * 0.75f, h * 0.51f))
        drawCircle(Color(0xFFFFCC77).copy(alpha = twinkle * 0.9f), 2.5f, Offset(w * 0.82f, h * 0.58f))

        // 3. Foreground: Brother back-view silhouette with dark hair & stylish jacket
        val brotherPath = Path().apply {
            moveTo(w * 0.08f, h)
            cubicTo(w * 0.10f, h * 0.75f, w * 0.18f, h * 0.62f, w * 0.32f, h * 0.56f) // left shoulder
            cubicTo(w * 0.38f, h * 0.52f, w * 0.40f, h * 0.42f, w * 0.42f, h * 0.36f) // neck
            // Head & messy anime hair
            cubicTo(w * 0.42f, h * 0.24f, w * 0.54f, h * 0.22f, w * 0.62f, h * 0.26f)
            cubicTo(w * 0.70f, h * 0.30f, w * 0.68f, h * 0.42f, w * 0.64f, h * 0.48f)
            cubicTo(w * 0.72f, h * 0.54f, w * 0.88f, h * 0.64f, w * 0.94f, h) // right shoulder
            close()
        }
        drawPath(
            path = brotherPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF281C16), Color(0xFF140D0A)),
                startY = h * 0.25f,
                endY = h
            )
        )

        // Warm rim light on brother's edge
        drawPath(
            path = brotherPath,
            color = Color(0xFFF7B070).copy(alpha = 0.45f),
            style = Stroke(width = 3.5f)
        )

        // 4. Cute white kitten sitting on brother's right shoulder looking back
        val catCenterX = w * 0.71f
        val catCenterY = h * 0.44f
        val catRadius = w * 0.09f

        // Kitten head
        drawCircle(
            color = Color(0xFFFFF9F5),
            radius = catRadius,
            center = Offset(catCenterX, catCenterY)
        )

        // Kitten ears
        val leftEar = Path().apply {
            moveTo(catCenterX - catRadius * 0.8f, catCenterY - catRadius * 0.4f)
            lineTo(catCenterX - catRadius * 0.6f, catCenterY - catRadius * 1.5f)
            lineTo(catCenterX - catRadius * 0.1f, catCenterY - catRadius * 0.9f)
            close()
        }
        drawPath(leftEar, Color(0xFFFFF9F5))
        drawPath(leftEar, Color(0xFFFFB6B6).copy(alpha = 0.7f), style = Fill)

        val rightEar = Path().apply {
            moveTo(catCenterX + catRadius * 0.1f, catCenterY - catRadius * 0.9f)
            lineTo(catCenterX + catRadius * 0.6f, catCenterY - catRadius * 1.5f)
            lineTo(catCenterX + catRadius * 0.8f, catCenterY - catRadius * 0.4f)
            close()
        }
        drawPath(rightEar, Color(0xFFFFF9F5))
        drawPath(rightEar, Color(0xFFFFB6B6).copy(alpha = 0.7f), style = Fill)

        // Kitten eyes & little nose
        drawCircle(Color(0xFF2B211E), 3.5f, Offset(catCenterX - catRadius * 0.35f, catCenterY - catRadius * 0.05f))
        drawCircle(Color(0xFF2B211E), 3.5f, Offset(catCenterX + catRadius * 0.35f, catCenterY - catRadius * 0.05f))
        drawCircle(Color(0xFFFF9E9E), 2.5f, Offset(catCenterX, catCenterY + catRadius * 0.25f))

        // Cute pink blush
        drawCircle(Color(0xFFFF9E9E).copy(alpha = 0.4f), 4.5f, Offset(catCenterX - catRadius * 0.5f, catCenterY + catRadius * 0.2f))
        drawCircle(Color(0xFFFF9E9E).copy(alpha = 0.4f), 4.5f, Offset(catCenterX + catRadius * 0.5f, catCenterY + catRadius * 0.2f))

        // Kitten paws resting on shoulder
        drawRoundRect(
            color = Color(0xFFFFF9F5),
            topLeft = Offset(catCenterX - catRadius * 0.7f, catCenterY + catRadius * 0.75f),
            size = Size(catRadius * 0.6f, catRadius * 0.4f),
            cornerRadius = CornerRadius(6f, 6f)
        )
    }
}

/**
 * Avatar for Brother with green online status indicator
 */
@Composable
fun BrotherAvatar(
    size: Dp = 46.dp,
    showOnlineDot: Boolean = true
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Avatar circle with cozy anime brother illustration
        Canvas(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFF2A201B))
                .border(1.5.dp, AccentAmber.copy(alpha = 0.4f), CircleShape)
        ) {
            val w = this.size.width
            val h = this.size.height

            // Background warm vignette
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF4A3427), Color(0xFF1E140E)),
                    center = Offset(w * 0.5f, h * 0.5f),
                    radius = w * 0.55f
                )
            )

            // Brother face & hair
            drawCircle(Color(0xFFF9D6B3), w * 0.26f, Offset(w * 0.5f, h * 0.46f))

            // Dark anime hair
            val hairPath = Path().apply {
                moveTo(w * 0.22f, h * 0.42f)
                cubicTo(w * 0.24f, h * 0.22f, w * 0.42f, h * 0.16f, w * 0.58f, h * 0.20f)
                cubicTo(w * 0.76f, h * 0.24f, w * 0.80f, h * 0.42f, w * 0.78f, h * 0.50f)
                cubicTo(w * 0.68f, h * 0.40f, w * 0.56f, h * 0.45f, w * 0.50f, h * 0.38f)
                cubicTo(w * 0.42f, h * 0.44f, w * 0.30f, h * 0.40f, w * 0.22f, h * 0.42f)
                close()
            }
            drawPath(hairPath, Color(0xFF241A14))

            // Eyes & gentle smile
            drawCircle(Color(0xFF241A14), 2f, Offset(w * 0.42f, h * 0.48f))
            drawCircle(Color(0xFF241A14), 2f, Offset(w * 0.58f, h * 0.48f))

            // Smile curve
            drawArc(
                color = Color(0xFF241A14),
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(w * 0.45f, h * 0.54f),
                size = Size(w * 0.10f, h * 0.08f),
                style = Stroke(width = 1.5f)
            )

            // Warm hoodie collar
            drawRoundRect(
                color = Color(0xFFE08D4B),
                topLeft = Offset(w * 0.25f, h * 0.72f),
                size = Size(w * 0.50f, h * 0.35f),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }

        // Online green badge
        if (showOnlineDot) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp)
                    .background(Color(0xFF25D366), CircleShape)
                    .border(2.dp, Color(0xFF14110F), CircleShape)
            )
        }
    }
}

/**
 * Avatar for Sister
 */
@Composable
fun SisterAvatar(
    size: Dp = 46.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFF2D201A))
            .border(1.5.dp, AccentGold.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Face
            drawCircle(Color(0xFFFBE0CA), w * 0.28f, Offset(w * 0.5f, h * 0.48f))

            // Soft brown hair with pigtails or side bangs
            val hair = Path().apply {
                moveTo(w * 0.18f, h * 0.55f)
                cubicTo(w * 0.18f, h * 0.25f, w * 0.45f, h * 0.18f, w * 0.58f, h * 0.20f)
                cubicTo(w * 0.82f, h * 0.24f, w * 0.82f, h * 0.55f, w * 0.82f, h * 0.65f)
                cubicTo(w * 0.75f, h * 0.42f, w * 0.60f, h * 0.40f, w * 0.50f, h * 0.36f)
                cubicTo(w * 0.38f, h * 0.42f, w * 0.26f, h * 0.45f, w * 0.18f, h * 0.55f)
                close()
            }
            drawPath(hair, Color(0xFF4A2A1A))

            // Eyes
            drawCircle(Color(0xFF22140D), 2.2f, Offset(w * 0.40f, h * 0.49f))
            drawCircle(Color(0xFF22140D), 2.2f, Offset(w * 0.60f, h * 0.49f))

            // Blush
            drawCircle(Color(0xFFFF9C9C).copy(alpha = 0.5f), 3.5f, Offset(w * 0.34f, h * 0.54f))
            drawCircle(Color(0xFFFF9C9C).copy(alpha = 0.5f), 3.5f, Offset(w * 0.66f, h * 0.54f))

            // Cute hair clip
            drawCircle(Color(0xFFFFB285), 2.5f, Offset(w * 0.30f, h * 0.32f))
        }
    }
}

/**
 * Night sky illustration with moon and stars for the Home date/time card
 */
@Composable
fun NightSkyIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Deep night sky gradient
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF1E2436), Color(0xFF141926), Color(0xFF0F121C))
            ),
            size = Size(w, h),
            cornerRadius = CornerRadius(16f, 16f)
        )

        // Glowing crescent moon
        val moonCenter = Offset(w * 0.78f, h * 0.40f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x55FFE8A0), Color(0x00FFE8A0)),
                center = moonCenter,
                radius = 32f
            ),
            radius = 32f,
            center = moonCenter
        )

        // Moon crescent
        val moonPath = Path().apply {
            addOval(androidx.compose.ui.geometry.Rect(moonCenter.x - 14f, moonCenter.y - 14f, moonCenter.x + 14f, moonCenter.y + 14f))
        }
        val maskPath = Path().apply {
            addOval(androidx.compose.ui.geometry.Rect(moonCenter.x - 8f, moonCenter.y - 18f, moonCenter.x + 16f, moonCenter.y + 10f))
        }
        drawPath(moonPath, Color(0xFFFFF4D0))
        drawPath(maskPath, Color(0xFF1B2132))

        // Tiny stars
        val stars = listOf(
            Offset(w * 0.18f, h * 0.28f),
            Offset(w * 0.35f, h * 0.42f),
            Offset(w * 0.55f, h * 0.22f),
            Offset(w * 0.68f, h * 0.65f),
            Offset(w * 0.88f, h * 0.72f),
            Offset(w * 0.28f, h * 0.68f)
        )
        for (star in stars) {
            drawCircle(Color(0xFFFFF2D0).copy(alpha = 0.8f), 1.8f, star)
        }

        // Two silhouetted friends / siblings sitting on hill looking at moon
        val hillPath = Path().apply {
            moveTo(0f, h)
            cubicTo(w * 0.3f, h * 0.82f, w * 0.7f, h * 0.80f, w, h * 0.85f)
            lineTo(w, h)
            close()
        }
        drawPath(hillPath, Color(0xFF0C0F17))

        // Sibling silhouettes sitting
        drawCircle(Color(0xFF0C0F17), 5f, Offset(w * 0.68f, h * 0.76f))
        drawRoundRect(Color(0xFF0C0F17), topLeft = Offset(w * 0.65f, h * 0.78f), size = Size(10f, 14f), cornerRadius = CornerRadius(2f, 2f))
        drawCircle(Color(0xFF0C0F17), 4.5f, Offset(w * 0.73f, h * 0.77f))
        drawRoundRect(Color(0xFF0C0F17), topLeft = Offset(w * 0.71f, h * 0.79f), size = Size(8f, 13f), cornerRadius = CornerRadius(2f, 2f))
    }
}
