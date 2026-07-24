package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PropertyCategory
import com.example.ui.screens.awladSaqrVillages
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun UnifiedTopFilterBar(
    searchQuery: String,
    selectedVillage: String,
    selectedCategoryId: String?,
    selectedDealType: String,
    selectedSortOption: String,
    isSeniorMode: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSelectVillage: (String) -> Unit,
    onSelectCategory: (String?) -> Unit,
    onSelectDealType: (String) -> Unit,
    onSelectSortOption: (String) -> Unit,
    onResetFilters: () -> Unit,
    onOpenAdvancedFilters: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var villageDropdownExpanded by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var sortDropdownExpanded by remember { mutableStateOf(false) }

    val categoriesList = PropertyCategory.entries
    val activeCategoryName = categoriesList.find { it.id == selectedCategoryId }?.titleArabic ?: "جميع الأقسام"

    val isFilterActive = searchQuery.isNotEmpty() ||
            selectedVillage != "الكل" ||
            selectedCategoryId != null ||
            selectedDealType != "الكل" ||
            selectedSortOption != "الأحدث"

    val fontSize = if (isSeniorMode) 14.sp else 12.sp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
        border = BorderStroke(1.5.dp, RoyalGold)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. Compact Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = "ابحث بالقرية، السعر، أو الكلمات...",
                        fontSize = fontSize,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = SoftGold,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "مسح",
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = DarkGold,
                    focusedContainerColor = DarkEmerald,
                    unfocusedContainerColor = DarkEmerald,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )

            // 2. Single Horizontal Filter Controls Bar
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 0) Advanced Filters Modal Trigger Button
                item {
                    Surface(
                        onClick = onOpenAdvancedFilters,
                        shape = RoundedCornerShape(12.dp),
                        color = RoyalGold,
                        border = BorderStroke(1.dp, SoftGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "فلترة متقدمة",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "فلترة متقدمة 🔍",
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }
                // A) المنطقة / القرية Dropdown Menu Button
                item {
                    Box {
                        Surface(
                            onClick = { villageDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedVillage != "الكل") RoyalGold else DarkEmerald,
                            border = BorderStroke(1.dp, if (selectedVillage != "الكل") SoftGold else DarkGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "المنطقة",
                                    tint = if (selectedVillage != "الكل") Color.Black else SoftGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (selectedVillage == "الكل") "المنطقة: الكل" else selectedVillage,
                                    fontSize = fontSize,
                                    fontWeight = if (selectedVillage != "الكل") FontWeight.Black else FontWeight.Medium,
                                    color = if (selectedVillage != "الكل") Color.Black else TextWhite
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "سهم",
                                    tint = if (selectedVillage != "الكل") Color.Black else SoftGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = villageDropdownExpanded,
                            onDismissRequest = { villageDropdownExpanded = false },
                            modifier = Modifier.background(DarkEmeraldCard)
                        ) {
                            Text(
                                text = "📍 اختر المنطقة أو القرية:",
                                color = SoftGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                            awladSaqrVillages.forEach { village ->
                                val isSelected = selectedVillage == village
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = village,
                                            color = if (isSelected) RoyalGold else TextWhite,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    },
                                    onClick = {
                                        onSelectVillage(village)
                                        villageDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // B) القسم Dropdown Menu Button
                item {
                    Box {
                        Surface(
                            onClick = { categoryDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedCategoryId != null) RoyalGold else DarkEmerald,
                            border = BorderStroke(1.dp, if (selectedCategoryId != null) SoftGold else DarkGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tag,
                                    contentDescription = "القسم",
                                    tint = if (selectedCategoryId != null) Color.Black else SoftGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (selectedCategoryId == null) "القسم: الكل" else activeCategoryName,
                                    fontSize = fontSize,
                                    fontWeight = if (selectedCategoryId != null) FontWeight.Black else FontWeight.Medium,
                                    color = if (selectedCategoryId != null) Color.Black else TextWhite
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "سهم",
                                    tint = if (selectedCategoryId != null) Color.Black else SoftGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = categoryDropdownExpanded,
                            onDismissRequest = { categoryDropdownExpanded = false },
                            modifier = Modifier.background(DarkEmeraldCard)
                        ) {
                            Text(
                                text = "🏠 اختر تصنيف العقار:",
                                color = SoftGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "الكل (جميع الأقسام)",
                                        color = if (selectedCategoryId == null) RoyalGold else TextWhite,
                                        fontWeight = if (selectedCategoryId == null) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                },
                                onClick = {
                                    onSelectCategory(null)
                                    categoryDropdownExpanded = false
                                }
                            )

                            categoriesList.forEach { cat ->
                                val isSelected = selectedCategoryId == cat.id
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = cat.titleArabic,
                                            color = if (isSelected) RoyalGold else TextWhite,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    },
                                    onClick = {
                                        onSelectCategory(cat.id)
                                        categoryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // C) المعاملة Toggle Chips (الكل / بيع / إيجار)
                item {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkEmerald)
                            .border(1.dp, DarkGold, RoundedCornerShape(12.dp))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("الكل", "بيع", "إيجار").forEach { dealOption ->
                            val isSelected = selectedDealType == dealOption
                            Surface(
                                onClick = { onSelectDealType(dealOption) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) RoyalGold else Color.Transparent
                            ) {
                                Text(
                                    text = dealOption,
                                    fontSize = fontSize,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                    color = if (isSelected) Color.Black else TextWhite,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // D) الترتيب Dropdown Button
                item {
                    Box {
                        Surface(
                            onClick = { sortDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedSortOption != "الأحدث") RoyalGold else DarkEmerald,
                            border = BorderStroke(1.dp, if (selectedSortOption != "الأحدث") SoftGold else DarkGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "الترتيب",
                                    tint = if (selectedSortOption != "الأحدث") Color.Black else SoftGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = selectedSortOption,
                                    fontSize = fontSize,
                                    fontWeight = if (selectedSortOption != "الأحدث") FontWeight.Black else FontWeight.Medium,
                                    color = if (selectedSortOption != "الأحدث") Color.Black else TextWhite
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "سهم",
                                    tint = if (selectedSortOption != "الأحدث") Color.Black else SoftGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = sortDropdownExpanded,
                            onDismissRequest = { sortDropdownExpanded = false },
                            modifier = Modifier.background(DarkEmeraldCard)
                        ) {
                            Text(
                                text = "🔃 ترتيب نتائج البحث:",
                                color = SoftGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                            listOf("الأحدث", "الأقل سعراً", "الأعلى سعراً").forEach { option ->
                                val isSelected = selectedSortOption == option
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option,
                                            color = if (isSelected) RoyalGold else TextWhite,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    },
                                    onClick = {
                                        onSelectSortOption(option)
                                        sortDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // E) Quick Reset Button (If active filters)
                if (isFilterActive) {
                    item {
                        Surface(
                            onClick = onResetFilters,
                            shape = RoundedCornerShape(12.dp),
                            color = StatusRejected.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, StatusRejected)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "إعادة ضبط",
                                    tint = StatusRejected,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "إلغاء الفلاتر ✖",
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusRejected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
