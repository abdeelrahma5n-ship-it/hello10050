package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Property
import com.example.data.model.PropertyCategory
import com.example.data.model.User
import com.example.ui.components.ComparisonFloatingBar
import com.example.ui.components.PropertyCard
import com.example.ui.components.PropertyComparisonDialog
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldElevated
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

val searchPostingDateOptions = listOf("الكل", "اليوم", "هذا الأسبوع", "هذا الشهر")
val searchAreaUnits = listOf("م²", "قيراط", "فدان")

@Composable
fun SearchScreen(
    searchQuery: String,
    selectedCategoryId: String?,
    selectedVillage: String,
    selectedDealType: String,
    selectedSortOption: String,
    minPrice: Double?,
    maxPrice: Double?,
    minArea: Double?,
    maxArea: Double?,
    areaUnit: String,
    postingDateFilter: String,
    comparedPropertyIds: Set<Long>,
    comparedProperties: List<Property>,
    approvedProperties: List<Property>,
    isSeniorMode: Boolean,
    isGuestMode: Boolean,
    currentUser: User?,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String?) -> Unit,
    onSelectVillage: (String) -> Unit,
    onSelectDealType: (String) -> Unit,
    onSelectSortOption: (String) -> Unit,
    onApplyAdvancedFilters: (
        minPrice: Double?,
        maxPrice: Double?,
        minArea: Double?,
        maxArea: Double?,
        areaUnit: String,
        village: String,
        categoryId: String?,
        postingDate: String
    ) -> Unit,
    onResetFilters: () -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onToggleCompareProperty: (Long) -> Boolean,
    onRemoveFromCompare: (Long) -> Unit,
    onClearComparison: () -> Unit,
    onOpenPropertyDetail: (Long) -> Unit,
    onOpenAiAssistantWithQuery: (String) -> Unit,
    onTrackCallClick: () -> Unit = {},
    onTrackWhatsappClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val titleFontSize = if (isSeniorMode) 22.sp else 18.sp
    val labelFontSize = if (isSeniorMode) 15.sp else 13.sp

    // Filter Panel Expand state
    var isFilterExpanded by remember { mutableStateOf(true) }

    // Internal Form States for Filter Panel
    var minPriceInput by remember { mutableStateOf(minPrice?.toLong()?.toString() ?: "") }
    var maxPriceInput by remember { mutableStateOf(maxPrice?.toLong()?.toString() ?: "") }
    var minAreaInput by remember { mutableStateOf(minArea?.toLong()?.toString() ?: "") }
    var maxAreaInput by remember { mutableStateOf(maxArea?.toLong()?.toString() ?: "") }
    var selectedUnitState by remember { mutableStateOf(if (searchAreaUnits.contains(areaUnit)) areaUnit else "م²") }
    var selectedVillageState by remember { mutableStateOf(selectedVillage) }
    var selectedCategoryState by remember { mutableStateOf(selectedCategoryId) }
    var selectedPostingDateState by remember { mutableStateOf(postingDateFilter) }

    var villageDropdownExpanded by remember { mutableStateOf(false) }
    var unitDropdownExpanded by remember { mutableStateOf(false) }

    var showComparisonModal by remember { mutableStateOf(false) }

    val categoriesList = PropertyCategory.entries

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmerald)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { Spacer(modifier = Modifier.height(10.dp)) }

            // 1. Search Screen Header Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = RoyalGold,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "البحث العقاري الشامل 🔍",
                                fontSize = titleFontSize,
                                fontWeight = FontWeight.Black,
                                color = SoftGold
                            )
                            Text(
                                text = "محرك بحث متطور لعقارات وأراضي أولاد صقر",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    // Toggle Filter Panel Button
                    Surface(
                        onClick = { isFilterExpanded = !isFilterExpanded },
                        shape = RoundedCornerShape(12.dp),
                        color = DarkEmeraldCard,
                        border = BorderStroke(1.dp, RoyalGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "تعديل الفلاتر",
                                tint = SoftGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isFilterExpanded) "إخفاء الفلتر" else "الفلتر الشامل",
                                fontSize = 12.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 2. Main Search Bar Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("ابحث باسم القرية، الشارع، أو نوع العقار...", fontSize = 13.sp, color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث", tint = SoftGold) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "مسح", tint = TextWhite)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoyalGold,
                        unfocusedBorderColor = DarkGold,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = DarkEmeraldCard,
                        unfocusedContainerColor = DarkEmeraldCard
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // 3. Expandable Comprehensive Filter Panel
            item {
                AnimatedVisibility(
                    visible = isFilterExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = DarkEmeraldCard,
                        border = BorderStroke(1.dp, RoyalGold)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "🎛️ خيارات الفلترة المتقدمة",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftGold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // A) Property Type Filter Chips
                            Text(
                                text = "1️⃣ نوع العقار والتصنيف:",
                                fontSize = labelFontSize,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val isAllSelected = selectedCategoryState == null
                                Surface(
                                    onClick = { selectedCategoryState = null },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isAllSelected) RoyalGold else DarkEmeraldElevated,
                                    border = BorderStroke(1.dp, if (isAllSelected) RoyalGold else DarkGold)
                                ) {
                                    Text(
                                        text = "الكل",
                                        color = if (isAllSelected) Color.Black else TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                categoriesList.forEach { cat ->
                                    val isSelected = selectedCategoryState == cat.id
                                    Surface(
                                        onClick = { selectedCategoryState = cat.id },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) RoyalGold else DarkEmeraldElevated,
                                        border = BorderStroke(1.dp, if (isSelected) RoyalGold else DarkGold)
                                    ) {
                                        Text(
                                            text = cat.titleArabic,
                                            color = if (isSelected) Color.Black else TextWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // B) Price Range Filter (EGP)
                            Text(
                                text = "2️⃣ نطاق السعر (جنيه مصري):",
                                fontSize = labelFontSize,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = minPriceInput,
                                    onValueChange = { minPriceInput = it.filter { c -> c.isDigit() } },
                                    label = { Text("السعر الأدنى", fontSize = 11.sp) },
                                    placeholder = { Text("مثلاً 200000", fontSize = 10.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RoyalGold,
                                        unfocusedBorderColor = DarkGold,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        focusedLabelColor = SoftGold,
                                        unfocusedLabelColor = TextMuted
                                    ),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                OutlinedTextField(
                                    value = maxPriceInput,
                                    onValueChange = { maxPriceInput = it.filter { c -> c.isDigit() } },
                                    label = { Text("السعر الأعلى", fontSize = 11.sp) },
                                    placeholder = { Text("مثلاً 1500000", fontSize = 10.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RoyalGold,
                                        unfocusedBorderColor = DarkGold,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        focusedLabelColor = SoftGold,
                                        unfocusedLabelColor = TextMuted
                                    ),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // C) Area Size Filter + Unit
                            Text(
                                text = "3️⃣ المساحة ووحدة القياس:",
                                fontSize = labelFontSize,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = minAreaInput,
                                    onValueChange = { minAreaInput = it.filter { c -> c.isDigit() } },
                                    label = { Text("أقل مساحة", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RoyalGold,
                                        unfocusedBorderColor = DarkGold,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        focusedLabelColor = SoftGold,
                                        unfocusedLabelColor = TextMuted
                                    ),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                OutlinedTextField(
                                    value = maxAreaInput,
                                    onValueChange = { maxAreaInput = it.filter { c -> c.isDigit() } },
                                    label = { Text("أعلى مساحة", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RoyalGold,
                                        unfocusedBorderColor = DarkGold,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        focusedLabelColor = SoftGold,
                                        unfocusedLabelColor = TextMuted
                                    ),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                // Unit Selector Dropdown
                                Box {
                                    Surface(
                                        onClick = { unitDropdownExpanded = true },
                                        shape = RoundedCornerShape(12.dp),
                                        color = DarkEmeraldElevated,
                                        border = BorderStroke(1.dp, RoyalGold),
                                        modifier = Modifier.height(56.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(selectedUnitState, color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SoftGold)
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = unitDropdownExpanded,
                                        onDismissRequest = { unitDropdownExpanded = false },
                                        modifier = Modifier.background(DarkEmeraldCard)
                                    ) {
                                        searchAreaUnits.forEach { unit ->
                                            DropdownMenuItem(
                                                text = { Text(unit, color = TextWhite) },
                                                onClick = {
                                                    selectedUnitState = unit
                                                    unitDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // D) Location / Village Filter
                            Text(
                                text = "4️⃣ القرية / المنطقة بمركز أولاد صقر:",
                                fontSize = labelFontSize,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    onClick = { villageDropdownExpanded = true },
                                    shape = RoundedCornerShape(12.dp),
                                    color = DarkEmeraldElevated,
                                    border = BorderStroke(1.dp, RoyalGold),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = selectedVillageState,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SoftGold)
                                    }
                                }

                                DropdownMenu(
                                    expanded = villageDropdownExpanded,
                                    onDismissRequest = { villageDropdownExpanded = false },
                                    modifier = Modifier.background(DarkEmeraldCard)
                                ) {
                                    awladSaqrVillages.forEach { village ->
                                        DropdownMenuItem(
                                            text = { Text(village, color = TextWhite) },
                                            onClick = {
                                                selectedVillageState = village
                                                villageDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // E) Posting Date Filter
                            Text(
                                text = "5️⃣ تاريخ النشر الإدراج:",
                                fontSize = labelFontSize,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                searchPostingDateOptions.forEach { dateOpt ->
                                    val isSelected = selectedPostingDateState == dateOpt
                                    Surface(
                                        onClick = { selectedPostingDateState = dateOpt },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) RoyalGold else DarkEmeraldElevated,
                                        border = BorderStroke(1.dp, DarkGold)
                                    ) {
                                        Text(
                                            text = dateOpt,
                                            color = if (isSelected) Color.Black else TextWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // F) Action Buttons (Apply & Reset)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        minPriceInput = ""
                                        maxPriceInput = ""
                                        minAreaInput = ""
                                        maxAreaInput = ""
                                        selectedUnitState = "م²"
                                        selectedVillageState = "الكل"
                                        selectedCategoryState = null
                                        selectedPostingDateState = "الكل"
                                        onSelectVillage("الكل")
                                        onSelectCategory(null)
                                        onResetFilters()
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftGold),
                                    border = BorderStroke(1.dp, DarkGold),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("إعادة ضبط", fontSize = 12.sp)
                                    }
                                }

                                Button(
                                    onClick = {
                                        val minP = minPriceInput.toDoubleOrNull()
                                        val maxP = maxPriceInput.toDoubleOrNull()

                                        val rawMinA = minAreaInput.toDoubleOrNull()
                                        val rawMaxA = maxAreaInput.toDoubleOrNull()

                                        val multiplier = when (selectedUnitState) {
                                            "قيراط" -> 175.0
                                            "فدان" -> 4200.0
                                            else -> 1.0
                                        }

                                        val minA = if (rawMinA != null) rawMinA * multiplier else null
                                        val maxA = if (rawMaxA != null) rawMaxA * multiplier else null

                                        onSelectVillage(selectedVillageState)
                                        onSelectCategory(selectedCategoryState)
                                        onApplyAdvancedFilters(
                                            minP, maxP,
                                            minA, maxA,
                                            selectedUnitState,
                                            selectedVillageState,
                                            selectedCategoryState,
                                            selectedPostingDateState
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1.4f)
                                ) {
                                    Text(
                                        text = "تطبيق الفلتر 🚀",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Dedicated AI Assistant Shortcut Banner Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = DarkEmeraldElevated,
                    border = BorderStroke(1.5.dp, SoftGold)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = RoyalGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "مساعد سمسارك العقاري ✨",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SoftGold
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = RoyalGold.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, RoyalGold)
                            ) {
                                Text(
                                    text = "الذكاء الاصطناعي",
                                    color = SoftGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "مستشارك العقاري الذكي لأسعار ومساحات وحالة كردون المباني بأولاد صقر، ومتابعة صفحة الفيسبوك الرسمية.",
                            fontSize = 12.sp,
                            color = TextWhite,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Suggested Prompt Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val suggestions = listOf(
                                "أسعار الأراضي الكردون 🏛️",
                                "شقة إيجار بأولاد صقر 🏢",
                                "الفرق بين الكردون والخارج 📜",
                                "صفحة الفيسبوك للتواصل 📘",
                                "التواصل المباشر مع أ/ عبدالرحمن 📞"
                            )

                            suggestions.forEach { chipText ->
                                Surface(
                                    onClick = { onOpenAiAssistantWithQuery(chipText) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = DarkEmeraldCard,
                                    border = BorderStroke(1.dp, DarkGold)
                                ) {
                                    Text(
                                        text = chipText,
                                        color = SoftGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Section Header for Filtered Properties
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نتائج البحث المتاحة",
                        fontSize = titleFontSize,
                        fontWeight = FontWeight.Black,
                        color = SoftGold
                    )
                    Text(
                        text = "(${approvedProperties.size} عقار مطابِق)",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            // 6. Filtered Property Cards Stream
            if (approvedProperties.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "لا توجد عقارات تطابق شروط الفلترة المحددة.",
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "اضغط على (إعادة ضبط) للبحث مجدداً في كافة عقارات أولاد صقر.",
                                color = TextMuted,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(approvedProperties, key = { it.id }) { prop ->
                    PropertyCard(
                        property = prop,
                        isSeniorMode = isSeniorMode,
                        isCompared = comparedPropertyIds.contains(prop.id),
                        onToggleFavorite = { id, current -> onToggleFavorite(id, current) },
                        onToggleCompare = { id ->
                            val success = onToggleCompareProperty(id)
                            if (!success) {
                                Toast.makeText(context, "يمكنك مقارنة 3 عقارات كحد أقصى ⚖️", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onClickDetail = onOpenPropertyDetail,
                        onTrackCallClick = onTrackCallClick,
                        onTrackWhatsappClick = onTrackWhatsappClick
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(90.dp)) }
        }

        // Floating Comparison Bar overlay
        ComparisonFloatingBar(
            comparedProperties = comparedProperties,
            isSeniorMode = isSeniorMode,
            onOpenComparisonModal = { showComparisonModal = true },
            onClearAll = onClearComparison,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )

        // Side-by-Side Property Comparison Dialog Table
        if (showComparisonModal) {
            PropertyComparisonDialog(
                comparedProperties = comparedProperties,
                isSeniorMode = isSeniorMode,
                onRemoveProperty = { onRemoveFromCompare(it) },
                onClearAll = {
                    onClearComparison()
                    showComparisonModal = false
                },
                onDismiss = { showComparisonModal = false },
                onTrackCallClick = onTrackCallClick,
                onTrackWhatsappClick = onTrackWhatsappClick
            )
        }
    }
}
