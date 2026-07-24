package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DealType
import com.example.data.model.Property
import com.example.data.model.PropertyCategory
import com.example.data.model.PropertyStatus
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(
    isSeniorMode: Boolean,
    onBack: () -> Unit,
    onSubmitProperty: (Property) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(PropertyCategory.HOUSES) }
    var priceText by remember { mutableStateOf("") }
    var isNegotiable by remember { mutableStateOf(true) }
    var areaText by remember { mutableStateOf("") }
    var dimensions by remember { mutableStateOf("") }
    var floorNumber by remember { mutableStateOf("") }
    var villageArea by remember { mutableStateOf("أولاد صقر - المدينة") }
    var addressDetails by remember { mutableStateOf("") }
    var mapUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var dealType by remember { mutableStateOf(DealType.SALE.titleArabic) }

    // Multiple Photos State
    val photosList = remember {
        mutableStateListOf(
            "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
            "https://images.unsplash.com/photo-1600585154340-be6161a56a0c"
        )
    }
    var newPhotoUrlInput by remember { mutableStateOf("") }
    var showAddPhotoInput by remember { mutableStateOf(false) }

    var isSubmittedSuccess by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var villageDropdownExpanded by remember { mutableStateOf(false) }

    val categories = PropertyCategory.entries
    val villages = awladSaqrVillages.filter { it != "الكل" }

    val presetSamplePhotos = listOf(
        "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9" to "واجهة منزل مدرن",
        "https://images.unsplash.com/photo-1512917774080-9991f1c4c750" to "فيلا / منزل عائلي",
        "https://images.unsplash.com/photo-1500382017468-9049fed747ef" to "أرض مباني / زراعية",
        "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2" to "شقة تمليك تشطيب فاخر"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkEmerald,
        topBar = {
            TopAppBar(
                title = { Text("إضافة عقار جديد", fontWeight = FontWeight.Bold, color = SoftGold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkEmeraldCard)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Workflow Info Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = DarkEmeraldCard,
                border = BorderStroke(1.dp, RoyalGold)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "تنبيه المراجعة", tint = SoftGold, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ملاحظة: سيتم إرسال العقار بحالة (قيد المراجعة) ولن يُنشر للعامة إلا بعد مراجعته والموافقة عليه من الإدارة (أ/ عبدالرحمن).",
                        fontSize = 12.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isSubmittedSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                    border = BorderStroke(2.dp, RoyalGold)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "نجاح", tint = RoyalGold, modifier = Modifier.size(54.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("تم إرسال العقار بنجاح!", color = SoftGold, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "العقار الآن بحالة (قيد المراجعة). سيقوم أ/ عبدالرحمن بمراجعته ونشره فوراً.",
                            color = TextWhite,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("العودة للرئيسية", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // Main Input Form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                    border = BorderStroke(1.dp, DarkGold)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("معلومات العقار الرئيسية:", fontWeight = FontWeight.Black, color = SoftGold, fontSize = 16.sp)

                        // Title
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("عنوان العقار (مثال: منزل دورين أو قطعة أرض ممتازة)", color = SoftGold) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalGold,
                                unfocusedBorderColor = DarkGold,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        // Category Dropdown
                        ExposedDropdownMenuBox(
                            expanded = categoryDropdownExpanded,
                            onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory.titleArabic,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("تصنيف العقار", color = SoftGold) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = categoryDropdownExpanded,
                                onDismissRequest = { categoryDropdownExpanded = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.titleArabic) },
                                        onClick = {
                                            selectedCategory = cat
                                            categoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Village Area Dropdown
                        ExposedDropdownMenuBox(
                            expanded = villageDropdownExpanded,
                            onExpandedChange = { villageDropdownExpanded = !villageDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = villageArea,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("المنطقة / القرية في أولاد صقر", color = SoftGold) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = villageDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = villageDropdownExpanded,
                                onDismissRequest = { villageDropdownExpanded = false }
                            ) {
                                villages.forEach { vil ->
                                    DropdownMenuItem(
                                        text = { Text(vil) },
                                        onClick = {
                                            villageArea = vil
                                            villageDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // -------------------------------------------------------------
                        // Price & Negotiation Toggle (Requirement 1)
                        // -------------------------------------------------------------
                        Column {
                            Text("السعر وحالة التفاوض:", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("السعر المطلوب (بالجنيه المصري)", color = SoftGold) },
                                leadingIcon = { Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "السعر", tint = RoyalGold) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Price & Negotiation Selector Chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    onClick = { isNegotiable = true },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isNegotiable) StatusApproved else DarkEmerald,
                                    border = BorderStroke(1.5.dp, if (isNegotiable) Color.White else DarkGold)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "🟢 قابل للتفاوض",
                                            color = if (isNegotiable) Color.White else TextWhite,
                                            fontWeight = if (isNegotiable) FontWeight.Black else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Surface(
                                    onClick = { isNegotiable = false },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (!isNegotiable) StatusRejected else DarkEmerald,
                                    border = BorderStroke(1.5.dp, if (!isNegotiable) Color.White else DarkGold)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "🔴 غير قابل للتفاوض",
                                            color = if (!isNegotiable) Color.White else TextWhite,
                                            fontWeight = if (!isNegotiable) FontWeight.Black else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Area & Dimensions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = areaText,
                                onValueChange = { areaText = it },
                                label = { Text("المساحة (م²)", color = SoftGold) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            OutlinedTextField(
                                value = dimensions,
                                onValueChange = { dimensions = it },
                                label = { Text("الأبعاد (مثال: 10م x 15م)", color = SoftGold) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )
                        }

                        // Description
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("وصف العقار والمرافق بالتفصيل", color = SoftGold) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalGold,
                                unfocusedBorderColor = DarkGold,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        // -------------------------------------------------------------
                        // Photo Upload Field (Requirement 1 - Multiple Images)
                        // -------------------------------------------------------------
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = "صور", tint = SoftGold, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("صور العقار (${photosList.size} صور مضافة)", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 14.sp)
                                }

                                TextButton(onClick = { showAddPhotoInput = !showAddPhotoInput }) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة صورة", tint = RoyalGold, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("إضافة صورة", color = RoyalGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Horizontal Thumbnail Carousel with Delete Icon
                            if (photosList.isNotEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    itemsIndexed(photosList) { idx, photoUrl ->
                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .border(1.dp, DarkGold, RoundedCornerShape(12.dp))
                                        ) {
                                            AsyncImage(
                                                model = photoUrl,
                                                contentDescription = "صورة العقار $idx",
                                                modifier = Modifier.matchParentSize(),
                                                contentScale = ContentScale.Crop
                                            )

                                            // Delete Icon
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(4.dp)
                                                    .size(22.dp)
                                                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                                                    .clickable { photosList.removeAt(idx) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(imageVector = Icons.Default.Close, contentDescription = "حذف", tint = Color.White, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            // Quick Preset Sample Photos Chips
                            Text("نماذج صور تجهيزية سريعة:", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(4.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                itemsIndexed(presetSamplePhotos) { _, pair ->
                                    Surface(
                                        onClick = { photosList.add(pair.first) },
                                        shape = RoundedCornerShape(8.dp),
                                        color = DarkEmerald,
                                        border = BorderStroke(1.dp, DarkGold)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = "إضافة", tint = SoftGold, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(pair.second, fontSize = 11.sp, color = TextWhite)
                                        }
                                    }
                                }
                            }

                            if (showAddPhotoInput) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = newPhotoUrlInput,
                                    onValueChange = { newPhotoUrlInput = it },
                                    label = { Text("أدخل رابط صورة (URL) جديدة", color = SoftGold) },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            if (newPhotoUrlInput.isNotBlank()) {
                                                photosList.add(newPhotoUrlInput.trim())
                                                newPhotoUrlInput = ""
                                                showAddPhotoInput = false
                                            }
                                        }) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "إضافة", tint = RoyalGold)
                                        }
                                    },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RoyalGold,
                                        unfocusedBorderColor = DarkGold,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite
                                    )
                                )
                            }
                        }

                        // -------------------------------------------------------------
                        // Video Upload Field (Requirement 1)
                        // -------------------------------------------------------------
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = "فيديو", tint = SoftGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("فيديو العقار والمعاينة:", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = videoUrl,
                                onValueChange = { videoUrl = it },
                                label = { Text("رابط الفيديو (YouTube / Drive / MP4 clip)", color = SoftGold) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                TextButton(onClick = {
                                    videoUrl = "https://www.youtube.com/watch?v=sample_awlad_saqr"
                                }) {
                                    Text("💡 إدراج نموذج فيديو توضيحي للمعاينة", fontSize = 11.sp, color = RoyalGold)
                                }
                            }
                        }

                        // -------------------------------------------------------------
                        // Google Maps Location Picker (Requirement 1)
                        // -------------------------------------------------------------
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Map, contentDescription = "خرائط جوجل", tint = SoftGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("موقع خريطة جوجل Google Maps:", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = addressDetails,
                                onValueChange = { addressDetails = it },
                                label = { Text("تفاصيل العنوان الدقيق والشارع", color = SoftGold) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = mapUrl,
                                onValueChange = { mapUrl = it },
                                label = { Text("رابط الموقع من Google Maps أو إحداثيات Pin", color = SoftGold) },
                                leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "موقع", tint = RoyalGold) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedButton(
                                onClick = {
                                    mapUrl = "https://maps.google.com/?q=Awlad+Saqr+$villageArea"
                                },
                                border = BorderStroke(1.dp, RoyalGold),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "تحديد موقع", tint = RoyalGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("📍 إدراج موقع الدبوس تلقائياً في منطقة ($villageArea)", fontSize = 11.sp, color = SoftGold, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                val price = priceText.toDoubleOrNull() ?: 0.0
                                val area = areaText.toDoubleOrNull() ?: 100.0
                                val combinedPhotos = if (photosList.isNotEmpty()) photosList.joinToString(",") else "https://images.unsplash.com/photo-1580587771525-78b9dba3b914"
                                val finalAddress = if (addressDetails.isNotBlank()) addressDetails else "أولاد صقر - $villageArea"

                                val newProp = Property(
                                    title = if (title.isNotBlank()) title else "عقار جديد بأولاد صقر",
                                    categoryId = selectedCategory.id,
                                    priceEgp = price,
                                    isNegotiable = isNegotiable,
                                    areaSqm = area,
                                    dimensions = dimensions,
                                    floorNumber = floorNumber,
                                    dealType = dealType,
                                    villageArea = villageArea,
                                    addressDetails = if (mapUrl.isNotBlank()) "$finalAddress ($mapUrl)" else finalAddress,
                                    description = if (description.isNotBlank()) description else "عقار متميز معروض للبيع أو الإيجار في أولاد صقر.",
                                    imageUrls = combinedPhotos,
                                    videoUrl = videoUrl,
                                    status = PropertyStatus.PENDING.titleArabic
                                )
                                onSubmitProperty(newProp)
                                isSubmittedSuccess = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("إرسال العقار للمراجعة 🚀", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
