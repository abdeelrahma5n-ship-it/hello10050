package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import com.example.data.model.Property
import com.example.data.model.User
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldElevated
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailScreen(
    property: Property?,
    isSeniorMode: Boolean,
    isGuestMode: Boolean = false,
    isCompared: Boolean = false,
    currentUser: User? = null,
    onBack: () -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onToggleCompare: (Long) -> Unit = {},
    onOpenAuthModal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    if (property == null) {
        Box(modifier = Modifier.fillMaxSize().background(DarkEmerald), contentAlignment = Alignment.Center) {
            Text("العقار غير موجود", color = TextWhite)
        }
        return
    }

    val images = property.imageUrls.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    var selectedImageIndex by remember { mutableStateOf(0) }
    var showVideoModal by remember { mutableStateOf(false) }

    var commentText by remember { mutableStateOf("") }
    var commentsList by remember {
        mutableStateOf(
            listOf(
                "محمود السيد" to "عقار متميز جداً والسعر مناسب لمنطقة أولاد صقر! 👍",
                "أحمد العربي" to "هل السعر قابل للنقاش أو التقسيط؟"
            )
        )
    }
    var showGuestCommentDialog by remember { mutableStateOf(false) }

    val isSold = property.status == "تم البيع" || property.status == "العقار مباع"
    val isRented = property.status == "تم الإيجار"
    val isUnavailable = isSold || isRented

    val titleFontSize = if (isSeniorMode) 22.sp else 18.sp
    val priceFontSize = if (isSeniorMode) 24.sp else 20.sp
    val bodyFontSize = if (isSeniorMode) 16.sp else 14.sp

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkEmerald,
        topBar = {
            TopAppBar(
                title = { Text("تفاصيل العقار", fontWeight = FontWeight.Bold, color = SoftGold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع", tint = TextWhite)
                    }
                },
                actions = {
                    Surface(
                        onClick = { onToggleCompare(property.id) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCompared) RoyalGold else Color.Black.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCompared) Color.White else SoftGold
                        ),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isCompared) "⚖️ في المقارنة" else "⚖️ إضافة للمقارنة",
                                color = if (isCompared) Color.Black else TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = { onToggleFavorite(property.id, property.isFavorite) }) {
                        Icon(
                            imageVector = if (property.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "المفضلة",
                            tint = if (property.isFavorite) Color.Red else TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkEmeraldCard)
            )
        },
        bottomBar = {
            // Sticky Bottom Communication Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = DarkEmeraldCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                if (isUnavailable) {
                    Button(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color(0xFF2A2A2A),
                            disabledContentColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (isSold) "🔒 تم بيع العقار - معروض في الأرشيف المرجعي فقط" else "🔒 تم إيجار العقار بالكامل",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Direct Call Button
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${property.contactPhone}")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkEmeraldElevated),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = "اتصال", tint = SoftGold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("اتصل بنا", fontWeight = FontWeight.Bold, color = TextWhite, fontSize = bodyFontSize)
                            }
                        }

                        // Direct WhatsApp Button
                        Button(
                            onClick = {
                                val waMsg = Uri.encode("أهلاً بك أ/ عبدالرحمن، أريد الاستفسار عن عقار (${property.title}) بسعر ${property.priceEgp.toInt()} ج.م")
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://wa.me/${property.contactWhatsapp}?text=$waMsg")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1.3f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("💬 تواصل واتساب", fontWeight = FontWeight.Black, color = Color.White, fontSize = bodyFontSize)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Photo Gallery Viewer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color.Black)
            ) {
                if (images.isNotEmpty()) {
                    AsyncImage(
                        model = images.getOrElse(selectedImageIndex) { images.first() },
                        contentDescription = property.title,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Overlay Ribbon Banner for Sold/Rented in Gallery
                if (isUnavailable) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .background(if (isSold) Color(0xDDCC1111) else Color(0xDD1B5E20))
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSold) "🔴 تم بيع العقار 🎉" else "🔑 تم الإيجار بالكامل",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                }

                // Video Badge Trigger
                if (property.videoUrl.isNotEmpty()) {
                    Surface(
                        onClick = { showVideoModal = true },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.75f),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.PlayCircle, contentDescription = "تشغيل فيديو", tint = RoyalGold, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("مشاهدة فيديو المعاينة", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }

                // Negotiable Tag overlay
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (property.isNegotiable) Color(0xFF2E7D32) else Color(0xFFC62828)
                ) {
                    Text(
                        text = if (property.isNegotiable) "قابل للتفاوض" else "غير قابل للتفاوض",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            // Thumbnail Carousel if multiple images
            if (images.size > 1) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(images) { idx, url ->
                        val isSelected = idx == selectedImageIndex
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    2.dp,
                                    if (isSelected) RoyalGold else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedImageIndex = idx }
                        ) {
                            AsyncImage(
                                model = url,
                                contentDescription = "صورة $idx",
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            // Title & Price Section Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = property.title,
                        fontSize = titleFontSize,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("السعر الكلي:", fontSize = bodyFontSize, color = TextMuted)
                        Text(
                            text = "${String.format("%,d", property.priceEgp.toLong())} ج.م",
                            fontSize = priceFontSize,
                            fontWeight = FontWeight.Black,
                            color = SoftGold
                        )
                    }
                }
            }

            // Specifications Table Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "المواصفات الفنية والمالية:",
                        fontSize = bodyFontSize,
                        fontWeight = FontWeight.Bold,
                        color = SoftGold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SpecRow(icon = Icons.Default.LocationOn, label = "الموقع والمنطقة", value = property.villageArea)
                    SpecRow(icon = Icons.Default.SquareFoot, label = "المساحة الإجمالية", value = "${property.areaSqm.toInt()} متر مربع")
                    if (property.dimensions.isNotEmpty()) SpecRow(icon = Icons.Default.SquareFoot, label = "الأبعاد والواجهة", value = property.dimensions)
                    if (property.floorNumber.isNotEmpty()) SpecRow(icon = Icons.Default.Layers, label = "الدور / الطابق", value = property.floorNumber)
                    SpecRow(icon = Icons.Default.Gavel, label = "الوضع القانوني", value = property.legalStatus)
                    SpecRow(icon = Icons.Default.CheckCircle, label = "نوع الصفقة", value = property.dealType)
                    SpecRow(icon = Icons.Default.Person, label = "المسئول عن المعاينة", value = property.ownerName)
                }
            }

            // Description Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "تفاصيل ووصف العقار:",
                        fontSize = bodyFontSize,
                        fontWeight = FontWeight.Bold,
                        color = SoftGold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = property.description,
                        fontSize = bodyFontSize,
                        color = TextWhite,
                        lineHeight = 22.sp
                    )
                }
            }

            // Google Maps Integrated Location Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Map, contentDescription = "خرائط جوجل", tint = SoftGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("موقع العقار في أولاد صقر:", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = bodyFontSize)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = property.addressDetails,
                        fontSize = bodyFontSize,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val mapQuery = Uri.encode("أولاد صقر ${property.villageArea} ${property.addressDetails}")
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$mapQuery"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkEmeraldElevated),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📍 فتح الموقع في خرائط Google Maps", color = SoftGold, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Comments & Reviews Card (التعليقات والتقييمات)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Comment, contentDescription = "التعليقات", tint = SoftGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("التعليقات والاستفسارات:", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = bodyFontSize)
                        }
                        Text("(${commentsList.size})", color = TextMuted, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    commentsList.forEach { (author, text) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color(0xFF093826), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = RoyalGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(author, color = RoyalGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(text, color = TextWhite, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isGuestMode || currentUser == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF062218), RoundedCornerShape(12.dp))
                                .border(1.dp, DarkGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .clickable { showGuestCommentDialog = true }
                                .padding(14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = "قفل", tint = RoyalGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "يلزم تسجيل الدخول لإضافة تعليق أو استفسار (اضغط هنا)",
                                    color = SoftGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                placeholder = { Text("أكتب تعليقك هنا...", color = TextMuted, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (commentText.isNotBlank()) {
                                        commentsList = commentsList + (currentUser.name to commentText)
                                        commentText = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("إرسال", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        if (showGuestCommentDialog) {
            AlertDialog(
                onDismissRequest = { showGuestCommentDialog = false },
                containerColor = DarkEmeraldCard,
                icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = RoyalGold) },
                title = {
                    Text("تسجيل الدخول مطلوب 🔒", color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                text = {
                    Text("عفواً، يجب تسجيل الدخول أولاً لتتمكن من إضافة تعليق أو المشاركة في الاستفسارات.", color = TextWhite, fontSize = 14.sp)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showGuestCommentDialog = false
                            onOpenAuthModal()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalGold)
                    ) {
                        Text("تسجيل الدخول الان 📲", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showGuestCommentDialog = false }) {
                        Text("إلغاء", color = SoftGold)
                    }
                }
            )
        }

        // Fast Video Modal Dialog
        if (showVideoModal) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                        border = androidx.compose.foundation.BorderStroke(2.dp, RoyalGold)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("معاينة الفيديو لعقار أولاد صقر", color = SoftGold, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { showVideoModal = false }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = TextWhite)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(property.videoUrl))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("مشاهدة الفيديو بملف وسائط خارجي 🎬", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpecRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = label, tint = SoftGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = label, color = TextMuted, fontSize = 13.sp)
            }
            Text(text = value, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        Divider(color = DarkGold.copy(alpha = 0.3f), thickness = 0.5.dp, modifier = Modifier.padding(top = 4.dp))
    }
}
