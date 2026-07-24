package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.PropertyCategory
import com.example.ui.screens.awladSaqrVillages
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

val dateOptions = listOf("الكل", "اليوم", "هذا الأسبوع", "هذا الشهر")
val areaUnits = listOf("م²", "قيراط", "فدان")

@Composable
fun AdvancedFilterDialog(
    initialMinPrice: Double?,
    initialMaxPrice: Double?,
    initialMinArea: Double?,
    initialMaxArea: Double?,
    initialAreaUnit: String,
    initialVillage: String,
    initialCategoryId: String?,
    initialPostingDate: String,
    isSeniorMode: Boolean,
    onApplyFilters: (
        minPrice: Double?,
        maxPrice: Double?,
        minArea: Double?,
        maxArea: Double?,
        areaUnit: String,
        village: String,
        categoryId: String?,
        postingDate: String
    ) -> Unit,
    onResetAll: () -> Unit,
    onDismiss: () -> Unit
) {
    var minPriceInput by remember { mutableStateOf(initialMinPrice?.toLong()?.toString() ?: "") }
    var maxPriceInput by remember { mutableStateOf(initialMaxPrice?.toLong()?.toString() ?: "") }

    var minAreaInput by remember { mutableStateOf(initialMinArea?.toLong()?.toString() ?: "") }
    var maxAreaInput by remember { mutableStateOf(initialMaxArea?.toLong()?.toString() ?: "") }
    var selectedUnit by remember { mutableStateOf(if (areaUnits.contains(initialAreaUnit)) initialAreaUnit else "م²") }

    var selectedVillageState by remember { mutableStateOf(initialVillage) }
    var selectedCategoryState by remember { mutableStateOf(initialCategoryId) }
    var selectedPostingDateState by remember { mutableStateOf(initialPostingDate) }

    var villageMenuExpanded by remember { mutableStateOf(false) }
    var unitMenuExpanded by remember { mutableStateOf(false) }

    val categoriesList = PropertyCategory.entries
    val activeCategoryName = categoriesList.find { it.id == selectedCategoryState }?.titleArabic ?: "الكل"

    val titleFontSize = if (isSeniorMode) 20.sp else 16.sp
    val labelFontSize = if (isSeniorMode) 15.sp else 13.sp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = DarkEmerald,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = SoftGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "البحث المتقدم والفلترة الشاملة",
                            fontSize = titleFontSize,
                            fontWeight = FontWeight.Black,
                            color = SoftGold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextWhite)
                    }
                }

                Text(
                    text = "حدد معايير السعر والمساحة والقرية وتاريخ النشر لتصفية عقارات أولاد صقر بلمسة واحدة",
                    fontSize = 12.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Divider(color = DarkGold.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(14.dp))

                // 1) Price Range Filter (Min & Max EGP)
                Text(
                    text = "💰 نطاق السعر (بالجنيه المصري):",
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
                        placeholder = { Text("مثلاً 200000", fontSize = 11.sp) },
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
                        placeholder = { Text("مثلاً 1500000", fontSize = 11.sp) },
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

                // Quick Price Presets
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        onClick = { minPriceInput = ""; maxPriceInput = "500000" },
                        shape = RoundedCornerShape(8.dp),
                        color = DarkEmeraldCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                    ) {
                        Text("< 500 ألف", color = SoftGold, fontSize = 11.sp, modifier = Modifier.padding(6.dp))
                    }
                    Surface(
                        onClick = { minPriceInput = "500000"; maxPriceInput = "1500000" },
                        shape = RoundedCornerShape(8.dp),
                        color = DarkEmeraldCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                    ) {
                        Text("500ك - 1.5M", color = SoftGold, fontSize = 11.sp, modifier = Modifier.padding(6.dp))
                    }
                    Surface(
                        onClick = { minPriceInput = "1500000"; maxPriceInput = "" },
                        shape = RoundedCornerShape(8.dp),
                        color = DarkEmeraldCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                    ) {
                        Text("> 1.5 مليون", color = SoftGold, fontSize = 11.sp, modifier = Modifier.padding(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2) Area Range Filter + Unit Dropdown
                Text(
                    text = "📐 نطاق المساحة ووحدة القياس:",
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

                    // Unit Dropdown
                    Box {
                        Surface(
                            onClick = { unitMenuExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            color = DarkEmeraldCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedUnit, color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SoftGold)
                            }
                        }

                        DropdownMenu(
                            expanded = unitMenuExpanded,
                            onDismissRequest = { unitMenuExpanded = false },
                            modifier = Modifier.background(DarkEmeraldCard)
                        ) {
                            areaUnits.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit, color = TextWhite) },
                                    onClick = {
                                        selectedUnit = unit
                                        unitMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3) Awlad Saqr Location Dropdown
                Text(
                    text = "📍 الموقع والقرية بمركز أولاد صقر:",
                    fontSize = labelFontSize,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        onClick = { villageMenuExpanded = true },
                        shape = RoundedCornerShape(12.dp),
                        color = DarkEmeraldCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedVillageState,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SoftGold)
                        }
                    }

                    DropdownMenu(
                        expanded = villageMenuExpanded,
                        onDismissRequest = { villageMenuExpanded = false },
                        modifier = Modifier.background(DarkEmeraldCard)
                    ) {
                        awladSaqrVillages.forEach { village ->
                            DropdownMenuItem(
                                text = { Text(village, color = TextWhite) },
                                onClick = {
                                    selectedVillageState = village
                                    villageMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4) Property Category Selector
                Text(
                    text = "🏠 تصنيف وقسم العقار:",
                    fontSize = labelFontSize,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val catAllSelected = selectedCategoryState == null
                    Surface(
                        onClick = { selectedCategoryState = null },
                        shape = RoundedCornerShape(10.dp),
                        color = if (catAllSelected) RoyalGold else DarkEmeraldCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                    ) {
                        Text(
                            text = "الكل",
                            color = if (catAllSelected) Color.Black else TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }

                    categoriesList.take(3).forEach { cat ->
                        val isSel = selectedCategoryState == cat.id
                        Surface(
                            onClick = { selectedCategoryState = cat.id },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) RoyalGold else DarkEmeraldCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                        ) {
                            Text(
                                text = cat.titleArabic,
                                color = if (isSel) Color.Black else TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoriesList.drop(3).forEach { cat ->
                        val isSel = selectedCategoryState == cat.id
                        Surface(
                            onClick = { selectedCategoryState = cat.id },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) RoyalGold else DarkEmeraldCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                        ) {
                            Text(
                                text = cat.titleArabic,
                                color = if (isSel) Color.Black else TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5) Posting Date Selector
                Text(
                    text = "📅 تاريخ إدراج العقار:",
                    fontSize = labelFontSize,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    dateOptions.forEach { dateOpt ->
                        val isSel = selectedPostingDateState == dateOpt
                        Surface(
                            onClick = { selectedPostingDateState = dateOpt },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) RoyalGold else DarkEmeraldCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                        ) {
                            Text(
                                text = dateOpt,
                                color = if (isSel) Color.Black else TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            minPriceInput = ""
                            maxPriceInput = ""
                            minAreaInput = ""
                            maxAreaInput = ""
                            selectedUnit = "م²"
                            selectedVillageState = "الكل"
                            selectedCategoryState = null
                            selectedPostingDateState = "الكل"
                            onResetAll()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold),
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

                            // Convert to m² if unit is Qirat or Feddan
                            val multiplier = when (selectedUnit) {
                                "قيراط" -> 175.0
                                "فدان" -> 4200.0
                                else -> 1.0
                            }

                            val minA = if (rawMinA != null) rawMinA * multiplier else null
                            val maxA = if (rawMaxA != null) rawMaxA * multiplier else null

                            onApplyFilters(
                                minP, maxP,
                                minA, maxA,
                                selectedUnit,
                                selectedVillageState,
                                selectedCategoryState,
                                selectedPostingDateState
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.4f)
                    ) {
                        Text(
                            text = "تطبيق الفلترة 🚀",
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
