package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PropertyCategory
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold

enum class CategoryIconType {
    HOUSE,
    MAP,
    APARTMENT,
    PLANT,
    KEY,
    LANDSCAPE
}

data class Category3DGridItem(
    val category: PropertyCategory,
    val iconType: CategoryIconType
)

val category3DGridItems = listOf(
    // Row 1 (RTL: Right -> Center -> Left)
    Category3DGridItem(PropertyCategory.APARTMENTS_SALE, CategoryIconType.APARTMENT), // Right: شقق تمليك
    Category3DGridItem(PropertyCategory.BUILDING_CORDON, CategoryIconType.MAP),       // Center: أراضي كردون
    Category3DGridItem(PropertyCategory.HOUSES, CategoryIconType.HOUSE),              // Left: منازل

    // Row 2 (RTL: Right -> Center -> Left)
    Category3DGridItem(PropertyCategory.OUTSIDE_CORDON, CategoryIconType.LANDSCAPE),  // Right: أراضي خارج الكردون
    Category3DGridItem(PropertyCategory.APARTMENTS_RENT, CategoryIconType.KEY),        // Center: شقق إيجار
    Category3DGridItem(PropertyCategory.AGRICULTURAL, CategoryIconType.PLANT)         // Left: أراضي زراعية
)

@Composable
fun CategoriesGrid(
    selectedCategoryId: String?,
    isSeniorMode: Boolean,
    onSelectCategory: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardHeight = if (isSeniorMode) 140.dp else 125.dp
    val fontSize = if (isSeniorMode) 19.sp else 18.sp
    val iconSize = if (isSeniorMode) 48.dp else 42.dp

    // Metallic Gold Brush for Text and Highlight Borders
    val metallicGoldTextBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF3C2),
            Color(0xFFE1B23D),
            Color(0xFF8A5A00)
        )
    )

    val cardBorderBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF4AF),
            Color(0xFFDDAA2E),
            Color(0xFFA76C00)
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Clear Filter Button
        if (selectedCategoryId != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFFFF4AF), Color(0xFFDDAA2E), Color(0xFFA76C00))
                        )
                    )
                    .clickable { onSelectCategory(null) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "عرض جميع الأقسام",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
            }
        }

        // 3 Columns x 2 Rows Grid with 18dp Spacing
        val rows = category3DGridItems.chunked(3)
        for (rowItems in rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                for (item in rowItems) {
                    val isSelected = selectedCategoryId == item.category.id
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(cardHeight)
                            .shadow(
                                elevation = if (isSelected) 12.dp else 8.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = Color.Black.copy(alpha = 0.6f)
                            )
                            .clickable { onSelectCategory(item.category.id) },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) RoyalGold else Color(0xFF01351F)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            brush = if (isSelected) Brush.linearGradient(listOf(SoftGold, RoyalGold)) else cardBorderBrush
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier.size(iconSize),
                                contentAlignment = Alignment.Center
                            ) {
                                when (item.iconType) {
                                    CategoryIconType.HOUSE -> GoldHouse3DIcon(size = iconSize)
                                    CategoryIconType.MAP -> GoldMap3DIcon(size = iconSize)
                                    CategoryIconType.APARTMENT -> GoldApartment3DIcon(size = iconSize)
                                    CategoryIconType.PLANT -> GoldAgriculture3DIcon(size = iconSize)
                                    CategoryIconType.KEY -> GoldRentKey3DIcon(size = iconSize)
                                    CategoryIconType.LANDSCAPE -> GoldOutsideCordon3DIcon(size = iconSize)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.category.titleArabic,
                                style = TextStyle(
                                    brush = if (isSelected) Brush.linearGradient(listOf(Color.Black, Color.DarkGray)) else metallicGoldTextBrush,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    shadow = if (isSelected) null else Shadow(
                                        color = Color.Black.copy(alpha = 0.8f),
                                        offset = Offset(1f, 2f),
                                        blurRadius = 3f
                                    )
                                ),
                                maxLines = 2,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}