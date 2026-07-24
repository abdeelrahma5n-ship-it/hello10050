package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.AboutDeveloperScreen
import com.example.ui.screens.AddPropertyScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PropertyDetailScreen
import com.example.ui.theme.SemsarkTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val initialChatTabState = mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleNotificationIntent(intent)
        enableEdgeToEdge()
        setContent {
            SemsarkTheme {
                SemsarkApp(initialChatTab = initialChatTabState.intValue)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        val targetTab = intent?.getStringExtra("target_tab")
        if (targetTab == "GROUP") {
            initialChatTabState.intValue = 1
        } else if (targetTab == "OFFICE") {
            initialChatTabState.intValue = 0
        }
    }
}

@Composable
fun SemsarkApp(
    initialChatTab: Int = 0,
    viewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()

    val isSeniorMode by viewModel.isSeniorMode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val selectedVillage by viewModel.selectedVillage.collectAsState()
    val selectedDealType by viewModel.selectedDealType.collectAsState()
    val selectedSortOption by viewModel.selectedSortOption.collectAsState()
    val approvedProperties by viewModel.approvedProperties.collectAsState()
    val favoriteProperties by viewModel.favoriteProperties.collectAsState()
    val pendingProperties by viewModel.pendingProperties.collectAsState()
    val allProperties by viewModel.allProperties.collectAsState()
    val registeredUsers by viewModel.registeredUsers.collectAsState()
    val callClicksCount by viewModel.callClicksCount.collectAsState()
    val whatsappClicksCount by viewModel.whatsappClicksCount.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val officeChatMessages by viewModel.officeChatMessages.collectAsState()
    val groupChatMessages by viewModel.groupChatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val isGuestMode by viewModel.isGuestMode.collectAsState()

    val minPrice by viewModel.minPrice.collectAsState()
    val maxPrice by viewModel.maxPrice.collectAsState()
    val minArea by viewModel.minArea.collectAsState()
    val maxArea by viewModel.maxArea.collectAsState()
    val areaUnit by viewModel.areaUnit.collectAsState()
    val postingDateFilter by viewModel.postingDateFilter.collectAsState()
    val comparedPropertyIds by viewModel.comparedPropertyIds.collectAsState()
    val comparedProperties by viewModel.comparedProperties.collectAsState()

    // Auth Guard: Force navigation to "auth" screen if user is not authenticated
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate("auth") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "auth",
        modifier = Modifier.fillMaxSize()
    ) {
        // Home Screen
        composable("home") {
            HomeScreen(
                isSeniorMode = isSeniorMode,
                searchQuery = searchQuery,
                selectedCategoryId = selectedCategoryId,
                selectedVillage = selectedVillage,
                selectedDealType = selectedDealType,
                selectedSortOption = selectedSortOption,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                areaUnit = areaUnit,
                postingDateFilter = postingDateFilter,
                comparedPropertyIds = comparedPropertyIds,
                comparedProperties = comparedProperties,
                approvedProperties = approvedProperties,
                chatMessages = chatMessages,
                officeChatMessages = officeChatMessages,
                groupChatMessages = groupChatMessages,
                isAiThinking = isAiThinking,
                currentUser = currentUser,
                isGuestMode = isGuestMode,
                isAdminLoggedIn = isAdminLoggedIn,
                initialChatTab = initialChatTab,
                onToggleSeniorMode = { viewModel.toggleSeniorMode() },
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onSelectCategory = { viewModel.selectCategory(it) },
                onSelectVillage = { viewModel.selectVillage(it) },
                onSelectDealType = { viewModel.selectDealType(it) },
                onSelectSortOption = { viewModel.selectSortOption(it) },
                onApplyAdvancedFilters = { minP, maxP, minA, maxA, unit, _, _, postingDate ->
                    viewModel.setPriceRange(minP, maxP)
                    viewModel.setAreaRange(minA, maxA, unit)
                    viewModel.setPostingDateFilter(postingDate)
                },
                onResetFilters = { viewModel.resetAllFilters() },
                onToggleFavorite = { id, current -> viewModel.toggleFavorite(id, current) },
                onToggleCompareProperty = { id -> viewModel.toggleCompareProperty(id) },
                onRemoveFromCompare = { id -> viewModel.removeFromCompare(id) },
                onClearComparison = { viewModel.clearComparison() },
                onSendMessageToAi = { viewModel.sendAiMessage(it) },
                onSendOfficeMessage = { viewModel.sendOfficeChatMessage(it) },
                onSendGroupMessage = { text, mediaUrl, mediaType, isAnnouncement ->
                    viewModel.sendGroupChatMessage(text, mediaUrl, mediaType, isAnnouncement)
                },
                onOpenAddProperty = { navController.navigate("add_property") },
                onOpenPropertyDetail = { propId -> navController.navigate("property_detail/$propId") },
                onOpenAboutDeveloper = { navController.navigate("about_developer") },
                onOpenAdminDashboard = { navController.navigate("admin_dashboard") },
                onOpenAuthModal = { navController.navigate("auth") },
                onOpenFavorites = { navController.navigate("favorites") },
                onTrackCallClick = { viewModel.trackCallClick() },
                onTrackWhatsappClick = { viewModel.trackWhatsappClick() }
            )
        }

        // Property Detail Screen
        composable(
            route = "property_detail/{propertyId}",
            arguments = listOf(navArgument("propertyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val propId = backStackEntry.arguments?.getLong("propertyId") ?: -1L
            val targetProperty = approvedProperties.find { it.id == propId }
                ?: favoriteProperties.find { it.id == propId }

            PropertyDetailScreen(
                property = targetProperty,
                isSeniorMode = isSeniorMode,
                isGuestMode = isGuestMode,
                isCompared = comparedPropertyIds.contains(propId),
                currentUser = currentUser,
                onBack = { navController.popBackStack() },
                onToggleFavorite = { id, current -> viewModel.toggleFavorite(id, current) },
                onToggleCompare = { id -> viewModel.toggleCompareProperty(id) },
                onOpenAuthModal = { navController.navigate("auth") }
            )
        }

        // Add Property Screen
        composable("add_property") {
            AddPropertyScreen(
                isSeniorMode = isSeniorMode,
                onBack = { navController.popBackStack() },
                onSubmitProperty = { newProp ->
                    viewModel.submitNewProperty(newProp) {
                        // Callback after submission
                    }
                }
            )
        }

        // About Developer Screen
        composable("about_developer") {
            AboutDeveloperScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Admin Dashboard Screen
        composable("admin_dashboard") {
            AdminDashboardScreen(
                isAdminLoggedIn = isAdminLoggedIn,
                pendingProperties = pendingProperties,
                allProperties = allProperties,
                registeredUsers = registeredUsers,
                callClicksCount = callClicksCount,
                whatsappClicksCount = whatsappClicksCount,
                onLoginAdmin = { passcode -> viewModel.loginAdmin(passcode) },
                onLogoutAdmin = { viewModel.logoutAdmin() },
                onApproveProperty = { id -> viewModel.approvePendingProperty(id) },
                onRejectProperty = { id, reason -> viewModel.rejectPendingProperty(id, reason) },
                onUpdatePropertyStatus = { id, newStatus -> viewModel.updatePropertyStatus(id, newStatus) },
                onUpdatePropertyDetails = { prop -> viewModel.updatePropertyDetails(prop) },
                onAddDirectProperty = { prop -> viewModel.addDirectPropertyByAdmin(prop) {} },
                onDeleteProperty = { prop -> viewModel.deleteProperty(prop) },
                onToggleBlockUser = { userId -> viewModel.toggleBlockUser(userId) },
                onBack = { navController.popBackStack() }
            )
        }

        // Auth Screen (Initial Startup Route)
        composable("auth") {
            AuthScreen(
                currentUser = currentUser,
                userPhone = userPhone,
                onLoginUserFull = { name, phone, email, method, city ->
                    viewModel.registerAndLoginUser(name, phone, email, method, city)
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onUpdateProfile = { name, username, phone, picUri, onResult ->
                    viewModel.updateUserProfile(name, username, phone, picUri, onResult)
                },
                onLogoutUser = {
                    viewModel.logoutUser()
                },
                onBrowseAsGuest = {
                    viewModel.enableGuestMode()
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                },
                onNavigateToFavorites = {
                    navController.navigate("favorites")
                }
            )
        }

        // Favorites Screen
        composable("favorites") {
            FavoritesScreen(
                favoriteProperties = favoriteProperties,
                isSeniorMode = isSeniorMode,
                isGuestMode = isGuestMode,
                currentUser = currentUser,
                onToggleFavorite = { id, current -> viewModel.toggleFavorite(id, current) },
                onOpenPropertyDetail = { propId -> navController.navigate("property_detail/$propId") },
                onBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("home") {
                            popUpTo("favorites") { inclusive = true }
                        }
                    }
                },
                onOpenAuthModal = {
                    navController.navigate("auth")
                }
            )
        }
    }
}
