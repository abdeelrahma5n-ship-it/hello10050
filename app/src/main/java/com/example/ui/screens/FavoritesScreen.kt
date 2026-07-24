package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import com.example.data.model.Property
import com.example.data.model.User
import com.example.ui.theme.RoyalGold
import com.example.ui.components.PropertyCard
import com.example.ui.components.SemsarkBottomBar
import com.example.ui.components.SemsarkTab
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoriteProperties: List<Property>,
    isSeniorMode: Boolean,
    isGuestMode: Boolean = true,
    currentUser: User? = null,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onOpenPropertyDetail: (Long) -> Unit,
    onBack: () -> Unit,
    onOpenAuthModal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkEmerald,
        topBar = {
            TopAppBar(
                title = { Text("العقارات المفضلة ❤️", fontWeight = FontWeight.Bold, color = SoftGold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkEmeraldCard)
            )
        },
        bottomBar = {
            SemsarkBottomBar(
                currentTab = SemsarkTab.FAVORITES,
                onTabSelected = { tab ->
                    when (tab) {
                        SemsarkTab.HOME, SemsarkTab.SEARCH, SemsarkTab.CHAT -> onBack()
                        SemsarkTab.FAVORITES -> { /* Current */ }
                        SemsarkTab.PROFILE -> onOpenAuthModal()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isGuestMode || currentUser == null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                    border = BorderStroke(1.dp, RoyalGold)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔒", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("أنت تتصفح كضيف حالياً", color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("سجل الدخول لحفظ وإدارة عقاراتك المفضلة وتزامنها بشكل دائم.", color = TextWhite, fontSize = 11.sp)
                        }
                        Button(
                            onClick = onOpenAuthModal,
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("دخول", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
            if (favoriteProperties.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❤️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "لم تقم بإضافة عقارات للمفضلة بعد",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "اضغط على رمز القلب في أي عقار ليتم حفظه هنا للعودة إليه لاحقاً",
                            color = SoftGold,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favoriteProperties, key = { it.id }) { prop ->
                        PropertyCard(
                            property = prop,
                            isSeniorMode = isSeniorMode,
                            onToggleFavorite = onToggleFavorite,
                            onClickDetail = onOpenPropertyDetail
                        )
                    }
                }
            }
        }
    }
}
