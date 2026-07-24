package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Property
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldElevated
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun PropertyCard(
    property: Property,
    isSeniorMode: Boolean,
    isCompared: Boolean = false,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onToggleCompare: (Long) -> Unit = {},
    onClickDetail: (Long) -> Unit,
    onTrackCallClick: () -> Unit = {},
    onTrackWhatsappClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val titleFontSize = if (isSeniorMode) 20.sp else 16.sp
    val priceFontSize = if (isSeniorMode) 22.sp else 18.sp
    val bodyFontSize = if (isSeniorMode) 16.sp else 13.sp
    val buttonPadding = if (isSeniorMode) 14.dp else 10.dp

    val firstImageUrl = property.imageUrls.split(",").firstOrNull()?.trim() ?: ""

    val isSold = property.status == "تم البيع" || property.status == "العقار مباع"
    val isRented = property.status == "تم الإيجار"
    val isUnavailable = isSold || isRented

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clickable { onClickDetail(property.id) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSold) Color(0xFFD32F2F) else if (isRented) RoyalGold else DarkGold.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Media Image Header Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isSeniorMode) 200.dp else 170.dp)
                    .background(Color(0xFF04271A))
            ) {
                if (firstImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = firstImageUrl,
                        contentDescription = property.title,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏠 سمسارك في أولاد صقر", color = SoftGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                // Prominent Sold / Rented Overlay Ribbon Banner (Requirement 2)
                if (isUnavailable) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .background(
                                if (isSold) Color(0xDDCC1111) else Color(0xDD1B5E20)
                            )
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSold) Icons.Default.Verified else Icons.Default.Verified,
                                contentDescription = "تم البيع",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSold) "🔴 تم البيع - العقار مباع 🎉" else "🔑 تم الإيجار بالكامل",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                // Top Badges (Deal Type + Compare + Favorite)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSold) Color(0xFFD32F2F) else RoyalGold
                    ) {
                        Text(
                            text = if (isSold) "تم البيع" else property.dealType,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Compare Button Chip
                        Surface(
                            onClick = { onToggleCompare(property.id) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCompared) RoyalGold else Color.Black.copy(alpha = 0.65f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isCompared) Color.White else SoftGold.copy(alpha = 0.8f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isCompared) "⚖️ مقارَن" else "⚖️ مقارنة",
                                    color = if (isCompared) Color.Black else TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Favorite Button
                        IconButton(
                            onClick = { onToggleFavorite(property.id, property.isFavorite) },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (property.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "المفضلة",
                                tint = if (property.isFavorite) Color.Red else Color.White
                            )
                        }
                    }
                }

                // Video Badge if available
                if (property.videoUrl.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "معاينة فيديو",
                            tint = SoftGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "معاينة فيديو",
                            color = TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Negotiable Tag at Bottom Right of Image
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = if (property.isNegotiable) Color(0xFF2E7D32) else Color(0xFFC62828)
                ) {
                    Text(
                        text = if (property.isNegotiable) "قابل للتفاوض" else "غير قابل للتفاوض",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Body Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Title
                Text(
                    text = property.title,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Location / Village
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "الموقع",
                        tint = SoftGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = property.villageArea,
                        fontSize = bodyFontSize,
                        color = SoftGold,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Specs Summary (Area + Dimensions)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SquareFoot,
                            contentDescription = "المساحة",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "المساحة: ${property.areaSqm.toInt()} م²",
                            fontSize = bodyFontSize,
                            color = TextWhite
                        )
                    }

                    if (property.dimensions.isNotEmpty()) {
                        Text(
                            text = property.dimensions,
                            fontSize = bodyFontSize,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Price Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "السعر المطلوب:",
                        fontSize = bodyFontSize,
                        color = TextMuted
                    )
                    Text(
                        text = "${String.format("%,d", property.priceEgp.toLong())} ج.م",
                        fontSize = priceFontSize,
                        fontWeight = FontWeight.Black,
                        color = SoftGold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Direct Contact Action Buttons (Phone & WhatsApp) / Muted when Sold
                if (isUnavailable) {
                    Button(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color(0xFF2A2A2A),
                            disabledContentColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isSold) "🔒 تم بيع العقار - معروض للأرشيف المرجعي" else "🔒 تم إيجار العقار بالكامل",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Call Phone Button
                        Button(
                            onClick = {
                                onTrackCallClick()
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${property.contactPhone}")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkEmeraldElevated),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "اتصل بنا",
                                    tint = SoftGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "اتصل بنا",
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = bodyFontSize
                                )
                            }
                        }

                        // WhatsApp Direct Button
                        Button(
                            onClick = {
                                onTrackWhatsappClick()
                                val waMsg = Uri.encode("أهلاً بك، أريد الاستفسار عن عقار (${property.title}) في تطبيق سمسارك")
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://wa.me/${property.contactWhatsapp}?text=$waMsg")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1.2f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "💬 واتساب",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = bodyFontSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
