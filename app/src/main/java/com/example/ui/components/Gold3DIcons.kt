package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 3D Metallic Gold Color Palette (as specified)
val GoldHighlight = Color(0xFFFFF4C0)
val GoldLight = Color(0xFFFFF3C2)
val GoldMid = Color(0xFFD8A320)
val GoldAccent = Color(0xFFE1B23D)
val GoldShadow = Color(0xFF845400)
val GoldDarkShadow = Color(0xFF4A2E00)

val GoldMetallicBrush = Brush.linearGradient(
    colors = listOf(
        GoldHighlight,
        GoldAccent,
        GoldMid,
        GoldShadow,
        GoldHighlight
    ),
    start = Offset(0f, 0f),
    end = Offset(120f, 120f)
)

val GoldIconBrush = Brush.verticalGradient(
    colors = listOf(
        GoldLight,
        GoldAccent,
        GoldMid,
        GoldShadow
    )
)

/**
 * Ultra Realistic 3D Metallic Gold Rearing Stallion Emblem (170dp, Ferrari style)
 */
@Composable
fun GoldRearingHorseEmblem(
    modifier: Modifier = Modifier,
    size: Dp = 170.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()

        // 1. Soft Outer Gold Glow behind emblem
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE2BD50).copy(alpha = 0.35f),
                    Color(0xFFD8A320).copy(alpha = 0.15f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.48f),
                radius = w * 0.48f
            ),
            radius = w * 0.48f,
            center = Offset(w * 0.5f, h * 0.48f)
        )

        // Function to construct the rearing stallion silhouette path
        fun createHorsePath(ox: Float = 0f, oy: Float = 0f): Path {
            return Path().apply {
                // Crown / Ears / Head Top
                moveTo((w * 0.46f) + ox, (h * 0.11f) + oy)
                cubicTo(
                    (w * 0.50f) + ox, (h * 0.08f) + oy,
                    (w * 0.56f) + ox, (h * 0.11f) + oy,
                    (w * 0.58f) + ox, (h * 0.16f) + oy
                ) // Snout/Muzzle
                lineTo((w * 0.52f) + ox, (h * 0.22f) + oy) // Jaw / Chin
                
                // Arched Neck & Mane
                cubicTo(
                    (w * 0.58f) + ox, (h * 0.26f) + oy,
                    (w * 0.66f) + ox, (h * 0.30f) + oy,
                    (w * 0.60f) + ox, (h * 0.38f) + oy
                )
                
                // Forelegs (Raised high in air facing left)
                lineTo((w * 0.72f) + ox, (h * 0.22f) + oy) // Raised front hoof 1
                lineTo((w * 0.68f) + ox, (h * 0.19f) + oy)
                lineTo((w * 0.58f) + ox, (h * 0.35f) + oy)
                lineTo((w * 0.78f) + ox, (h * 0.32f) + oy) // Raised front hoof 2
                lineTo((w * 0.72f) + ox, (h * 0.42f) + oy)
                
                // Powerful Chest & Torso
                cubicTo(
                    (w * 0.56f) + ox, (h * 0.48f) + oy,
                    (w * 0.52f) + ox, (h * 0.58f) + oy,
                    (w * 0.46f) + ox, (h * 0.66f) + oy
                )
                
                // Hind Legs (Planted powerfully)
                lineTo((w * 0.58f) + ox, (h * 0.80f) + oy) // Hind hoof 1
                lineTo((w * 0.50f) + ox, (h * 0.86f) + oy)
                lineTo((w * 0.42f) + ox, (h * 0.70f) + oy)
                lineTo((w * 0.36f) + ox, (h * 0.84f) + oy) // Hind hoof 2
                lineTo((w * 0.28f) + ox, (h * 0.82f) + oy)
                
                // Flank & Rump
                cubicTo(
                    (w * 0.26f) + ox, (h * 0.62f) + oy,
                    (w * 0.28f) + ox, (h * 0.50f) + oy,
                    (w * 0.36f) + ox, (h * 0.38f) + oy
                )
                
                // Flowing Tail
                cubicTo(
                    (w * 0.18f) + ox, (h * 0.48f) + oy,
                    (w * 0.14f) + ox, (h * 0.66f) + oy,
                    (w * 0.22f) + ox, (h * 0.78f) + oy
                )
                cubicTo(
                    (w * 0.26f) + ox, (h * 0.62f) + oy,
                    (w * 0.30f) + ox, (h * 0.46f) + oy,
                    (w * 0.40f) + ox, (h * 0.28f) + oy
                )
                close()
            }
        }

        // 2. Drop Shadow (0 12 30 rgba(0,0,0,0.45))
        drawPath(
            path = createHorsePath(ox = 4f, oy = 10f),
            brush = Brush.linearGradient(
                colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent),
                start = Offset(0f, 0f),
                end = Offset(0f, h)
            )
        )

        // 3. Dark Shadow Base Layer
        drawPath(
            path = createHorsePath(ox = 1.5f, oy = 3f),
            brush = Brush.linearGradient(
                colors = listOf(GoldDarkShadow, Color.Black)
            )
        )

        // 4. Main 3D Metallic Gold Body Layer
        drawPath(
            path = createHorsePath(ox = 0f, oy = 0f),
            brush = GoldMetallicBrush
        )

        // 5. Embossed Bevel Highlight Stroke
        val highlightStroke = Path().apply {
            moveTo(w * 0.46f, h * 0.11f)
            cubicTo(w * 0.50f, h * 0.08f, w * 0.56f, h * 0.11f, w * 0.58f, h * 0.16f)
            lineTo(w * 0.52f, h * 0.22f)
            cubicTo(w * 0.58f, h * 0.26f, w * 0.66f, h * 0.30f, w * 0.60f, h * 0.38f)
            lineTo(w * 0.72f, h * 0.22f)
        }
        drawPath(
            path = highlightStroke,
            brush = Brush.verticalGradient(
                colors = listOf(Color.White, GoldHighlight, GoldAccent)
            ),
            style = Stroke(width = 3.5f, cap = StrokeCap.Round)
        )
    }
}

/**
 * 1. House with Chimney Icon (منازل)
 */
@Composable
fun GoldHouse3DIcon(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val strokeW = w * 0.09f

        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            lineTo(w * 0.88f, h * 0.42f)
            lineTo(w * 0.88f, h * 0.85f)
            lineTo(w * 0.12f, h * 0.85f)
            lineTo(w * 0.12f, h * 0.42f)
            close()
        }

        val chimney = Path().apply {
            moveTo(w * 0.70f, h * 0.28f)
            lineTo(w * 0.70f, h * 0.15f)
            lineTo(w * 0.80f, h * 0.15f)
            lineTo(w * 0.80f, h * 0.36f)
        }

        val door = Rect(w * 0.40f, h * 0.55f, w * 0.60f, h * 0.85f)
        val window1 = Rect(w * 0.22f, h * 0.52f, w * 0.34f, h * 0.66f)

        // Shadow
        drawPath(path, Brush.verticalGradient(listOf(GoldDarkShadow, Color.Black)), style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(chimney, Brush.verticalGradient(listOf(GoldDarkShadow, Color.Black)), style = Stroke(width = strokeW, cap = StrokeCap.Round))

        // Gold Icon
        drawPath(path, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(chimney, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round))
        drawRect(GoldIconBrush, topLeft = door.topLeft, size = door.size, style = Stroke(width = strokeW * 0.8f))
        drawRect(GoldIconBrush, topLeft = window1.topLeft, size = window1.size, style = Stroke(width = strokeW * 0.7f))
    }
}

/**
 * 2. Folded Map with Pin Icon (أراضي كردون)
 */
@Composable
fun GoldMap3DIcon(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val strokeW = w * 0.085f

        val mapPath = Path().apply {
            moveTo(w * 0.12f, h * 0.42f)
            lineTo(w * 0.38f, h * 0.32f)
            lineTo(w * 0.62f, h * 0.42f)
            lineTo(w * 0.88f, h * 0.32f)
            lineTo(w * 0.88f, h * 0.80f)
            lineTo(w * 0.62f, h * 0.90f)
            lineTo(w * 0.38f, h * 0.80f)
            lineTo(w * 0.12f, h * 0.90f)
            close()
        }

        val pinHeadCenter = Offset(w * 0.50f, h * 0.22f)
        val pinRadius = w * 0.14f

        drawPath(mapPath, Brush.linearGradient(listOf(GoldDarkShadow, Color.Black)), style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawCircle(Brush.linearGradient(listOf(GoldDarkShadow, Color.Black)), radius = pinRadius, center = pinHeadCenter + Offset(2f, 2f))

        drawPath(mapPath, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawCircle(GoldIconBrush, radius = pinRadius, center = pinHeadCenter, style = Stroke(width = strokeW))
        drawCircle(GoldIconBrush, radius = pinRadius * 0.4f, center = pinHeadCenter)

        val pinPointer = Path().apply {
            moveTo(w * 0.38f, h * 0.28f)
            lineTo(w * 0.50f, h * 0.48f)
            lineTo(w * 0.62f, h * 0.28f)
        }
        drawPath(pinPointer, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round))
    }
}

/**
 * 3. Multi-Story Apartment Building Icon (شقق تمليك)
 */
@Composable
fun GoldApartment3DIcon(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val strokeW = w * 0.085f

        val buildingPath = Path().apply {
            moveTo(w * 0.20f, h * 0.88f)
            lineTo(w * 0.20f, h * 0.20f)
            lineTo(w * 0.80f, h * 0.20f)
            lineTo(w * 0.80f, h * 0.88f)
            close()
        }

        val topTower = Path().apply {
            moveTo(w * 0.35f, h * 0.20f)
            lineTo(w * 0.35f, h * 0.10f)
            lineTo(w * 0.65f, h * 0.10f)
            lineTo(w * 0.65f, h * 0.20f)
        }

        val winSize = Size(w * 0.12f, h * 0.10f)

        drawPath(buildingPath, Brush.linearGradient(listOf(GoldDarkShadow, Color.Black)), style = Stroke(width = strokeW, cap = StrokeCap.Round))

        drawPath(buildingPath, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(topTower, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        drawRect(GoldIconBrush, topLeft = Offset(w * 0.30f, h * 0.30f), size = winSize)
        drawRect(GoldIconBrush, topLeft = Offset(w * 0.58f, h * 0.30f), size = winSize)
        drawRect(GoldIconBrush, topLeft = Offset(w * 0.30f, h * 0.48f), size = winSize)
        drawRect(GoldIconBrush, topLeft = Offset(w * 0.58f, h * 0.48f), size = winSize)

        drawRect(GoldIconBrush, topLeft = Offset(w * 0.40f, h * 0.68f), size = Size(w * 0.20f, h * 0.20f), style = Stroke(width = strokeW * 0.8f))
    }
}

/**
 * 4. Agricultural Plant Sprout Icon (أراضي زراعية)
 */
@Composable
fun GoldAgriculture3DIcon(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val strokeW = w * 0.088f

        val stem = Path().apply {
            moveTo(w * 0.50f, h * 0.88f)
            lineTo(w * 0.50f, h * 0.42f)
        }

        val leftLeaf = Path().apply {
            moveTo(w * 0.50f, h * 0.62f)
            cubicTo(w * 0.20f, h * 0.58f, w * 0.12f, h * 0.32f, w * 0.35f, h * 0.25f)
            cubicTo(w * 0.50f, h * 0.35f, w * 0.50f, h * 0.50f, w * 0.50f, h * 0.62f)
            close()
        }

        val rightLeaf = Path().apply {
            moveTo(w * 0.50f, h * 0.52f)
            cubicTo(w * 0.80f, h * 0.48f, w * 0.88f, h * 0.22f, w * 0.65f, h * 0.15f)
            cubicTo(w * 0.50f, h * 0.25f, w * 0.50f, h * 0.40f, w * 0.50f, h * 0.52f)
            close()
        }

        drawPath(stem, Brush.linearGradient(listOf(GoldDarkShadow, Color.Black)), style = Stroke(width = strokeW, cap = StrokeCap.Round))
        drawPath(leftLeaf, Brush.linearGradient(listOf(GoldDarkShadow, Color.Black)))
        drawPath(rightLeaf, Brush.linearGradient(listOf(GoldDarkShadow, Color.Black)))

        drawPath(stem, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round))
        drawPath(leftLeaf, GoldIconBrush)
        drawPath(rightLeaf, GoldIconBrush)
    }
}

/**
 * 5. House with Key Icon (شقق إيجار)
 */
@Composable
fun GoldRentKey3DIcon(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val strokeW = w * 0.085f

        val roof = Path().apply {
            moveTo(w * 0.50f, h * 0.12f)
            lineTo(w * 0.88f, h * 0.45f)
            lineTo(w * 0.88f, h * 0.88f)
            lineTo(w * 0.12f, h * 0.88f)
            lineTo(w * 0.12f, h * 0.45f)
            close()
        }

        val keyCenter = Offset(w * 0.50f, h * 0.50f)
        val keyRadius = w * 0.10f
        val keyStem = Path().apply {
            moveTo(w * 0.50f, h * 0.58f)
            lineTo(w * 0.50f, h * 0.78f)
            lineTo(w * 0.60f, h * 0.78f)
            moveTo(w * 0.50f, h * 0.70f)
            lineTo(w * 0.58f, h * 0.70f)
        }

        drawPath(roof, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawCircle(GoldIconBrush, radius = keyRadius, center = keyCenter, style = Stroke(width = strokeW * 0.9f))
        drawPath(keyStem, GoldIconBrush, style = Stroke(width = strokeW * 0.8f, cap = StrokeCap.Round))
    }
}

/**
 * 6. Tree and Landscape Icon (أراضي خارج الكردون)
 */
@Composable
fun GoldOutsideCordon3DIcon(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val strokeW = w * 0.085f

        val treeCrownCenter = Offset(w * 0.50f, h * 0.32f)
        val treeRadius = w * 0.22f

        val trunk = Path().apply {
            moveTo(w * 0.50f, h * 0.52f)
            lineTo(w * 0.50f, h * 0.72f)
        }

        val hill1 = Path().apply {
            moveTo(w * 0.10f, h * 0.72f)
            cubicTo(w * 0.30f, h * 0.62f, w * 0.50f, h * 0.78f, w * 0.90f, h * 0.68f)
        }
        val hill2 = Path().apply {
            moveTo(w * 0.10f, h * 0.85f)
            cubicTo(w * 0.40f, h * 0.78f, w * 0.70f, h * 0.92f, w * 0.90f, h * 0.82f)
        }

        drawCircle(Brush.linearGradient(listOf(GoldDarkShadow, Color.Black)), radius = treeRadius, center = treeCrownCenter + Offset(2f, 2f))

        drawCircle(GoldIconBrush, radius = treeRadius, center = treeCrownCenter, style = Stroke(width = strokeW))
        drawPath(trunk, GoldIconBrush, style = Stroke(width = strokeW * 1.2f, cap = StrokeCap.Round))
        drawPath(hill1, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round))
        drawPath(hill2, GoldIconBrush, style = Stroke(width = strokeW, cap = StrokeCap.Round))
    }
}

