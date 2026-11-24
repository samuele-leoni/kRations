package com.example.krations.profile.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ProfileIcon(modifier: Modifier = Modifier.size(24.dp), color: Color = Color(0xFF211F26)) {
    Canvas(modifier) {
        val stroke = Stroke(width = size.minDimension * 0.09f, cap = StrokeCap.Round)
        drawCircle(color = color, style = stroke)
        drawCircle(
            color = color,
            radius = size.minDimension * 0.14f,
            center = Offset(size.width / 2f, size.height * 0.38f),
            style = stroke,
        )
        val shoulders = Path().apply {
            moveTo(size.width * 0.23f, size.height * 0.78f)
            cubicTo(
                size.width * 0.25f, size.height * 0.61f,
                size.width * 0.75f, size.height * 0.61f,
                size.width * 0.77f, size.height * 0.78f,
            )
        }
        drawPath(shoulders, color = color, style = stroke)
    }
}
