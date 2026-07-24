package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomHomeBanner(
    isSeniorMode: Boolean,
    modifier: Modifier = Modifier
) {
    val fontSize = if (isSeniorMode) 18.sp else 16.sp

    // Metallic gold gradient: Top #FFF4AF -> Middle #DDAA2E -> Bottom #A76C00
    val bannerGoldBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF4AF),
            Color(0xFFDDAA2E),
            Color(0xFFA76C00)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(10.dp, RoundedCornerShape(28.dp), spotColor = Color.Black.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(28.dp))
            .background(brush = bannerGoldBrush)
            .border(1.5.dp, Color(0xFFFFF9D2), RoundedCornerShape(28.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Left Side: Shield + "ثقة في التعامل"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color(0xFF00150C), RoundedCornerShape(10.dp))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "ثقة",
                        tint = Color(0xFFFFF4AF),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ثقة في التعامل",
                    color = Color(0xFF00150C),
                    fontWeight = FontWeight.Black,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center
                )
            }

            // Center Vertical Divider
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(1.5.dp)
                    .background(Color(0xFF704700))
            )

            // Right Side: Search + "سهولة في البحث"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "سهولة",
                    tint = Color(0xFF00150C),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "سهولة في البحث",
                    color = Color(0xFF00150C),
                    fontWeight = FontWeight.Black,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}