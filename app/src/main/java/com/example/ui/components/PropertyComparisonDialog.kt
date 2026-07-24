package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.model.Property
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldElevated
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PropertyComparisonDialog(
    comparedProperties: List<Property>,
    isSeniorMode: Boolean,
    onRemoveProperty: (Long) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit,
    onTrackCallClick: () -> Unit = {},
    onTrackWhatsappClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val titleFontSize = if (isSeniorMode) 20.sp else 16.sp
    val bodyFontSize = if (isSeniorMode) 15.sp else 12.sp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(24.dp),
            color = DarkEmerald,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⚖️ نظام مقارنة العقارات",
                            fontSize = titleFontSize,
                            fontWeight = FontWeight.Black,
                            color = SoftGold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = CircleShape,
                            color = RoyalGold
                        ) {
                            Text(
                                text = "${comparedProperties.size}/3",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (comparedProperties.isNotEmpty()) {
                            IconButton(onClick = onClearAll) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "مسح الكل",
                                    tint = SoftGold
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = TextWhite
                            )
                        }
                    }
                }

                Text(
                    text = "جدول مقارنة شامل لمعايير المواصفات والأسعار والموقع لخدمة أهالي أولاد صقر",
                    fontSize = bodyFontSize,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Divider(color = DarkGold.copy(alpha = 0.5f))

                if (comparedProperties.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚖️", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "لم تقم بجمع أي عقارات للمقارنة بعد.",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "اضغط على زر (⚖️ مقارنة) على أحدث البطاقات لإضافتها هنا (حتى 3 عقارات).",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    val columnWidth = 180.dp

                    // Scrollable Horizontal Table Area
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            comparedProperties.forEach { prop ->
                                val firstImg = prop.imageUrls.split(",").firstOrNull()?.trim() ?: ""
                                val pricePerSqm = if (prop.areaSqm > 0) prop.priceEgp / prop.areaSqm else 0.0
                                val areaQirat = prop.areaSqm / 175.0

                                val isCordon = prop.categoryId == "building_cordon" || prop.title.contains("كردون") || prop.description.contains("كردون")
                                val cordonLabel = if (isCordon) "✅ داخل كردون المباني" else "🌾 خارج الكردون / أرض زراعية"

                                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("ar"))
                                val dateStr = try { dateFormat.format(Date(prop.createdAt)) } catch (e: Exception) { "حديثاً" }

                                Column(
                                    modifier = Modifier
                                        .width(columnWidth)
                                        .background(DarkEmeraldCard, RoundedCornerShape(16.dp))
                                        .border(1.dp, RoyalGold, RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                ) {
                                    // 1) Image & Title & Remove Button
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Black)
                                    ) {
                                        if (firstImg.isNotEmpty()) {
                                            AsyncImage(
                                                model = firstImg,
                                                contentDescription = prop.title,
                                                modifier = Modifier.matchParentSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier.matchParentSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("🏠 سمسارك", color = SoftGold, fontSize = 12.sp)
                                            }
                                        }

                                        IconButton(
                                            onClick = { onRemoveProperty(prop.id) },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .size(28.dp)
                                                .background(Color.Red.copy(alpha = 0.85f), CircleShape)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "حذف",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = prop.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.height(36.dp)
                                    )

                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkGold.copy(alpha = 0.3f))

                                    // 2) Price & Negotiation Tag
                                    Text("💰 السعر والتفاوض", fontSize = 11.sp, color = TextMuted)
                                    Text(
                                        text = "${String.format("%,d", prop.priceEgp.toLong())} ج.م",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = SoftGold
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (prop.isNegotiable) Color(0xFF1B5E20) else Color(0xFF880E4F),
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Text(
                                            text = if (prop.isNegotiable) "قابل للتفاوض" else "سعر نهائي",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkGold.copy(alpha = 0.3f))

                                    // 3) Area
                                    Text("📐 المساحة الإجمالية", fontSize = 11.sp, color = TextMuted)
                                    Text(
                                        text = "${prop.areaSqm.toInt()} م²",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    if (prop.areaSqm >= 100) {
                                        Text(
                                            text = "(~${String.format("%.1f", areaQirat)} قيراط)",
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }

                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkGold.copy(alpha = 0.3f))

                                    // 4) Estimated Price per Unit
                                    Text("📊 متوسط سعر المتر/القيراط", fontSize = 11.sp, color = TextMuted)
                                    Text(
                                        text = "${String.format("%,d", pricePerSqm.toLong())} ج.م/م²",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalGold
                                    )

                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkGold.copy(alpha = 0.3f))

                                    // 5) Village / Location
                                    Text("📍 القرية / المنطقة", fontSize = 11.sp, color = TextMuted)
                                    Text(
                                        text = prop.villageArea,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkGold.copy(alpha = 0.3f))

                                    // 6) Cordon Status
                                    Text("🏛️ موقف الكردون", fontSize = 11.sp, color = TextMuted)
                                    Text(
                                        text = cordonLabel,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCordon) Color(0xFF81C784) else SoftGold
                                    )

                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkGold.copy(alpha = 0.3f))

                                    // 7) Video Availability
                                    Text("🎥 فيديو المعاينة", fontSize = 11.sp, color = TextMuted)
                                    Text(
                                        text = if (prop.videoUrl.isNotEmpty()) "🎥 فيديو متوفر" else "غير متوفر",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (prop.videoUrl.isNotEmpty()) SoftGold else TextMuted
                                    )

                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkGold.copy(alpha = 0.3f))

                                    // 8) Posting Date
                                    Text("📅 تاريخ النشر", fontSize = 11.sp, color = TextMuted)
                                    Text(
                                        text = dateStr,
                                        fontSize = 11.sp,
                                        color = TextWhite
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // 9) Direct Contact Action Buttons
                                    Button(
                                        onClick = {
                                            onTrackCallClick()
                                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                                data = Uri.parse("tel:${prop.contactPhone}")
                                            }
                                            context.startActivity(intent)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = DarkEmeraldElevated),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Call, contentDescription = null, tint = SoftGold, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("اتصال", fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Button(
                                        onClick = {
                                            onTrackWhatsappClick()
                                            val waMsg = Uri.encode("استفسار من مقارنة العقارات عن (${prop.title})")
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse("https://wa.me/${prop.contactWhatsapp}?text=$waMsg")
                                            }
                                            context.startActivity(intent)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("💬 واتساب", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Footer Close Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("إغلاق الجدول", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
        }
    }
}
