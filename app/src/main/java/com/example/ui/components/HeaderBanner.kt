package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold

@Composable
fun HeaderBanner(
    isSeniorMode: Boolean,
    onToggleSeniorMode: () -> Unit,
    onOpenAboutDeveloper: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onOpenAuthModal: () -> Unit,
    onTrackCallClick: () -> Unit = {},
    onTrackWhatsappClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val mainTitleSize = if (isSeniorMode) 52.sp else 48.sp
    val subtitleSize = if (isSeniorMode) 28.sp else 24.sp
    val taglineSize = if (isSeniorMode) 19.sp else 17.sp

    // Metallic Gold Gradient: #FFF3BA -> #F2C448 -> #AF7200
    val metallicGoldBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF3BA),
            Color(0xFFF2C448),
            Color(0xFFAF7200)
        )
    )

    // Dark Emerald Radial Gradient Background
    val headerBgBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF004E2D),
            Color(0xFF00381F),
            Color(0xFF00150C)
        ),
        radius = 1200f
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = headerBgBrush)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Senior Mode Toggle
            Surface(
                onClick = onToggleSeniorMode,
                shape = RoundedCornerShape(20.dp),
                color = if (isSeniorMode) RoyalGold else DarkEmeraldCard.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold.copy(alpha = 0.5f)),
                modifier = Modifier
                    .padding(2.dp)
                    .animateContentSize()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessibilityNew,
                        contentDescription = "وضع كبار السن",
                        tint = if (isSeniorMode) Color.Black else SoftGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSeniorMode) "وضع النواعم" else "تكبير الخط",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSeniorMode) Color.Black else Color.White
                    )
                }
            }

            // Right Action Icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onOpenAboutDeveloper) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "عن المطور",
                        tint = SoftGold.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onOpenAdminDashboard) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "لوحة التحكم",
                        tint = RoyalGold.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onOpenAuthModal) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "الحساب",
                        tint = SoftGold.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Centerpiece 3D Golden Rearing Stallion Emblem (170dp)
        Box(
            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            GoldRearingHorseEmblem(size = 170.dp)
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Title 1: "- سمسارك -"
        Text(
            text = "ـ سمسارك ـ",
            style = TextStyle(
                brush = metallicGoldBrush,
                fontSize = mainTitleSize,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.85f),
                    offset = Offset(2.5f, 4.5f),
                    blurRadius = 8f
                )
            )
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Subtitle: "في أولاد صقر"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color(0xFFF2C448))
                        )
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ـ في أولاد صقر ـ",
                style = TextStyle(
                    brush = metallicGoldBrush,
                    fontSize = subtitleSize,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.75f),
                        offset = Offset(1.5f, 3f),
                        blurRadius = 4f
                    )
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFF2C448), Color.Transparent)
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tagline: "كل العقارات في مكان واحد"
        Text(
            text = "كل العقارات في مكان واحد",
            fontSize = taglineSize,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.92f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))
    }
}