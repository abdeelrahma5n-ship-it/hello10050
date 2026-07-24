package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DealType
import com.example.data.model.Property
import com.example.data.model.User
import com.example.ui.components.AdvancedFilterDialog
import com.example.ui.components.BottomHomeBanner
import com.example.ui.components.CategoriesGrid
import com.example.ui.components.ComparisonFloatingBar
import com.example.ui.components.FloatingAiAssistant
import com.example.ui.components.HeaderBanner
import com.example.ui.components.PropertyCard
import com.example.ui.components.PropertyComparisonDialog
import com.example.ui.components.SemsarkBottomBar
import com.example.ui.components.SemsarkTab
import com.example.ui.components.SocialContactRow
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.ChatMessage

val awladSaqrVillages = listOf(
    "الكل (أولاد صقر)",
    "مدينة أولاد صقر (المركز)",
    "قصاصين الأزهار",
    "الصوفية",
    "تلراك",
    "بني حسن",
    "الفدادنة",
    "جزيرة الشافعي",
    "الزور أسطال"
)

val sortOptionsList = listOf(
    "الأحدث أولاً",
    "السعر: من الأقل للأعلى",
    "السعر: من الأعلى للأقل",
    "المساحة: الأكبر أولاً"
)

@Composable
fun HomeScreen(
    isSeniorMode: Boolean,
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
    chatMessages: List<ChatMessage>,
    officeChatMessages: List<ChatMessage> = emptyList(),
    groupChatMessages: List<ChatMessage> = emptyList(),
    isAiThinking: Boolean,
    currentUser: User?,
    isGuestMode: Boolean,
    isAdminLoggedIn: Boolean = false,
    initialChatTab: Int = 0,
    onToggleSeniorMode: () -> Unit,
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
    onToggleCompareProperty: (Long) -> Unit,
    onRemoveFromCompare: (Long) -> Unit,
    onClearComparison: () -> Unit,
    onSendMessageToAi: (String) -> Unit,
    onSendOfficeMessage: (String) -> Unit = {},
    onSendGroupMessage: (text: String, mediaUrl: String?, mediaType: String?, isAnnouncement: Boolean) -> Unit = { _, _, _, _ -> },
    onOpenAddProperty: () -> Unit,
    onOpenPropertyDetail: (Long) -> Unit,
    onOpenAboutDeveloper: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onOpenAuthModal: () -> Unit,
    onOpenFavorites: () -> Unit,
    onTrackCallClick: () -> Unit = {},
    onTrackWhatsappClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(SemsarkTab.HOME) }
    var showAdvancedFilterModal by remember { mutableStateOf(false) }
    var showComparisonModal by remember { mutableStateOf(false) }
    var openAiChatExternally by remember { mutableStateOf(false) }
    var externalChatTab by remember { mutableStateOf(initialChatTab) }

    // Filter Logic
    val filteredList = approvedProperties.filter { prop ->
        val matchesQuery = searchQuery.isBlank() ||
                prop.title.contains(searchQuery, ignoreCase = true) ||
                prop.description.contains(searchQuery, ignoreCase = true) ||
                prop.villageArea.contains(searchQuery, ignoreCase = true) ||
                prop.addressDetails.contains(searchQuery, ignoreCase = true)

        val matchesCategory = selectedCategoryId == null || prop.categoryId == selectedCategoryId
        val matchesVillage = selectedVillage == "الكل (أولاد صقر)" || selectedVillage == "الكل" || prop.villageArea == selectedVillage
        val matchesDealType = selectedDealType == "الكل" || prop.dealType == selectedDealType

        val matchesMinPrice = minPrice == null || prop.priceEgp >= minPrice
        val matchesMaxPrice = maxPrice == null || prop.priceEgp <= maxPrice

        val matchesMinArea = minArea == null || prop.areaSqm >= minArea
        val matchesMaxArea = maxArea == null || prop.areaSqm <= maxArea

        matchesQuery && matchesCategory && matchesVillage && matchesDealType &&
                matchesMinPrice && matchesMaxPrice && matchesMinArea && matchesMaxArea
    }.sortedWith { a, b ->
        when (selectedSortOption) {
            "السعر: من الأقل للأعلى" -> a.priceEgp.compareTo(b.priceEgp)
            "السعر: من الأعلى للأقل" -> b.priceEgp.compareTo(a.priceEgp)
            "المساحة: الأكبر أولاً" -> b.areaSqm.compareTo(a.areaSqm)
            else -> b.createdAt.compareTo(a.createdAt)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkEmerald,
        bottomBar = {
            SemsarkBottomBar(
                currentTab = activeTab,
                onTabSelected = { tab ->
                    activeTab = tab
                    when (tab) {
                        SemsarkTab.FAVORITES -> onOpenFavorites()
                        SemsarkTab.PROFILE -> onOpenAuthModal()
                        SemsarkTab.CHAT -> {
                            externalChatTab = 0
                            openAiChatExternally = true
                        }
                        else -> {}
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Floating Add Property Button
                FloatingActionButton(
                    onClick = onOpenAddProperty,
                    containerColor = RoyalGold,
                    contentColor = Color.Black,
                    shape = CircleShape,
                    modifier = Modifier.shadow(8.dp, CircleShape)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "إضافة عقار")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("أضف عقارك", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // AI Assistant
                FloatingAiAssistant(
                    officeChatMessages = officeChatMessages,
                    groupChatMessages = groupChatMessages,
                    chatMessages = chatMessages,
                    isAiThinking = isAiThinking,
                    currentUser = currentUser,
                    isGuestMode = isGuestMode,
                    isAdminLoggedIn = isAdminLoggedIn,
                    onSendOfficeMessage = onSendOfficeMessage,
                    onSendGroupMessage = onSendGroupMessage,
                    onSendMessage = onSendMessageToAi,
                    onOpenAuthModal = onOpenAuthModal,
                    isOpenExternally = openAiChatExternally,
                    initialTab = externalChatTab,
                    onDismissExternal = { openAiChatExternally = false }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. Header Emblem Banner
                item {
                    HeaderBanner(
                        isSeniorMode = isSeniorMode,
                        onToggleSeniorMode = onToggleSeniorMode,
                        onOpenAboutDeveloper = onOpenAboutDeveloper,
                        onOpenAdminDashboard = onOpenAdminDashboard,
                        onOpenAuthModal = onOpenAuthModal,
                        onTrackCallClick = onTrackCallClick,
                        onTrackWhatsappClick = onTrackWhatsappClick
                    )
                }

                // 2. 3D Categories Grid (6 Cards 3x2)
                item {
                    CategoriesGrid(
                        selectedCategoryId = selectedCategoryId,
                        isSeniorMode = isSeniorMode,
                        onSelectCategory = onSelectCategory
                    )
                }

                // 3. Bottom Metallic Gold Home Banner
                item {
                    BottomHomeBanner(isSeniorMode = isSeniorMode)
                }

                // 4. Social Contacts Matrix Row
                item {
                    SocialContactRow(
                        onTrackCallClick = onTrackCallClick,
                        onTrackWhatsappClick = onTrackWhatsappClick
                    )
                }

                // 5. Search Bar & Advanced Filter Trigger
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChange,
                                placeholder = { Text("ابحث عن منزل، أرض، شقة...", fontSize = 13.sp, color = TextMuted) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث", tint = SoftGold) },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { onSearchQueryChange("") }) {
                                            Icon(Icons.Default.Clear, contentDescription = "مسح", tint = TextWhite)
                                        }
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedContainerColor = DarkEmeraldCard,
                                    unfocusedContainerColor = DarkEmeraldCard
                                )
                            )

                            // Advanced Filter Button
                            Surface(
                                onClick = { showAdvancedFilterModal = true },
                                shape = RoundedCornerShape(16.dp),
                                color = DarkEmeraldCard,
                                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                                modifier = Modifier.size(52.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "تصفية متقدمة",
                                        tint = SoftGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        // Horizontal Deal Type Selector
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                val isSelected = selectedDealType == "الكل"
                                Surface(
                                    onClick = { onSelectDealType("الكل") },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) RoyalGold else DarkEmeraldCard,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                                ) {
                                    Text(
                                        text = "الكل",
                                        color = if (isSelected) Color.Black else TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            items(DealType.entries.toTypedArray()) { deal ->
                                val isSelected = selectedDealType == deal.titleArabic
                                Surface(
                                    onClick = { onSelectDealType(deal.titleArabic) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) RoyalGold else DarkEmeraldCard,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                                ) {
                                    Text(
                                        text = deal.titleArabic,
                                        color = if (isSelected) Color.Black else TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 6. Listings Header Counter
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "العروض المتاحة (${filteredList.size})",
                            fontSize = if (isSeniorMode) 20.sp else 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftGold
                        )

                        if (minPrice != null || maxPrice != null || minArea != null || selectedCategoryId != null || selectedVillage != "الكل (أولاد صقر)") {
                            Surface(
                                onClick = onResetFilters,
                                shape = RoundedCornerShape(8.dp),
                                color = DarkEmeraldCard,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
                            ) {
                                Text(
                                    text = "إلغاء الفلاتر",
                                    color = SoftGold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // 7. Property Cards List
                if (filteredList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "لا توجد عقارات تطابق خيارات البحث حالياً",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { prop ->
                        PropertyCard(
                            property = prop,
                            isSeniorMode = isSeniorMode,
                            isCompared = comparedPropertyIds.contains(prop.id),
                            onToggleFavorite = onToggleFavorite,
                            onToggleCompare = onToggleCompareProperty,
                            onClickDetail = onOpenPropertyDetail,
                            onTrackCallClick = onTrackCallClick,
                            onTrackWhatsappClick = onTrackWhatsappClick
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // Comparison Floating Bar
            ComparisonFloatingBar(
                comparedProperties = comparedProperties,
                isSeniorMode = isSeniorMode,
                onOpenComparisonModal = { showComparisonModal = true },
                onClearAll = onClearComparison,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp)
            )
        }
    }

    // Advanced Filter Modal
    if (showAdvancedFilterModal) {
        AdvancedFilterDialog(
            initialMinPrice = minPrice,
            initialMaxPrice = maxPrice,
            initialMinArea = minArea,
            initialMaxArea = maxArea,
            initialAreaUnit = areaUnit,
            initialVillage = selectedVillage,
            initialCategoryId = selectedCategoryId,
            initialPostingDate = postingDateFilter,
            isSeniorMode = isSeniorMode,
            onApplyFilters = { minP, maxP, minA, maxA, unit, vil, cat, date ->
                onSelectVillage(vil)
                onSelectCategory(cat)
                onApplyAdvancedFilters(minP, maxP, minA, maxA, unit, vil, cat, date)
            },
            onResetAll = onResetFilters,
            onDismiss = { showAdvancedFilterModal = false }
        )
    }

    // Comparison Modal
    if (showComparisonModal) {
        PropertyComparisonDialog(
            comparedProperties = comparedProperties,
            isSeniorMode = isSeniorMode,
            onRemoveProperty = onRemoveFromCompare,
            onClearAll = onClearComparison,
            onDismiss = { showComparisonModal = false },
            onTrackCallClick = onTrackCallClick,
            onTrackWhatsappClick = onTrackWhatsappClick
        )
    }
}