package com.example.clockanimation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.clockanimation.ui.theme.ClockAnimationTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClockAnimationTheme(
            ) {
                SideEffect {
                    val window = this@MainActivity.window
                    window.statusBarColor = Color.Black.toArgb()
                    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
                }
                Surface(
                    color = Color.Black
                ) {
                    AnimatedScreen()
                }
            }
        }
    }
}


@Composable
fun AnimatedScreen(clockSize: Dp = 200.dp, numberOfCircles: Int = 10) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        BackgroundStroke(modifier = Modifier.size(clockSize))
        VibratingCirclesScreen(numberOfCircles = numberOfCircles)
        AnimatedClock(modifier = Modifier.size(clockSize))
    }
}

@Composable
fun AnimatedClock(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val secondsRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing)
        ), label = ""
    )

    val minutesRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600000, easing = LinearEasing)
        ), label = ""
    )

    Canvas(modifier = modifier.size(200.dp)) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)
        val handOffset = 20f // Offset to shift the hands from the center

        drawCircle(color = Color.Black, radius = radius)

        // Draw seconds hand
        val secondsLength = radius * 0.8f
        val secondsStart = Offset(
            x = center.x - handOffset * cos(Math.toRadians(secondsRotation.toDouble() - 90).toFloat()),
            y = center.y - handOffset * sin(Math.toRadians(secondsRotation.toDouble() - 90).toFloat())
        )
        val secondsEnd = Offset(
            x = center.x + secondsLength * cos(Math.toRadians(secondsRotation.toDouble() - 90).toFloat()),
            y = center.y + secondsLength * sin(Math.toRadians(secondsRotation.toDouble() - 90).toFloat())
        )
        drawLine(
            color = Color.Blue,
            start = secondsStart,
            end = secondsEnd,
            strokeWidth = 4f
        )

        // Draw minutes hand
        val minutesLength = radius * 0.6f
        val minutesStart = Offset(
            x = center.x - handOffset * cos(Math.toRadians(minutesRotation.toDouble() - 90).toFloat()),
            y = center.y - handOffset * sin(Math.toRadians(minutesRotation.toDouble() - 90).toFloat())
        )
        val minutesEnd = Offset(
            x = center.x + minutesLength * cos(Math.toRadians(minutesRotation.toDouble() - 90).toFloat()),
            y = center.y + minutesLength * sin(Math.toRadians(minutesRotation.toDouble() - 90).toFloat())
        )
        drawLine(
            color = Color.Red,
            start = minutesStart,
            end = minutesEnd,
            strokeWidth = 8f
        )
    }
}

@Composable
fun BackgroundStroke(modifier: Modifier) {
    val strokeColor by rememberInfiniteTransition(label = "").animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Canvas(modifier = modifier.size(200.dp)) {
        val radius = size.minDimension / 2
        drawCircle(
            color = strokeColor,
            radius = radius + 1,
            style = Stroke(width = 4f)
        )
    }
}

@Composable
fun VibratingCirclesScreen(numberOfCircles: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        VibratingCircles(modifier = Modifier.fillMaxSize(), numberOfCircles = numberOfCircles)
    }
}

@Composable
fun VibratingCircles(
    numberOfCircles: Int = 10,
    sizeFactor: Float = 2.5f,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val colorList = List(numberOfCircles) {
        infiniteTransition.animateColor(
            initialValue = Color(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat(),
                1f
            ),
            targetValue = Color(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat(),
                1f
            ),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = Random.nextInt(500, 1500),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
    }

    val sizeList = List(numberOfCircles) {
        infiniteTransition.animateFloat(
            initialValue = 50f * sizeFactor,
            targetValue = 100f * sizeFactor,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = Random.nextInt(500, 1500),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
    }

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        for (i in 0 until numberOfCircles) {
            val angle = 2 * Math.PI * i / numberOfCircles
            val offsetX = (100 * cos(angle)).toFloat()
            val offsetY = (100 * sin(angle)).toFloat()

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(colorList[i].value, Color.Transparent),
                    center = Offset(centerX + offsetX, centerY + offsetY),
                    radius = sizeList[i].value * 5f // Sharpen the gradient
                ),
                radius = sizeList[i].value,
                center = Offset(centerX + offsetX, centerY + offsetY)
            )
        }
    }
}